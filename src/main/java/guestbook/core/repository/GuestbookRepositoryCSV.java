package guestbook.core.repository;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import guestbook.core.model.GuestRecord;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import static java.util.stream.Collectors.toList;

public class GuestbookRepositoryCSV implements GuestbookRepository {

    private final String repositoryFile;

    public GuestbookRepositoryCSV(String repositoryFile) {
        this.repositoryFile = repositoryFile;
    }

    public void save(GuestRecord guestRecord) throws RepositoryException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");//TODO ???
        try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(repositoryFile, true), CSVFormat.DEFAULT)) {
            csvPrinter.print(guestRecord.getUuid());
            csvPrinter.print(guestRecord.getFirstName());
            csvPrinter.print(guestRecord.getLastName());
            csvPrinter.print(sdf.format(guestRecord.getDate()));
            csvPrinter.println();
            csvPrinter.flush();
        } catch (Exception e) {
            throw new RepositoryException("Problem podczas zapisywania", e);
        }
    }

    @Override
    public void delete(GuestRecord guestRecord) throws RepositoryException {

        try {
            List<GuestRecord> records = getAllGuests();
            Files.newOutputStream(Paths.get(repositoryFile)); //TODO change to truncate method
            records.stream().filter(gr->!gr.getUuid().equals(guestRecord.getUuid())).forEach(gr-> {//TODO ???
                try {
                    save(gr);
                } catch (RepositoryException e) {
                    throw new RuntimeException(e);
//                    throw e;
                }
            });
        } catch (Exception e) {
            throw new RepositoryException("Problem podczas usuwania", e);
        }
    }

    public List<GuestRecord> getAllGuests() throws RepositoryException {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader("uuid", "firstName", "lastName", "date");
        try (CSVParser csvParser = new CSVParser(new FileReader(repositoryFile), csvFormat)) {
            return csvParser.getRecords().stream()
                    .map(this::csvRecordToGuestRecord)
                    .collect(toList());
        } catch (Exception e) {
            throw new RepositoryException("Problem podczas odczytu", e);
        }
    }

    private GuestRecord csvRecordToGuestRecord(CSVRecord r) throws RuntimeException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss"); //TODO ???
        try {
            UUID gUUID = UUID.fromString(r.get("uuid"));
            String gFirstName = r.get("firstName");
            String gLastName = r.get("lastName");
            Date gDate = sdf.parse(r.get("date"));
            return new GuestRecord(gUUID, gFirstName, gLastName, gDate);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
