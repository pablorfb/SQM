import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	private static PrintWriter out;
	private static BufferedReader in;
	private static BufferedReader stdIn;
	
	private static Thread threadClient;
	private static Thread threadServer;
	
	static boolean quit = false;
	static Socket clientSocket;
	public static void main(String[] args) throws IOException {
		//test connection to server by manually input
		// or run the Pop3Server_test.java to auto test with JUnit
		
		//get host and port
		String hostName = "localhost";
		int portNumber = 9000;
		
		
		
		try {
				clientSocket = new Socket(hostName, portNumber);
				 out = new PrintWriter(
						clientSocket.getOutputStream(), true);
				 in = new BufferedReader(new InputStreamReader(
						clientSocket.getInputStream()));
				 stdIn = new BufferedReader(new InputStreamReader(
					System.in));
			
			//String fromServer;
			//String fromUser = "";

			/*BufferedReader bufferedReader = new BufferedReader(new FileReader(
					"test.txt"));
			String line = null;
			*/
			//while (true) {
				
				Runnable r1 = new Runnable() {
					public void run() {
						String fromServer="";
						  
						try {
							while (!quit) {
								if ((fromServer = in.readLine()) != null) {
									if (!fromServer.equals(""))
										System.out.println("Server: " + fromServer);
								}
								if (fromServer.equals("+OK user quit")) quit();
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
					}
				};
				
				
				Runnable r2 = new Runnable() {
					public void run() {
						String fromUser="";
						
						try {
							
								//fromUser=stdIn.readLine();
							//Thread;
							while (!quit) {
								
								fromUser=stdIn.readLine();
								if (fromUser != null){
									System.out.print("Client: " + fromUser + "\n");
									out.println(fromUser);
								}
								
								if (fromUser.toUpperCase().equals("QUIT")) {
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				};
			
				threadServer = new Thread(r1);
				threadClient = new Thread(r2);
				
				threadServer.start();
				threadClient.start();
			
				//System.out.print("yeah");
				//System.out.print("Client: " + fromUser + "\n");
				//if dont want to maually in put
				//this part auto load from file and send to server
				/*
				if ((line = bufferedReader.readLine()) != null) {
					if (line.equals("")) continue;
					fromUser = line.split("\t")[0];
					System.out.print("Client: " + fromUser + "\n");
					
					out.println(fromUser);
				}*/
				
			//}
			

		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + hostName);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to "
					+ hostName);
			System.exit(1);
		}
	}

	
	private static void quit(){
		quit = true;
		try {
			out.close();
			in.close();
			stdIn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}
