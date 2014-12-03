
public interface QueryProcessorInterface {
	public String[] getUsersList();
	boolean checkUsername(String username);
	boolean sendMsg(String username, String message);
	boolean checkSyntax(String text);
	boolean sendMsgAll(String message);
	void removeUser();
	void setUser();
}
