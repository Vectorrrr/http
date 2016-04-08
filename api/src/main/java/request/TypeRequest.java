package request;

import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by igladush on 07.04.16.
 */
public enum TypeRequest {
    POST("POST"), GET("GET"), UNKNOWN("UNKNOWN");
    private static final Logger log = Logger.getLogger(TypeRequest.class);
    private static final String CANT_FIND_REQUEST = "I can't find a request type. Check correct you regexp. I return a unknown type";
    private static final String REGEX_FOR_SEARCH_TYPE_REQUEST = "([A-Z]+(?=\\s))";
    private String typeRequest;

    TypeRequest(String typeRequest) {
        this.typeRequest = typeRequest;
    }

    private String typeRequest() {
        return typeRequest;
    }

    //todo write static method get request type
    public static TypeRequest getTypeRequest(String request) {
        Matcher matcher = Pattern.compile(REGEX_FOR_SEARCH_TYPE_REQUEST).matcher(request);
        if (matcher.find()) {
            return convertToTypeRequst(matcher.group(1));
        }
        log.warn(CANT_FIND_REQUEST);
        return TypeRequest.UNKNOWN;
    }

    private static TypeRequest convertToTypeRequst(String nameType) {
        for (TypeRequest tr : TypeRequest.values()) {
            if (tr.typeRequest().equals(nameType)) {
                return tr;
            }
        }
        return TypeRequest.UNKNOWN;
    }
}
