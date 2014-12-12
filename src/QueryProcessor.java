

public class QueryProcessor implements QueryProcessorInterface {

	
	
	@Override
	public String[] getUsersList() {
		// TODO Auto-generated method stub
		//return a list of string, listing username of everyone
		return null;
	}

	@Override
	public boolean checkUsername(String username) {
		// TODO Auto-generated method stub
		//also check if username is existed
		return username.matches("/^[a-zA-Z0-9_-]{3,16}$/");
	}

	@Override
	public boolean sendMsg(String username, String message) {
		// TODO Auto-generated method stub
		//check syntax of the message and then
		//send the message to username
		return false;
	}

	@Override
	public boolean sendMsgAll(String message) {
		// TODO Auto-generated method stub
		//send message to everyone on the list
		return false;
	}

	@Override
	public void removeUser() {
		//remove current user from the list of the thread
		//then terminate the connection
		// TODO Auto-generated method stub

	}

}
