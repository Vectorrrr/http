import page.PageProcessor;
import request.Request;
import response.Header;
import session.SessionHolder;

/**
 * Home page of the site that allows the user to log out
 * @author Gladush Ivan
 * @since 12.04.16.
 */
public class MainPage implements PageProcessor {

    private static final String REDIRECT_SITE = "site-0/Login";

    @Override
    public String process(Request request, String response) {
        int id = request.getSessionId();
        if (SessionHolder.containsSession(id)) {
            String bodyResponse = String.format(response, request.getUserInformation());
            return Header.httpOk(bodyResponse.length(),id).addBody(bodyResponse).build();
        }
        return Header.redirect(REDIRECT_SITE,-1).build();
    }
}
