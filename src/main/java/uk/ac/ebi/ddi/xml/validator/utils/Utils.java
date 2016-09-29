package uk.ac.ebi.ddi.xml.validator.utils;

import org.apache.log4j.Logger;
import uk.ac.ebi.ddi.xml.validator.parser.model.*;
import uk.ac.ebi.ddi.xml.validator.parser.model.Date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 19/08/2015
 */
public class Utils {

    private static final Logger logger = Logger.getLogger(Utils.class);

    public static String ERROR = "Error";
    public static String WARN  = "Warn";

    public static String NOT_FOUND_MESSAGE = "The entry do not contain:";
    public static String NOT_FOUND_UPDATED = "The entry do not contain or is out of range:";
    public static String ENTRY_NOT_FOUND = "Entry:";
    public static String REPORT_SPACE  = " ";


    public static List<Tuple> validateSemantic(Entry entry){

        List<Tuple> errors = new ArrayList<>();

        if(entry.getId() == null || entry.getId().isEmpty())
            errors.add(new Tuple(ERROR, NOT_FOUND_MESSAGE + REPORT_SPACE + Field.ID.getFullName()));

        if(entry.getName() == null || entry.getName().getValue() == null || entry.getName().getValue().isEmpty())
            errors.add(new Tuple(ERROR, ENTRY_NOT_FOUND + REPORT_SPACE + entry.getId() + REPORT_SPACE + NOT_FOUND_MESSAGE + Field.NAME));

        if(entry.getDescription() == null || entry.getDescription().isEmpty()){
            errors.add(new Tuple(ERROR, ENTRY_NOT_FOUND + REPORT_SPACE + entry.getId() + REPORT_SPACE + NOT_FOUND_MESSAGE + REPORT_SPACE+ Field.DESCRIPTION));
        }

        if(entry.getDates() != null && !entry.getDates().isEmpty()){
            List<Field> fields = Field.getValuesByCategory(FieldCategory.DATE, FieldType.UNKNOWN);
            for (Field field: fields){
                String errorCode = (field.getType() == FieldType.MANDATORY)? ERROR: WARN;
                boolean found = false;
                for(Date date: entry.getDates().getDate())
                    if(field.getName().equalsIgnoreCase(date.getType()) && date.getValue() != null && validateDate(date.getValue()))
                        found = true;
                if(!found){
                    errors.add(new Tuple(errorCode, ENTRY_NOT_FOUND + REPORT_SPACE + entry.getId() + REPORT_SPACE + NOT_FOUND_UPDATED + REPORT_SPACE + field.getFullName()));
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
                    errors.add(new Tuple(errorCode, ENTRY_NOT_FOUND + REPORT_SPACE + entry.getId() + REPORT_SPACE + NOT_FOUND_MESSAGE + REPORT_SPACE + field.getFullName()));
                }
            }
        }

        if(entry.getAdditionalFields() != null && !entry.getAdditionalFields().isEmpty()){
            List<Field> fields = Field.getValuesByCategory(FieldCategory.ADDITIONAL, FieldType.UNKNOWN);
            for (Field field: fields){
                String errorCode = (field.getType() == FieldType.MANDATORY)? ERROR: WARN;
                boolean found = false;
                for(uk.ac.ebi.ddi.xml.validator.parser.model.Field ref: entry.getAdditionalFields().getField())
                    if(field.getName().equalsIgnoreCase(ref.getName()) && ref.getName() != null)
                        found = true;
                if(!found){
                    errors.add(new Tuple(errorCode, ENTRY_NOT_FOUND + REPORT_SPACE + entry.getId() + REPORT_SPACE+ NOT_FOUND_MESSAGE + REPORT_SPACE + field.getFullName()));
                }
            }
        }

        return errors;
    }

    private static boolean validateDate(String value) {
        String[] dateValues = new String[]{"yyyy-MM-dd", "dd-MMM-yyyy HH:mm:ss"};

        for(String dateStr: dateValues){
            try {
                java.util.Date date = new SimpleDateFormat(dateStr).parse(value);
                java.util.Date currentDate = new java.util.Date();
                if(currentDate.compareTo(date) >= 0)
                    return true;
            } catch (ParseException e) {
                logger.debug(e.getMessage());
            }
        }

        return false;
    }


}
