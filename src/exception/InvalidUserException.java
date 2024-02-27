package exception;

import PhotoCloudApp.BaseLogger;
/**
 * a InvalidUserException the create a new type of exception
 * @author ahmet
 *
 */
public class InvalidUserException extends Exception{
	/**
	 * a constructor for the exception
	 * @param message error message
	 */
	public InvalidUserException(String message) {
		super("User is invalid because " + message);
		BaseLogger.error("User is invalid because " + message);
	}
}
