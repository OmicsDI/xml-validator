
package uk.ac.ebi.ddi.xml.validator.parser.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the uk.ac.ebi.ddi.xml.validator.parser.model package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Database_QNAME = new QName("", "database");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: uk.ac.ebi.ddi.xml.validator.parser.model
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Entry }
     * 
     */
    public Entry createEntry() {
        return new Entry();
    }

    /**
     * Create an instance of {@link Database }
     * 
     */
    public Database createDatabase() {
        return new Database();
    }

    /**
     * Create an instance of {@link Entries }
     * 
     */
    public Entries createEntries() {
        return new Entries();
    }

    /**
     * Create an instance of {@link AdditionalFields }
     * 
     */
    public AdditionalFields createAdditionalFields() {
        return new AdditionalFields();
    }

    /**
     * Create an instance of {@link Field }
     * 
     */
    public Field createField() {
        return new Field();
    }

    /**
     * Create an instance of {@link CrossReferences }
     * 
     */
    public CrossReferences createCrossReferences() {
        return new CrossReferences();
    }

    /**
     * Create an instance of {@link Reference }
     * 
     */
    public Reference createReference() {
        return new Reference();
    }

    /**
     * Create an instance of {@link DatesType }
     * 
     */
    public DatesType createDatesType() {
        return new DatesType();
    }

    /**
     * Create an instance of {@link Date }
     * 
     */
    public Date createDate() {
        return new Date();
    }

    /**
     * Create an instance of {@link Entry.Name }
     * 
     */
    public Entry.Name createEntryName() {
        return new Entry.Name();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Database }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "database")
    public JAXBElement<Database> createDatabase(Database value) {
        return new JAXBElement<>(_Database_QNAME, Database.class, null, value);
    }

}
