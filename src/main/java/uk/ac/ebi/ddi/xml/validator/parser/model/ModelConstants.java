package uk.ac.ebi.ddi.xml.validator.parser.model;

import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Map;

public class ModelConstants {
    public static final String MODEL_PKG = "uk.ac.ebi.ddi.xml.validator.parser.model";
    public static final String JAXB_ENCODING_PROPERTY = "jaxb.encoding";
    public static final String JAXB_FORMATTING_PROPERTY = "jaxb.formatted.output";
    public static final String JAXB_FRAGMENT_PROPERTY = "jaxb.fragment";

    private static Map<Class, QName> indexClass = new HashMap<>();

    public static QName getQNameForClass(Class<? extends IDataObject> aClass) {
        indexClass.put(Database.class, new QName("database"));
        indexClass.put(Entry.class, new QName("entry"));
        return indexClass.get(aClass);
    }

}

