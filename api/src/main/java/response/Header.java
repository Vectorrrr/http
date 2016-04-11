package response;

/**
 * Enum contains components of the response header
 * @author Gladush Ivan
 * @since 06.04.16.
 */
//todo make property
public enum Header {
    PAGE_FOUND("HTTP/1.1 302 Found"),
    REDIRECT_TO_ROOT_LOCATION("Location: http://localhost:8080"),
    REDIRECT_TO_MAIN_LOCATION("Location: http://localhost:8080/Main"),
    HTTP_OK("HTTP/1.1 200 OK"),
    SET_COOKIE("Set-Cookie: session=%d;"),
    CONTENT_LENGTH("Content-Length:  %d"),
    CLOSE_CONNECTION("Connection: close"),
    HTTP_NOT_FOUND("HTTP/1.1 404 NotFound");

    Header(String header) {
        this.header = header;
    }

    private String header;

    @Override
    public String toString() {
        return header;
    }
}
