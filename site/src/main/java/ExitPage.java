import page.PageProcessor;
import request.Request;
import response.Header;
import session.SessionHolder;

/**
 * @author Gladush Ivan
 * @since 12.04.16.
 */
public class ExitPage implements PageProcessor {

    private static final String REDIRECT_PAGE = "site-0/Root";

    @Override
    public String process(Request request, String response) {
        SessionHolder.removeSession(request.getSessionId());
        return Header.redirect(REDIRECT_PAGE,-1).build();

    }
}
