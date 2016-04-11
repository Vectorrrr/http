package loader;


import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

/**
 * Class allows you to create a boot loader
 * property from a specific site
 * @author Gladush Ivan
 * @since 29.03.16.
 */
public class PropertyLoader {
    private static final Logger log = Logger.getLogger(PropertyLoader.class);
    private static final String EXCEPTION_DOWNLOAD_PROPERTIES = "I can't download properties %s";
    private static final String EXCEPTION_PROPERTY_DONT_FOUND = "This property not exist";
    private  Properties properties = new Properties();

    private PropertyLoader(String fileName) throws IOException {
        properties.load(ClassLoader.getSystemResourceAsStream(fileName));

    }



    public static PropertyLoader getPropertyLoader(String fileName){
        try {
       return new PropertyLoader(fileName);
        } catch (IOException e) {
            log.error(String.format(EXCEPTION_DOWNLOAD_PROPERTIES, e));
            throw new IllegalStateException(String.format(EXCEPTION_DOWNLOAD_PROPERTIES, e));
        }
    }
    public  String property(String propertyKey) {
        String property = (String) properties.get(propertyKey);
        if (property == null) {
            log.error(EXCEPTION_PROPERTY_DONT_FOUND);
            throw new IllegalStateException(EXCEPTION_PROPERTY_DONT_FOUND);
        }
        return property;
    }
}