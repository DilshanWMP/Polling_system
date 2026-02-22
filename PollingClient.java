import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class PollingClient {

    static int port = 7777;

    public static void main(String[] args) {
        System.out.println("\t\t Polling Client");
        System.out.println("\t\t================\n");

        Scanner scannerInput = new Scanner(System.in);
        
        System.out.print("Enter Server IP Address (Press Enter for localhost): ");
        String ipAddress = scannerInput.nextLine();
        if (ipAddress.isEmpty()) {
            ipAddress = "127.0.0.1";
        }

        try {
            Socket socket = new Socket(ipAddress, port);
            System.out.println("Connected to Polling Server....\n");

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String serverMessage;
            while ((serverMessage = in.readLine()) != null) {
                if (serverMessage.equals("END_OF_MENU")) {
                    break;
                }
                System.out.println(serverMessage);
            }

            System.out.print("Your vote: ");
            String output = scannerInput.nextLine();
            out.println(output);

            while ((serverMessage = in.readLine()) != null) {
                if (serverMessage.equals("END_OF_RESULTS")) {
                    break;
                }
                System.out.println(serverMessage);
            }

            socket.close();
            out.close();
            in.close();

        } catch (IOException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
        }
    }
}
