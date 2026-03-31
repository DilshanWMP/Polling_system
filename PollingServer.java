package Project;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class PollingServer {

    private static final int PORT = 7777;
    private static final Map<String, Integer> pollResults = new ConcurrentHashMap<>();
    private static final List<String> pollOptions = new ArrayList<>();
    private static String pollQuestion = "";

    public static void main(String[] args) throws IOException {

        System.out.println("\t\t LAN Polling Server Configuration");
        System.out.println("\t\t=================================\n");

        Scanner scanner = new Scanner(System.in);

        // 1. Get the question from the host
        System.out.print("Enter the poll question: ");
        pollQuestion = scanner.nextLine();

        // 2. Get the number of options
        System.out.print("How many options will there be? ");
        int numOptions = Integer.parseInt(scanner.nextLine());

        // 3. Loop to get each option text and set up the results map
        for (int i = 1; i <= numOptions; i++) {
            System.out.print("Enter text for option " + i + ": ");
            String optionText = scanner.nextLine();
            pollOptions.add(optionText);
            pollResults.put(String.valueOf(i), 0); // Sets keys like "1", "2", etc.
        }

        System.out.println("\nPoll setup complete! Starting server...");
        System.out.println("\n\t\t LAN Polling Server Running");
        System.out.println("\t\t===========================\n");

        ServerSocket serverSocket = new ServerSocket(PORT);
        int current_clients = 1;

        try {
            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("Client " + current_clients + " is Connected............\n");

                // Pass the custom question and options into the thread
                PollingServerThread ct = new PollingServerThread(client, current_clients, pollResults, pollQuestion, pollOptions);
                ct.start();
                current_clients++;
            }
        } finally {
            serverSocket.close();
            scanner.close();
        }
    }
}
