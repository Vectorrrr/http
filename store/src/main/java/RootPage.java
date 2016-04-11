import page.PageProcessor;
import request.Request;
import response.ResponseBuilder;

import static response.Header.*;

/**
 * @author Gladush Ivan
 * @since 11.04.16.
 */
public class RootPage implements PageProcessor {
    @Override
    public String process(Request request, String response) {
        return new ResponseBuilder().addHeader(HTTP_OK).addHeader(CLOSE_CONNECTION).addHeader(CONTENT_LENGTH,response.length())
                .addBody(response).build();
    }
}
