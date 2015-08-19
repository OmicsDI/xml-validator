package uk.ac.ebi.ddi.xml.validator.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 19/08/2015
 */
public enum Field {

    PUBLICATION("publication", FieldType.MANDATORY, FieldCategory.DATE),
    REPOSITORY("repository", FieldType.MANDATORY, FieldCategory.ADDITIONAL),
    OMICS("omics_type", FieldType.MANDATORY, FieldCategory.ADDITIONAL),
    LINK("full_dataset_link", FieldType.MANDATORY, FieldCategory.ADDITIONAL),
    SUBMITTER("submitter", FieldType.MANDATORY, FieldCategory.ADDITIONAL),
    SUBMITTER_EMAIL("submitter_email", FieldType.SUGGESTED, FieldCategory.ADDITIONAL),
    TAXONOMY("TAXONOMY", FieldType.SUGGESTED, FieldCategory.CROSSREF),
    PUBMED("pubmed", FieldType.SUGGESTED, FieldCategory.CROSSREF),
    INSTRUMENT("instrument_platform", FieldType.SUGGESTED, FieldCategory.ADDITIONAL),
    DATA("data_protocol", FieldType.SUGGESTED, FieldCategory.ADDITIONAL),
    SAMPLE("sample_protocol", FieldType.SUGGESTED, FieldCategory.ADDITIONAL);

    private final String name;
    private final FieldType type;
    private final FieldCategory category;


    private Field(String name, FieldType type, FieldCategory category) {
        this.name = name;
        this.type = type;
        this.category = category;

    }

    /**
     * Return a set of Properties by the Type of the Field if they are mandatory or
     * not.
     * @param type
     * @return
     */
    public static List<Field> getValuesByType(FieldType type){
        Field[] values = Field.values();
        List<Field> vReturn = new ArrayList<Field>();
        for (Field value: values){
            if(value.getType() == type)
                vReturn.add(value);
        }
        return vReturn;
    }

    public String getName() {
        return name;
    }

    public FieldType getType() {
        return type;
    }

    public FieldCategory getCategory() {
        return category;
    }

    public static List<Field> getValuesByCategory(FieldCategory category){
        Field[] values = Field.values();
        List<Field> vReturn = new ArrayList<Field>();
        for (Field value: values){
            if(value.getCategory() == category)
                vReturn.add(value);
        }
        return vReturn;
    }
}
