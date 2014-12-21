package serverside;
import java.util.Set;


public interface QueryProcessorInterface {
	public boolean isLoggedIn();
	public boolean login(String username);
	public Set<String> getUsersList();
	boolean checkUsername(String username);
	String sendMsg(String username, String message);
	String sendMsgAll(String message);
	boolean removeUser();
}
