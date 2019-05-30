
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
 */
@XmlRegistry
public class ObjectFactory {

    private static final QName DATABASE_QNAME = new QName("", "database");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package:
     * uk.ac.ebi.ddi.xml.validator.parser.model
     */
    public ObjectFactory() {
    }

    public Entry createEntry() {
        return new Entry();
    }

    public Database createDatabase() {
        return new Database();
    }

    public Entries createEntries() {
        return new Entries();
    }

    public AdditionalFields createAdditionalFields() {
        return new AdditionalFields();
    }

    public Field createField() {
        return new Field();
    }

    public CrossReferences createCrossReferences() {
        return new CrossReferences();
    }

    public Reference createReference() {
        return new Reference();
    }

    public DatesType createDatesType() {
        return new DatesType();
    }

    public Date createDate() {
        return new Date();
    }

    public Entry.Name createEntryName() {
        return new Entry.Name();
    }

    @XmlElementDecl(namespace = "", name = "database")
    public JAXBElement<Database> createDatabase(Database value) {
        return new JAXBElement<>(DATABASE_QNAME, Database.class, null, value);
    }

}
