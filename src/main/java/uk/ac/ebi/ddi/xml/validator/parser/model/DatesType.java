
package uk.ac.ebi.ddi.xml.validator.parser.model;

import org.omg.CORBA.PUBLIC_MEMBER;
import uk.ac.ebi.ddi.xml.validator.utils.*;
import uk.ac.ebi.ddi.xml.validator.utils.Field;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for datesType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="datesType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="date" type="{}dateType" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "datesType", propOrder = {
    "date"
})
public class DatesType
    implements Serializable, IDataObject
{

    private final static long serialVersionUID = 105L;
    @XmlElement(required = true)
    protected List<Date> date;

    /**
     * Gets the value of the date property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the date property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDate().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Date }
     * 
     * 
     */
    public List<Date> getDate() {
        if (date == null) {
            date = new ArrayList<Date>();
        }
        return this.date;
    }

    public boolean isEmpty(){
        date = getDate();
        return date.isEmpty();
    }

    public boolean containsPublicationDate(){
        if(date != null && !date.isEmpty()){
            for(Date dateField: date){
                if(dateField.getType().equalsIgnoreCase(Field.PUBLICATION.getName()))
                    return true;
            }
        }
        return false;
    }

    public void addDefaultPublicationDate() {
        String toAdd = null;
        if(date !=null && !date.isEmpty()){
            for(Date dateField: date){
                if(dateField.getType().equalsIgnoreCase(Field.PUBLICATION_UPDATED.getName()))
                   toAdd = dateField.getValue();
            }
        }
        if(toAdd != null)
            date.add(new Date(Field.PUBLICATION.getName(), toAdd));
    }
}
