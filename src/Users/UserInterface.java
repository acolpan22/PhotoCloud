package Users;
/**
 * a userInterface to help user classes
 * @author ahmet
 *
 */
public interface UserInterface {
	/**
	 * a login method to log login events to application_log.txt
	 */
	void login();
	/**
	 * a logout method to log logout events to application_log.txt
	 */
	void logout();
	/**
	 * a created method to log when a new user is created to application_log.txt
	 */
	void created();
}
