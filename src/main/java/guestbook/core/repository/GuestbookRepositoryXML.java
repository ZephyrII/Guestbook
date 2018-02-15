package guestbook.core.repository;

import guestbook.core.model.GuestRecord;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
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
            Document doc = getDocument();
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

    private Document getDocument() throws ParserConfigurationException, SAXException, IOException { //TODO zamiast try/catch???
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        File file = new File(repositoryFile);
        if(file.exists())
            return db.parse(new File(repositoryFile));
        else {
            file.createNewFile();
            return initDocument();
        }
    }

    private Document initDocument() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();
            doc.appendChild(doc.createElement("records"));
            return doc;
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }


    public void save(GuestRecord guestRecord) throws RepositoryException {

        SimpleDateFormat sdf = createFormatter();
        try {
            Document doc = getDocument();
            Element records = (Element) doc.getElementsByTagName("records").item(0);
            Element record = doc.createElement("guestRecord");
            record.appendChild(createTextNode("uuid", guestRecord.getUuid().toString(), doc));
            record.appendChild(createTextNode("firstName", guestRecord.getFirstName(), doc));
            record.appendChild(createTextNode("lastName", guestRecord.getLastName(), doc));
            record.appendChild(createTextNode("date", sdf.format(guestRecord.getDate()), doc));
            records.appendChild(record);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(repositoryFile));
            transformer.transform(source, result);
        } catch (Exception e) {
            throw new RepositoryException("Problem podczas zapisu", e);
        }

    }

    private Element createTextNode(String key, String value, Document doc) {
        Element element = doc.createElement(key);
        Text text = doc.createTextNode(value);
        element.appendChild(text);
        return element;
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
