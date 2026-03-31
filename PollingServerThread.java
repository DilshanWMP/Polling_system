package Project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class PollingServerThread extends Thread {

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private int client_id;
    private Map<String, Integer> pollResults;

    public PollingServerThread(Socket client_socket, int cid, Map<String, Integer> results) throws IOException {
        this.client = client_socket;
        this.client_id = cid;
        this.pollResults = results;
        this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        this.out = new PrintWriter(client.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
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

                System.out.println("\n--- Live Updates after Client " + client_id + " voted ---");
                System.out.println("MERN Stack: " + pollResults.get("1"));
                System.out.println("Spring Boot & React: " + pollResults.get("2"));
                System.out.println("Flutter & Firebase: " + pollResults.get("3"));
                System.out.println("--------------------\n");
            } else {
                out.println("\nInvalid choice. Vote not recorded.");
            }

            out.println("Current Live Results:");
            out.println("MERN Stack: " + pollResults.get("1"));
            out.println("Spring Boot & React: " + pollResults.get("2"));
            out.println("Flutter & Firebase: " + pollResults.get("3"));
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
