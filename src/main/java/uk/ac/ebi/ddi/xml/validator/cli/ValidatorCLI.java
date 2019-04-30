package uk.ac.ebi.ddi.xml.validator.cli;

import org.apache.commons.cli.*;
import uk.ac.ebi.ddi.ddidomaindb.dataset.Field;
import uk.ac.ebi.ddi.ddidomaindb.dataset.FieldCategory;
import uk.ac.ebi.ddi.ddidomaindb.dataset.FieldType;
import uk.ac.ebi.ddi.xml.validator.parser.OmicsXMLFile;
import uk.ac.ebi.ddi.xml.validator.utils.Tuple;
import uk.ac.ebi.ddi.xml.validator.utils.Utils;

import java.io.File;
import java.io.PrintStream;
import java.util.*;

/**
 * This class allow the user to interact with the library:
 * - Validate files.
 * - Read files, merge files etc
 *
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 19/08/2015
 */
public class ValidatorCLI {

    public static void main(String[] args) throws Exception {

        // Definite command line
        CommandLineParser parser = new PosixParser();
        Options options = new Options();
        //Help page
        String helpOpt = "help";
        options.addOption("h", helpOpt, false, "print help message");

        String reportFileOpt = "reportFile";
        options.addOption(reportFileOpt, true,
                "Record error/warn messages into outfile. If not set, print message on the screen.");

        String inFileOpt = "inFile";
        options.addOption(inFileOpt, true,
                "Input file or Directory fo ve processed. If the value is a directory, " +
                        "the procedure will be applied to all files");

        String levelOpt = "check";
        options.addOption(levelOpt, true, "Choose validation level (default level is Warn): \n" +
            "\t Warn:  This category do a complete Schema Validation and Mandatory/Recommended/Additional fields \n" +
            "\t Error: This category do a validation at level of XML Schema, Mandatory Fields");

        String convertOpt = "merge";
        String numberOpt = "property=value";

        Option convertOption = OptionBuilder.withArgName(numberOpt)
                .hasArgs()
                .withValueSeparator()
                .withDescription("Convert a given directory files to an out put with other options")
                .create(convertOpt);
        options.addOption(convertOption);
        // Parse command line
        CommandLine line = parser.parse(options, args);

        if (line.hasOption(helpOpt) || !line.hasOption(inFileOpt)) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("ValidatorCLI", options);
        } else {

            List<File> files = new ArrayList<>();
            File inFile = new File(line.getOptionValue(inFileOpt));

            String checkValue = line.getOptionValue(levelOpt);
            if (checkValue == null || !checkValue.equalsIgnoreCase(Utils.WARN)) {
                checkValue = Utils.ERROR;
            } else {
                checkValue = Utils.WARN;
            }

            String reportName = line.getOptionValue(reportFileOpt);

            if (inFile.exists() && inFile.isDirectory()) {
                File[] fileList = inFile.listFiles();
                for (File a : fileList) {
                    if (OmicsXMLFile.hasFileHeader(a)) {
                        files.add(a);
                    }
                }
            } else if (inFile.exists()) {
                files.add(inFile);
            }

            if (files.size() == 0) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("ValidatorCLI", options);
                System.exit(1);
            }

            Map<File, List<Tuple>> errors = new HashMap<>();
            for (File file : files) {
                List<Tuple> error = OmicsXMLFile.validateSchema(file);
                error.addAll(OmicsXMLFile.validateSemantic(file));
                if (errors.containsKey(file)) {
                    error.addAll(errors.get(file));
                }
                errors.put(file, error);
            }

            if (!errors.isEmpty()) {
                if (reportName != null) {
                    PrintStream reportFile = new PrintStream(new File(reportName));
                    generateSummaryReport(errors, reportFile, checkValue);
                    for (File file : errors.keySet()) {
                        for (Tuple error : errors.get(file)) {
                            if (Objects.equals(checkValue, Utils.WARN) || (error.getValue() == Utils.ERROR)) {
                                reportFile.println(
                                        file.getAbsolutePath() + "\t" + error.getKey() + "\t" + error.getValue());
                            }
                        }
                    }

                    reportFile.close();
                } else {
                    for (File file : errors.keySet()) {
                        PrintStream reportFile = new PrintStream(new File(file.getAbsolutePath() + ".error.csv"));
                        generateSummaryReport(errors, reportFile, checkValue);
                        for (Tuple error : errors.get(file)) {
                            if (Objects.equals(checkValue, Utils.WARN) || (error.getValue() == Utils.ERROR)) {
                                reportFile.println(file.getName() + "\t" + error.getKey() + "\t" + error.getValue());
                            }
                        }
                        reportFile.close();
                    }
                }

            }
        }
    }

    public static void generateSummaryReport(Map<File, List<Tuple>> errors, PrintStream reportFile, String errorLevel) {
        int numberErrors = 0;
        int numberWars = 0;
        Map<Field, Integer> fields = new HashMap<>();
        for (List<Tuple> errorList : errors.values()) {
            for (Tuple error : errorList) {
                String message = (String) error.getValue();
                if (error.getKey() == Utils.ERROR) {
                    numberErrors++;
                } else {
                    numberWars++;
                }
                for (Field field : Field.getFields()) {
                    if (message.contains(field.getFullName())) {
                        if (!fields.containsKey(field)) {
                            fields.put(field, 0);
                        }
                        fields.put(field, fields.get(field) + 1);
                    }
                }
            }

        }
        reportFile.println("The number of Errors in the files: " + numberErrors);
        reportFile.println("The number of Warnings in the files: " + numberWars);
        for (Map.Entry entry : fields.entrySet()) {
            Field field = (Field) entry.getKey();
            Integer errorNumber = (Integer) entry.getValue();
            String error = (field.getType() == FieldType.MANDATORY) ? Utils.ERROR : Utils.WARN;
            if (Objects.equals(errorLevel, Utils.WARN) || (Objects.equals(error, Utils.ERROR))) {
                if (field.getCategory() == FieldCategory.DATE) {
                    reportFile.println(error + " The number of datasets without or outdated "
                            + field.getFullName() + " is " + errorNumber);
                } else {
                    reportFile.println(error + " The number of datasets without "
                            + field.getFullName() + " is " + errorNumber);
                }

            }
        }
    }


}
