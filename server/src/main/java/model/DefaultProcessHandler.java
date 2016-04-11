package model;

import response.Header;
import response.ResponseBuilder;

/**
 * If the request came from the browser,
 * which does not contain a URL handler that
 * will process this request this class
 * @author Gladush Ivan
 * @since 11.04.16.
 */
public class DefaultProcessHandler {
    public static String notFound(){
        return new ResponseBuilder().addHeader(Header.HTTP_NOT_FOUND).addHeader(Header.CLOSE_CONNECTION).build();
    }
}
