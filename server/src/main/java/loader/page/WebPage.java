package loader.page;

/**
 * Class contains the information content of the page,
 * as well as the date of the last of its mutations both
 * @author Gladush Ivan
 * @since 29.03.16.
 */
public class WebPage {
    private String pageContent;
    private long lastChangesDate;

    private WebPage(String pageContent, long lastChangesDate) {
        this.pageContent = pageContent;
        this.lastChangesDate = lastChangesDate;
    }

    public WebPage(String page) {
        this(page, System.currentTimeMillis());
    }

    public String pageContent() {
        return pageContent;
    }

    public long lastChangesDate() {
        return lastChangesDate;
    }
}
