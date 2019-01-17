package uk.ac.ebi.ddi.xml.validator.utils;

/**
 * This code is licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * ==Overview==
 * <p>
 * This class
 * <p>
 * Created by ypriverol (ypriverol@gmail.com) on 22/01/2017.
 */
public enum OmicsType {

    PROTEOMICS("Proteomics", "Proteomics", null, new String[]{"Proteomics"}),
    METABOLOMICS("Metabolomics", "Metabolomics", null, new String[]{"Metabolomics"}),
    GENOMICS("Genomics", "Genomics", null, new String[]{"Genomics", "Epigeomics"}),
    TRANSCRIPTOMICS("Transcriptomics", "Transcriptomics", null, new String[]{"Transcriptomics"}),
    PHOSPHO_PROTEOMICS("Phosphoproteomics", "Phosphorylation Proteomics ", new OmicsType[]{PROTEOMICS}, new String[]{}),
    INTERACTOMICS("Interactomics", "Interactomics", null, new String[]{""}),
    MULTIOMICS("Multiomics", "Multiomics", null, new String[]{"Interactomics", "Binding"}),
    UKNOWN("UNKNOWN", "Unknown", null, new String[]{"Unknown", "none"});

    private String name;
    private String fullName;
    private OmicsType[] parentType;
    private String[] synonyms;

    OmicsType(String name, String fullName, OmicsType[] parentType, String[] synonyms) {
        this.name = name;
        this.fullName = fullName;
        this.parentType = parentType;
        this.synonyms = synonyms;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public OmicsType[] getParentType() {
        return parentType;
    }

    public void setParentType(OmicsType[] parentType) {
        this.parentType = parentType;
    }

    public static OmicsType getOmicsType(String name) {
        for (OmicsType omicsType: values()) {
            if (omicsType.synonyms != null && omicsType.synonyms.length > 0) {
                for (String synonym : omicsType.synonyms) {
                    if (synonym.equalsIgnoreCase(name)) {
                        return omicsType;
                    }
                }
            }
        }
        return UKNOWN;
    }
}
