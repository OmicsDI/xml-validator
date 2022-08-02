package uk.ac.ebi.ddi.xml.validator.utils;

import org.junit.Assert;
import org.junit.Test;

public class UtilsTest {

    @Test
    public void testDate() {
        Assert.assertTrue(Utils.validateDate("2017-01-16"));
    }

    @Test
    public void testDate_FurtherInTheFuture_ShouldReturnFalse() {
        Assert.assertTrue(!Utils.validateDate("2023-01-16"));
    }
}
