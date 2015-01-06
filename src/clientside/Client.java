package clientside;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
		Client c = new Client(false);
		c.startConnection();

	}

	//close all stream
	public void quit(){
		quit = true;
		try {
			if (out!= null)
				out.close();
			if (in!= null)
				in.close();
			if (stdIn!= null)
				stdIn.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	//force the client to exit
	public void forceExit(){
		System.exit(1);
	}
	
	//main body of Client, handle connection, send message and get response
	public void startConnection(){
		//assume the host is run on the same machine, hence "localhost"
		String hostName = "localhost";
		int portNumber = 9000;
		
		
		try {
			//start connection and in, out stream
				clientSocket = new Socket(hostName, portNumber);
				 out = new PrintWriter(
						clientSocket.getOutputStream(), true);
				 in = new BufferedReader(new InputStreamReader(
						clientSocket.getInputStream()));
				 stdIn = new BufferedReader(new InputStreamReader(
					System.in));
				
				 //get message from server, put in a separate thread
				Runnable r1 = new Runnable() {
					public void run() {
						String fromServer="";
						  
						try {
							while (!quit) {
								fromServer = in.readLine();
								
								if (!fromServer.equals("")) {
									System.out.println("Server: " + fromServer);
									if (testing) {
										testOutput = fromServer;
									}
								}
								if (fromServer.equals("+OK user quit")) quit();
								
								
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							quit();
						} 
					}
				};
				
				//send message to the server
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
							quit();
						}
					}
				};
			
				threadServer = new Thread(r1);
				threadClient = new Thread(r2);
				
				threadServer.start();
				threadClient.start();
			

		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + hostName);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to "
					+ hostName);
			System.exit(1);
			
		}
	}
	
	//set input message, only for testing purpose
	public void sendMessage(String input){
		testInput = input;
	}
	
	//get output message, only for testing purpose
	public String getOutput() {
		if (testOutput!=null) {
			String output = testOutput;
			testOutput = null;
			return output;
		} else return null;
		
	}
}
