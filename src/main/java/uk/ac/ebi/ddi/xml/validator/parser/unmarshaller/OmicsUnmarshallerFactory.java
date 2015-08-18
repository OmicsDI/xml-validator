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

	private static final Logger logger = Logger.getLogger(OmicsUnmarshallerFactory.class);

    private static OmicsUnmarshallerFactory instance = new OmicsUnmarshallerFactory();
    private static JAXBContext jc = null;

    private OmicsUnmarshallerFactory() {
    }

    public static OmicsUnmarshallerFactory getInstance() {
        return instance;
    }

    public OmicsDataUnmarshaller initializeUnmarshaller() {

        try {
            // Lazy caching of the JAXB Context.
            if (jc == null) {
                jc = JAXBContext.newInstance(ModelConstants.MODEL_PKG);
            }

            //create unmarshaller
            OmicsDataUnmarshaller pum = new OmicsDataUnmarshallerImpl();
            logger.debug("Unmarshaller Initialized");

            return pum;

        } catch (JAXBException e) {
            logger.error("UnmarshallerFactory.initializeUnmarshaller", e);
            throw new IllegalStateException("Could not initialize unmarshaller", e);
        }
    }

    private class OmicsDataUnmarshallerImpl implements OmicsDataUnmarshaller {

        private Unmarshaller unmarshaller = null;

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
			
			T retval;
            try {

                if (xmlSnippet == null || element == null) {
                    return null;
                }

                @SuppressWarnings("unchecked")
                JAXBElement<T> holder = unmarshaller.unmarshal(new SAXSource(new InputSource(new StringReader(xmlSnippet))), element.getClassType());
                retval = holder.getValue();

            } catch (JAXBException e) {
                throw new Exception("Error unmarshalling object: " + e.getMessage(), e);
            }

            return retval;
		}

    }
}

