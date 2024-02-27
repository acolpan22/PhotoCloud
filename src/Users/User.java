package Users;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import PhotoCloudApp.BaseLogger;
import Sharing.Post;
import exception.IncorrectInputException;
import exception.InvalidUserException;

/**
 * User is used to represent users and its elements
 */
public abstract class User implements UserInterface{
	//User fields
	private final String nickname;
	private String password;
	private String name;
	private String surname;
	private String age;
	private String email;
	private String profilePhotoPath;
	protected UserTypes userType;
	public final boolean isAdmin;
	public List<String> userImages = new ArrayList<String>();
	public int count = 0;
	//static user fields to store users and shared images
	public static List<User> users = new ArrayList<User>();
	public static List<String> sharedImages = new ArrayList<String>();
	
	/**
	 * A constructor for user with all of its arguments
	 * @paramString nickname
	 * @paramString password
	 * @paramString name
	 * @paramString surname
	 * @param age
	 * @paramString email
	 * @paramString profilePhotoPath
	 * @param type
	 * @param isAdmin
	 * @throws InvalidUserException if the email or nickname is being used by another user
	 * @throws IncorrectInputException if the parameters are in wrong format
	 */
	public User(String nickname, String password, String name, String surname, String age, String email, String profilePhotoPath, boolean isAdmin, int count) throws InvalidUserException, IncorrectInputException {
		validateNickname(nickname);
		this.nickname = nickname;
		validatePassword(password);
		this.password = password;
		validateName(name);
		this.name = name;
		validateSurname(surname);
		this.surname = surname;
		validateAge(age);
		this.age = age;
		this.isAdmin = isAdmin;
		validateEmail(email);
		this.email = email;
		this.profilePhotoPath = profilePhotoPath;
		this.count = count;
		users.add(this);
	}
	/**
	 * a confirmUser method to verify given nicknames and passwords to help login process
	 * @paramString nickname
	 * @paramString password
	 * @return if the nickname and password matches true, if not false
	 */
	public static boolean verifyUser(String nickname, String password) {
		for (User i: users) {
			if ((nickname.equals(i.getNickname())) && (password.equals(i.getPassword()))) {
				return true;
			}
		}
		BaseLogger.error("User " + nickname + " has attemted to log in.");
		return false;
	}
	
	/**
	 * getter method for the nickname field
	 * @return nickname
	 */
	public String getNickname() {
		return nickname;
	}
	/**
	 * getter method for the password field
	 * @return password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * a getter method to retrieve the User object with the given nickname
	 * @paramString nickname
	 * @return User
	 */
	public static User getUser(String nickname) {
		for (User i: users) {
			if (nickname.equals(i.getNickname())) {
				return i;
			}
		}
		return null;
	}
	/**
	 * getter method for the email field
	 * @return email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * verifyAdmin is a method to understand if an admin has assigned already or not
	 * @return if there is an admin true, if not false
	 */
	public static boolean verifyAdmin() {
		for (User i: users) {
			if (i.isAdmin) {
				return true;
			}
		}
		return false;
	}
	/**
	 * a method to check if a string is made of digits
	 * @param strNum
	 * @return
	 */
	public static boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        double d = Double.parseDouble(strNum);
	    } catch (NumberFormatException e) {
	        return false;
	    }
	    return true;
	}
	/**
	 * a method to check if password is in the right format
	 * @param password to check
	 * @throws IncorrectInputException if its not in the right format
	 */
	public static boolean validatePassword(String password) throws IncorrectInputException {
		//a regular expression to match our password's condition
		String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,20}$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(password);
		if (password.isEmpty()) {
			throw new IncorrectInputException("password cannot be empty");
		}
		if (!m.matches()) {
			String message = "password is not in the right format\n It should:\n";
			message += "-contains at least 8 characters and at most 20 characters\n-contains at least one digit\n-contains at least one upper case letter\n-contains at least one lower case letter\n-contains at least one special character\n-doesn’t contain any white space";
			throw new IncorrectInputException(message);
		} 
		return true;
	}
	/**
	 * a method to check if nickname is in the right conditions
	 * @param nickname to check
	 * @throws InvalidUserException if it is being used by another user
	 * @throws IncorrectInputException if its not in the right format
	 */
	public static boolean validateNickname(String nickname) throws InvalidUserException, IncorrectInputException {
		if (nickname.isEmpty()) {
			throw new IncorrectInputException("nickname cannot be empty");
		}
		for (int i = 0; i < users.size(); i++){
			if (users.get(i).getNickname() == nickname) {
				throw new InvalidUserException("this nickname is being used by another user");
			}
		}
		return true;
	}
	/**
	 * a method to check if name is in the right format
	 * @param name to check
	 * @throws IncorrectInputException if its not in the right format
	 */
	public static boolean validateName(String name) throws IncorrectInputException {
		if (name.isEmpty()) {
			throw new IncorrectInputException("name cannot be empty");
		}
		//checks the name with a special regular expression
		if (!name.matches("^[A-Za-ziçşöğüıÇÖİŞÜĞI]+(\\s[A-Za-ziçşöğüıÇÖİŞÜĞI]+)?$")) {
			throw new IncorrectInputException("name is in wrong format");
		}
		return true;
	}
	/**
	 * a method to check if surname is in the right format
	 * @param surname to check
	 * @throws IncorrectInputException if its not in the right format
	 */
	public static boolean validateSurname(String surname) throws IncorrectInputException {
		if (surname.isEmpty()) {
			throw new IncorrectInputException("surname cannot be empty");
		}
		//checks the surname with a special regular expression
		if (!surname.matches("^[A-Za-ziçşöğüıÇÖİŞÜĞI]*$")) {
			throw new IncorrectInputException("surname is in wrong format");
		}
		return true;
	}
	/**
	 * a method to check if age is in the right format
	 * @param age to check
	 * @throws IncorrectInputException if its not in the right format
	 */
	public static boolean validateAge(String age) throws IncorrectInputException {
		if (age.isEmpty()) {
			throw new IncorrectInputException("age cannot be empty");
		}
		if (!isNumeric(age)) {
			throw new IncorrectInputException("age cannot contain any letters");
		}
		if (Integer.parseInt(age) <= 0) {
			throw new IncorrectInputException("age cannot be equal or lower than zero");
		}
		return true;
	}
	/**
	 * a method to check if email is in the right format
	 * @param email
	 * @throws InvalidUserException if it is being used by another user
	 * @throws IncorrectInputException if its not in the right format
	 */
	public static boolean validateEmail(String email) throws InvalidUserException, IncorrectInputException {
		if (email.isEmpty()) {
			throw new IncorrectInputException("email cannot be empty");
		}
		// a special regular expression to check email format
		String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"; 
		Pattern pattern = Pattern.compile(regex); 
		Matcher matcher = pattern.matcher(email);
		if (!matcher.matches()) {
			throw new IncorrectInputException("email is in wrong format");
		}
		for (int i = 0; i < users.size(); i++){
			if (users.get(i).getEmail() == email) {
				throw new InvalidUserException("this email is being used by another user");
			}
		}
		return true;
	}
	/**
	 * a getter method for profilePhotoPath field
	 * @return profilePhotoPath
	 */
	public String getProfilePhotoPath() {
		return profilePhotoPath;
	}
	/**
	 * a removeUser method to remove user from users.txt and users list
	 * it deletes user and all of its images and posts from the app
	 * @param nickname to delete
	 */
	public static void removeUser(String nickname) {
		//removes its images
		User user = getUser(nickname);
		for (String i: user.userImages) {
			removePhoto(i);
		}
		//removes it from users list
		for (User i: users) {
			if (nickname.matches(i.getNickname())) {
				users.remove(i);
			}
		}
		//removes it from users.txt file
		try {
			File file = new File(User.class.getResource("/data/users.txt").toURI());
			BufferedReader reader = new BufferedReader(new FileReader(file));
	        FileWriter filewriter = new FileWriter(file, true);
			BufferedWriter writer = new BufferedWriter(filewriter);
			List<String> lines = new ArrayList<String>();
			String line;
			while ((line = reader.readLine()) != null) {
	        	String[] tokens = line.split(",");
	        	String newLine = "";
	        	if (tokens[0].matches(nickname)) {
	            	continue;
	            } else {
	            	newLine = line;
	            }
	            lines.add(newLine);
	        }
	        FileWriter Xfilewriter = new FileWriter(file);
			BufferedWriter Xwriter = new BufferedWriter(Xfilewriter);
			Xwriter.write("");
			Xwriter.close();
	        for (int i = 0; i < lines.size(); i++) {
	        	writer.write(lines.get(i));
	        	writer.newLine();
	        }
	        reader.close();
	        writer.close(); 
		}catch (IOException | URISyntaxException e1) {
			BaseLogger.error(e1.getMessage());
		}
		BaseLogger.info("User " + nickname + " has been successfully removed");
	}
	/**
	 * a getter method for user type
	 * @return userType
	 */
	public UserTypes getUserType() {
		return userType;
	}
	/**
	 * a getter method for name field
	 * @return name
	 */
	public String getName() {
		return name;
	}
	/**
	 * a getter method for surname field
	 * @return surname
	 */
	public String getSurname() {
		return surname;
	}
	/**
	 * a getter method for age field
	 * @return age
	 */
	public String getAge() {
		return age;
	}
	/**
	 * a word replacer method to change fields from user fields in user.txt file
	 * application uses this method when the user changes one of their personal information
	 * or to increase the count of the user
	 * @param nickname 
	 * @param wordToInsert
	 * @param filePath
	 * @param field
	 */
	public static void wordReplacer(String nickname, String wordToInsert, URI filePath, String field) {
		try {
			File file = new File(filePath);
			BufferedReader reader = new BufferedReader(new FileReader(file));
            FileWriter filewriter = new FileWriter(file, true);
			BufferedWriter writer = new BufferedWriter(filewriter);
			List<String> lines = new ArrayList<String>();
            int index = 0;
            //to determine which field is going to change
            switch (field) {
            	case "password":
            		index = 1;
            		break;
            	case "name":
            		index = 2;
            		break;
            	case "surname":
            		index = 3;
            		break;
            	case "age":
            		index = 4;
            		break;
            	case "email":
            		index = 5;
            		break;
            	case "profilePhoto":
            		index = 6;
            		break;
            	case "count":
            		index = 9;
            		break;
            }
            //replacement
            String line;
            while ((line = reader.readLine()) != null) {
            	String[] tokens = line.split(",");
            	String newLine = "";
            	if (tokens[0].matches(nickname)) {
                	for (int i = 0; i < tokens.length; i++) {
                		if (i == index) {
                			newLine += wordToInsert;
                		}else {
                			newLine += tokens[i];
                		}
                		if (i != tokens.length-1) {
                			newLine += ",";
                		}
                	}
                } else {
                	newLine = line;
                }
                lines.add(newLine);
            }
            FileWriter Xfilewriter = new FileWriter(file);
			BufferedWriter Xwriter = new BufferedWriter(Xfilewriter);
			Xwriter.write("");
			Xwriter.close();
            for (int i = 0; i < lines.size(); i++) {
            	writer.write(lines.get(i));
            	writer.newLine();
            }
            reader.close();
            writer.close();
		} catch (IOException e) {
			BaseLogger.error(e.getMessage());
		}
	}
	/**
	 * a setter method to set a new password
	 * @param password new password
	 */
	public void setPassword(String password) {
		try {
			wordReplacer(this.nickname, password, User.class.getResource("/data/users.txt").toURI(), "password");
		} catch (URISyntaxException e) {
			BaseLogger.error(e.getMessage());
		}
		this.password = password;
	}
	/**
	 * a setter method to set a new name
	 * @param name new name
	 */
	public void setName(String name) {
		try {
			wordReplacer(this.nickname, name, User.class.getResource("/data/users.txt").toURI(), "name");
		} catch (URISyntaxException e) {
			BaseLogger.error(e.getMessage());
		}
		this.name = name;
	}
	/**
	 * a setter method to set a new surname
	 * @param surname new surname
	 */
	public void setSurname(String surname) {
		try {
			wordReplacer(this.nickname, surname, User.class.getResource("/data/users.txt").toURI(), "surname");
		} catch (URISyntaxException e) {
			BaseLogger.error(e.getMessage());
		}
		this.surname = surname;
	}
	/**
	 * a setter method to set a new age
	 * @param age new age
	 */
	public void setAge(String age) {
		try {
			wordReplacer(this.nickname, age, User.class.getResource("/data/users.txt").toURI(), "age");
		} catch (URISyntaxException e) {
			BaseLogger.error(e.getMessage());
		}
		
		this.age = age;
	}
	/**
	 * a setter method to set a new email
	 * @param email new email
	 */
	public void setEmail(String email) {
		try {
			wordReplacer(this.nickname, email, User.class.getResource("/data/users.txt").toURI(), "email");
		} catch (URISyntaxException e) {
			BaseLogger.error(e.getMessage());
		}
		this.email = email;
	}
	/**
	 * a setter method to set a new profilePhotoPath
	 * @param profilePhotoPath new profilePhotoPath
	 */
	public void setProfilePhotoPath(String profilePhotoPath) {
		try {
			wordReplacer(this.nickname, profilePhotoPath, User.class.getResource("/data/users.txt").toURI(), "profilePhoto");
		} catch (URISyntaxException e) {
			BaseLogger.error(e.getMessage());
		}
		this.profilePhotoPath = profilePhotoPath;
	}
	/**
	 * update the number to create a file path to user's new photo
	 */
	public void updateCount() {
		try {
			wordReplacer(this.nickname, String.valueOf(this.count + 1), User.class.getResource("/data/users.txt").toURI(), "count");
		} catch (URISyntaxException e) {
			BaseLogger.error(e.getMessage());
		}
		this.count += 1;
	}
	/**
	 * a getUserByPhoto method to retrieve image's owner user
	 * @param filePath the image's file path to find its owner
	 * @return the owner user
	 */
	public static User getUserByPhoto(String filePath){
		for (User i: users) {
			for (String a: i.userImages) {
				if (filePath.matches(a)) {
					return i;
				}
			}
		}
		return null;
	}
	/**
	 * a removePhoto method to remove photos from images.txt, userImages list, sharedImages list, and if it is shared the posts of it
	 * @param photoPath the path of the image to remove
	 */
	public static void removePhoto(String photoPath) {
		try {
			//retrieve its owner and remove it from its list
			User user = getUserByPhoto(photoPath);
			user.userImages.remove(photoPath);
			//remove it from images.txt
			File file = new File(User.class.getResource("/data/images.txt").toURI());
			BufferedReader reader = new BufferedReader(new FileReader(file));
	        FileWriter filewriter = new FileWriter(file, true);
			BufferedWriter writer = new BufferedWriter(filewriter);
			List<String> lines = new ArrayList<String>();
			String line;
			while ((line = reader.readLine()) != null) {
            	String[] tokens = line.split(",");
            	String newLine = "";
            	if (tokens[1].matches(photoPath)) {
                	continue;
                } else {
                	newLine = line;
                }
                lines.add(newLine);
            }
            FileWriter Xfilewriter = new FileWriter(file);
			BufferedWriter Xwriter = new BufferedWriter(Xfilewriter);
			Xwriter.write("");
			Xwriter.close();
            for (int i = 0; i < lines.size(); i++) {
            	writer.write(lines.get(i));
            	writer.newLine();
            }
            reader.close();
            writer.close();
            //if it is shared remove it's post
            if (Post.posts.contains(Post.getPost(photoPath))) {
            	sharedImages.remove(photoPath);
            	Post.removePost(Post.getPost(photoPath));
            }
            //and finally remove it from the images directory
            Path path = Path.of(photoPath);
            Files.delete(path);
		} catch (IOException | URISyntaxException e1) {
			BaseLogger.error(e1.getMessage());
		}
	BaseLogger.info("Photo " + photoPath + " has successfully been removed");
	}
	/**
	 * a getExtension method to retrieve image's extension
	 * @param filePath the image's path to get it's extension
	 * @return extension
	 */
	public static String getExtension(String filePath) {
		for (int i = 0; i < filePath.toCharArray().length; i++) {
			if (filePath.toCharArray()[i] == '.') {
				return filePath.substring(i, filePath.toCharArray().length);
			}
		}
		return null;
	}
	/**
	 * overriding toString method to show user's nicknames in the combo boxes placed in discover page
	 */
	@Override
	public String toString() {
		return this.getNickname();
	}
}
