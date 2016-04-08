package page;


import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Gladush Ivan
 * @since 06.04.16.
 */
public class PageLoader {
    private static final Logger log = Logger.getLogger(PageLoader.class);
    private static final String PATH_TO_DEF_DIR=ClassLoader.getSystemResource("resource").getFile();
    public static String getPage(String pageName) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(ClassLoader.getSystemResource(pageName).getFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            log.error(String.format("I can't download page %s because %s", pageName, e.getMessage()));
        }
        return sb.toString();
    }

    public static long lastModifiedFile(String fileName) {
        return new File(fileName).lastModified();
    }
}
