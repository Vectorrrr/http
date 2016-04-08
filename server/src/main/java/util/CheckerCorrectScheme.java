package util;


import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

/**
 * Class allow check correct xml file
 * @author Gladush Ivan
 * @since 29.03.16.
 */
public class CheckerCorrectScheme {
    private static final Logger log = Logger.getLogger(CheckerCorrectScheme.class);
    private static final String INCCORECT_SCHEM = "This file does not meet the scheme %s";

    /**
     * The method receives a file to check
     * and the scheme for which you want to
     * check the file. Returns the check result
     */
    public static boolean isCorrectFile(String pathToFile, String pathToScheme) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

            File schemaLocation = new File(ClassLoader.getSystemResource(pathToScheme).getFile());
            Schema schema = factory.newSchema(schemaLocation);
            Validator validator = schema.newValidator();
            Source source = new StreamSource(pathToFile);
            validator.validate(source);
            return true;
        } catch (SAXException | IOException e) {
            log.warn(String.format(INCCORECT_SCHEM, e.getMessage()));
            return false;
        }
    }
}
