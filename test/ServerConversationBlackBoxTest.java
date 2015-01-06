import static org.junit.Assert.*;

import serverside.Conversation;
import serverside.Server;

import org.junit.Test;

import java.io.IOException;
import java.net.BindException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;

public class ServerConversationBlackBoxTest {

	Thread server;
	boolean initServer = false;
	
	@Before
	public void setUp() throws Exception {
		if (!initServer){
			server = new Thread(new Runnable() {
				
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
	
			});
			server.start();
			initServer = true;
		}
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("Run @After"); // for illustration
		
	}
	
	@Test(expected= BindException.class)
	public void serverTest() throws UnknownHostException, IOException {
		Server.main(null);
	}
	
	//Black box testing Conversation class 
	@Test
	public void conversationTest() {

		Conversation c1 = new Conversation(new Socket());
		Conversation c2 = new Conversation(new Socket());
		
		assertFalse( c1.login(null) );
		assertFalse(c1.login("") ) ;
		assertFalse(c1.logout() ) ;
		assertFalse(c1.isLoggedIn());
		
		
		
		assertTrue(c1.login("abfs123154asdasf2s1fsa34sa56dssaf321sa5f4sa6d5sa132fsa65d4") ) ;
		assertTrue(c1.logout());
		
		assertTrue(c1.login("a") ) ;
		assertTrue(c1.isLoggedIn());
		assertFalse(c1.login("a") ) ;
		assertFalse(c1.login("aasav") ) ;
		
		assertEquals(1, Conversation.getUsers().size());
		
		assertEquals(0, c1.getNumberOfMessages());
		
		assertTrue(c1.sendBroadcast("abc").startsWith("Message sent."));
		assertEquals(1, c1.getNumberOfMessages());
		assertTrue(c1.sendBroadcast("abc").startsWith("Message sent."));
		assertEquals(2, c1.getNumberOfMessages());
		
		assertTrue(c1.sendMessage("b","abc").startsWith("Addresee not found."));
		assertTrue(c2.login("b") ) ;
		assertTrue(c1.sendBroadcast("abc").startsWith("Message sent."));
		assertTrue(c1.sendMessage("b","abc").startsWith("Streams not initialized."));
		assertEquals(2, Conversation.getUsers().size());
		assertTrue(c2.logout());
		assertEquals(1, Conversation.getUsers().size());
		
		c1.logout();
		

	}


}

