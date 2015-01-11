package serverside;

import java.util.Set;



public class QueryProcessor {

	Conversation conversation;
	public QueryProcessor(Conversation conversation) {
		this.conversation = conversation;
	
	}
	
	//get list of user
	public String stat() {
		Set<String> userList=getUsersList();
		String ans = "+OK there are "+userList.size()+" users online";
		if (isLoggedIn())
			ans+=", "+ conversation.getNumberOfMessages() +" messages sent";
		ans+=".";
		return ans;
	}
	
	//iden command
	public String iden(String username) {
		if (isLoggedIn()) {
			return "-BAD you are alerady logged in";
		
		}
		
		if (checkUsername(username)){
			if (login(username))
				return "+OK welcome "+username;
			else return "-BAD cannot login";
		} else {
			return "-BAD this user already login, choose another name";
		}
		
	}
	
	//list command
	public String list() {
		
		if (!isLoggedIn()) {
			return "-BAD not logged in yet";
		} else {
			Set<String> userlist = getUsersList();
			String ans= "+OK there are "+userlist.size()+" users logged in: ";
			boolean startList = true;
			for (String username : userlist) {
				if (startList) {
					ans+=username;
					startList = false;
				}
				else ans+=", "+username;
			}
			return ans;
		}
	}
	
	//send message command
	public String mesg(String username, String msg) {
		if (!isLoggedIn()) {
			return "-BAD not logged in yet";
		} else {
			return sendMsg(username, msg);
			
		}
	}
	
	//send message to all user command
	public String hail(String msg) {
		if (!isLoggedIn()) {
			return "-BAD not logged in yet";
		} else {
			return sendMsgAll(msg);
			
		}
	}

	//quit command
	public String quit() {
		if (!isLoggedIn()) {
			return "-BAD not logged in yet";
		} else {
			conversation.setLogout(true);
			return "+OK user quit";
		}
		
	}
	
	//get user who currently log in
	public Set<String> getUsersList() {
		// TODO Auto-generated method stub
		//return a list of string, listing username of everyone
		
		return conversation.getUsers();
		//return null;
	}

	//check if username already used
	public boolean checkUsername(String username) {
		// TODO Auto-generated method stub
		
		Set<String> userlist = getUsersList();
		
		if (!userlist.contains(username)) return true;
		else return false;
	}

	//send message to a user
	public String sendMsg(String username, String message) {
		// TODO Auto-generated method stub
		
		if (getUsersList().contains(username)) {
			return conversation.sendMessage(username, "+MESG: "+message);
		} else return "-BAD user not existed";
	}
	
	//send message to everyone on the list
	public String sendMsgAll(String message) {
		// TODO Auto-generated method stub
		return conversation.sendBroadcast("+MESG: "+message);
	}

	//check if logged in
	public boolean isLoggedIn() {
		// TODO Auto-generated method stub
		return conversation.isLoggedIn();
	}

	//log user in
	public boolean login(String username) {
		// TODO Auto-generated method stub
		return conversation.login(username);
	}

}
