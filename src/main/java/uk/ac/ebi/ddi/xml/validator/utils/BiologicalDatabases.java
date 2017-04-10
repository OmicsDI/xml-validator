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
 * Created by ypriverol (ypriverol@gmail.com) on 24/01/2017.
 */
public enum BiologicalDatabases {

    ENSEMBL("ensembl", "ensembl database"),
    GPMDB("gpmdb", "gpmdb protein identifiers"),
    UNIPROT("uniprot", "Uniprot Database");

    private String name;
    private String fullName;

    BiologicalDatabases(String name, String fullName) {
        this.name = name;
        this.fullName = fullName;
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
}
