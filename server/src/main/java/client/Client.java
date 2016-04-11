package client;

import holder.ProcessorHolder;
import org.apache.log4j.Logger;
import request.Connection;
import request.Request;

/**
 * This class models a processing
 * request to the server
 * @author Gladush Ivan
 * @since 29.03.16.
 */
public class Client implements Runnable {
    private final Logger log = Logger.getLogger(Client.class);
    private static final String INCORRECT_CLOSE = "Incorrect close connection. May be client close socket in browser %s";

    private Connection connection;

    public Client(Connection connection) {
        log.info("New client");
        this.connection = connection;

    }

    @Override
    public void run() {
        try {
            Request request = connection.getRequest();
            connection.writeResponse(ProcessorHolder.process(request));
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(String.format(INCORRECT_CLOSE, e.getMessage()));
        }
    }
}
