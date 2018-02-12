package guestbook.core.repository;

import guestbook.core.model.GuestRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class GuestbookRepositorySQL implements GuestbookRepository {

    private final static String INSERT_QUERY = "INSERT INTO guest_records (id, first_name, last_name, created_at) VALUES (?, ?, ?, ?)";
    private final static String DELETE_QUERY = "DELETE FROM guest_records WHERE id = ?";

    private final String username;
    private final String password;

    public GuestbookRepositorySQL(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public List<GuestRecord> getAllGuests() throws RepositoryException {

        try(Connection connection = obtainConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM guest_records ORDER BY created_at DESC");
            List<GuestRecord> result = new ArrayList<>();
            while(rs.next()) {
                GuestRecord gr = new GuestRecord();
                gr.setUuid(UUID.fromString(rs.getString("id")));
                gr.setFirstName(rs.getString("first_name"));
                gr.setLastName(rs.getString("last_name"));
                gr.setDate(rs.getTimestamp("created_at"));
                result.add(gr);
            }
            return result;
        } catch (Exception e) {
            throw new RepositoryException("Nie można pobrać danych", e);
        }
    }

    @Override
    public void save(GuestRecord guestRecord) throws RepositoryException {

        try(Connection connection = obtainConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(INSERT_QUERY);
            pstmt.setString(1, guestRecord.getUuid().toString());
            pstmt.setString(2, guestRecord.getFirstName());
            pstmt.setString(3, guestRecord.getLastName());
            pstmt.setTimestamp(4, new Timestamp(guestRecord.getDate().getTime()));
            pstmt.execute();
        } catch (Exception e) {
            throw new RepositoryException("Nie można zapisać danych", e);
        }
    }

    @Override
    public void delete(GuestRecord guestRecord) throws RepositoryException{
        try(Connection connection = obtainConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(DELETE_QUERY);
            pstmt.setString(1, guestRecord.getUuid().toString());
            pstmt.execute();
        } catch (Exception e) {
            throw new RepositoryException("Nie można usunąć danych", e);
        }
    }

    private Connection obtainConnection() {
        try {
            Properties connectionProps = new Properties();
            connectionProps.put("user", username);
            connectionProps.put("password", password);

            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/guestbook",
                    connectionProps);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
