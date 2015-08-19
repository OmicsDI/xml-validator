package uk.ac.ebi.ddi.xml.validator.cli;

import org.apache.commons.cli.*;
import uk.ac.ebi.ddi.xml.validator.parser.OmicsXMLFile;
import uk.ac.ebi.ddi.xml.validator.utils.Tuple;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class allow the user to interact with the library:
 *  - Validate files.
 *  - Read files, merge files etc
 *
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 19/08/2015
 */
public class validatorCLI {

    @SuppressWarnings("static-access")
    public static void main(String[] args) throws Exception {

        // Definite command line
        CommandLineParser parser = new PosixParser();
        Options options = new Options();

        //Help page
        String helpOpt = "help";
        options.addOption("h", helpOpt, false, "print help message");

        String reportFileOpt = "reportFile";
        options.addOption(reportFileOpt, true, "Record error/warn messages into outfile. If not set, print message on the screen.");

        String inFileOpt  = "inFile";
        options.addOption(inFileOpt, true, "Input file or Directory fo ve processed. If the value is a directory, the procedure will be applied to all files");

        String levelOpt = "check";
        options.addOption(levelOpt, true, "Choose validation level (default level is Warn): \n" +
                "\t Warn: This category do a complete Schema and semantic validation of the file \n" +
                "\t Error: This category do a validation at level of XML Schema");

        String convertOpt = "merge";
        String numberOpt  = "property=value";

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
            formatter.printHelp("validatorCLI", options);
        }else {

            List<File> files = new ArrayList<File>();
            File inFile = new File (line.getOptionValue(inFileOpt));

            String checkValue = line.getOptionValue(levelOpt);
            if(checkValue == null || !checkValue.equalsIgnoreCase("Warn"))
                checkValue = "Error";
            else
                checkValue = "Warn";

            String reportName = line.getOptionValue(reportFileOpt);

            if(inFile.exists() && inFile.isDirectory()){
                File[] fileList = inFile.listFiles();
                for(File a: fileList){
                    if (OmicsXMLFile.hasFileHeader(a)){
                        files.add(a);
                    }
                }
            }else if (inFile.exists())
                files.add(inFile);

            if(files.size()  == 0){
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("validatorCLI", options);
                System.exit(1);
            }

            Map<File, List<Tuple>> errors = new HashMap<File, List<Tuple>>();
            for(File file: files){

                List<Tuple> error = OmicsXMLFile.validateSchema(file);
                if(checkValue.equalsIgnoreCase("Warn")){
                    error.addAll(OmicsXMLFile.validateSemantic(file));
                }
                if(errors.containsKey(file)){
                   error.addAll(errors.get(file));
                }
                errors.put(file, error);
            }

            if(!errors.isEmpty()){
                if(reportName != null){
                    PrintStream reportFile = new PrintStream(new File(reportName));
                    for(File file: errors.keySet()){
                        for (Tuple error: errors.get(file))
                            reportFile.println(file.getAbsolutePath() + "\t" + error.getKey() + "\t" + error.getValue());
                    }
                    reportFile.close();
                }else{
                    for(File file: errors.keySet()){
                        PrintStream reportFile = new PrintStream(new File(file.getAbsolutePath() + ".error.csv"));
                        for (Tuple error: errors.get(file))
                            reportFile.println(file.getName() + "\t" + error.getKey() + "\t" + error.getValue());
                        reportFile.close();
                    }
                }

            }
        }
    }

}
