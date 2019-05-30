
package uk.ac.ebi.ddi.xml.validator.parser.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for entriesType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="entriesType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="entry" type="{}entryType" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "entriesType", propOrder = {
        "entry"
})
public class Entries
        implements Serializable, IDataObject {

    private static long serialVersionUID = 105L;
    @XmlElement(required = true)
    protected List<Entry> entry;

    /**
     * Default constructor
     */
    public Entries() {
    }

    /**
     * Constructor with all the entries by default
     */
    public Entries(List<Entry> entry) {
        this.entry = entry;
    }

    /**
     * Gets the value of the entry property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the entry property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEntry().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Entry }
     *
     * @return list of entries
     */

    public List<Entry> getEntry() {
        if (entry == null) {
            entry = new ArrayList<>();
        }
        return this.entry;
    }

    public void addEntry(Entry entry) {
        if (this.entry == null) {
            this.entry = new ArrayList<>();
        }
        this.entry.add(entry);
    }

}
