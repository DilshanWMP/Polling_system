package Project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class PollingClient {

    static int PORT = 7777;

    public static void main(String[] args) throws IOException {

        System.out.println("\t\t Polling Client");
        System.out.println("\t\t====================\n\n");

        Scanner scan = new Scanner(System.in);
        
        System.out.print("Enter Server IP Address (Press Enter for localhost): ");
        String ipAddress = scan.nextLine();
        if (ipAddress.isEmpty()) {
            ipAddress = "127.0.0.1";
        }

        Socket socket = new Socket(ipAddress, PORT);
        System.out.println("Server is Connected............\n\n");

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        try {
            String serverMessage;
            while ((serverMessage = in.readLine()) != null) {
                if (serverMessage.equals("END_OF_MENU")) {
                    break;
                }
                System.out.println("Server says : " + serverMessage);
            }

            System.out.print("Client : ");
            String output = scan.nextLine();
            out.println(output);

            while ((serverMessage = in.readLine()) != null) {
                if (serverMessage.equals("END_OF_RESULTS")) {
                    break;
                }
                System.out.println("Server says : " + serverMessage);
            }

        } finally {
            out.close();
            in.close();
            socket.close();
            scan.close();
        }
    }
}
