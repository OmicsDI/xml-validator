package uk.ac.ebi.ddi.xml.validator.parser.model;

import java.util.HashSet;
import java.util.Set;

public enum DataElement {

    ENTRY("/database/entries/entry", Entry.class),
    DATABASE("/database/", Database.class);

    private final String xpath;

    @SuppressWarnings("rawtypes")
    private final Class type;

    private static final Set<String> XPATHS;

    static {
        XPATHS = new HashSet<>();
        for (DataElement xpath : values()) {
            XPATHS.add(xpath.getXpath());
        }
    }

    DataElement(String xpath, @SuppressWarnings("rawtypes") Class clazz) {
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
        return XPATHS;
    }

}
