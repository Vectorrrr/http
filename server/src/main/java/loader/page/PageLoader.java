package loader.page;


import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class keeps in memory all the pages of
 * websites. It can return a page, while if the
 * missing page in memory or it was changed, the
 * class update the contents of the page
 * @author Gladush Ivan
 * @since 06.04.16.
 */
public class PageLoader {
    private static final Logger log = Logger.getLogger(PageLoader.class);

    public static String getPage(String pageName) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(pageName)))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
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
