package server;

import org.apache.log4j.Logger;
import utils.PropertyLoader;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by igladush on 07.04.16.
 */
public class Server implements Runnable,AutoCloseable {
    private static final Logger log=Logger.getLogger(Server.class);
    private static final String SERVER_STOPED = "Server stoped";
    private static final String NEW_SOCKET = "Accept new socket";
    private static final String ERROR_IN_SERVER_WORK = "An error occurred while the server work %s";

    public static void main(String[] args) throws Exception {
        int port = Integer.valueOf(PropertyLoader.property("port"));
        Server server = new Server(port);
        new Thread(server).start();
        server.close();

    }

    private int port;
    private boolean runnable=true;
    private Server(int port){
        this.port=port;
    }
    @Override
    public void run() {
        try {
            ServerSocket serverSocket=new ServerSocket(port);
            while(runnable){
                Socket socket=serverSocket.accept();
                log.info(NEW_SOCKET);
            }
        } catch (IOException e) {
            log.error(String.format(ERROR_IN_SERVER_WORK, e.getMessage()));
        }
        log.info(SERVER_STOPED);
    }

    @Override
    public void close() throws Exception {
        new Socket(InetAddress.getByName("172.18.192.177"),8080);
        runnable = false;

    }
}
