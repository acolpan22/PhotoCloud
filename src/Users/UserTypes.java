package Users;
/**
 * a UserTypes enum to represent user types
 * @author ahmet
 *
 */
public enum UserTypes {
	//the types
	Free, Hobbyist, Professional;
	/**
	 * overriding toString method to retrieve user type data from user.txt when the application is launched
	 */
	@Override
	public String toString() {
	    switch (this) {
	        case Free:
	            return "Free";
	        case Hobbyist:
	            return "Hobbyist";
	        case Professional:
	            return "Professional";
	        default:
	            return null;
	    }
	}
}
