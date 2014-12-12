import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	public static void main(String[] args) throws IOException {
		//test connection to server by manually input
		// or run the Pop3Server_test.java to auto test with JUnit
		
		//get host and port
		String hostName = "localhost";
		int portNumber = 9000;
		
		try (Socket clientSocket = new Socket(hostName, portNumber);
				PrintWriter out = new PrintWriter(
						clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(
						clientSocket.getInputStream()));) {
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(
					System.in));
			String fromServer;
			String fromUser = "";

			/*BufferedReader bufferedReader = new BufferedReader(new FileReader(
					"test.txt"));
			String line = null;
			*/
			while (true) {
				while ((fromServer = in.readLine()) != null) {
					if (!in.ready()) break;
					System.out.println("Server: " + fromServer);
					
				}
				
				if (fromUser != null) {
					fromUser=stdIn.readLine();
					System.out.print("Client: " + fromUser + "\n");
					out.println(fromUser);
				}
				
				//if dont want to maually in put
				//this part auto load from file and send to server
				/*
				if ((line = bufferedReader.readLine()) != null) {
					if (line.equals("")) continue;
					fromUser = line.split("\t")[0];
					System.out.print("Client: " + fromUser + "\n");
					
					out.println(fromUser);
				}*/
				
			}
			

		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + hostName);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to "
					+ hostName);
			System.exit(1);
		}
	}

}
