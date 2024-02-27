/************** Pledge of Honor ******************************************
I hereby certify that I have completed this programming project on my own
without any help from anyone else. The effort in the project thus belongs
completely to me. I did not search for a solution, or I did not consult any
program written by others or did not copy any program from other sources. I
read and followed the guidelines provided in the project description.
READ AND SIGN BY WRITING YOUR NAME SURNAME AND STUDENT ID
SIGNATURE: <Ahmet Yasin Colpan, 83271>
*************************************************************************/
package PhotoCloudApp;

import java.awt.desktop.UserSessionEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.imageio.ImageIO;

import Sharing.Post;
import Sharing.PostPage;
import Sharing.SharingPage;
import Users.FreeUser;
import Users.HobbyistUser;
import Users.ProfessionalUser;
import Users.User;
import Users.UserTypes;
import editing.ImageMatrix;
import editing.PhotoEditingPage;
import exception.IncorrectInputException;
import exception.InvalidUserException;

/**
 * Main class is the class that starts our PhotoCloud application
 * @author ahmet
 *
 */
public class Main {
	//startTime field to calculate runtime
	public static long startTime = System.currentTimeMillis();
	
	/*
	 * to run the application
	 */
	public static void main(String[] args) throws URISyntaxException, IOException {
		preRun();
		BaseLogger.info("PhotoCloud has launched");
		new LoginPage();
	}
	/**
	 * preRun method to load all necessary information to our application
	 */
	public static void preRun() {
		//The part where it loads all of the users from users.txt
		File userFile;
		File imagesFile;
		File postsFile;
		try {
			userFile = new File(User.class.getResource("/data/users.txt").toURI());
			Scanner userScanner = new Scanner(userFile);
			while (userScanner.hasNextLine()) {
				String line = userScanner.nextLine();
				if (line == "") {
					break;
				}
				String[] tokens = line.split(",");
				String nickname = tokens[0];
				String password = tokens[1];
				String name = tokens[2];
				String surname = tokens[3];
				String age = tokens[4];
				String email = tokens[5];
				String profilePhotoPath = tokens[6];
				String type = tokens[7];
				UserTypes userType = UserTypes.valueOf(type);
				boolean isAdmin = Boolean.parseBoolean(tokens[8]);
				int count = Integer.parseInt(tokens[9]);
				if (userType == UserTypes.Free) {
					new FreeUser(nickname, password, name, surname, age, email, profilePhotoPath, isAdmin, count);
				} else if (userType == UserTypes.Hobbyist) {
					new HobbyistUser(nickname, password, name, surname, age, email, profilePhotoPath, isAdmin, count);
				} else if (userType == UserTypes.Professional) {
					new ProfessionalUser(nickname, password, name, surname, age, email, profilePhotoPath, isAdmin, count);
				}
			}
			userScanner.close();
			BaseLogger.info("Users are loaded");
			//The part where it uploads user's images to their list from images.txt 
			imagesFile = new File(User.class.getResource("/data/images.txt").toURI());
			Scanner imageScanner = new Scanner(imagesFile);
			while (imageScanner.hasNextLine()) {
				String line1 = imageScanner.nextLine();
				if (line1 == "") {
					break;
				}
				String[] tokens1 = line1.split(",");
				String nickname = tokens1[0];
				String filePath = tokens1[1];
				for (User i: User.users) {
					if (i.getNickname().matches(nickname)) {
						i.userImages.add(filePath);
					}
				}
			}
			imageScanner.close();
			BaseLogger.info("Images are loaded");
			//the part where it uploads posts from posts.txt
			postsFile = new File(User.class.getResource("/data/posts.txt").toURI());
			Scanner postsScanner = new Scanner(postsFile);
			while (postsScanner.hasNextLine()) {
				String line2 = postsScanner.nextLine();
				if (line2 == "") {
					break;
				}
				String[] tokens2 = line2.split("#");
				String nickname = tokens2[0];
				String filePath = tokens2[1];
				String description = tokens2[2];
				String[] comments = tokens2[3].split(",");
				Map<String, String> commentsMap = new LinkedHashMap<>();
				for (int i = 0; i<comments.length; i++) {
					String[] comment = comments[i].split(":");
					if (comment.length < 2) {
						break;
					}
					String key = comment[0];
					String value = comment[1];
					commentsMap.put(key, value);
				}
				String[] likes = tokens2[4].split(",");
				List<String> likesList = new ArrayList<>();
				for (String i: likes) {
					if (i.matches(" ")) {
						continue;
					}
					likesList.add(i);
				}
				String[] dislikes = tokens2[5].split(",");
				List<String> dislikesList = new ArrayList<>();
				for (String i: dislikes) {
					if (i.matches(" ")) {
						continue;
					}
					dislikesList.add(i);
				}
				Post post = new Post(User.getUser(nickname), filePath, description);
				post.setComments(commentsMap);
				post.setLikes(likesList);
				post.setDislikes(dislikesList);
			}
			postsScanner.close();
			BaseLogger.info("Posts are loaded");
		} catch (URISyntaxException |IncorrectInputException |FileNotFoundException | InvalidUserException e) {
			BaseLogger.error(e.getMessage());
		}
	}
}
