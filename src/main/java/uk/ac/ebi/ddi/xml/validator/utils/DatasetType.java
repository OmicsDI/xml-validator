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
 * Created by ypriverol (ypriverol@gmail.com) on 03/02/2017.
 */
public enum DatasetType {

    DATASET("dataset", "General Dataset", null),
    ASSAY("assay", "Assay within a Dataset", DATASET);

    private String name;
    private String description;
    private DatasetType parentType;

    DatasetType(String dataset, String description, DatasetType datasetType) {
        this.name = dataset;
        this.description = description;
        this.parentType = datasetType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DatasetType getParentType() {
        return parentType;
    }

    public void setParentType(DatasetType parentType) {
        this.parentType = parentType;
    }
}
