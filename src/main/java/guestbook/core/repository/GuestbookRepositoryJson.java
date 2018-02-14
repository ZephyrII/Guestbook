package guestbook.core.repository;


import guestbook.core.model.GuestRecord;

import javax.json.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

/**
 * @see GuestbookRepository
 */
public class GuestbookRepositoryJson implements GuestbookRepository {


    private final String repositoryFile;

    public GuestbookRepositoryJson(String repositoryFile) {
        this.repositoryFile = repositoryFile;
    }

    public List<GuestRecord> getAllGuests() throws  RepositoryException{
        try {
            JsonArray records = readRecordsFromFile();
            return records.stream()
                    .map(this::jsonRecordToGuestRecord)
                    .collect(toList());
        } catch (Exception e) {
            throw new RepositoryException("Problem podczas odczytu", e);
        }
    }

    private JsonArray readRecordsFromFile() throws Exception {
        try (JsonReader jsonReader = Json.createReader(new FileReader(repositoryFile))) {
            JsonObject jo = jsonReader.readObject();
            return jo.getJsonArray("records");
        } catch(Exception e) {
            throw e;
        }
    }

    private GuestRecord jsonRecordToGuestRecord(JsonValue jv) throws RuntimeException {
        SimpleDateFormat sdf = createFormatter();
        try {
            JsonObject record = jv.asJsonObject();
            String uuid = record.getString("uuid");
            String firstName = record.getString("firstName");
            String lastName = record.getString("lastName");
            String date = record.getString("date");
            Date gDate = sdf.parse(date);
            return new GuestRecord(UUID.fromString(uuid), firstName, lastName, gDate);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void save(GuestRecord guestRecord) throws RepositoryException {
        JsonArray records;
        try {
            records = Json.createArrayBuilder(readRecordsFromFile())
                            .add(guestRecordToJsonObject(guestRecord))
                            .build();
        } catch (Exception e) {
            throw new RepositoryException("Problem podczas odczytu", e);
        }
        writeJsonArrayToFile(records);
    }

    private void writeJsonArrayToFile(JsonArray records) throws RepositoryException {
        try (JsonWriter writer = Json.createWriter(new FileWriter(repositoryFile))) {

            JsonArray resultArrayBuilder = Json.createArrayBuilder(records).build();
            JsonObject result = Json.createObjectBuilder()
                    .add("records", resultArrayBuilder)
                    .build();

            writer.writeObject(result);
        } catch (Exception e) {
            throw new RepositoryException("Problem podczas zapisu", e);
        }
    }

    @Override
    public void delete(GuestRecord guestRecord) throws RepositoryException {

        try {
            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            JsonArray jsonArray = readRecordsFromFile();
            jsonArray.stream()
                     .filter(jo -> !guestRecord.getUuid().equals(jsonRecordToGuestRecord(jo).getUuid()))
                     .forEach(jsonArrayBuilder::add);
            writeJsonArrayToFile(jsonArrayBuilder.build());
        } catch (Exception e) {
            throw new RepositoryException("Problem podczas usuwania", e);
        }
    }

    private JsonObject guestRecordToJsonObject(GuestRecord guestRecord) {
        return Json.createObjectBuilder()
            .add("uuid", guestRecord.getUuid().toString())
            .add("firstName", guestRecord.getFirstName())
            .add("lastName", guestRecord.getLastName())
            .add("date", createFormatter().format(guestRecord.getDate()))
            .build();
    }

    private SimpleDateFormat createFormatter() {
        return new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    }//TODO ???



}
