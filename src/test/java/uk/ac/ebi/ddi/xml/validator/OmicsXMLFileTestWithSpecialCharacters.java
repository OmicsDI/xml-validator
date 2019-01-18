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


public class OmicsXMLFileTestWithSpecialCharacters {

    File file;

    OmicsXMLFile reader;

    @Before
    public void setUp() throws Exception {

        URL fileURL = this.getClass().getClassLoader().getResource("PRIDE_EBEYE_PXD007896.xml");

        file = new File(fileURL.toURI());

        reader = new OmicsXMLFile(file);

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetEntryIds() throws Exception {
        Assert.assertEquals(reader.getEntryIds().size(),1);
        reader.getEntryById("PXD007896");
    }
}