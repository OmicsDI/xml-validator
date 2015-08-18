package uk.ac.ebi.ddi.xml.validator.parser.unmarshaller;

import uk.ac.ebi.ddi.xml.validator.parser.model.DataElement;
import uk.ac.ebi.ddi.xml.validator.parser.model.DataObject;


public interface OmicsDataUnmarshaller {
	public <T extends DataObject> T unmarshal(String xmlSnippet, DataElement element) throws Exception;
}
