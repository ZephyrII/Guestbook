package guestbook.core.repository;

import guestbook.core.model.GuestRecord;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GuestbookRepositoryXML implements GuestbookRepository {

    private final String repositoryFile;

    public GuestbookRepositoryXML(String repositoryFile) {
        this.repositoryFile = repositoryFile;
    }

    public List<GuestRecord> getAllGuests() throws RepositoryException {

        SimpleDateFormat sdf = createFormatter();
        List<GuestRecord> gr = new ArrayList<>();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(repositoryFile));
            NodeList list = doc.getElementsByTagName("records").item(0).getChildNodes();
            for (int i=0; i < list.getLength(); i++) {
                Element record = (Element)list.item(i);
                String uuid = record.getElementsByTagName("uuid").item(0).getTextContent();
                String firstName = record.getElementsByTagName("firstName").item(0).getTextContent();
                String lastName = record.getElementsByTagName("lastName").item(0).getTextContent();
                String date = record.getElementsByTagName("date").item(0).getTextContent();

                gr.add(new GuestRecord(UUID.fromString(uuid), firstName, lastName, sdf.parse(date)));
            }

        } catch (Exception e) {
            throw new RepositoryException("Problem podczas odczytu", e);
        }

        return gr;
    }


    public void save(GuestRecord guestRecord) throws RepositoryException {

        try {
            //TODO save xml using DOM
        } catch (Exception e) {
            throw new RepositoryException("Problem podczas zapisu", e);
        }

    }

    @Override
    public void delete(GuestRecord guestRecord) throws RepositoryException {
        try {
            //TODO delete record using DOM
        } catch (Exception e) {
            throw new RepositoryException("Problem podczas zapisu", e);
        }
    }

    private SimpleDateFormat createFormatter() {
        return new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    }
}
