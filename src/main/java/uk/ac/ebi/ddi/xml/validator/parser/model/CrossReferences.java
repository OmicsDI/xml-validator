
package uk.ac.ebi.ddi.xml.validator.parser.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for cross_referencesType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="cross_referencesType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ref" type="{}refType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cross_referencesType", propOrder = {
        "ref"
})
public class CrossReferences
        implements Serializable, IDataObject {

    private static final long serialVersionUID = 105L;

    protected List<Reference> ref;

    /**
     * Gets the value of the ref property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ref property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRef().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Reference }
     *
     * @return list of references
     */
    public List<Reference> getRef() {
        if (ref == null) {
            ref = new ArrayList<>();
        }
        return this.ref;
    }

    public boolean isEmpty() {
        ref = getRef();
        return ref.isEmpty();
    }

    @Override
    public String toString() {
        return "CrossReferences{" +
                "ref=" + ref +
                '}';
    }

    public void setRef(List<Reference> ref) {
        this.ref = ref;
    }

    public boolean containsValue(String value) {
        if (ref != null && !ref.isEmpty()) {
            for (Reference reference : ref) {
                if (reference.getDbkey().equalsIgnoreCase(value)) {
                    return true;
                }
            }
        }
        return false;
    }
}
