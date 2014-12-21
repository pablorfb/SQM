package serverside;


import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class CommandInterpreter {
	QueryProcessor qp;
	int msgSent;
	
	public CommandInterpreter (QueryProcessor queryProcessor ) {
		qp= queryProcessor;
		msgSent=0;
	}
	String response;
	
	public String handleInput(String input) throws ParserConfigurationException, SAXException, IOException {
		//response = new String();
		response = "";
		
		
		if (generalSyntaxChecking(input)) {
			
				if (input.toUpperCase().startsWith("QUIT")) {
					response = qp.quit();
				} else if (input.toUpperCase().startsWith("STAT")) {
					response = qp.stat();
				} else if (input.toUpperCase().startsWith("LIST")) {
					response = qp.list();
				} else if (input.toUpperCase().startsWith("IDEN")) {
					response = qp.iden(input.split(" ")[1]);
				} else if (input.toUpperCase().startsWith("MESG")) {
					String[] segments= input.split(" ",3);
					response = qp.mesg(segments[1], segments[2]);
				} else if (input.toUpperCase().startsWith("HAIL")) {
					response = qp.hail(input.split(" ",2)[1]);
				}
		} else {
			response+="-BAD invalid input";
		}
		
		
		return response+"\r\n";
		//return input;
		
	}
	
	public boolean generalSyntaxChecking(String input){
		String[] segment = input.split(" ", 2);
		segment[0]=segment[0].toUpperCase();
		String[] parameters = null;
		
		if (segment.length>1) parameters= segment[1].split(" ");
		if (input.length()>=segment[0].length()+2){
			if (input.charAt(segment[0].length()+1)!=' ')
				return true;
		}
		
		if (segment[0].equals("QUIT") || segment[0].equals("STAT") || segment[0].equals("LIST")) {
			if (segment.length==1) return true;
		} else if (segment[0].equals("IDEN") || segment[0].equals("HAIL")) {
			if (parameters!= null)
				if (parameters.length == 1) {
					if (segment[0].equals("IDEN")) {
						if (checkUsernameSyntax(parameters[0])) 
							return true;
					} else return true;
				}
		} else if (segment[0].equals("MESG")) {
			if (parameters!= null)
				if (parameters.length == 2)
					return true;
		}
		return false;
	}
	
	public boolean checkUsernameSyntax(String username){
		//username.matches("^[a-zA-Z0-9_-]{3,16}$");
		return username.matches("^[a-zA-Z0-9_-]{3,16}$");
	}

}
