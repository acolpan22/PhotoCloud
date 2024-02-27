package exception;

import PhotoCloudApp.BaseLogger;
/**
 * a IncorrectInputExceptin class to create a new type of exception
 * @author ahmet
 *
 */
public class IncorrectInputException extends Exception{
	/**
	 * a constructo for the exception
	 * @param message error message
	 */
	public IncorrectInputException(String message){
		super("Incorrect input because " + message);
		BaseLogger.error("Incorrect input because " + message);
	}
}
