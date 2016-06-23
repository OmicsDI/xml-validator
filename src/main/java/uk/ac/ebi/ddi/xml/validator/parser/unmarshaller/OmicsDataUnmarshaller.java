package uk.ac.ebi.ddi.xml.validator.parser.unmarshaller;

import uk.ac.ebi.ddi.xml.validator.parser.model.DataElement;
import uk.ac.ebi.ddi.xml.validator.parser.model.IDataObject;


public interface OmicsDataUnmarshaller {
	public <T extends IDataObject> T unmarshal(String xmlSnippet, DataElement element) throws Exception;
}
