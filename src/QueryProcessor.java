import java.util.Set;



public class QueryProcessor {

	Conversation conversation;
	QueryProcessor(Conversation conversation) {
		this.conversation = conversation;
	
	}
	
	public String stat() {
		Set<String> userList=getUsersList();
		String ans = "+OK there are "+userList.size()+" users online";
		if (isLoggedIn())
			ans+=", "+"..."+" messages sent";
		ans+=".";
		return ans;
	}
	
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
	public String mesg(String username, String msg) {
		if (!isLoggedIn()) {
			return "-BAD not logged in yet";
		} else {
			return sendMsg(username, msg);
			
		}
	}
	
	public String hail(String msg) {
		if (!isLoggedIn()) {
			return "-BAD not logged in yet";
		} else {
			return sendMsgAll(msg);
			
		}
	}

	public String quit() {
		if (!isLoggedIn()) {
			return "-BAD not logged in yet";
		} else {
			if (removeUser())
				return "+OK user quit";
			else return "-BAD log out error";
		}
		
	}
	
	
	public Set<String> getUsersList() {
		// TODO Auto-generated method stub
		//return a list of string, listing username of everyone
		
		return conversation.getUsers();
		//return null;
	}

	
	public boolean checkUsername(String username) {
		// TODO Auto-generated method stub
		//also check if username is existed
		
		Set<String> userlist = getUsersList();
		
		if (!userlist.contains(username)) return true;
		else return false;
		//return username.matches("/^[a-zA-Z0-9_-]{3,16}$/");
	}

	
	public String sendMsg(String username, String message) {
		// TODO Auto-generated method stub
		//check if username exist getUsersList().contains(username)
		//check syntax of the message and then
		//going through list, find user
		//send the message to username
		//
		
		if (getUsersList().contains(username)) {
		//check syntax
			return conversation.sendMessage(username, "+MESG: "+message);
		} else return "-BAD user not existed";
		//
		
		//return "";
	}
	
	
	public String sendMsgAll(String message) {
		// TODO Auto-generated method stub
		//send message to everyone on the list
		return conversation.sendBroadcast("+MESG: "+message);
		//return "";
	}

	
	public boolean removeUser() {
		//remove current user from the list of the thread
		//then terminate the connection
		// TODO Auto-generated method stub
		if (conversation.isLoggedIn()) {
			conversation.setLogout(true);
			return true;
		} else return false;

	}

	
	public boolean isLoggedIn() {
		// TODO Auto-generated method stub
		return conversation.isLoggedIn();
		//return false;
	}

	
	public boolean login(String username) {
		// TODO Auto-generated method stub
		return conversation.login(username);
		//return false;
	}

}
