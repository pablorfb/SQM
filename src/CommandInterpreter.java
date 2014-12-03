

import java.io.IOException;
import java.util.Arrays;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class CommandInterpreter {
	String state;
	String username;
	QueryProcessorInterface qp;
	final static String AUTHORIZATION = "AUTHORIZATION";
	final static String LOGGEDIN = "LOGGED IN";
	Server server;
	int msgSent;
	
	public CommandInterpreter (Server server) {
		this.server=server;
		//qp= new DatabaseInterface_Implement();
		state = "AUTHORIZATION";
		msgSent=0;
	}
	String response;
	
	public String handleInput(String input) throws ParserConfigurationException, SAXException, IOException {
		//response = new String();
		response = "";
		String[] segment = input.split(" ", 2);
		segment[0]=segment[0].toUpperCase();
		String[] parameters = null;
		
		if (segment.length>1) parameters= segment[1].split(" ");
		int flag = 0;
		if (input.length()>=segment[0].length()+2){
			if (input.charAt(segment[0].length()+1)==' '){
				response= "-BAD invalid syntax";
				flag=1;
			}
		}
		if (flag!=1) {
			
				if (segment[0].equals("QUIT")) {
					if (segment.length==1) response = quit();
					else{
						response = "-BAD incorrect number of parameters";
					}
				} else if (segment[0].equals("STAT")) {
					if (segment.length==1) response = stat();
					else {
						response = "-BAD incorrect number of parameters";
					}
				} else if (segment[0].equals("LIST")) {
					if (segment.length==1) response = list();
					else {
						response = "-BAD incorrect number of parameters";
					}
				
				} if (segment[0].equals("IDEN")) {
					if (segment.length==2) response = iden(parameters[0]);
					else{
						response = "-BAD incorrect number of parameters";
					}
				} if (segment[0].equals("MESG")) {
					if (segment.length==2) response = mesg(parameters[0], segment[1].split(" ",2)[1]);
					else{
						response = "-BAD incorrect number of parameters";
					}
				} if (segment[0].equals("HAIL")) {
					if (segment.length==2) response = hail(segment[1]);
					else{
						response = "-BAD incorrect number of parameters";
					}
				} else {
					response = "-BAD invalid function";
				}
		}
		
		
		return response+"\r\n";
		//return input;
		
	}

	private String stat() {
		String userList[]=qp.getUsersList();
		String ans = "+OK there are "+userList.length+" users online, your status is "+state;
		if (state.equals(LOGGEDIN))
			ans+=", "+msgSent+" messages sent";
		ans+=".";
		return ans;
	}
	
	private String iden(String username) {
		if (qp.checkUsername(username)){
			this.username = username;
			state = LOGGEDIN;
			return "+OK welcome "+username;
		} else {
			return "-BAD unavailable username";
		}
	}
	private String list() {
		
		if (state.equals(AUTHORIZATION)) {
			return "-BAD not logged in yet";
		} else {
			String[] userlist = qp.getUsersList();
			String ans= "+OK there are "+userlist.length+" users logged in: ";
			for (int i=0; i<userlist.length; i++) {
				if (i==userlist.length-1) ans+=userlist[i]+".";
				ans+=userlist[i]+", ";
			}
			return ans;
		}
	}
	private String mesg(String username, String msg) {
		if (state.equals(AUTHORIZATION)) {
			return "-BAD not logged in yet";
		} else {
			if (qp.checkUsername(username)) 
				return "-BAD user is not available";
			else if (qp.sendMsg(username, msg)) {
				return "+OK message sent to "+username;
			} else {
				return "-BAD message not sent";
 			}
		}
	}
	
	private String hail(String msg) {
		if (state.equals(AUTHORIZATION)) {
			return "-BAD not logged in yet";
		} else {
			if (qp.sendMsgAll(msg)) {
				return "+OK message sent";
			} else {
				return "-BAD message not sent";
 			}
		}
	}

	private String quit() {
		if (state.equals(AUTHORIZATION)) {
			return "-BAD not logged in yet";
		} else {
			qp.removeUser();
			return "+OK user quit";
		}
		
	}

}
