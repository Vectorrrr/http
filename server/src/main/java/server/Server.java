package server;

import org.apache.log4j.Logger;
import loader.PropertyLoader;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by igladush on 07.04.16.
 */
public class Server implements Runnable,AutoCloseable {
    private static final Logger log=Logger.getLogger(Server.class);
    private static final String SERVER_STOPED = "Server stoped";
    private static final String NEW_SOCKET = "Accept new socket";
    private static final String ERROR_IN_SERVER_WORK = "An error occurred while the server work %s";
    public static final String CONTROL_SERVER_EXCEPTION = "Server controller method catch exception %s";


    private static  Server server=null;
    public static void main(String[] args) throws Exception {
        int port = Integer.valueOf(PropertyLoader.property("port"));
        server = new Server(port);
        new Thread(server).start();
        startServerControl();

    }

    private static void startServerControl() {
        try(Scanner scanner = new Scanner(System.in)){
            int temp=0;
            while(temp==0){
                printMainMenu();
                temp=doInstruction(scanner.nextInt());
            }
        }catch(Exception e){
            log.error(String.format(CONTROL_SERVER_EXCEPTION,e.getMessage()));
        }
    }

    private static int doInstruction(int answer) throws Exception {
        switch (answer) {
            case 1:
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

    private int port;
    private boolean runnable=true;
    private Server(int port){
        this.port=port;
    }
    @Override
    public void run() {
        try(ServerSocket serverSocket=new ServerSocket(port)) {
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
        //todo make this a property
        new Socket(InetAddress.getByName(PropertyLoader.property("server.host")),8080);
        runnable = false;

    }
}
