
package uk.ac.ebi.ddi.xml.validator.parser.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for refType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="refType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="dbname" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="dbkey" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="boost"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}float"&gt;
 *             &lt;minInclusive value="1"/&gt;
 *             &lt;maxInclusive value="3"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "refType")
public class Reference implements Serializable, IDataObject {

    private static final long serialVersionUID = 105L;
    @XmlAttribute(name = "dbname", required = true)
    protected String dbname;
    @XmlAttribute(name = "dbkey", required = true)
    protected String dbkey;
    @XmlAttribute(name = "boost")
    protected Float boost;

    /**
     * Gets the value of the dbname property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDbname() {
        return dbname;
    }

    /**
     * Sets the value of the dbname property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDbname(String value) {
        this.dbname = value;
    }

    /**
     * Gets the value of the dbkey property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDbkey() {
        return dbkey;
    }

    /**
     * Sets the value of the dbkey property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDbkey(String value) {
        this.dbkey = value;
    }

    /**
     * Gets the value of the boost property.
     *
     * @return possible object is
     * {@link Float }
     */
    public Float getBoost() {
        return boost;
    }

    /**
     * Sets the value of the boost property.
     *
     * @param value allowed object is
     *              {@link Float }
     */
    public void setBoost(Float value) {
        this.boost = value;
    }

    @Override
    public String toString() {
        return "Reference{" +
                "dbname='" + dbname + '\'' +
                ", dbkey='" + dbkey + '\'' +
                ", boost=" + boost +
                '}';
    }
}
