package uk.ac.ebi.ddi.xml.validator;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.ddi.xml.validator.exception.DDIException;
import uk.ac.ebi.ddi.xml.validator.parser.OmicsXMLFile;
import uk.ac.ebi.ddi.xml.validator.parser.marshaller.OmicsDataMarshaller;
import uk.ac.ebi.ddi.xml.validator.parser.model.Database;
import uk.ac.ebi.ddi.xml.validator.parser.model.Entries;
import uk.ac.ebi.ddi.xml.validator.parser.model.Entry;
import uk.ac.ebi.ddi.xml.validator.utils.Tuple;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Set;


public class OmicsXMLFileTest {

    File file;

    OmicsXMLFile reader;

    @Before
    public void setUp() throws Exception {

        //URL fileURL = OmicsXMLFileTest.class.getClassLoader().getResource("px_example_data.xml");
        //URL fileURL = OmicsXMLFileTest.class.getClassLoader().getResource("by-PRIDE_EBEYE_PXD002837_bycovid.xml");
        URL fileURL = OmicsXMLFileTest.class.getClassLoader().getResource("PRIDE_EBEYE_Wrong_PRD000123.xml");
        //by-covid_cessda_omicsdi_noid.xml,/home/gaur/px_example_data.xml,PRIDE_EBEYE_Wrong_PRD000123.xml

        file = new File(fileURL.toURI());

        reader = new OmicsXMLFile(file);

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetEntryById() throws Exception {

        Entry entry = reader.getEntryById("PRD000123");

        Assert.assertEquals(entry.getName().getValue(), "Large scale qualitative and quantitative profiling of tyrosine phosphorylation using a combination of phosphopeptide immuno-affinity purification and stable isotope dimethyl labeling");

        System.out.println(entry.toString());

    }

    @Test
    public void testGetEntryIds() throws Exception {

        Assert.assertEquals(reader.getEntryIds().size(),1);

    }

    @Test
    public void marshall() throws DDIException {

        FileWriter fw;
        File tmpFile;
        try {
            tmpFile = File.createTempFile("tmpMzML", ".xml");
            tmpFile.deleteOnExit();
            fw = new FileWriter(tmpFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Could not create or write to temporary file for marshalling.");
        }

        OmicsDataMarshaller mm = new OmicsDataMarshaller();

        Entry entry = reader.getEntryById("PRD000123");

        Database database = new Database();
        database.setDescription("new description");
        database.setEntryCount(10);
        database.setRelease("2010");
        Entries entries = new Entries();
        entries.addEntry(entry);
        database.setEntries(entries);
        mm.marshall(database, fw);

        OmicsXMLFile.isSchemaValid(tmpFile);


    }

    @Test
    public void testGetEntryByIndex() throws Exception {

        int index = 0;

        Entry entry = reader.getEntryByIndex(index);

        Assert.assertEquals(entry.getName().getValue(), "Large scale qualitative and quantitative profiling of tyrosine phosphorylation using a combination of phosphopeptide immuno-affinity purification and stable isotope dimethyl labeling");

        System.out.println(entry.toString());

    }

    @Test
    public void testSemanticValidation(){

        //List<Tuple> errorsSchema = OmicsXMLFile.validateSchema(this.file);
        //errorsSchema.stream().forEach(r-> System.out.println(r.getValue().toString()));

        Set<Tuple> errors = OmicsXMLFile.validateSemantic(this.file);
        //errors.stream().forEach(r-> System.out.println(r.getValue().toString()));
        errors.stream().filter(r -> r.getKey().equals("Error")).forEach(r-> System.out.println(r.getValue().toString()));
    }
}
