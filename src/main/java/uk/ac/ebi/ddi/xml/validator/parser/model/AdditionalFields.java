package uk.ac.ebi.ddi.xml.validator.parser.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for additional_fieldsType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="additional_fieldsType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="field" type="{}fieldType" maxOccurs="unbounded" minOccurs="6"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "additional_fieldsType", propOrder = {
        "field"
})
public class AdditionalFields
        implements Serializable, IDataObject {

    private static final long serialVersionUID = 105L;
    @XmlElement(required = true)
    protected List<Field> field;

    /**
     * Gets the value of the field property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the field property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getField().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Field }
     *
     * @return list of fields
     */
    public List<Field> getField() {
        if (field == null) {
            field = new ArrayList<>();
        }
        return this.field;
    }

    public boolean isEmpty() {
        field = getField();
        return field.isEmpty();
    }

    public void setField(List<Field> field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return "AdditionalFields{" +
                "field=" + field +
                '}';
    }
}
