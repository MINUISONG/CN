import java.io.*;
import java.net.*;
import java.util.concurrent.*;

// QuizServer class
public class gameServer {
    private static final int PORT = 12345; // Server port number
    private static final String[] QUESTIONS = {
        "What is the day of Song Min-ui’s birthday, excluding the month?",
        "What is Song Min-ui’s favorite sport?", 
        "What is Song Min-ui's favorite song?"
    };
    private static final String[] ANSWERS = { "24", "basketball", "everything" };

    public static void main(String[] args) {
        // Create ThreadPool (limit to 10 threads)
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server is running on port " + PORT);

            // Accept a single client and handle communication
            Socket clientSocket = serverSocket.accept();
            threadPool.execute(new ClientHandler(clientSocket, QUESTIONS, ANSWERS));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close the server socket
            if (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                    System.out.println("Server socket closed.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // Shutdown the ThreadPool
            threadPool.shutdown();
        }
    }
}

// ClientHandler class
class ClientHandler implements Runnable {
    private Socket clientSocket;
    private int score = 0;
    private String[] questions;
    private String[] answers;

    // ClientHandler constructor
    ClientHandler(Socket socket, String[] questions, String[] answers) {
        this.clientSocket = socket;
        this.questions = questions;
        this.answers = answers;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            // Send questions to the client and receive responses
            for (int i = 0; i < questions.length; i++) {
                out.println("QUESTION: " + questions[i]); // Send question
                String response = in.readLine();
                
                if (response != null && response.startsWith("ANSWER: ")) {
                    String answer = response.substring(8).trim(); // Remove "ANSWER: " and trim
                    if (answer.equalsIgnoreCase(answers[i])) {
                        out.println("FEEDBACK: Correct"); // Send feedback if the answer is correct
                        score++; // Increment score
                    } else {
                        out.println("FEEDBACK: Incorrect"); // Send feedback if the answer is incorrect
                    }
                }
            }

            // Send final score
            out.println("SCORE: " + score);
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close(); // Close the client socket
                System.out.println("Client socket closed. Server shutting down.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}