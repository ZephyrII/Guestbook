package guestbook.core.repository;

import guestbook.core.model.GuestRecord;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
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
            Document doc = getDocument();
            NodeList list = doc.getDocumentElement().getChildNodes();
            for (int i=0; i < list.getLength(); i++) { //TODO ???
                Element record = (Element)list.item(i);
                String uuid = record.getAttribute("uuid");
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

    private Document getDocument() throws RepositoryException {

        try {
            File file = new File(repositoryFile);
            if (file.exists()) {
                DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                return db.parse(new File(repositoryFile));
            } else {
                file.createNewFile();
                Document doc = initDocument();
                writeDocumentToRepositoryFile(doc);
                return doc;
            }
        } catch (Exception e) {
            throw new RepositoryException("Problem podczas otwierania repozytorium", e);
        }
    }

    private Document initDocument() {

        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            doc.appendChild(doc.createElement("records"));
            return doc;
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }


    public void save(GuestRecord guestRecord) throws RepositoryException {

        try {
            Document doc = getDocument();
            Element record = doc.createElement("guestRecord");
            record.setAttribute("uuid", guestRecord.getUuid().toString());
            record.appendChild(createTextNode("firstName", guestRecord.getFirstName(), doc));
            record.appendChild(createTextNode("lastName", guestRecord.getLastName(), doc));
            record.appendChild(createTextNode("date", createFormatter().format(guestRecord.getDate()), doc));
            doc.getDocumentElement().appendChild(record);

            writeDocumentToRepositoryFile(doc);
        } catch (Exception e) {
            throw new RepositoryException("Problem podczas zapisu", e);
        }

    }

    private void writeDocumentToRepositoryFile(Document doc) throws TransformerException { //TODO try-catch ???

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
//        DOMSource source = new DOMSource(doc);
//        StreamResult result = new StreamResult(new File(repositoryFile));
        transformer.transform(new DOMSource(doc), new StreamResult(new File(repositoryFile)));//TODO ???
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
            Document doc = getDocument();
            NodeList records = doc.getDocumentElement().getChildNodes();
            for (int i=0; i < records.getLength(); i++) {
                Element record = (Element)records.item(i);
                if(record.getAttribute("uuid").equals(guestRecord.getUuid().toString()))
                    doc.getDocumentElement().removeChild(record);
            }
            writeDocumentToRepositoryFile(doc);
        } catch (Exception e) {
            throw new RepositoryException("Problem podczas usuwania", e);
        }
    }

    private SimpleDateFormat createFormatter() {
        return new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    }
}
