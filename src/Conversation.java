import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Conversation implements Runnable {
	PrintWriter out;
	BufferedReader in;
	Socket clientSocket;

	public Conversation(Socket clientSocket) {
		this.clientSocket = clientSocket;
		try {
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
		} catch (IOException e) {
			System.out.println("Couldn't initialize conversation");
			e.printStackTrace();
		}

	}

	public void run() {
		String userInput;
		System.out.println("echo: Initialized");
		out.println("echo: Welcome");
		try {
			while ((userInput = in.readLine()) != null) {
				out.println(userInput.toUpperCase()); // Use Protocol Handler
														// here
				System.out.println("received: " + userInput);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally { //Close all connections
			out.close();
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					clientSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		System.out.println("End of Conversation");
	}

}
