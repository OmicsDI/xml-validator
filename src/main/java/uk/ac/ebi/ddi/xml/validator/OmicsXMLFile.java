package uk.ac.ebi.ddi.xml.validator;

import psidev.psi.tools.xxindex.StandardXpathAccess;
import psidev.psi.tools.xxindex.index.IndexElement;
import psidev.psi.tools.xxindex.index.XpathIndex;
import uk.ac.ebi.ddi.xml.validator.exception.DDIException;
import uk.ac.ebi.ddi.xml.validator.parser.model.DataElement;
import uk.ac.ebi.ddi.xml.validator.parser.model.Entry;
import uk.ac.ebi.ddi.xml.validator.parser.unmarshaller.OmicsDataUnmarshaller;
import uk.ac.ebi.ddi.xml.validator.parser.unmarshaller.OmicsUnmarshallerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 18/08/2015
 */
public class OmicsXMLFile {

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

        // initialize the spectra maps
        initializeEntryMaps();

    }


    private void initializeEntryMaps() throws DDIException {

        List<IndexElement> entries = index.getElements(DataElement.ENTRY.getXpath());

        idToIndexElementMap = new HashMap<String, IndexElement>(entries.size());

        entryIds = new ArrayList<String>(entries.size());

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
            HashMap<String, String> foundAttributes = new HashMap<String, String>();

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
            Entry mzDataSpectrum = unmarshaller.unmarshal(xml, DataElement.ENTRY);

            return mzDataSpectrum;
        } catch (Exception e) {
            throw new DDIException("Failed to unmarshal an Entry", e);
        }
    }

    public Entry getEntryByIndex(Integer id) throws DDIException {
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
            Entry mzDataSpectrum = unmarshaller.unmarshal(xml, DataElement.ENTRY);

            return mzDataSpectrum;
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
        List<IndexElement> convertedIndex = new ArrayList<IndexElement>(index.size());

        for (IndexElement e : index) {
            int size = (int) (e.getStop() - e.getStart());
            convertedIndex.add(e);
        }

        return convertedIndex;
    }

    /**
     * Iterator over all spectra in the mzData file
     * and returns mzData Spectrum objects.
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

                Entry entry = unmarshaller.unmarshal(xml, DataElement.ENTRY);

                return entry;
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
            String snipplet = new String(bytes);
            return snipplet;

        } catch (IOException e) {
            throw new DDIException("Failed to read from mzData file.", e);
        }
    }
}
