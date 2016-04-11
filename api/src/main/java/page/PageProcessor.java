package page;

import request.Request;

/**
 * All handlers certain URL must implement this interface
 * @author Gladush Ivan
 * @since 29.03.16.
 */
public interface PageProcessor {
    /**
     * The method takes as input a query and response
     * skeleton. Performing the necessary logic of the
     * request, the method returns a full response
     * */
    String process(Request request,String response);
}
