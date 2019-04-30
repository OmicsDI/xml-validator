package uk.ac.ebi.ddi.xml.validator.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.ddi.ddidomaindb.dataset.DSField;
import uk.ac.ebi.ddi.ddidomaindb.dataset.Field;
import uk.ac.ebi.ddi.ddidomaindb.dataset.FieldCategory;
import uk.ac.ebi.ddi.ddidomaindb.dataset.FieldType;
import uk.ac.ebi.ddi.xml.validator.parser.model.Date;
import uk.ac.ebi.ddi.xml.validator.parser.model.Entry;
import uk.ac.ebi.ddi.xml.validator.parser.model.Reference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 19/08/2015
 */
public class Utils {

    public static final String ERROR = "Error";
    public static final String WARN = "Warn";

    public static final String NOT_FOUND_MESSAGE = "The entry do not contain:";
    public static final String NOT_FOUND_UPDATED = "The entry do not contain or is out of range:";
    public static final String ENTRY_NOT_FOUND = "Entry:";
    public static final String REPORT_SPACE = " ";

    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);


    public static List<Tuple> validateSemantic(Entry entry) {

        List<Tuple> errors = new ArrayList<>();

        if (entry.getId() == null || entry.getId().isEmpty()) {
            errors.add(new Tuple<>(ERROR, NOT_FOUND_MESSAGE + REPORT_SPACE + DSField.ID.getFullName()));
        }
        if (entry.getName() == null || entry.getName().getValue() == null || entry.getName().getValue().isEmpty()) {
            errors.add(new Tuple<>(ERROR,
                ENTRY_NOT_FOUND + REPORT_SPACE + entry.getId() + REPORT_SPACE + NOT_FOUND_MESSAGE + DSField.NAME));
        }
        if (entry.getDescription() == null || entry.getDescription().isEmpty()) {
            errors.add(new Tuple<>(ERROR,
                    ENTRY_NOT_FOUND + REPORT_SPACE + entry.getId() + REPORT_SPACE + NOT_FOUND_MESSAGE + REPORT_SPACE
                            + DSField.DESCRIPTION));
        }

        if (entry.getDates() != null && !entry.getDates().isEmpty()) {
            List<Field> fields = Field.getValuesByCategory(FieldCategory.DATE, FieldType.UNKNOWN);
            for (Field field : fields) {
                String errorCode = (field.getType() == FieldType.MANDATORY) ? ERROR : WARN;
                boolean found = false;
                for (Date date : entry.getDates().getDate()) {
                    if (field.getName().equalsIgnoreCase(date.getType()) && date.getValue() != null
                            && validateDate(date.getValue())) {
                        found = true;
                    }
                }
                if (!found) {
                    errors.add(new Tuple<>(errorCode,
                            ENTRY_NOT_FOUND + REPORT_SPACE + entry.getId() + REPORT_SPACE + NOT_FOUND_UPDATED
                                    + REPORT_SPACE + field.getFullName()));
                }
            }
        }

        if (entry.getCrossReferences() != null && !entry.getCrossReferences().isEmpty()) {
            List<Field> fields = Field.getValuesByCategory(FieldCategory.CROSSREF);
            for (Field field : fields) {
                String errorCode = (field.getType() == FieldType.MANDATORY) ? ERROR : WARN;
                boolean found = false;
                for (Reference ref : entry.getCrossReferences().getRef()) {
                    if (field.getName().equalsIgnoreCase(ref.getDbname()) && ref.getDbname() != null) {
                        found = true;
                    }
                }
                if (!found) {
                    errors.add(new Tuple<>(errorCode,
                            ENTRY_NOT_FOUND + REPORT_SPACE + entry.getId() + REPORT_SPACE + NOT_FOUND_MESSAGE
                                    + REPORT_SPACE + field.getFullName()));
                }
            }
        }

        if (entry.getAdditionalFields() != null && !entry.getAdditionalFields().isEmpty()) {
            List<Field> fields = Field.getValuesByCategory(FieldCategory.ADDITIONAL, FieldType.UNKNOWN);
            for (Field field : fields) {
                String errorCode = (field.getType() == FieldType.MANDATORY) ? ERROR : WARN;
                boolean found = false;
                for (uk.ac.ebi.ddi.xml.validator.parser.model.Field ref : entry.getAdditionalFields().getField()) {
                    if (field.getName().equalsIgnoreCase(ref.getName()) && ref.getName() != null) {
                        found = true;
                    }
                }
                if (!found) {
                    errors.add(new Tuple<>(errorCode,
                            ENTRY_NOT_FOUND + REPORT_SPACE + entry.getId() + REPORT_SPACE + NOT_FOUND_MESSAGE
                                    + REPORT_SPACE + field.getFullName()));
                }
            }
        }

        return errors;
    }

    public static boolean validateDate(String value) {
        String[] dateValues = new String[]{"yyyy-MM-dd", "dd-MMM-yyyy HH:mm:ss"};

        for (String dateStr : dateValues) {
            try {
                java.util.Date date = new SimpleDateFormat(dateStr).parse(value);
                java.util.Date currentDate = new java.util.Date();
                if (currentDate.compareTo(date) >= 0) {
                    return true;
                }
            } catch (ParseException ignore) {
                LOGGER.info("Date {} couldn't parsing", value);
            }
        }
        return false;
    }
}
