import static org.junit.Assert.*;

import serverside.CommandInterpreter;
import serverside.Conversation;
import serverside.QueryProcessor;
import serverside.Server;

import org.junit.Test;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;

public class InterpreterQueryWhiteBoxTest {

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
	
	//testing the functionality of CommandInterpreter class
	@Test
	public void InterpreterTest() {
		QueryProcessor qp = new QueryProcessor(new Conversation(new Socket()));
		CommandInterpreter ci = new CommandInterpreter(qp);
		
		assertFalse(ci.checkUsernameSyntax("a"));
		assertTrue(ci.checkUsernameSyntax("abc"));
		assertTrue(ci.checkUsernameSyntax("abcdefghiklmnop"));
		assertFalse(ci.checkUsernameSyntax("123456789123456789"));
		assertTrue(ci.checkUsernameSyntax("aAbB"));
		assertTrue(ci.checkUsernameSyntax("aA_Bc-19"));
		assertFalse(ci.checkUsernameSyntax("aA_Bc?!\""));
		
		assertFalse(ci.generalSyntaxChecking("abc"));
		assertFalse(ci.generalSyntaxChecking("quit "));
		assertFalse(ci.generalSyntaxChecking("stat  "));
		assertFalse(ci.generalSyntaxChecking("list abc"));
		
		assertTrue(ci.generalSyntaxChecking("quit"));
		assertTrue(ci.generalSyntaxChecking("stat"));
		assertTrue(ci.generalSyntaxChecking("list"));
		
		assertFalse(ci.generalSyntaxChecking("iden"));
		assertFalse(ci.generalSyntaxChecking("iden "));
		assertFalse(ci.generalSyntaxChecking("iden abc def"));
		assertFalse(ci.generalSyntaxChecking("iden a!?%"));
		assertTrue(ci.generalSyntaxChecking("iden abc"));
		
		assertFalse(ci.generalSyntaxChecking("hail"));
		assertTrue(ci.generalSyntaxChecking("hail "));
		assertTrue(ci.generalSyntaxChecking("hail abc def"));
		
		assertFalse(ci.generalSyntaxChecking("mesg"));
		assertFalse(ci.generalSyntaxChecking("mesg "));
		assertFalse(ci.generalSyntaxChecking("mesg abc "));
		assertFalse(ci.generalSyntaxChecking("mesg a!$c def"));
		assertTrue(ci.generalSyntaxChecking("mesg abc def"));
		
		
		
	}
	
	//testing the functionality of QueryProcessor class
	@Test
	public void QueryTest() {
		Conversation c1 = new Conversation(new Socket());
		Conversation c2 = new Conversation(new Socket());
		
		QueryProcessor qp1 = new QueryProcessor(c1);
		
		
		
		assertEquals(0,qp1.getUsersList().size());
		
		c2.login("a");
		
		assertFalse(qp1.checkUsername("a"));
		assertEquals(1,qp1.getUsersList().size());
		
		assertTrue(qp1.hail("abc").startsWith("-BAD"));
		
		c1.login("b");
		assertTrue(qp1.hail("abc").startsWith("Message sent"));
		c1.logout();
		assertTrue(qp1.hail("abc").startsWith("-BAD"));

		c1.login("b");
		assertTrue(qp1.isLoggedIn());
		assertTrue(qp1.iden("b").contains("alerady logged in"));
		c1.logout();
		assertFalse(qp1.isLoggedIn());
		assertTrue(qp1.iden("a").contains("choose another name"));
		assertTrue(qp1.iden("b").startsWith("+OK"));
		c1.logout();
		assertTrue(qp1.iden("").startsWith("-BAD"));
		assertTrue(qp1.iden(null).startsWith("-BAD"));
		assertTrue(qp1.iden("").startsWith("-BAD"));
		
		assertTrue(qp1.sendMsgAll("").startsWith("Message sent"));
		
		c1.logout();
		assertTrue(qp1.list().startsWith("-BAD"));
		c1.login("b");
		assertEquals("+OK there are 2 users logged in: b, a", qp1.list());
		
		assertTrue(qp1.mesg("c", "abc").startsWith("-BAD"));
		assertEquals("Streams not initialized.",qp1.mesg("a", "abc"));
		
		c1.logout();
		assertTrue(qp1.quit().startsWith("-BAD"));
		c1.login("b");
		assertTrue(qp1.quit().startsWith("+OK"));
		
		c1.logout();
		c2.logout();
		
	}


}

