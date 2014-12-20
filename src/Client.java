import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	private PrintWriter out;
	private BufferedReader in;
	private BufferedReader stdIn;
	
	private Thread threadClient;
	private Thread threadServer;
	
	boolean quit = false;
	Socket clientSocket;
	
	boolean testing = false;
	
	String testInput = null;
	
	String testOutput = null;
	
	public Client (boolean testing) {
		this.testing = testing;
	}
	
	public static void main(String[] args) throws IOException {
		//test connection to server by manually input
		// or run the Pop3Server_test.java to auto test with JUnit
		Client c = new Client(false);
		c.startConnection();
		
		//get host and port

	}

	
	public void quit(){
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
	
	public void startConnection(){
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
									if (!fromServer.equals("")) {
										System.out.println("Server: " + fromServer);
										if (testing) {
											testOutput = fromServer;
										}
									}
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
						String fromUser = null;
						
						try {
							
								//fromUser=stdIn.readLine();
							//Thread;
							while (!quit) {
								System.setIn(new ByteArrayInputStream("stat\r\n".getBytes("UTF-8")));
								//System.in.read("stat\r\n".getBytes());
								if (!testing)
									fromUser=stdIn.readLine();
								else {
									if (testInput!=null) {
								
										fromUser = testInput;
										testInput = null;
									} else {
										fromUser = null;
									}
								}
								
								if (fromUser != null){
									System.out.print("Client: " + fromUser + "\n");
									out.println(fromUser);
								
								
									if (fromUser.toUpperCase().equals("QUIT")) {
										try {
											Thread.sleep(1000);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
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
	
	public void sendMessage(String input){
		testInput = input;
	}
	
	public String getOutput() {
		if (testOutput!=null) {
			String output = testOutput;
			testOutput = null;
			return output;
		} else return null;
		
	}
}
