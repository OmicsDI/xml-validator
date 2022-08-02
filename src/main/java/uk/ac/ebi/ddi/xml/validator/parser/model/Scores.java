package uk.ac.ebi.ddi.xml.validator.parser.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by gaur on 20/09/17.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "additional_fieldsType", propOrder = {
        "field"
})
public class Scores {
    private int citationCount;

    private int reanalysisCount;

    private int viewCount;

    private int searchCount;

    public int getCitationCount() {
        return citationCount;
    }

    public void setCitationCount(int citationCount) {
        this.citationCount = citationCount;
    }

    public int getReanalysisCount() {
        return reanalysisCount;
    }

    public void setReanalysisCount(int reanalysisCount) {
        this.reanalysisCount = reanalysisCount;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(int searchCount) {
        this.searchCount = searchCount;
    }

}
