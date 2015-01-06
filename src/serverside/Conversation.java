package serverside;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

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
	public static final String INVALID_STREAMS = "Streams not initialized.";


	private PrintWriter out;
	private BufferedReader in;
	private Socket clientSocket;
	private CommandInterpreter commandInterpreter;
	private AtomicInteger messagesSent = new AtomicInteger(0);
	private String username;

	private boolean logout = false;

	public Conversation(Socket clientSocket) {
		this.clientSocket = clientSocket;

		try {
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
		} catch (IOException e) {
			System.out.println("Couldn't initialize streams. ");
		}

		commandInterpreter = new CommandInterpreter(new QueryProcessor(this));
	}

	public boolean login(String username) {
		synchronized (conversations) {
			if (username == null || username == "" || this.username != null)
				return false;
			if (conversations.get(username) == null) {
				this.username = username;
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

	public boolean isLoggedIn() {
		if (username != null)
			return true;
		return false;
	}

	public String sendMessage(String destination, String message) {
		Conversation addressee;
		synchronized (conversations) {
			addressee = conversations.get(destination);
		}
		if (addressee == null )
			return ADDRESSE_NOT_FOUND;
		if (addressee.out == null)
			return INVALID_STREAMS;
		addressee.out.println(message);
		messagesSent.incrementAndGet();
		return MESSAGE_SENT;
	}

	public String sendBroadcast(String message) {
		Collection<Conversation> addressees;
		synchronized (conversations) {
			addressees = conversations.values();
		}
		for (Conversation addressee : addressees) {
			if (addressee != this && addressee.out!=null)
				addressee.out.println(message);
		}
		messagesSent.incrementAndGet();
		return MESSAGE_SENT;
	}

	public int getNumberOfMessages() {
		return messagesSent.get();
	}

	public boolean logout() {
		if (username != null) {
			synchronized (conversations) {
				conversations.remove(username);
				close();
				username = null;
				return true;
			}
		}

		return false;
	}

	public void setLogout(boolean logout) {
		this.logout = logout;

	}

	public void run() {
		String userInput;
		out.println("echo: Welcome");
		try {
			while ((userInput = in.readLine()) != null) {
				out.println(commandInterpreter.handleInput(userInput));
				if (logout)
					break;
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
			logout();
		}

		System.out.println("End of Conversation");
	}

	private void close() {

		try {
			if (out != null)
				out.close();
			if (in != null)
				in.close();
		} catch (IOException e) {
			System.out.println("Couldn't close all streams");
		} finally {
			try {
				if (clientSocket != null)
					clientSocket.close();
			} catch (IOException e) {
				System.out.println("Couldn't close all streams");
			}
		}
	}
}
