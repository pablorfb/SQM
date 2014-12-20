import static org.junit.Assert.*;

import org.junit.Test;


import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProtocolBlackBoxTesting {

	Client[] clients = new Client[100];

	@Before
	public void setUp() throws Exception {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Server.main(null);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}).start();
		for (int i = 0; i < clients.length; i++) {
			clients[i] = new Client(true);
		}
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("Run @After"); // for illustration
		for (int i = 0; i < clients.length; i++) {
			//if (!clients[i].quit)
			//	clients[i].quit();
		}
	}
	
	@Test
	public void normalTest() {

		clients[0].startConnection();
		waitForNextOutput(clients[0]);
		
		//Not loggedIn tests
		clients[0].sendMessage("");
		assertTrue(waitForNextOutput(clients[0]).startsWith("-BAD"));

		clients[0].sendMessage("list");
		assertTrue(waitForNextOutput(clients[0]).startsWith(
				"-BAD"));

		clients[0].sendMessage("mesg");
		assertTrue(waitForNextOutput(clients[0]).startsWith("-BAD"));

//		clients[0].sendMessage("mesg user");
//		assertTrue(waitForNextOutput(clients[0]).startsWith("-BAD"));

		clients[0].sendMessage("mesg user hello");
		assertTrue(waitForNextOutput(clients[0]).startsWith("-BAD"));

	//	clients[0].sendMessage("mesg tung");
	//	assertTrue(waitForNextOutput(clients[0]).startsWith("-BAD"));

		clients[0].sendMessage("mesg tung hello");
		assertTrue(waitForNextOutput(clients[0]).startsWith("-BAD"));

		clients[0].sendMessage("hail");
		assertTrue(waitForNextOutput(clients[0]).startsWith("-BAD"));

		clients[0].sendMessage("hail hello");
		assertTrue(waitForNextOutput(clients[0]).startsWith("-BAD"));

		clients[0].sendMessage("quit");
		assertTrue(waitForNextOutput(clients[0]).startsWith("-BAD"));

		clients[0].sendMessage("stat");
		assertTrue(waitForNextOutput(clients[0]).startsWith("+OK there are 0"));
		
		//Logged in test

		clients[0].sendMessage("iden");
		assertTrue(waitForNextOutput(clients[0]).startsWith("-BAD"));

		clients[0].sendMessage("iden tung");
		assertTrue(waitForNextOutput(clients[0]).startsWith("+OK"));

		clients[0].sendMessage("iden tung");
		assertTrue(waitForNextOutput(clients[0]).startsWith("-BAD"));

		clients[0].sendMessage("iden otherUsername");
		assertTrue(waitForNextOutput(clients[0]).startsWith("-BAD"));

		clients[0].sendMessage("stat");
		assertTrue(waitForNextOutput(clients[0]).startsWith("+OK there are 1"));

		clients[0].sendMessage("list");
		assertTrue(waitForNextOutput(clients[0]).equals(
				"+OK there are 1 users logged in: tung"));

		clients[0].sendMessage("mesg");
		assertTrue(waitForNextOutput(clients[0]).startsWith("-BAD"));

		clients[0].sendMessage("mesg user");
		assertTrue(waitForNextOutput(clients[0]).startsWith("-BAD"));

		clients[0].sendMessage("mesg user hello");
		assertTrue(waitForNextOutput(clients[0]).startsWith("-BAD"));

//		clients[0].sendMessage("mesg tung");
//		assertTrue(waitForNextOutput(clients[0]).startsWith("-BAD"));

		clients[0].sendMessage("mesg tung hello");
		assertTrue(waitForNextOutput(clients[0]).startsWith("+Mesg:"));

		clients[0].sendMessage("hail");
		assertTrue(waitForNextOutput(clients[0]).startsWith("-BAD"));

		clients[0].sendMessage("hail hello");
		assertTrue(waitForNextOutput(clients[0]).startsWith(" Message sent."));

		clients[0].sendMessage("quit");
		assertTrue(waitForNextOutput(clients[0]).startsWith("+OK user quit"));
		
		

	}

	private String waitForNextOutput(Client c) {
		String output;
		while (true) {
			output = c.getOutput();
			if (output != null) {

				// assertTrue(output.startsWith("+OK"));
				break;
			}
		}
		//System.out.println(output);
		return output;

	}

}
