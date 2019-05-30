package uk.ac.ebi.ddi.xml.validator.parser.marshaller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.Marshaller;

public class ObjectClassListener extends Marshaller.Listener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectClassListener.class);

    public void beforeMarshal(Object source) {
        //this class will only be associated with a Marshaller when
        //the logging level is set to DEBUG
        LOGGER.debug("marshalling: " + source.getClass());

    }

    public void afterMarshal(Object source) {
        LOGGER.debug("  marshalled: " + source.getClass());
    }
}
