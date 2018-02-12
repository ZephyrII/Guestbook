package guestbook.core.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.UUID;

/**
 * Object which represents single guest in guestbook
 */
@XmlRootElement(name = "record")
public class GuestRecord {
    private UUID uuid;
    private String firstName;
    private String lastName;
    private Date date;

    public GuestRecord() {}

    /**
     * Creates GuestRecord object based on first and last name. UUID and date are generated automatically.
     * @param firstName guest's first name
     * @param lastName guest's last name
     */
    public GuestRecord(String firstName, String lastName) {
        this(UUID.randomUUID(), firstName, lastName, new Date());
    }

    /**
     * Creates GuestRecord object based on first and last name, uuid and date
     * @param uuid guest's uuid
     * @param firstName guest's first name
     * @param lastName guest's last name
     * @param date guest's date
     */
    public GuestRecord(UUID uuid, String firstName, String lastName, Date date) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = date;
    }

    @Override
    public String toString() {
        return  " First name: " + this.firstName +
                " Last name: " + this.lastName;
    }

    public String toDetailedString() {
        return  "Uuid:" + this.uuid.toString() +
                " First name: " + this.firstName +
                " Last name: " + this.lastName +
                " Date: " + this.date.toString();
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
