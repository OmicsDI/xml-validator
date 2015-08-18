package uk.ac.ebi.ddi.xml.validator;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.ddi.xml.validator.parser.model.Entry;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.*;

public class OmicsXMLFileTest {

    File file;

    OmicsXMLFile reader;

    @Before
    public void setUp() throws Exception {

        URL fileURL = OmicsXMLFileTest.class.getClassLoader().getResource("PRIDE_EBEYE_PRD000123.xml");

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
}