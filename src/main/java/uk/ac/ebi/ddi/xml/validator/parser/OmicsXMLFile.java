package uk.ac.ebi.ddi.xml.validator.parser;

import org.xml.sax.SAXException;
import psidev.psi.tools.xxindex.StandardXpathAccess;
import psidev.psi.tools.xxindex.index.IndexElement;
import psidev.psi.tools.xxindex.index.XpathIndex;
import uk.ac.ebi.ddi.xml.validator.exception.DDIException;
import uk.ac.ebi.ddi.xml.validator.parser.model.DataElement;
import uk.ac.ebi.ddi.xml.validator.parser.model.Database;
import uk.ac.ebi.ddi.xml.validator.parser.model.Entry;
import uk.ac.ebi.ddi.xml.validator.parser.model.SummaryDatabase;
import uk.ac.ebi.ddi.xml.validator.parser.unmarshaller.OmicsDataUnmarshaller;
import uk.ac.ebi.ddi.xml.validator.parser.unmarshaller.OmicsUnmarshallerFactory;
import uk.ac.ebi.ddi.xml.validator.utils.Tuple;
import uk.ac.ebi.ddi.xml.validator.utils.Utils;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class reads an XML file for DDI and generate a set of data structures to handle the underlying data structures, objects
 * IT also provide classes for validation using the DDI Schema, etc. Finally it provides a set of utilities to write Omics XML files.
 *
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 18/08/2015
 */
public class OmicsXMLFile {

    private static Pattern OmicsXMLFilePatter = Pattern.compile(".*(<database>).*");
    /**
     * The mzXML source file.
     */
    private File sourcefile;

    /**
     * Map with the spectra's index attribute as key
     * and the corresponding IndexElement as value.
     */
    private Map<String, IndexElement> idToIndexElementMap;

    /**
     * The unmarshaller to use.
     */
    OmicsDataUnmarshaller unmarshaller;

    /**
     * The random access file object is
     * used to read the file. This object
     * is only opened once.
     */
    private RandomAccessFile accessFile;
    /**
     * The actual XPath index to use.
     */
    private XpathIndex index;
    /**
     * The XPath access to use.
     */
    private StandardXpathAccess xpathAccess;


    private ArrayList<String> entryIds;


    private SummaryDatabase database = null;

    /**
     * Pattern used to extract xml attribute name
     * value pairs.
     */
    private static final Pattern xmlAttributePattern = Pattern.compile("(\\w+)=\"([^\"]*)\"");


    public OmicsXMLFile(File file) throws DDIException {

        this.sourcefile = file;

        indexFile();

        // create the unmarshaller
        unmarshaller = OmicsUnmarshallerFactory.getInstance().initializeUnmarshaller();

        //Inizialize attributes for each Entry
        initializeAttributeMaps();

        // initialize the entry maps
        initializeEntryMaps();

    }

    private void initializeAttributeMaps() throws DDIException {
        List<IndexElement> databases = index.getElements(DataElement.DATABASE.getXpath());
        if(databases != null && !databases.isEmpty() && databases.size() == 1){
            String xml = readSnipplet(databases.get(0));

            try {
                Database entry = unmarshaller.unmarshal(xml, DataElement.DATABASE);
                database = new SummaryDatabase();
                database.setDescription(entry.getDescription());
                database.setName(entry.getName());
                database.setRelease(entry.getRelease());
                database.setEntryCount(entry.getEntryCount());
                database.setReleaseDate(entry.getReleaseDate());

            } catch (Exception e) {
                throw new DDIException("Failed to unmarshal an Entry", e);
            }

        }
    }

    private void initializeEntryMaps() throws DDIException {

        List<IndexElement> entries = index.getElements(DataElement.ENTRY.getXpath());

        idToIndexElementMap = new HashMap<>(entries.size());

        entryIds = new ArrayList<>(entries.size());

        for (IndexElement entry : entries) {
            // read the attributes
            Map<String, String> attributes = readElementAttributes(entry);

            if (!attributes.containsKey("id"))
                throw new DDIException("Entry element with missing id attribute at line " + entry.getLineNumber());

            idToIndexElementMap.put(attributes.get("id"), entry);
            entryIds.add(attributes.get("id"));
        }
    }

    /**
     * Indexes the current sourcefile and creates the
     * index and xpathAccess objects.
     * @throws DDIException Thrown when the sourcefile cannot be accessed.
     */
    private void indexFile() throws DDIException {
        try {
            // build the xpath
            xpathAccess = new StandardXpathAccess(sourcefile, DataElement.getXpaths());

            // save the index
            index = xpathAccess.getIndex();
        } catch (IOException e) {
            throw new DDIException("Failed to index omicsDI file.", e);
        }
    }

    /**
     * Reads the given element's attributes and returns
     * them as a Map with the attribute's name as key
     * and its value as value.
     * @param indexElement
     * @return
     * @throws DDIException
     */
    private Map<String, String> readElementAttributes(IndexElement indexElement) throws DDIException {

        RandomAccessFile access = getRandomAccess();

        // process the file line by line
        try {
            // initialize the run attributes
            HashMap<String, String> foundAttributes = new HashMap<>();

            // go to the beginning of element
            access.seek(indexElement.getStart());

            // just read the beginning of the element (250 elements should be sufficient)
            byte[] headerBuffer = new byte[250];

            access.read(headerBuffer);

            String headerString = new String(headerBuffer);

            // make sure the whole header was retrieved
            while (!headerString.contains(">")) {
                // read another header string
                access.seek(indexElement.getStart() + headerString.length());

                access.read(headerBuffer);

                headerString += new String(headerBuffer);
            }

            // remove all new line characters
            headerString = headerString.replace("\n", "");

            // remove everything after the first ">"
            headerString = headerString.substring(0, headerString.indexOf('>') + 1);

            // parse the line
            Matcher matcher = xmlAttributePattern.matcher(headerString);

            while (matcher.find()) {
                String name = matcher.group(1);
                String value = matcher.group(2);

                if (name != null && value != null)
                    foundAttributes.put(name, value);
            }

            return foundAttributes;
        } catch (IOException e) {
            throw new DDIException("Failed to read omicsDI file.", e);
        }
    }

    /**
     * Returns the random access file object to access
     * the source file.
     * @return
     * @throws DDIException
     */
    private RandomAccessFile getRandomAccess() throws DDIException {
        if (accessFile != null)
            return accessFile;

        try {
            accessFile = new RandomAccessFile(sourcefile, "r");
        } catch (FileNotFoundException e) {
            throw new DDIException("Could not find mzData file '" + sourcefile.getPath() + "'", e);
        }

        return accessFile;
    }

    /**
     * This function return the information of an element using the the id in the XML, for example PXD00001
     * @param id the id value in the file
     * @return Entry
     * @throws DDIException
     */
    public Entry getEntryById(String id) throws DDIException {
        // make sure the spectrum exists

        if (!idToIndexElementMap.containsKey(id))
            throw new DDIException("Entry with id '" + id + "' does not exist.");

        // get the index element
        IndexElement indexElement = idToIndexElementMap.get(id);

        if (indexElement == null)
            throw new DDIException("Fail during the indexin");

        // get the snipplet
        String xml = readSnipplet(indexElement);

        // unmarshall the object
        try {
            return unmarshaller.unmarshal(xml, DataElement.ENTRY);
        } catch (Exception e) {
            throw new DDIException("Failed to unmarshal an Entry", e);
        }
    }

    public List<Entry> getAllEntries() throws DDIException {
        List<Entry> entries = new ArrayList<>();
        for(String id: getEntryIds())
            entries.add(getEntryById(id));
        return entries;
    }

    /**
     * THis function return an entry using the index element in the file for example first element (index = 0), second element (index = 1)
     * @param index the index in the List of file elements
     * @return Entry
     * @throws DDIException
     */
    public Entry getEntryByIndex(Integer index) throws DDIException {
        // make sure the spectrum exists

        if (idToIndexElementMap.size() < index || index < 0)
            throw new DDIException("Entry with id '" + index + "' does not exist.");

        // get the index element

        String id = entryIds.get(index);

        if (id == null)
            throw new DDIException("Fail during the indexin");

        try {

            return getEntryById(id);
        } catch (Exception e) {
            throw new DDIException("Failed to unmarshal an Entry", e);
        }
    }

    /**
     * Return all the entry Ids from the XML
     * @return List of Ids
     */
    public List<String> getEntryIds(){
        return entryIds;
    }

    /**
     * @param index
     * @return
     */
    private List<IndexElement> convertIndexElements(List<IndexElement> index) {
        List<IndexElement> convertedIndex = new ArrayList<>(index.size());

        for (IndexElement e : index) {
            int size = (int) (e.getStop() - e.getStart());
            convertedIndex.add(e);
        }

        return convertedIndex;
    }

    /**
     * Iterator over all spectra in the omics DDI file
     * and returns Entry  objects.
     * @author jg
     *
     */
    private class EntryIterator implements Iterator<Entry> {
        /**
         * Iterator over all spectrum IndexElements.
         * Most functions are simply passed on to this
         * iterator.
         */
        private final Iterator<String> idIterator = entryIds.iterator();

        @Override
        public boolean hasNext() {
            return idIterator.hasNext();
        }

        @Override
        public Entry next() {
            String id = idIterator.next();
            IndexElement indexElement = idToIndexElementMap.get(Integer.parseInt(id));

            try {
                String xml = readSnipplet(indexElement);

                return unmarshaller.unmarshal(xml, DataElement.ENTRY);
            } catch (Exception e) {
                throw new RuntimeException("Failed to load spectrum from mzData file.", e);
            }
        }

        @Override
        public void remove() {
            // not supported
        }
    }

    /**
     * Reads a given XML Snipplet from the file and returns
     * it as a String.
     * @param indexElement An IndexElement specifying the position to read.
     * @return
     * @throws DDIException
     */
    private String readSnipplet(IndexElement indexElement) throws DDIException {
        // read the XML from the file
        RandomAccessFile access = getRandomAccess();

        // calculate the snipplets length
        int length = (int) (indexElement.getStop() - indexElement.getStart());

        // create the byte buffer
        byte[] bytes = new byte[length];

        try {
            // move to the position in the file
            access.seek(indexElement.getStart());

            // read the snipplet
            access.read(bytes);

            // create and return the string
            return new String(bytes);

        } catch (IOException e) {
            throw new DDIException("Failed to read from mzData file.", e);
        }
    }

    public static boolean isSchemaValid(File xmlFile) {
        boolean retval;

        // 1. Lookup a factory for the W3C XML Schema language
        SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

        // 2. Compile the schema.
        URL schemaLocation;
        schemaLocation = OmicsXMLFile.class.getClassLoader().getResource("omicsdi.xsd");

        Schema schema;
        try {
            schema = factory.newSchema(schemaLocation);
        } catch (SAXException e) {
            e.printStackTrace();
            throw new IllegalStateException("Could not compile Schema for file: " + schemaLocation);
        }

        // 3. Get a validator from the schema.
        Validator validator = schema.newValidator();

        // 4. Parse the document you want to check.
        Source source = new StreamSource(xmlFile);

        // 5. Check the document (throws an Exception if not valid)
        try {
            validator.validate(source);
            retval = true;
        } catch (SAXException ex) {
            System.out.println(xmlFile.getName() + " is not valid because ");
            System.out.println(ex.getMessage());
            retval = false;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Could not validate file because of file read problems for source: " + xmlFile.getAbsolutePath());
        }

        return retval;
    }

    public static boolean hasFileHeader(File file) throws DDIException {
        if(file.getAbsolutePath().toLowerCase().endsWith(".xml")){
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(file));
                // read the first ten lines
                StringBuilder content = new StringBuilder();
                for (int i = 0; i < 20; i++) {
                    content.append(reader.readLine());
                }
                // check file type
                Matcher matcher = OmicsXMLFilePatter.matcher(content);
                return matcher.find();
            } catch (Exception e) {
                throw  new DDIException("Failed to read file", e);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        // do nothing here
                    }
                }
            }
        }
        return false;
    }

    /**
     * Return Error as a Tuple with the Code of the Error and the message.
     * @param file the file to be validated
     * @return the list of errors
     */
    public static List<Tuple> validateSchema(File file) {
        List<Tuple> errors = new ArrayList<>();
        // 1. Lookup a factory for the W3C XML Schema language
        SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

        // 2. Compile the schema.
        URL schemaLocation;
        schemaLocation = OmicsXMLFile.class.getClassLoader().getResource("omicsdi.xsd");

        Schema schema;
        try {
            schema = factory.newSchema(schemaLocation);
        } catch (SAXException e) {
            e.printStackTrace();
            throw new IllegalStateException("Could not compile Schema for file: " + schemaLocation);
        }

        // 3. Get a validator from the schema.
        Validator validator = schema.newValidator();

        // 4. Parse the document you want to check.
        Source source = new StreamSource(file);

        // 5. Check the document (throws an Exception if not valid)
        try {
            validator.validate(source);
        } catch (SAXException | IOException ex) {
            errors.add(new Tuple(Utils.ERROR, ex.getMessage()));
        }
        return errors;
    }

    public static List<Tuple> validateSemantic(File file) {
        List<Tuple> errors = new ArrayList<>();
        try {
            OmicsXMLFile reader = new OmicsXMLFile(file);
            List<String> Ids    = reader.getEntryIds();
            // Retrive all the entries and retrieve the warning semantic validation
            for(String id: Ids){
                List<Tuple> error = Utils.validateSemantic(reader.getEntryById(id));
                errors.addAll(error);
            }
        } catch (DDIException e) {
            errors.add(new Tuple(Utils.ERROR, e.getMessage()));

        }
        return errors;
    }

    public String getName() {
        return (database != null)? database.getName():null;
    }

    public String getDescription() {
        return (database != null)? database.getDescription():null;
    }

    public String getRelease() {
        return (database != null)?database.getRelease():null;
    }

    public String getReleaseDate() {
        return (database != null)?database.getReleaseDate():null;
    }

    public Integer getEntryCount() {
        return (database != null)?database.getEntryCount():null;
    }


    public void setDatabaseName(String name){
        if(database != null)
            database.setName(name);
    }


}
