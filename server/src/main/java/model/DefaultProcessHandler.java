package model;

import page.PageProcessor;
import request.Request;
import response.Header;

/**
 * If the request came from the browser,
 * which does not contain a URL handler that
 * will process this request this class
 * @author Gladush Ivan
 * @since 11.04.16.
 */
public class DefaultProcessHandler implements PageProcessor {
    @Override
    public String process(Request request, String response) {
        return Header.notFound();
    }
}
