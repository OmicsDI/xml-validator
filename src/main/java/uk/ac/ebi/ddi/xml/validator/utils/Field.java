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
    PUBLICATION_UPDATED("updated", FieldType.UNKNOWN, FieldCategory.DATE, "Dataset Updated Date"),
    REPOSITORY("repository", FieldType.MANDATORY, FieldCategory.ADDITIONAL, "Dataset Repository"),
    OMICS("omics_type", FieldType.MANDATORY, FieldCategory.ADDITIONAL, "Dataset Omics Type"),
    LINK("full_dataset_link", FieldType.MANDATORY, FieldCategory.ADDITIONAL, "Full Dataset Link in the Original Database"),
    SUBMITTER("submitter", FieldType.MANDATORY, FieldCategory.ADDITIONAL, "Dataset Submitter information"),
    SUBMITTER_EMAIL("submitter_email", FieldType.SUGGESTED, FieldCategory.ADDITIONAL, "Dataset Submitter email"),
    SUBMITTER_AFFILIATION("submitter_affiliation", FieldType.OPTIONAL, FieldCategory.ADDITIONAL, "Dataset Submitter Affiliation"),
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
    GPMDB_MODEL("model", FieldType.UNKNOWN, FieldCategory.ADDITIONAL, "MODEL"),
    MEDLINE("MEDLINE", FieldType.UNKNOWN, FieldCategory.CROSSREF, "MEDLINE Reference"),
    SUBMISSION_DATE("submission", FieldType.UNKNOWN, FieldCategory.DATE, "Submission Date"),
    SOFTWARE_INFO("software", FieldType.UNKNOWN, FieldCategory.ADDITIONAL, "Software"),
    SPECIE_FIELD("species", FieldType.UNKNOWN, FieldCategory.ADDITIONAL, "Specie"),
    CELL_TYPE_FIELD("cell_type", FieldType.UNKNOWN, FieldCategory.ADDITIONAL, "Cell Type"),
    DISEASE_FIELD("disease", FieldType.UNKNOWN, FieldCategory.ADDITIONAL, "Disease"),
    TISSUE_FIELD("tissue", FieldType.UNKNOWN, FieldCategory.ADDITIONAL, "Tissue"),
    SECONDARY_ACCESSION("additional_accession", FieldType.OPTIONAL,FieldCategory.ADDITIONAL, "Secondary Accession"),
    ENSEMBL_EXPRESSION_ATLAS("ensembl", FieldType.UNKNOWN, FieldCategory.CROSSREF, "Gene reference to ENSEMBL"),

    SUBMITTER_KEYWORDS("submitter_keywords", FieldType.UNKNOWN, FieldCategory.ADDITIONAL, "Submitter Keywords"),
    DATASET_FILE("dataset_file", FieldType.OPTIONAL, FieldCategory.ADDITIONAL, "Dataset File Link"),
    FILE_SIZE("file_size", FieldType.OPTIONAL, FieldCategory.ADDITIONAL, "Total File Size for the Dataset"),
    FILE_COUNT("file_count", FieldType.OPTIONAL, FieldCategory.ADDITIONAL, "Number of Files by Dataset"),
    PTM_MODIFICATIONS("ptm_modification", FieldType.OPTIONAL, FieldCategory.ADDITIONAL, "Posttranslations Modifications"),
    STUDY_FACTORS("study_factor", FieldType.OPTIONAL, FieldCategory.ADDITIONAL, "General Study Factor"),
    TECHNOLOGY_TYPE("technology_type", FieldType.OPTIONAL, FieldCategory.ADDITIONAL, "Additional Tags of the Technology"),
    PROTEOMEXCHANGE_TYPE_SUBMISSION("proteomexchange_type_submission", FieldType.OPTIONAL, FieldCategory.ADDITIONAL, "Complete or Partial Submission"),
    PUBCHEM_ID("pubchem_id", FieldType.OPTIONAL, FieldCategory.ADDITIONAL, "Pubchem Metabolite Identifier"),
    METABOLITE_NAME("metabolite_name", FieldType.OPTIONAL, FieldCategory.ADDITIONAL, "Metabolite Name"),
    PROTEIN_NAME("protein_name", FieldType.OPTIONAL, FieldCategory.ADDITIONAL, "Protein Names for the Database"),
    FUNDING("funding", FieldType.OPTIONAL, FieldCategory.ADDITIONAL, "Funding agency or grant"),
    CURATOR_KEYWORDS("curator_keywords", FieldType.UNKNOWN, FieldCategory.ADDITIONAL, "Submitter Keywords"),
    DATASET_TYPE("dataset_type", FieldType.MANDATORY, FieldCategory.ADDITIONAL, "Type of Experiment"),
    GENE_NAME("gene_name",FieldType.OPTIONAL ,FieldCategory.ADDITIONAL , "Additional Gene Name");

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
        List<Field> vReturn = new ArrayList<>();
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
        List<Field> vReturn = new ArrayList<>();
        for (Field value: values){
            if(value.getCategory() == category)
                vReturn.add(value);
        }
        return vReturn;
    }

    public String getFullName() {
        return fullName;
    }

    public static List<Field> getValuesByCategory(FieldCategory category, FieldType unknown) {
        Field[] values = Field.values();
        List<Field> vReturn = new ArrayList<>();
        for (Field value: values){
            if(value.getCategory() == category && !(value.getType() == unknown))
                vReturn.add(value);
        }
        return vReturn;
    }
}
