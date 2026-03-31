package Project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class PollingServerThread extends Thread {

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private int client_id;
    private Map<String, Integer> pollResults;
    private String question;
    private List<String> options;

    public PollingServerThread(Socket client_socket, int cid, Map<String, Integer> results, String question, List<String> options) throws IOException {
        this.client = client_socket;
        this.client_id = cid;
        this.pollResults = results;
        this.question = question;
        this.options = options;
        this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        this.out = new PrintWriter(client.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            out.println("Welcome to the Polling System!");
            out.println("Question: " + question);
            
            // Dynamically print the options to the client
            for (int i = 0; i < options.size(); i++) {
                out.println((i + 1) + ": " + options.get(i));
            }
            
            out.println("Enter your choice (1 to " + options.size() + "):");
            out.println("END_OF_MENU");

            String response = in.readLine();

            if (response != null && pollResults.containsKey(response)) {
                pollResults.put(response, pollResults.get(response) + 1);
                out.println("\nVote recorded successfully!");

                System.out.println("\n--- Live Updates after Client " + client_id + " voted ---");
                // Dynamically print the live results to the Server terminal
                for (int i = 0; i < options.size(); i++) {
                    String key = String.valueOf(i + 1);
                    System.out.println(options.get(i) + ": " + pollResults.get(key));
                }
                System.out.println("--------------------\n");
            } else {
                out.println("\nInvalid choice. Vote not recorded.");
            }

            out.println("Current Live Results:");
            // Dynamically print the final results to the Client
            for (int i = 0; i < options.size(); i++) {
                String key = String.valueOf(i + 1);
                out.println(options.get(i) + ": " + pollResults.get(key));
            }
            out.println("END_OF_RESULTS");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
                out.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
