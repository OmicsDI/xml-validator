package uk.ac.ebi.ddi.xml.validator.parser.marshaller;

import com.ctc.wstx.api.EmptyElementHandler;
import com.ctc.wstx.api.WstxOutputProperties;
import com.ctc.wstx.stax.WstxOutputFactory;
import com.sun.xml.internal.txw2.output.IndentingXMLStreamWriter;
import org.apache.log4j.Logger;

import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLOutputFactory2;
import uk.ac.ebi.ddi.xml.validator.parser.model.Database;
import uk.ac.ebi.ddi.xml.validator.parser.model.IDataObject;
import uk.ac.ebi.ddi.xml.validator.parser.model.ModelConstants;


import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;


public class OmicsDataMarshaller {

    private static final Logger logger = Logger.getLogger(OmicsDataMarshaller.class);

    public <T extends IDataObject> String marshall(T object) {
        StringWriter sw = new StringWriter();
        this.marshall(object, sw);
        return sw.toString();
    }

    public <T extends IDataObject> void marshall(T object, OutputStream os) {
        this.marshall(object, new OutputStreamWriter(os));
    }

    public <T extends IDataObject> void marshall(T object, Writer out) {

        if (object == null) {
            throw new IllegalArgumentException("Cannot marshall a NULL object");
        }

        try {
            MarshallerFactory marshallerFactory  = MarshallerFactory.getInstance();
            Marshaller marshaller = marshallerFactory.initializeMarshaller();
//            marshaller.setProperty(WstxOutputProperties.P_OUTPUT_EMPTY_ELEMENT_HANDLER, new EmptyElementHandler.SetEmptyElementHandler(marshallerFactory.emptyElements));

            // Set JAXB_FRAGMENT_PROPERTY to true for all objects that do not have
            // a @XmlRootElement annotation
            // ToDo: add handling of indexedmzML (-> add flag to control treatment as fragment or not)
            if (!(object instanceof Database)) {
                marshaller.setProperty(ModelConstants.JAXB_FRAGMENT_PROPERTY, true);
                if (logger.isDebugEnabled()) logger.debug("Object '" + object.getClass().getName() +
                                                          "' will be treated as root element.");
            } else {
                if (logger.isDebugEnabled()) logger.debug("Object '" + object.getClass().getName() +
                                                          "' will be treated as fragment.");
            }

            QName aQName = ModelConstants.getQNameForClass(object.getClass());

            // before marshalling out, wrap in a Custom XMLStreamWriter
            // to fix a JAXB bug: http://java.net/jira/browse/JAXB-614
            // also wrapping in IndentingXMLStreamWriter to generate formatted XML
            //XMLStreamWriter xmlStreamWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(out);
            System.setProperty("javax.xml.stream.XMLOutputFactory", "com.sun.xml.internal.stream.XMLOutputFactoryImpl");

//            XMLOutputFactory factory = XMLOutputFactory.newFactory("com.sun.xml.internal.stream.XMLOutputFactoryImpl", null);
            XMLOutputFactory2 factory = new WstxOutputFactory();

            factory.setProperty(WstxOutputProperties.P_OUTPUT_EMPTY_ELEMENT_HANDLER, new EmptyElementHandler.SetEmptyElementHandler(marshallerFactory.emptyElements));
            //XMLOutputFactory factory = XMLOutputFactory.newFactory();
            XMLStreamWriter xmlStreamWriter = factory.createXMLStreamWriter(out);

            IndentingXMLStreamWriter writer = new IndentingXMLStreamWriter(new EscapingXMLStreamWriter(xmlStreamWriter));
            marshaller.marshal( new JAXBElement(aQName, object.getClass(), object), writer );

        } catch (JAXBException e) {
            logger.error("MzMLMarshaller.marshall", e);
            throw new IllegalStateException("Error while marshalling object:" + object.toString());
        } catch (XMLStreamException e) {
            logger.error("MzMLMarshaller.marshall", e);
            throw new IllegalStateException("Error while marshalling object:" + object.toString());
        }

    }

    // ToDo: default marshaller can only cope with mzML or sub-elements 
    // ToDo: ?? new marshal method to create indexedmzML (with parameter specifying the elements to index)

    

}
