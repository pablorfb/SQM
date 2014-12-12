import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Server {

	final static int PORTNUMBER = 9000;
	

	public static void main(String[] args) throws UnknownHostException,
			IOException {

		try (ServerSocket serverSocket = new ServerSocket(PORTNUMBER);) {
			while (true) {
				Socket clientSocket = serverSocket.accept();
				Conversation conversation = new Conversation(clientSocket);
				conversations.add(conversation);
				System.out.println(clientSocket.getLocalAddress() + " " + clientSocket);
				new Thread(conversation).start();

			}
		}
	}
	
	

}
