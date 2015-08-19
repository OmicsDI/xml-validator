# xml-validator
----------------------------------------
[![Build Status](https://travis-ci.org/BD2K-DDI/xml-validator.svg)](https://travis-ci.org/BD2K-DDI/xml-validator)

The xml validator provide a way of validating the XML files for the BD2K-DDI project. The current library and corresponding commandline tool provides different options
for checking the quality of the files and the metadata they provide. The library is based on the rules and specifications for the [OmicsDI](https://github.com/BD2K-DDI/specifications)
project. The schema validation will provide a first validation of the file using the [OmicsDI](https://github.com/BD2K-DDI/specifications/blob/master/docs/schema/OmicsDISchema.xsd)
schema; the validation can be done for a directory or per files. Some of the validation steps, each entry in the file must contain this properties:
 - id
 - name
 - publication (dates)
 - omics_type
 - repository
 - full_dataset_link

For a full list of properties and rules, please visit the current specification [OmicsDI](https://github.com/BD2K-DDI/specifications).



## How to use the tool

The main commandline tool validatorCLI can be download from the releases pages [release](https://github.com/BD2K-DDI/xml-validator/releases), after unzip the tool it can be called as:

```sh

>  java -jar validatorCLI.jar --help
usage: validatorCLI
 -check <arg>              Choose validation level (default level is
                           Warn):
                           Warn: This category do a complete Schema and
                           semantic validation of the file
                           Error: This category do a validation at level
                           of XML Schema
 -h,--help                 print help message
 -inFile <arg>             Input file or Directory fo ve processed. If the
                           value is a directory, the procedure will be
                           applied to all files
 -merge <property=value>   Convert a given directory files to an out put
                           with other options
 -reportFile <arg>         Record error/warn messages into outfile. If not
                           set, print message on the screen.
```

If the user is interested to validate the file at the schema level (not semantic validation):

```sh

>  java -jar validatorCLI.jar -inFile PRIDE-XML.xml -check Error


```

The latest command will produce a file with the extension PRIDE-XML.xml.output.csv with all the errors **if the file have errors**. If the user is interested to do semantic validation of the
file, the command should be refined to:

```sh

>  java -jar validatorCLI.jar -inFile PRIDE-XML.xml -check Warn


```


## Support

For any issue or support questions, please contact pride-support@ebi.ac.uk

 