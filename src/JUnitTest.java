import static org.junit.Assert.*;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class JUnitTest {

	Client c;
	 @Before
   public void setUp() throws Exception {
	Runnable r1 = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				Server.main(null);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	};
	new Thread(r1).start();
	 c = new Client(true);
   }
 
   @After
   public void tearDown() throws Exception {
      System.out.println("Run @After"); // for illustration
      c.quit();
   }
	   
	@Test
	public void normalTest() {
		
			c.startConnection();
			waitForNextOutput(c);
			
			c.sendMessage("stat");
			assertTrue(waitForNextOutput(c).startsWith("+OK there are 0"));
			
			c.sendMessage("iden tung");
			assertTrue(waitForNextOutput(c).startsWith("+OK"));
			
			c.sendMessage("stat");
			assertTrue(waitForNextOutput(c).startsWith("+OK there are 1"));
			
			c.sendMessage("list");
			assertTrue(waitForNextOutput(c).startsWith("+OK"));
			
			c.sendMessage("mesg tung asda");
			assertTrue(waitForNextOutput(c).startsWith("+MESG"));
			assertTrue(waitForNextOutput(c).startsWith("Message sent"));
			
			c.sendMessage("quit");
			assertTrue(waitForNextOutput(c).startsWith("+OK"));
			
			
			//wait(1000);
		
		
	}
	
	private String waitForNextOutput(Client c){
		String output;
		while (true) {
			output =c.getOutput(); 
			if (output!=null) {
				
				//assertTrue(output.startsWith("+OK"));
				break;
			}
		}
		return output;
		
	}

}
