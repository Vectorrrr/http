package request;

import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The class allows you to receive a
 * variety of information from the request header
 * @author Gladush Ivan
 * @since 29.03.16.
 */
public class RequestProcessor {
    private static final Logger log= Logger.getLogger(RequestProcessor.class);
    private static final String REGEX_FOR_GET_URL="((?<= )/(.*)(?= HTTP))";
    private static final String ROOT_PAGE_URL = "/";
    private static final String REGEX_FOR_SESSION_FROM_COOKIE ="((?<=session=)((-){0,1}[0-9]+))";

    public static String getURL(String request){
        Matcher matcher = Pattern.compile(REGEX_FOR_GET_URL).matcher(request);
        if(matcher.find()){
            return matcher.group(1);
        }
        log.warn(DONT_FIND_URL);
        return ROOT_PAGE_URL;

    }
    public static int getSessionFromCookies(String request){
        Matcher matcher= Pattern.compile(REGEX_FOR_SESSION_FROM_COOKIE).matcher(request);
        if(matcher.find()){
            return Integer.valueOf(matcher.group(1));
        }
        return -1;
    }
    private static final String DONT_FIND_URL = "URL doesn't contains url. Method return a root page URL";
}