package Project;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PollingServer {

    private static final int PORT = 7777;
    private static final Map<String, Integer> pollResults = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {

        System.out.println("\t\t LAN Polling Server");
        System.out.println("\t\t====================\n\n");

        pollResults.put("1", 0);
        pollResults.put("2", 0);
        pollResults.put("3", 0);

        ServerSocket serverSocket = new ServerSocket(PORT);
        int current_clients = 1;

        try {
            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("Client " + current_clients + " is Connected............\n\n");

                PollingServerThread ct = new PollingServerThread(client, current_clients, pollResults);
                ct.start();
                current_clients++;
            }
        } finally {
            serverSocket.close();
        }
    }
}
