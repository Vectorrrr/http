import page.PageProcessor;
import request.Request;
import response.Header;

/**
 * @author Gladush Ivan
 * @since 11.04.16.
 */
public class RootPage implements PageProcessor {
    @Override
    public String process(Request request, String response) {
        return Header.httpOk(response.length()).addBody(response).build();

    }
}
