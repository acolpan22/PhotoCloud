package PhotoCloudApp;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import Users.User;

/**
 * A BaseLogger class to log messages to txt files
 * @author ahmet
 *
 */
public class BaseLogger {
	
    static FileHandler fh_info;
    static FileHandler fh_error;
    
    /**
     * A BaseLogger info method in order to log information to txt file we have in the resources
     * @param message
     */
    public static void info(String message) {
		Logger info_logger = Logger.getLogger("INFO");
	   
		try {
			fh_info = new FileHandler(User.class.getResource("/logs/application_log.txt").getPath());
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}  
		info_logger.addHandler(fh_info);
		SimpleFormatter formatter = new SimpleFormatter();  
		fh_info.setFormatter(formatter);    
		info_logger.info(message);
	}
	
    /**
     * A BaseLogger info method in order to log errors to txt file we have in the resources
     * @param message
     */
	public static void error(String message) {
		Logger error_logger = Logger.getLogger("ERROR");
		   
		try {
			fh_error = new FileHandler(User.class.getResource("/logs/application_error.txt").getPath());
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}  
		error_logger.addHandler(fh_error);
		SimpleFormatter formatter = new SimpleFormatter();  
		fh_error.setFormatter(formatter);    
		error_logger.severe(message);
	}
}
