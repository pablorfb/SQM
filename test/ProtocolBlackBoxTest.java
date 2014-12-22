import static org.junit.Assert.*;

import serverside.Server;

import org.junit.Test;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;

import clientside.Client;

public class ProtocolBlackTest {

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
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("Run @After"); // for illustration
	}

	@Test
	public void normalTest() {

		notLoggedIn(new Client(true));

		loggedIn(new Client(true));

		conversation2(new Client(true), new Client(true));

		conversation3(new Client(true), new Client(true), new Client(true));

	}

	private void notLoggedIn(Client client) {
		System.out.println("####NOT LOGGED IN TEST#####");

		client.startConnection();
		waitForNextOutput(client);

		client.sendMessage("");
		assertTrue(waitForNextOutput(client).startsWith("-BAD"));

		client.sendMessage("list");
		assertTrue(waitForNextOutput(client).startsWith("-BAD"));

		client.sendMessage("mesg");
		assertTrue(waitForNextOutput(client).startsWith("-BAD"));

		client.sendMessage("mesg user");
		assertTrue(waitForNextOutput(client).startsWith("-BAD"));

		client.sendMessage("mesg user hello");
		assertTrue(waitForNextOutput(client).startsWith("-BAD"));

		client.sendMessage("mesg tung");
		assertTrue(waitForNextOutput(client).startsWith("-BAD"));

		client.sendMessage("mesg tung hello");
		assertTrue(waitForNextOutput(client).startsWith("-BAD"));

		client.sendMessage("hail");
		assertTrue(waitForNextOutput(client).startsWith("-BAD"));

		client.sendMessage("hail hello");
		assertTrue(waitForNextOutput(client).startsWith("-BAD"));

		client.sendMessage("stat");
		assertTrue(waitForNextOutput(client).startsWith("+OK there are 0"));

		client.sendMessage("quit");
		assertTrue(waitForNextOutput(client).startsWith(
				"-BAD not logged in yet"));

		client.quit();

	}

	private void loggedIn(Client client) {

		System.out.println("####LOGGED IN TEST#####");

		client.startConnection();
		waitForNextOutput(client);

		client.sendMessage("iden");
		assertTrue(waitForNextOutput(client).startsWith("-BAD"));

		client.sendMessage("iden tung");
		assertTrue(waitForNextOutput(client).startsWith("+OK"));

		client.sendMessage("iden tung");
		assertTrue(waitForNextOutput(client).startsWith("-BAD"));

		client.sendMessage("iden otherUsername");
		assertTrue(waitForNextOutput(client).startsWith("-BAD"));

		client.sendMessage("stat");
		assertTrue(waitForNextOutput(client).startsWith("+OK there are 1"));

		client.sendMessage("list");
		assertTrue(waitForNextOutput(client).equals(
				"+OK there are 1 users logged in: tung"));

		client.sendMessage("mesg");
		assertTrue(waitForNextOutput(client).startsWith("-BAD"));

		client.sendMessage("mesg user");
		assertTrue(waitForNextOutput(client).startsWith("-BAD"));

		client.sendMessage("mesg user hello");
		assertTrue(waitForNextOutput(client).startsWith("-BAD"));

		client.sendMessage("mesg tung");
		assertTrue(waitForNextOutput(client).startsWith("-BAD"));

		client.sendMessage("mesg tung hello");
		assertTrue(waitForNextOutput(client).startsWith("+MESG:"));
		assertTrue(waitForNextOutput(client).startsWith("Message sent."));
		client.sendMessage("hail");
		assertTrue(waitForNextOutput(client).startsWith("-BAD"));

		client.sendMessage("hail hello");
		assertTrue(waitForNextOutput(client).startsWith("Message sent."));

		client.sendMessage("quit");
		assertTrue(waitForNextOutput(client).startsWith("+OK user quit"));
	}

	private void conversation2(Client c1, Client c2) {

		System.out.println("####CONVERSATION 2 TEST#####");

		c1.startConnection();
		waitForNextOutput(c1);

		c2.startConnection();
		waitForNextOutput(c2);

		c1.sendMessage("iden tung");
		assertTrue(waitForNextOutput(c1).startsWith("+OK"));

		c1.sendMessage("stat");
		assertTrue(waitForNextOutput(c1).startsWith("+OK there are 1"));

		c2.sendMessage("iden pablo");
		assertTrue(waitForNextOutput(c2).startsWith("+OK"));

		c1.sendMessage("stat");
		assertTrue(waitForNextOutput(c1).startsWith("+OK there are 2"));

		c2.sendMessage("stat");
		assertTrue(waitForNextOutput(c2).startsWith("+OK there are 2"));

		c1.sendMessage("mesg Hey There!");
		assertTrue(waitForNextOutput(c1).startsWith("-BAD"));

		c1.sendMessage("mesg pablo Hey There");
		assertTrue(waitForNextOutput(c1).startsWith("Message sent."));
		assertTrue(waitForNextOutput(c2).startsWith("+MESG: Hey There"));
		c1.sendMessage("stat");
		assertTrue(waitForNextOutput(c1).startsWith(
				"+OK there are 2 users online, 1 messages sent."));

		c2.sendMessage("mesg tung Hey There");
		assertTrue(waitForNextOutput(c2).startsWith("Message sent."));
		assertTrue(waitForNextOutput(c1).startsWith("+MESG: Hey There"));
		c2.sendMessage("stat");
		assertTrue(waitForNextOutput(c2).startsWith(
				"+OK there are 2 users online, 1 messages sent."));

		c1.sendMessage("quit");
		assertTrue(waitForNextOutput(c1).startsWith("+OK user quit"));

		c2.sendMessage("stat");
		assertTrue(waitForNextOutput(c2).startsWith("+OK there are 1"));

		c2.sendMessage("quit");
		assertTrue(waitForNextOutput(c2).startsWith("+OK user quit"));

	}

	private void conversation3(Client c1, Client c2, Client c3) {

		System.out.println("####CONVERSATION 3 TEST#####");

		c1.startConnection();
		waitForNextOutput(c1);

		c2.startConnection();
		waitForNextOutput(c2);

		c1.sendMessage("iden tung");
		assertTrue(waitForNextOutput(c1).startsWith("+OK"));

		c1.sendMessage("stat");
		assertTrue(waitForNextOutput(c1).startsWith("+OK there are 1"));

		c2.sendMessage("iden pablo");
		assertTrue(waitForNextOutput(c2).startsWith("+OK"));

		c1.sendMessage("stat");
		assertTrue(waitForNextOutput(c1).startsWith("+OK there are 2"));

		c3.startConnection();
		waitForNextOutput(c3);

		c3.sendMessage("iden tung");
		assertTrue(waitForNextOutput(c3).startsWith("-BAD"));

		c3.sendMessage("iden tom");
		assertTrue(waitForNextOutput(c3).startsWith("+OK"));

		c3.sendMessage("stat");
		assertTrue(waitForNextOutput(c3).startsWith("+OK there are 3"));

		c1.sendMessage("mesg tom Hey There!");
		assertTrue(waitForNextOutput(c1).equals("Message sent."));
		assertTrue(waitForNextOutput(c3).equals("+MESG: Hey There!"));

		c3.sendMessage("mesg tung Hey There!");
		assertTrue(waitForNextOutput(c3).equals("Message sent."));
		assertTrue(waitForNextOutput(c1).equals("+MESG: Hey There!"));

		c1.sendMessage("mesg tom How are you?");
		assertTrue(waitForNextOutput(c1).equals("Message sent."));
		assertTrue(waitForNextOutput(c3).equals("+MESG: How are you?"));

		c1.sendMessage("stat");
		assertTrue(waitForNextOutput(c1).equals(
				"+OK there are 3 users online, 2 messages sent."));

		c2.sendMessage("mesg tom Hey There!");
		assertTrue(waitForNextOutput(c2).equals("Message sent."));
		assertTrue(waitForNextOutput(c3).equals("+MESG: Hey There!"));

		c1.sendMessage("mesg pablo Hey There!");
		assertTrue(waitForNextOutput(c1).equals("Message sent."));
		assertTrue(waitForNextOutput(c2).equals("+MESG: Hey There!"));

		c1.sendMessage("stat");
		assertTrue(waitForNextOutput(c1).equals(
				"+OK there are 3 users online, 3 messages sent."));

		c1.sendMessage("HAIL Hi everyone!!!");
		assertTrue(waitForNextOutput(c1).equals("Message sent."));
		assertTrue(waitForNextOutput(c2).equals("+MESG: Hi everyone!!!"));
		assertTrue(waitForNextOutput(c3).equals("+MESG: Hi everyone!!!"));
		
		assertTrue(waitForNextOutput(c1).equals(
				"+OK there are 3 users online, 4 messages sent."));

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
		// System.out.println(output);
		return output;

	}

}
