import java.io.*;
import java.net.*;

public class gameClient {
    private static String serverIP;
    private static int port;

    public static void main(String[] args) {
        // Load server information
        loadServerInfo();

        try (Socket socket = new Socket(serverIP, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to the server at " + serverIP + ":" + port);

            String serverMessage;
            while ((serverMessage = in.readLine()) != null) {
                if (serverMessage.startsWith("QUESTION: ")) {
                    System.out.println(serverMessage);
                    System.out.print("Your answer: ");
                    String answer = consoleInput.readLine();
                    out.println("ANSWER: " + answer);
                } else if (serverMessage.startsWith("FEEDBACK: ")) {
                    System.out.println(serverMessage);
                } else if (serverMessage.startsWith("SCORE: ")) {
                    System.out.println("Your final score: " + serverMessage.substring(7));
                    
                 // Delay for 3 seconds before closing the connection
                    try {
                        Thread.sleep(1000); // 1000 milliseconds = 1 seconds
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    
                    break;
                }
            }
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: " + serverIP);
        } catch (IOException e) {
            System.out.println("I/O error: " + e.getMessage());
        } finally {
            System.out.println("Client socket closed. Goodbye!");
        }
    }

    // Load server information from server_info.dat file
    private static void loadServerInfo() {
        try (BufferedReader br = new BufferedReader(new FileReader("server_info.dat"))) {
            serverIP = br.readLine(); // Read IP address from the first line
            port = Integer.parseInt(br.readLine()); // Read port number from the second line
        } catch (FileNotFoundException e) {
            System.out.println("server_info.dat not found. Using default settings.");
            serverIP = "127.0.0.1";
            port = 12345;
        } catch (IOException e) {
            System.out.println("Error reading server_info.dat. Using default settings.");
            serverIP = "127.0.0.1";
            port = 12345;
        }
    }
}