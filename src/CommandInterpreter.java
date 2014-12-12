

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class CommandInterpreter {
	QueryProcessorInterface qp;
	int msgSent;
	
	public CommandInterpreter (QueryProcessor queryProcessor ) {
		qp= queryProcessor;
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
		Set<String> userList=qp.getUsersList();
		String ans = "+OK there are "+userList.size()+" users online";
		if (qp.isLoggedIn())
			ans+=", "+msgSent+" messages sent";
		ans+=".";
		return ans;
	}
	
	private String iden(String username) {
		if (qp.isLoggedIn()) {
			return "-BAD user alerady logged in";
		
		}
		if (qp.checkUsername(username)){
			if (qp.login(username))
				return "+OK welcome "+username;
			else return "-BAD cannot login";
		} else {
			return "-BAD unavailable username";
		}
	}
	private String list() {
		
		if (!qp.isLoggedIn()) {
			return "-BAD not logged in yet";
		} else {
			Set<String> userlist = qp.getUsersList();
			String ans= "+OK there are "+userlist.size()+" users logged in: ";
			boolean startList = true;
			for (String username : userlist) {
				if (startList) {
					ans+=username;
					startList = false;
				}
				ans+=", "+username;
			}
			return ans;
		}
	}
	private String mesg(String username, String msg) {
		if (!qp.isLoggedIn()) {
			return "-BAD not logged in yet";
		} else {
			return qp.sendMsg(username, msg);
			
		}
	}
	
	private String hail(String msg) {
		if (!qp.isLoggedIn()) {
			return "-BAD not logged in yet";
		} else {
			return qp.sendMsgAll(msg);
			
		}
	}

	private String quit() {
		if (!qp.isLoggedIn()) {
			return "-BAD not logged in yet";
		} else {
			if (qp.removeUser())
				return "+OK user quit";
			else return "-BAD log out error";
		}
		
	}

}
