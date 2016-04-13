package response;

import org.apache.log4j.Logger;

/**
 * The class allows you to specify header and
 * body to respond, as well as create an answer
 * @author Gladush Ivan
 * @since 11.04.16.
 */
public class ResponseBuilder {
    private static final Logger log = Logger.getLogger(ResponseBuilder.class);
    private static final String NEW_LINE = "\r\n";
    private static final String HEADER_RESPONSE = "Header response %s";
    private StringBuilder header = new StringBuilder();
    private StringBuilder body = new StringBuilder();

    public ResponseBuilder addHeader(String header) {
        this.header.append(header).append(NEW_LINE);
        return this;
    }


    public ResponseBuilder addHeader(String header, int i) {
        return addHeader(String.format(header, i));
    }

    public ResponseBuilder addBody(String body) {
        this.body.append(body);
        return this;
    }

    public String build() {
        log.info(String.format(HEADER_RESPONSE, header.toString()));
        return header.append(NEW_LINE).append(body).toString();
    }
}