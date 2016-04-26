package uk.ac.ebi.ddi.xml.validator.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 19/08/2015
 */
public enum Field {

    ID("id", FieldType.MANDATORY, FieldCategory.DATA, "Dataset Identifier"),
    NAME("name", FieldType.MANDATORY, FieldCategory.DATA, "Dataset Name"),
    DESCRIPTION("description", FieldType.MANDATORY, FieldCategory.DATA, "Dataset Description"),
    PUBLICATION("publication", FieldType.MANDATORY, FieldCategory.DATE, "Dataset Publication Date"),
    PUBLICATION_UPDATED("updated", FieldType.OPTIONAL, FieldCategory.DATE, "Dataset Updated Date"),
    REPOSITORY("repository", FieldType.MANDATORY, FieldCategory.ADDITIONAL, "Dataset Repository"),
    OMICS("omics_type", FieldType.MANDATORY, FieldCategory.ADDITIONAL, "Dataset Omics Type"),
    LINK("full_dataset_link", FieldType.MANDATORY, FieldCategory.ADDITIONAL, "Full Dataset Link in the Original Database"),
    SUBMITTER("submitter", FieldType.MANDATORY, FieldCategory.ADDITIONAL, "Dataset Submitter information"),
    SUBMITTER_EMAIL("submitter_email", FieldType.SUGGESTED, FieldCategory.ADDITIONAL, "Dataset Submitter email"),
    TAXONOMY("TAXONOMY", FieldType.SUGGESTED, FieldCategory.CROSSREF, "Dataset NCBI TAXONOMY"),
    PUBMED("pubmed", FieldType.SUGGESTED, FieldCategory.CROSSREF, "Dataset Pubmed Id"),
    INSTRUMENT("instrument_platform", FieldType.SUGGESTED, FieldCategory.ADDITIONAL, "Instrument Platform"),
    DATA("data_protocol", FieldType.SUGGESTED, FieldCategory.ADDITIONAL, "Dataset Abstract Data Protocol"),
    SAMPLE("sample_protocol", FieldType.SUGGESTED, FieldCategory.ADDITIONAL, "Dataset Abstract Sample Protocol"),
    ENRICH_TITLE("name_synonyms", FieldType.UNKNOWN, FieldCategory.ADDITIONAL, "Synonyms"),
    ENRICH_ABSTRACT("description_synonyms", FieldType.UNKNOWN, FieldCategory.ADDITIONAL, "Description Synonyms"),
    ENRICH_SAMPLE("sample_synonyms", FieldType.UNKNOWN, FieldCategory.ADDITIONAL, "Sample Synonyms"),
    ENRICH_DATA("data_synonyms", FieldType.UNKNOWN, FieldCategory.ADDITIONAL, "Data Synonyms"),
    PUBMED_ABSTRACT("pubmed_abstract", FieldType.UNKNOWN, FieldCategory.ADDITIONAL, "PUBMED Abstract"),
    PUBMED_TITLE("pubmed_title", FieldType.UNKNOWN, FieldCategory.ADDITIONAL, "Pubmed Title Synonyms"),
    PUBMED_AUTHORS("pubmed_authors", FieldType.UNKNOWN, FieldCategory.ADDITIONAL, "Pubmed Authors"),
    ENRICH_PUBMED_ABSTRACT("pubmed_abstract_synonyms", FieldType.UNKNOWN, FieldCategory.ADDITIONAL, "Pubmed Abstract Synonyms"),
    ENRICHE_PUBMED_TITLE("pubmed_title_synonyms", FieldType.UNKNOWN, FieldCategory.ADDITIONAL, "Pubmed title Synonyms"),
    GPMDB_MODEL("model", FieldType.OPTIONAL, FieldCategory.ADDITIONAL, "MODEL");


    private final String name;
    private final FieldType type;
    private final FieldCategory category;
    private final String fullName;


    Field(String name, FieldType type, FieldCategory category, String fullName) {
        this.name = name;
        this.type = type;
        this.category = category;
        this.fullName = fullName;

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

    public String getFullName() {
        return fullName;
    }
}
