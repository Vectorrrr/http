package server;

import client.Client;
import loader.ClassManager;
import loader.PropertyLoader;
import loader.SiteLoader;
import org.apache.log4j.Logger;
import request.Connection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class allows you to run the server
 * and add it to the sites to work with them.
 * The server runs on port 8080 to the address
 * that is set in preferences. You can stop
 * the server. After stopping the server, all
 * the information about uploading sites will be lost
 * @author Gladush Ivan
 * @since 08.04.16.
 */
public class Server implements Runnable,AutoCloseable {
    private static final Logger log = Logger.getLogger(Server.class);
    private static final String SERVER_STOPPED = "Server stopped";
    private static final String NEW_SOCKET = "Accept new socket";
    private static final String ERROR_IN_SERVER_WORK = "An error occurred while the server work %s";
    private static final String CONTROL_SERVER_EXCEPTION = "Server controller method catch exception %s";
    private static final PropertyLoader PROPERTY_LOADER = PropertyLoader.getPropertyLoader("server.configuration.properties");
    private static final String JAR_FILE_LOAD_INCORRECT = "Jar file has been loaded correctly, check that all the necessary resources in the archive";

    private static ExecutorService executor = Executors.newFixedThreadPool(Integer.valueOf(PROPERTY_LOADER.property("count.thread.for.client")));
    private static Scanner sc = new Scanner(System.in);
    private static Server server = null;
    private static SiteLoader siteLoader = new SiteLoader();

    private int port;
    private boolean runnable = true;

    public static void main(String[] args) {
        server = new Server(Integer.valueOf(PROPERTY_LOADER.property("port")));
        ClassManager.initProcessorHolder();
        new Thread(server).start();
        startServerControl();
        sc.close();

    }

    private static int doInstruction(int answer) throws Exception {
        switch (answer) {
            case 1:
                System.out.println("Input path to file");
                if (!siteLoader.download(sc.nextLine())) {
                    log.error(JAR_FILE_LOAD_INCORRECT);
                }
                break;
            case 0:
                server.close();
                return 1;
            default:
                System.out.println("You input incorrect command please reaped");
                log.warn("Unknown command from user");
                break;
        }
        return 0;
    }

    private static void printMainMenu() {
        System.out.println("If you want add site type 1");
        System.out.println("If you want stop work server type 0");
    }

    private static void startServerControl() {
        try (Scanner scanner = new Scanner(System.in)) {
            int temp = 0;
            while (temp == 0) {
                printMainMenu();
                temp = doInstruction(scanner.nextInt());
            }
        } catch (Exception e) {
            log.error(String.format(CONTROL_SERVER_EXCEPTION, e.getMessage()));
        }
    }

    private Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (runnable) {
                Socket socket = serverSocket.accept();
                executor.execute(new Client(new Connection(socket)));
                log.info(NEW_SOCKET);
            }
        } catch (IOException e) {
            log.error(String.format(ERROR_IN_SERVER_WORK, e.getMessage()));
        }
        log.info(SERVER_STOPPED);
    }

    @Override
    public void close() throws Exception {
        new Socket(InetAddress.getByName(PROPERTY_LOADER.property("server.host")), 8080);
        runnable = false;
        executor.shutdownNow();

    }
}
