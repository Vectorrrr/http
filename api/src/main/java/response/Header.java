package response;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Gladush Ivan
 * @since 12.04.16.
 */
public class Header {
    private static final String EMPTY_STRING = "";
    private static  Map<String,String> headers=new HashMap<String,String>(){{
        put("PAGE_FOUND", "HTTP/1.1 302 Found");
        put("REDIRECT_TO_LOCATION", "Location: http://localhost:8080/%s");
        put("HTTP_OK", "HTTP/1.1 200 OK");
        put("SET_COOKIE", "Set-Cookie: session=%d;");
        put("CONTENT_LENGTH","Content-Length:  %d");
        put("CLOSE_CONNECTION","Connection: close");
        put("HTTP_NOT_FOUND","HTTP/1.1 404 NotFound");
    }};

    public static void addNewHeader(String key,String value){
        headers.put(key,value);
    }
    public static String getValue(String key){
        String value=headers.get(key);
        if(value==null){
            return EMPTY_STRING;
        }
        return value;
    }

    public static String notFound() {
        return new ResponseBuilder().addHeader(headers.get("HTTP_NOT_FOUND")).
                addHeader(headers.get("CLOSE_CONNECTION")).build();
    }

    public static ResponseBuilder httpOk(int length) {
        return new ResponseBuilder().addHeader(headers.get("HTTP_OK")).addHeader(headers.get("CONTENT_LENGTH"),length).
                addHeader(headers.get("CLOSE_CONNECTION"));
    }
    public static ResponseBuilder httpOk(int length, int sessionId) {
        return httpOk(length).addHeader(headers.get("SET_COOKIE"),sessionId);
    }


    public static ResponseBuilder redirect(String siteName, int sessionId) {
        return new ResponseBuilder().addHeader(headers.get("PAGE_FOUND")).
                addHeader(String.format(headers.get("REDIRECT_TO_LOCATION"),siteName)).
                addHeader(headers.get("SET_COOKIE"),sessionId);
    }


}
