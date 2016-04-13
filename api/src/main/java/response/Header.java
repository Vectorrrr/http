package response;

import java.util.HashMap;
import java.util.Map;

/**
 * The class contains a set of standard primary
 * server response headers. The user can expand
 * or modify this list. Just class contains methods
 * that allow you to create a simplified commonly
 * used headers
 * @author Gladush Ivan
 * @since 12.04.16.
 */
public class Header {
    private static final String EMPTY_STRING = "";
    private static Map<String, String> headers = new HashMap<String, String>() {{
        put("PAGE_FOUND", "HTTP/1.1 302 Found");
        put("REDIRECT_TO_LOCATION", "Location: http://localhost:8080/%s");
        put("HTTP_OK", "HTTP/1.1 200 OK");
        put("SET_COOKIE", "Set-Cookie: session=%d;");
        put("CONTENT_LENGTH", "Content-Length:  %d");
        put("CLOSE_CONNECTION", "Connection: close");
        put("HTTP_NOT_FOUND", "HTTP/1.1 404 NotFound");
    }};

    //todo in the future, these methods will be needed, and now not to leave them?
    public static void addNewHeader(String key, String value) {
        headers.put(key, value);
    }

    public static String getValue(String key) {
        String value = headers.get(key);
        if (value == null) {
            return EMPTY_STRING;
        }
        return value;
    }

    /**
     * The method generates a response
     * header on the page is not found
     */
    public static String notFound() {
        return new ResponseBuilder().addHeader(headers.get("HTTP_NOT_FOUND")).
                addHeader(headers.get("CLOSE_CONNECTION")).build();
    }

    /**
     * The method generates a header that page is found
     * and indicates the title, the length of the answer,
     * which takes in the parameters
     */
    public static ResponseBuilder httpOk(int length) {
        return new ResponseBuilder().addHeader(headers.get("HTTP_OK")).addHeader(headers.get("CONTENT_LENGTH"), length).
                addHeader(headers.get("CLOSE_CONNECTION"));
    }

    /**
     * The method generates a header that page is found and
     * indicates the title, the length of the answer, which
     * takes in the parameters and sets a cookie with the
     * session number
     */
    public static ResponseBuilder httpOk(int length, int sessionId) {
        return httpOk(length).addHeader(headers.get("SET_COOKIE"), sessionId);
    }

    /**
     * method generates a response header to redirect to a
     * particular site, as well as sets the session in cookies
     */
    public static ResponseBuilder redirect(String siteName, int sessionId) {
        return new ResponseBuilder().addHeader(headers.get("PAGE_FOUND")).
                addHeader(String.format(headers.get("REDIRECT_TO_LOCATION"), siteName)).
                addHeader(headers.get("SET_COOKIE"), sessionId);
    }
}
