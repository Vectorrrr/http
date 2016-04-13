package request;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * This class models the connection
 * between the client and the server.
 * It allows you to read the request and
 * send a response. The request is read only
 * once. The same method can close the socket
 * @author Gladush Ivan
 * @since 29.03.16.
 */
public class Connection  implements AutoCloseable {
    private static final Logger log = Logger.getLogger(Connection.class);
    private static final String EXCEPTION_READ_SOCKET = "I can't read socket, because %s %s";
    private static final String EMPTY_STRING = "";
    private static final String INCORRECT_PARAMETER_IN_CONTENT = "Incorrect parameter in content %s";
    private static final String CANT_SEND_RESPONSE = "Can't send response client because %s";
    private static final String CONTENT_HEADER = "Content-Length: ";
    private static final String AMPERSAND = "&";
    private static final String EQUALLY = "=";

    private Socket socket;
    private Request request = null;

    private BufferedReader reader;
    private OutputStream writer;

    public Connection(Socket socket) {
        this.socket = socket;
    }

    /**
     * Method checks for the request in the class,
     * and if it is not read request from a socket
     */
    public Request getRequest() {
        try {
            if (request == null) {
                createReader();
                parseContentRequest(readContent(readHeader()));
            }
        } catch (IOException e) {
            log.error(String.format(EXCEPTION_READ_SOCKET, e.getMessage()));
            throw new IllegalStateException(String.format(EXCEPTION_READ_SOCKET, e.getMessage(), MANUAL_DECIDE_PROBLEM));
        }
        return request;
    }

    /**
     * Method parse content and add this content in request
     */
    private void parseContentRequest(String content) {
        for (String parameter : content.split(AMPERSAND)) {
            String[] keyValue = parameter.split(EQUALLY);
            if (keyValue.length != 2) {
                log.warn(String.format(INCORRECT_PARAMETER_IN_CONTENT, parameter));
            } else {
                request.addValue(keyValue[0], keyValue[1]);
            }
        }
    }

    /**
     * Returns the body content of the request. If this request is get returns 0
     */
    private String readContent(int contentLength) throws IOException {
        StringBuilder body = new StringBuilder(EMPTY_STRING);
        for (int i = 0; i < contentLength; i++) {
            body.append((char) reader.read());
        }
        return body.toString();
    }

    /**
     * Read header request and init the Request without parameter
     * return a content length from header
     */
    private int readHeader() throws IOException {
        StringBuilder head = new StringBuilder();
        String line;
        line = reader.readLine();
        TypeRequest type = TypeRequest.getTypeRequest(line);
        int contentLength = 0;
        do {
            head.append('\n').append(line);
            if (TypeRequest.POST.equals(type) &&
                    line.startsWith(CONTENT_HEADER)) {
                contentLength = Integer.parseInt(line.split(" ")[1]);
            }
        } while (!EMPTY_STRING.equals(line = reader.readLine()));

        request = new Request(type, head.toString());
        return contentLength;
    }

    private void createReader() throws IOException {
        if (reader == null) {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
    }

    /**
     * returns whether a response has been sent or not
     */
    public boolean writeResponse(String s) {
        try {
            createWriter();
            writer.write(s.getBytes());
        } catch (IOException e) {
            log.error(String.format(CANT_SEND_RESPONSE, e.getMessage()));
            return false;
        }
        return true;
    }

    private void createWriter() throws IOException {
        if (writer == null) {
            writer = socket.getOutputStream();
        }
    }

    @Override
    public void close() throws Exception {
        socket.close();
    }


    private static final String MANUAL_DECIDE_PROBLEM = "The error due to the fact that the socket can" +
            "not be used to read the data. Check the premature closure of the socket, or that " +
            "your application does not close the socket. Perhaps the client is disconnected.";
}
