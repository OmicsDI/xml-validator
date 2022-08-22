package uk.ac.ebi.ddi.xml.validator.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.ddi.ddidomaindb.dataset.*;
import uk.ac.ebi.ddi.xml.validator.parser.model.Date;
import uk.ac.ebi.ddi.xml.validator.parser.model.Entry;
import uk.ac.ebi.ddi.xml.validator.parser.model.Reference;
import uk.ac.ebi.ddi.xml.validator.parser.model.SummaryDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Utils {

    public static final String ERROR = "Error";
    public static final String WARN = "Warning";

    public static final String NOT_FOUND_MESSAGE = "The entry does not contain:";
    public static final String DATABASE_NOT_FOUND_MESSAGE="The database file does not contain:";
    public static final String NOT_FOUND_UPDATED = "The entry does not contain or is out of range:";
    public static final String ENTRY_NOT_FOUND = "Entry";
    public static final String REPORT_SPACE = " ";
    public static final String COLON = ":";

    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);


    public static List<Tuple> validateSemantic(Entry entry) {

        List<Tuple> errors = new ArrayList<Tuple>();

        if (entry.getId() == null || entry.getId().isEmpty()) {
            errors.add(new Tuple<>(ERROR, "[" + entry.getId() + "]" + COLON + REPORT_SPACE + "["+ ERROR+"]" + COLON + REPORT_SPACE + NOT_FOUND_MESSAGE +
                    REPORT_SPACE + DSField.ID.getFullName()));
        }
        if (entry.getName() == null || entry.getName().getValue() == null || entry.getName().getValue().isEmpty()) {
            errors.add(new Tuple<>(ERROR,
                ENTRY_NOT_FOUND + REPORT_SPACE + "[" + entry.getId() + "]" + COLON + REPORT_SPACE +
                        "["+ ERROR+"]" + COLON + REPORT_SPACE + NOT_FOUND_MESSAGE + DSField.NAME));
        }
        if (entry.getDescription() == null || entry.getDescription().isEmpty()) {
            errors.add(new Tuple<>(ERROR,
                    ENTRY_NOT_FOUND + REPORT_SPACE + "[" + entry.getId() + "]" + COLON + REPORT_SPACE +
                            "["+ ERROR+"]" + COLON + REPORT_SPACE + NOT_FOUND_MESSAGE + REPORT_SPACE +
                            DSField.DESCRIPTION));
        }

        if (entry.getDates() != null && !entry.getDates().isEmpty()) {
            Field.getFields().add(DSField.Date.PUBLICATION);
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
                            ENTRY_NOT_FOUND + REPORT_SPACE + "[" + entry.getId() + "]" + COLON +REPORT_SPACE
                                    + "["+ errorCode+"]" + COLON + REPORT_SPACE + NOT_FOUND_UPDATED + REPORT_SPACE +
                                    field.getFullName()));
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
                            ENTRY_NOT_FOUND + REPORT_SPACE + "[" + entry.getId() + "]" + COLON + REPORT_SPACE
                                    + "["+ errorCode+"]" + COLON + REPORT_SPACE + NOT_FOUND_MESSAGE + REPORT_SPACE +
                                    field.getFullName()));
                }
            }
        }

        if (entry.getAdditionalFields() != null && !entry.getAdditionalFields().isEmpty()) {
            Field.getFields().add(DSField.Additional.IS_PRIVATE);
            //Field.getFields().add(DSField.Additional.LINK);

            List<Field> fields = Field.getValuesByCategory(FieldCategory.ADDITIONAL, FieldType.UNKNOWN);
            for (Field field : fields) {
                String errorCode = (field.getType() == FieldType.MANDATORY) ? ERROR : WARN;
                boolean found = false;
                for (uk.ac.ebi.ddi.xml.validator.parser.model.Field ref : entry.getAdditionalFields().getField()) {
                    if (field.getName().equalsIgnoreCase(ref.getName()) && ref.getName() != null && !ref.getValue().isEmpty()) {
                        found = true;
                    }
                }
                if (!found) {
                    errors.add(new Tuple<>(errorCode,
                            ENTRY_NOT_FOUND + REPORT_SPACE + "[" + entry.getId() + "]" + COLON + REPORT_SPACE
                                    + "["+ errorCode+"]" + COLON + REPORT_SPACE+ NOT_FOUND_MESSAGE + REPORT_SPACE
                                    + field.getFullName()));
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

    public static Set<Tuple> validateDatabase(SummaryDatabase summaryDatabase){

        Set<Tuple> errors = new HashSet<Tuple>();

        if (summaryDatabase.getName() == null || summaryDatabase.getName().isEmpty()) {
            errors.add(new Tuple<>(ERROR, "["+ ERROR+"]" + COLON + REPORT_SPACE + DATABASE_NOT_FOUND_MESSAGE +
                    REPORT_SPACE + DBField.NAME.getFullName()));
        }
        if (summaryDatabase.getDescription() == null || summaryDatabase.getDescription().isEmpty()) {
            errors.add(new Tuple<>(WARN, "["+ WARN+"]" + COLON + REPORT_SPACE + DATABASE_NOT_FOUND_MESSAGE +
                    REPORT_SPACE + DBField.DESCRIPTION.getFullName()));
        }
        if (summaryDatabase.getUrl() == null || summaryDatabase.getUrl().isEmpty()) {
            errors.add(new Tuple<>(WARN, "["+ WARN+"]" + COLON + REPORT_SPACE + DATABASE_NOT_FOUND_MESSAGE +
                    REPORT_SPACE + DBField.URL.getFullName()));
        }
        if (summaryDatabase.getKeywords() == null || summaryDatabase.getKeywords().isEmpty()) {
            errors.add(new Tuple<>(WARN, "["+ WARN+"]" + COLON + REPORT_SPACE + DATABASE_NOT_FOUND_MESSAGE +
                    REPORT_SPACE + DBField.KEYWORDS.getFullName()));
        }
        if (summaryDatabase.getEntryCount() == null || summaryDatabase.getEntryCount().equals(0)) {
            errors.add(new Tuple<>(WARN, "["+ WARN+"]" + COLON + REPORT_SPACE + DATABASE_NOT_FOUND_MESSAGE +
                    REPORT_SPACE + DBField.ENTRY_COUNT.getFullName()));
        }
        if (summaryDatabase.getReleaseDate() == null || summaryDatabase.getReleaseDate().isEmpty()) {
            errors.add(new Tuple<>(WARN, "["+ WARN+"]" + COLON + REPORT_SPACE + DATABASE_NOT_FOUND_MESSAGE +
                    REPORT_SPACE + DBField.RELEASE_DATE.getFullName()));
        }
        return errors;
    }
}
