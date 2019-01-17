package uk.ac.ebi.ddi.xml.validator.parser.unmarshaller;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import uk.ac.ebi.ddi.xml.validator.parser.model.DataElement;
import uk.ac.ebi.ddi.xml.validator.parser.model.IDataObject;
import uk.ac.ebi.ddi.xml.validator.parser.model.ModelConstants;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.sax.SAXSource;
import java.io.StringReader;


public class OmicsUnmarshallerFactory {

    private static final Logger LOGGER = Logger.getLogger(OmicsUnmarshallerFactory.class);

    private static OmicsUnmarshallerFactory instance;
    private static JAXBContext jc;

    private OmicsUnmarshallerFactory() {
    }

    public static OmicsUnmarshallerFactory getInstance() {
        if (instance == null) {
            instance = new OmicsUnmarshallerFactory();
        }
        return instance;
    }

    public OmicsDataUnmarshaller initializeUnmarshaller() {

        try {
            if (jc == null) {
                jc = JAXBContext.newInstance(ModelConstants.MODEL_PKG);
            }
            return new OmicsDataUnmarshallerImpl();

        } catch (JAXBException e) {
            LOGGER.error("UnmarshallerFactory.initializeUnmarshaller", e);
            throw new IllegalStateException("Could not initialize unmarshaller", e);
        }
    }

    private class OmicsDataUnmarshallerImpl implements OmicsDataUnmarshaller {

        private Unmarshaller unmarshaller;

        private OmicsDataUnmarshallerImpl() throws JAXBException {
            unmarshaller = jc.createUnmarshaller();
        }

        /**
         * Add synchronization feature, unmarshaller is not thread safe by default.
         *
         * @param xmlSnippet raw xml string
         * @param element    The mzXML element to unmarshall
         * @param <T>        an instance of class type.
         * @return T    return an instance of class type.
         */
        @Override
        public synchronized <T extends IDataObject> T unmarshal(String xmlSnippet, DataElement element)
                throws Exception {

            if (xmlSnippet == null || element == null) {
                return null;
            }

            @SuppressWarnings("unchecked")
            JAXBElement<T> holder = unmarshaller.unmarshal(
                    new SAXSource(new InputSource(new StringReader(xmlSnippet))), element.getClassType());
            return holder.getValue();
        }
    }
}

