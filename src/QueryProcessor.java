import java.util.ArrayList;
import java.util.Set;



public class QueryProcessor implements QueryProcessorInterface {

	Conversation conversation;
	QueryProcessor(Conversation conversation) {
		this.conversation = conversation;
	
	}
	
	@Override
	public Set<String> getUsersList() {
		// TODO Auto-generated method stub
		//return a list of string, listing username of everyone
		
		return conversation.getUsers();
		//return null;
	}

	@Override
	public boolean checkUsername(String username) {
		// TODO Auto-generated method stub
		//also check if username is existed
		
		Set<String> userlist = getUsersList();
		if (!userlist.contains(username) && username.matches("/^[a-zA-Z0-9_-]{3,16}$/")) return true;
		else return false;
		//return username.matches("/^[a-zA-Z0-9_-]{3,16}$/");
	}

	@Override
	public String sendMsg(String username, String message) {
		// TODO Auto-generated method stub
		//check if username exist getUsersList().contains(username)
		//check syntax of the message and then
		//going through list, find user
		//send the message to username
		//
		
		if (getUsersList().contains(username)) {
		//check syntax
		return conversation.sendMessage(username, message);
		} else return "-BAD wrong username syntax";
		//
		
		//return "";
	}

	@Override
	public String sendMsgAll(String message) {
		// TODO Auto-generated method stub
		//send message to everyone on the list
		return conversation.sendBroadcast(message);
		//return "";
	}

	@Override
	public boolean removeUser() {
		//remove current user from the list of the thread
		//then terminate the connection
		// TODO Auto-generated method stub
		return conversation.logout();

	}

	@Override
	public boolean isLoggedIn() {
		// TODO Auto-generated method stub
		return conversation.isLoggedIn();
		//return false;
	}

	@Override
	public boolean login(String username) {
		// TODO Auto-generated method stub
		return conversation.login(username);
		//return false;
	}

}
