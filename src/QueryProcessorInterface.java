
public interface QueryProcessorInterface {
	public String[] getUsersList();
	boolean checkUsername(String username);
	boolean sendMsg(String username, String message);
	boolean sendMsgAll(String message);
	void removeUser();
}
