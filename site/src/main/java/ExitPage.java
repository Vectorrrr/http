
import page.PageProcessor;
import request.Request;

/**
 * Created by igladush on 07.04.16.
 */
public class ExitPage implements PageProcessor {


    @Override
    public String process(Request request, String response) {
        return response;
    }

}
