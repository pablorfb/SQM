import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class Conversation implements Runnable {

	private static HashMap<String, Conversation> conversations = new HashMap<>();
	public static final String MESSAGE_SENT = "Message sent.";
	public static final String ADDRESSE_NOT_FOUND = "Addresee not found.";
	public static final String SUCCESFULLY_LOGGED_IN = "Succesfully logged in.";
	public static final String USERNAME_NOT_FOUND = "Username not found.";
	public static final String SUCCESFULLY_LOGGED_OUT = "Succesfully logged out.";
	public static final String ALREADY_LOGGED_IN = "User already logged.";
	

	private PrintWriter out;
	private BufferedReader in;
	private Socket clientSocket;
	private CommandInterpreter commandInterpreter;

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
		
		commandInterpreter = new CommandInterpreter(new QueryProcessor(this));
	}

	public boolean login(String username) {
		synchronized (conversations) {
			if (conversations.get(username) == null) {
				conversations.put(username, this);
				return true;
			}
		}
		return false;
	}

	public static Set<String> getUsers() {
		synchronized (conversations) {
			return conversations.keySet();
		}
	}
	
	public boolean isLoggedIn(){
		Set<String> users = Conversation.getUsers();
		synchronized (conversations) {
			for (String user : users){
				if (conversations.get(user) == this){
					return true;
				}
			}
		}
		return false;
				
	}

	public String sendMessage(String destination, String message) {
		Conversation adressee;
		synchronized (conversations) {
			adressee = conversations.get(destination);
		}
		if (adressee == null)
			return ADDRESSE_NOT_FOUND;
		adressee.out.write(message);
		return MESSAGE_SENT;
	}

	public String sendBroadcast(String message) {
		Collection<Conversation> addressees;
		synchronized (conversations) {
			addressees = conversations.values();
		}
		for (Conversation addressee : addressees) {
			if (addressee != this)
				addressee.out.write(message);
		}
		return MESSAGE_SENT;
	}
	
	public boolean logout() {
		Set<String> users = Conversation.getUsers();
		synchronized (conversations) {
			for (String user : users){
				if (conversations.get(user) == this){
					conversations.remove(user);
					close();
					return true;
				}
			}
		}
		
		return false;
	}


	public void run() {
		String userInput;
		System.out.println("echo: Initialized");
		out.println("echo: Welcome");
		try {
			while ((userInput = in.readLine()) != null) {
				out.println( commandInterpreter.handleInput(userInput));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally { // Close all connections
			close();
		}

		System.out.println("End of Conversation");
	}

	private void close(){
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
}
