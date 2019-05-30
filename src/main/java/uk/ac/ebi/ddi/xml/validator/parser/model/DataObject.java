package uk.ac.ebi.ddi.xml.validator.parser.model;

import javax.xml.bind.annotation.XmlTransient;

public abstract class DataObject implements IDataObject {

    @XmlTransient
    protected long hid;

    public long getHid() {
        return hid;
    }
}
