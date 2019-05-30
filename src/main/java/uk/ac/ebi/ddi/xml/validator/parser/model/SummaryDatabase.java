package uk.ac.ebi.ddi.xml.validator.parser.model;

public class SummaryDatabase {

    protected String name;

    protected String description;

    protected String release;

    protected String releaseDate;

    protected Integer entryCount;


    public SummaryDatabase() {
    }

    public SummaryDatabase(String name,
                           String description,
                           String release,
                           String releaseDate,
                           Integer entryCount) {
        this.name = name;
        this.description = description;
        this.release = release;
        this.releaseDate = releaseDate;
        this.entryCount = entryCount;
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

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getEntryCount() {
        return entryCount;
    }

    public void setEntryCount(Integer entryCount) {
        this.entryCount = entryCount;
    }
}
