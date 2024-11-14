import java.io.*;
import java.net.*;
import java.util.*;

public class DataClient {
	public static void main(String[] args) throws IOException {
		Socket socket = new Socket("localhost",59090);
		Scanner in = new Scanner(socket.getInputStream());
		System.out.println("Server response: " + in.nextLine());
	
		
	}

}
