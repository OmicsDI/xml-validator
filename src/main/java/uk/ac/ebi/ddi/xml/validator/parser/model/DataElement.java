package uk.ac.ebi.ddi.xml.validator.parser.model;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 18/08/2015
 */
public enum DataElement {

    ENTRY(		"/database/entries/entry", Entry.class);

    private final String xpath;

    @SuppressWarnings("rawtypes")
    private final Class type;

    private static final Set<String> xpaths;

    static {
        xpaths = new HashSet<String>();
        for (DataElement xpath : values()) {
            xpaths.add(xpath.getXpath());
        }
    }

    private DataElement(String xpath, @SuppressWarnings("rawtypes") Class clazz) {
        this.xpath = xpath;
        this.type = clazz;
    }

    public String getXpath() {
        return xpath;
    }

    @SuppressWarnings("rawtypes")
    public Class getClassType() {
        return type;
    }

    public static Set<String> getXpaths() {
        return xpaths;
    }

}
