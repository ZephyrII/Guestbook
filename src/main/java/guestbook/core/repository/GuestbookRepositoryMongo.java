package guestbook.core.repository;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import guestbook.core.model.GuestRecord;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class GuestbookRepositoryMongo implements GuestbookRepository {


    private final String DATABASE;
    private final String COLLECTION;

    public GuestbookRepositoryMongo(String database, String collection) {

        this.DATABASE = database;
        this.COLLECTION = collection;
    }
    @Override
    public List<GuestRecord> getAllGuests() throws RepositoryException {

        SimpleDateFormat sdf = createFormatter();
        List<GuestRecord> records = new ArrayList<>();
        try {
            Block<Document> reader = record -> {
                try {
                    String uuid = record.getString("uuid");
                    String firstName = record.getString("firstName");
                    String lastName = record.getString("lastName");
                    String date = record.getString("date");
//                    Date gDate = sdf.parse(date);
                    records.add(new GuestRecord(UUID.fromString(uuid), firstName, lastName, new Date()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };
            MongoCollection mongoCollection = obtainConnection();
            mongoCollection.find().forEach(reader);
        } catch (Exception e) {
            throw new RepositoryException("Problem podczas odczytu", e);
        }
        return records;
    }

    @Override
    public void save(GuestRecord guestRecord) throws RepositoryException {
        try {
            MongoCollection mongoCollection = obtainConnection();
            mongoCollection.insertOne(new Document("uuid", guestRecord.getUuid().toString())
                                            .append("firstName", guestRecord.getFirstName())
                                            .append("lastName", guestRecord.getLastName())
                                            .append("date", guestRecord.getDate().toString()));


        } catch (Exception e) {
            throw new RepositoryException("Problem podczas zapisu", e);
        }

    }

    @Override
    public void delete(GuestRecord guestRecord) throws RepositoryException {

    }
    private MongoCollection obtainConnection() {
        try {
                MongoClient mongoClient = new MongoClient();
                MongoDatabase database = mongoClient.getDatabase(DATABASE);
                return database.getCollection(COLLECTION);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }
    private SimpleDateFormat createFormatter() {
        return new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    }

}
