import model.User;
import page.PageProcessor;
import request.Request;
import request.TypeRequest;
import response.Header;
import session.SessionHolder;

/**
 * @author Gladush Ivan
 * @since 12.04.16.
 */
public class LoginPage implements PageProcessor {

    private static final String SITE_REDIRECT = "site-0/Main";

    @Override
    public String process(Request request, String response) {
        if (TypeRequest.GET.equals(request.typeRequest())) {
            return doGet(request,response);
        }
        return doPost(request);

    }

    private String doGet(Request request,String response) {
        int sesId = request.getSessionId();
        if (SessionHolder.containsSession(sesId)) {
            return redirectPageAnswer(sesId);
        }

        return Header.httpOk(response.length()).addBody(response).build();
    }

    private String doPost(Request request) {
        User user = createUser(request);
        return redirectPageAnswer(SessionHolder.addSession(user));
    }

    private User createUser(Request request) {
        return new User(request.getParameter("name"), request.getParameter("surname"));
    }

    private String redirectPageAnswer(int sessionId) {
        return Header.redirect(SITE_REDIRECT,sessionId).build();
       }
}
