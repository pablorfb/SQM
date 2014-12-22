import static org.junit.Assert.*;

import serverside.Conversation;
import serverside.Server;

import org.junit.Test;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;

import clientside.Client;

public class ConversationWhiteBoxTest {

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

		
	 Conversation c1 = new Conversation(new Socket());
	 Conversation c2 = new Conversation(new Socket());
	 assertFalse( c1.login(null) );
	 assertFalse(c1.login("") ) ;
	 assertFalse(c1.logout() ) ;
	 assertFalse(c1.isLoggedIn());
	 assertTrue(c1.login("a") ) ;
	 assertTrue(c1.isLoggedIn());
	 assertFalse(c1.login("a") ) ;
	 assertFalse(c2.login("a") ) ;
	 assertTrue(c2.login("b") ) ;
	 


	 assertEquals("Addresee not found.",  c1.sendMessage(null, "Hey!"));
	 assertEquals("Streams not initialized.",  c1.sendMessage("b", "Hey!"));
	 
	 assertEquals(2, Conversation.getUsers().size());
	 assertTrue(c2.logout() ) ;
	 assertEquals(1, Conversation.getUsers().size());
	 assertTrue(c1.logout() ) ;
	}


}

