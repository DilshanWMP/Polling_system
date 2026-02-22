import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PollingServer {

    static int port = 7777;
    private static final Map<String, Integer> pollResults = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        System.out.println("\t\t LAN Polling Server");
        System.out.println("\t\t====================\n");

        pollResults.put("1", 0);
        pollResults.put("2", 0);
        pollResults.put("3", 0);

        ServerSocket serverSocket = new ServerSocket(port);

        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New voter connected: " + clientSocket.getInetAddress());
                
                ClientHandler handler = new ClientHandler(clientSocket);
                handler.start();
            }
        } finally {
            serverSocket.close();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                out.println("Welcome to the 24th Batch Polling System!");
                out.println("What is the preferred tech stack for the upcoming module?");
                out.println("1: MERN Stack");
                out.println("2: Spring Boot & React");
                out.println("3: Flutter & Firebase");
                out.println("Enter your choice (1, 2, or 3):");
                out.println("END_OF_MENU");

                String response = in.readLine();
                
                if (response != null && pollResults.containsKey(response)) {
                    pollResults.put(response, pollResults.get(response) + 1);
                    out.println("\nVote recorded successfully!");
                } else {
                    out.println("\nInvalid choice. Vote not recorded.");
                }

                out.println("Current Live Results:");
                out.println("MERN Stack: " + pollResults.get("1"));
                out.println("Spring Boot & React: " + pollResults.get("2"));
                out.println("Flutter & Firebase: " + pollResults.get("3"));
                out.println("END_OF_RESULTS");

            } catch (IOException e) {
                System.out.println("Connection error: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Error closing socket.");
                }
            }
        }
    }
}
