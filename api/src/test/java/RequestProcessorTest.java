import org.junit.Test;
import request.RequestProcessor;
import request.TypeRequest;

import static junit.framework.TestCase.assertEquals;

/**
 * @author Gladush Ivan
 * @since 11.04.16.
 */
public class RequestProcessorTest {
    private static final String EMPTY_STRING = "";

    @Test
    public void get_method_name_test1() {
        assertEquals(TypeRequest.POST, TypeRequest.getTypeRequest("POST /?fname=sdf&surname=sdfsdf HTTP/1.1Host:"));
    }

    @Test
    public void get_method_name_test2() {
        assertEquals(TypeRequest.GET, TypeRequest.getTypeRequest("GET /?fnamsdfe=sdf&surnfdgdfgame=sdfsdf HTTP/1.1Host:"));
    }

    @Test
    public void get_method_name_test3() {
        assertEquals(TypeRequest.UNKNOWN, TypeRequest.getTypeRequest("DFSF /?fnamsdfe=sdf&surnfdgdfgame=sdfsdf HTTP/1.1Host:"));
    }

    @Test
    public void get_method_name_test4() {
        assertEquals(TypeRequest.UNKNOWN, TypeRequest.getTypeRequest(" /?fnamsdfe=sdf&surnfdgdfgame=sdfsdf HTTP/1.1Host:"));
    }

    @Test
    public void get_url_to_root_page1() {
        assertEquals("/", RequestProcessor.getURL("GET / HTTP/1.1"));
    }

    @Test
    public void get_url_to_root_page2() {
        assertEquals("/", RequestProcessor.getURL("GET / HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Cache-Control: max-age=0\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\n" +
                "Upgrade-Insecure-Requests: 1\n" +
                "User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36"));
    }

    @Test
    public void get_url_to_concrete_page() {
        assertEquals("/Vanya/Gladush/com/net", RequestProcessor.getURL("POST /Vanya/Gladush/com/net HTTP/1.1"));
    }

    @Test
    public void incorrect_request_to_get_url() {
        assertEquals("/", RequestProcessor.getURL("This is incorrect request"));
    }

    @Test
    public void get_negative_session_from_cookies1() {
        assertEquals(-1, RequestProcessor.getSessionFromCookies(
                "Cookie:session=-1"));
    }

    @Test
    public void get_positive_session_from_cookies_() {
        assertEquals(12, RequestProcessor.getSessionFromCookies(
                "POST /Vanya/Gladush/com/net HTTP/1.1\n" +
                        "This is incorrect request\n" +
                        "Cookie:name=Vanya surname=Gladush session=12;"));
    }

    @Test
    public void get_empty_session_from_cookie1() {
        assertEquals(-1, RequestProcessor.getSessionFromCookies("nt-length:34807\n" +
                "content-type:text/html; charset=utf-8\n" +
                "date:Wed, 30 Mar 2016 13:55:37 GMT\n" +
                "expires:Wed, 29 Mar 2017 13:42:27 GMT\n" +
                "status:200\n" +
                "vary:Accept-Encoding\n" +
                "x-content-type-options:nosniff\n" +
                "x-fb-debug:ArFXPTTOv9DwHfCcHsaflJMF0kzIjjYEcGNLqV3OAJ6cdL5ShqJmn6P+Nent1hhVH86viM2Dk/rqNAU7U7bOIg==\n" +
                "x-xss-protection:0\n" +
                "Cookies:name=Vanya, surname=12\n"+
                "Request Headers\n"));
    }

    @Test
    public void get_empty_cookie2() {
        assertEquals(-1, RequestProcessor.getSessionFromCookies("status:200\n" +
                "vary:Accept-Encoding\n" +
                "x-content-type-options:nosniff\n" +
                "Cookie:"));
    }

}