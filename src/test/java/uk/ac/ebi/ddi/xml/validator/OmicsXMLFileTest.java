package uk.ac.ebi.ddi.xml.validator;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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

    @Test


    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetEntryById() throws Exception {

    }

    @Test
    public void testGetEntryIds() throws Exception {

        Assert.assertEquals(reader.getEntryIds().size(),1);

    }
}