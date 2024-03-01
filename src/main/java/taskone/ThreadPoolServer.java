package taskone;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ThreadPoolServer {

    public static void main(String[] args) throws Exception {
        int port;
        int maxConnections;
        StringList strings = new StringList();

        if (args.length != 2) {
            // gradle runServer -Pport=9099 -PmaxConnections=5 -q --console=plain
            System.out.println("Usage: gradle runServer -Pport=9099 -PmaxConnections=5 -q --console=plain");
            System.exit(1);
        }
        port = -1;
        maxConnections = -1;
        try {
            port = Integer.parseInt(args[0]);
            maxConnections = Integer.parseInt(args[1]);
        } catch (NumberFormatException nfe) {
            System.out.println("[Port] and [MaxConnections] must be integers");
            System.exit(2);
        }
        ServerSocket server = new ServerSocket(port);

        // Create a fixed-size thread pool to handle incoming connections
        ExecutorService executor = Executors.newFixedThreadPool(maxConnections);

        System.out.println("Server Started...");
        while (true) {
            System.out.println("Accepting a Request...");
            Socket sock = server.accept();

            // Submit the ClientHandler to the thread pool
            ClientHandler handler = new ClientHandler(sock, strings);
            executor.submit(handler);
        }
    }
}
