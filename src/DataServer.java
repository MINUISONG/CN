import java.io.*;
import java.net.*;
import java.util.*;

public class DataServer {
	public static void main(String[] args) throws IOException {
		ServerSocket listner = new ServerSocket(59090);
		System.out.println("The date server is running..");
		
		while (true) {
			try (Socket socket = listner.accept()) {
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				out.println(new Date().toString());
				

			} 
		}
	}

}
