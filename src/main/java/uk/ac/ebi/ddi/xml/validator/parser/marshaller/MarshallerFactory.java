package uk.ac.ebi.ddi.xml.validator.parser.marshaller;

import com.ctc.wstx.stax.WstxOutputFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.ddi.xml.validator.parser.model.ModelConstants;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.util.HashSet;
import java.util.Set;

public class MarshallerFactory extends WstxOutputFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(MarshallerFactory.class);

    private static MarshallerFactory instance = new MarshallerFactory();

    private static JAXBContext jc = null;

    public  Set<String> emptyElements = new HashSet<>();

    public static MarshallerFactory getInstance() {
        return instance;
    }

    private MarshallerFactory() {
        emptyElements.add("ref");
    }

    public Marshaller initializeMarshaller() {
        LOGGER.debug("Initializing Marshaller for mzML.");
        try {
            // Lazy caching of JAXB context.
            if (jc == null) {
                jc = JAXBContext.newInstance(ModelConstants.MODEL_PKG);
            }
            //create marshaller and set basic properties

            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(ModelConstants.JAXB_ENCODING_PROPERTY, "UTF-8");
            marshaller.setProperty(ModelConstants.JAXB_FORMATTING_PROPERTY, false);

            // Register a listener that calls before/afterMarshalOperation on ParamAlternative/-List objects.
            // See: ParamAlternative.beforeMarshalOperation and ParamAlternativeList.beforeMarshalOperation
            marshaller.setListener(new ObjectClassListener());

            LOGGER.info("Marshaller initialized");

            return marshaller;

        } catch (JAXBException e) {
            LOGGER.error("MarshallerFactory.initializeMarshaller", e);
            throw new IllegalStateException("Can't initialize marshaller: " + e.getMessage());
        }
    }

}
