package utils;

/**
 * Created by igladush on 07.04.16.
 */

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

/**
 * Class load property
 * @author Gladush Ivan
 * @since 29.03.16.
 */
public class PropertyLoader {
    private static final Logger log = Logger.getLogger(PropertyLoader.class);
    private static final String EXCEPTION_DOWNLOAD_PROPERTIES = "I can't download properties %s";
    private static final String EXCEPTION_PROPETIE_DONT_FOUND = "This property not exist";
    private static Properties properties = new Properties();

    static {
        try {
            properties.load(ClassLoader.getSystemResourceAsStream("server.configuration.properties"));
        } catch (IOException e) {
            log.error(String.format(EXCEPTION_DOWNLOAD_PROPERTIES, e));
            throw new IllegalStateException(String.format(EXCEPTION_DOWNLOAD_PROPERTIES, e));
        }
    }


    public static String property(String propertyKey) {
        String property = (String) properties.get(propertyKey);
        if (property == null) {
            log.error(EXCEPTION_PROPETIE_DONT_FOUND);
            throw new IllegalStateException(EXCEPTION_PROPETIE_DONT_FOUND);
        }
        return property;
    }
}