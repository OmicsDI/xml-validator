package uk.ac.ebi.ddi.xml.validator.utils;

import uk.ac.ebi.ddi.xml.validator.parser.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 19/08/2015
 */
public class Utils {

    public static String ERROR = "ERROR";
    public static String WARN   = "WARN";


    public static List<Tuple> validateSemantic(Entry entry){

        List<Tuple> errors = new ArrayList<Tuple>();

        if(entry.getId() == null || entry.getId().isEmpty())
            errors.add(new Tuple(ERROR, "The entry do not contain Id"));

        if(entry.getName() == null || entry.getName().getValue() == null || entry.getName().getValue().isEmpty())
            errors.add(new Tuple(ERROR, entry.getId() + "\tThe entry do not contain Name"));

        if(entry.getDescription() == null || entry.getDescription().isEmpty()){
            errors.add(new Tuple(WARN, entry.getId() + "\tThe entry do not contain Name"));
        }

        if(entry.getDates() != null && !entry.getDates().isEmpty()){
            List<Field> fields = Field.getValuesByCategory(FieldCategory.DATE);
            for (Field field: fields){
                String errorCode = (field.getType() == FieldType.MANDATORY)? ERROR: WARN;
                boolean found = false;
                for(Date date: entry.getDates().getDate())
                    if(field.getName().equalsIgnoreCase(date.getType()) && date.getValue() != null)
                        found = true;
                if(!found){
                    errors.add(new Tuple(errorCode, entry.getId() + "\t The entry do not contain the Field\t" + field.getName()));
                }
            }
        }

        if(entry.getCrossReferences() != null && !entry.getCrossReferences().isEmpty()){
            List<Field> fields = Field.getValuesByCategory(FieldCategory.CROSSREF);
            for (Field field: fields){
                String errorCode = (field.getType() == FieldType.MANDATORY)? ERROR: WARN;
                boolean found = false;
                for(Reference ref: entry.getCrossReferences().getRef())
                    if(field.getName().equalsIgnoreCase(ref.getDbname()) && ref.getDbname() != null)
                        found = true;
                if(!found){
                    errors.add(new Tuple(errorCode, entry.getId() + "\t The entry do not contain the Field\t" + field.getName()));
                }
            }
        }

        if(entry.getAdditionalFields() != null && !entry.getAdditionalFields().isEmpty()){
            List<Field> fields = Field.getValuesByCategory(FieldCategory.ADDITIONAL);
            for (Field field: fields){
                String errorCode = (field.getType() == FieldType.MANDATORY)? ERROR: WARN;
                boolean found = false;
                for(uk.ac.ebi.ddi.xml.validator.parser.model.Field ref: entry.getAdditionalFields().getField())
                    if(field.getName().equalsIgnoreCase(ref.getName()) && ref.getName() != null)
                        found = true;
                if(!found){
                    errors.add(new Tuple(errorCode, entry.getId() + "\t The entry do not contain the Field\t" + field.getName()));
                }
            }
        }

        return errors;
    }
}
