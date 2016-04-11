package loader.page;


import loader.PropertyLoader;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * The class contains a URL of the page and
 * its contents, when referring to a class with
 * a request to obtain the page, the class checks its
 * existence and presence in it updated. If the page is
 * not there or it downloads updates for its new, otherwise
 * it returns the current version
 * @author Gladush Ivan
 * @since 29.03.16.
 */
public class PageHolder {
    private static final Logger log = Logger.getLogger(PageHolder.class);
    private static final PropertyLoader PROPERTY_LOADER=PropertyLoader.getPropertyLoader("server.configuration.properties");
    private static final String DEFAULT_PAGES_DIRRECTORY = PROPERTY_LOADER.property("dirrectory.for.site.pages");
    /**
     * MAP contains key-value pairs where
     * the key serves the page file name, as the
     * value of its contents
     */
    private static Map<String, WebPage> pages = new HashMap<>();

    public static String getPage(String site) {
        String pathInDir = String.format("%s%s.txt", DEFAULT_PAGES_DIRRECTORY, site);
        WebPage webPage = pages.get(site);

        if (webPage == null || webPage.lastChangesDate() < PageLoader.lastModifiedFile(pathInDir)) {
            log.info(String.format(UPDATE_PAGE_INFORMATION, site));
            pages.put(site, new WebPage(PageLoader.getPage(pathInDir)));
        }

        return pages.get(site).pageContent();
    }

    private static final String UPDATE_PAGE_INFORMATION = "Updated information on the page with the URL %s";
}
