package Sharing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import PhotoCloudApp.BaseLogger;
import Users.User;
/**
 * a Post class to represent posts
 * @author ahmet
 *
 */
public class Post {
	//post fields
	private User owner;
	private String photoPath; 
	private String description;
	//a linked hash map to sort comments by date
	public Map<String, String> comments = new LinkedHashMap<>();
	public List<String> likes = new ArrayList<>();
	public List<String> dislikes = new ArrayList<>();
	//static posts to store all of the posts
	public static List<Post> posts = new ArrayList<>();
	
	/**
	 * a constructor for a new post 
	 * @param owner of the post
	 * @param photoPath path of the image that is shared
	 * @param description an optional description
	 */
	public Post(User owner, String photoPath, String description) {
		this.owner = owner;
		this.photoPath = photoPath;
		this.description = description;
		posts.add(this);
		User.sharedImages.add(photoPath);
	}
	/**
	 * a getPost method to retrieve post from the posts list with the shared image path
	 * @param filePath the post's image's path
	 * @return post object
	 */
	public static Post getPost(String filePath){
		for (Post i: posts) {
			if (i.getPhotoPath().matches(filePath)) {
				return i;
			}
		}
		return null;
	}
	/**
	 * a getter method to retrieve photo path
	 * @return photoPath
	 */
	public String getPhotoPath() {
		return photoPath;
	}
	/**
	 * a getter method to retrieve owner
	 * @return owner
	 */
	public User getOwner() {
		return owner;
	}
	/**
	 * a getter method to retrieve description
	 * @return description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * a setter method to set post's comments from posts.txt
	 * @param comments map to load
	 */
	public void setComments(Map<String, String> comments) {
		this.comments = comments;
	}
	/**
	 * a setter method to set post's likes from posts.txt
	 * @param likes list to load
	 */ 
	public void setLikes(List<String> likes) {
		this.likes = likes;
	}
	/**
	 * a setter method to set post's dislikes from posts.txt
	 * @param dislikes list to load
	 */
	public void setDislikes(List<String> dislikes) {
		this.dislikes = dislikes;
	}
	/**
	 * an addComment method to add a comment to comments map and change it from posts.txt
	 * @param nickname of the user who wrote the comment
	 * @param comment
	 */
	public void addComment(String nickname, String comment) {
		//adding it to map
		this.comments.put(nickname, comment);
		//changing it from posts.txt
		String commentsLine = "";
		int count = 0;
		for (Map.Entry<String, String> i: this.comments.entrySet()){
			commentsLine += i.getKey() + ":" + i.getValue();
			count += 1;
			if (count != this.comments.size()){
				commentsLine += ",";
			}
		}
		try {
			wordReplacer(this.photoPath, commentsLine, User.class.getResource("/data/posts.txt").toURI(), "comments");
		} catch (URISyntaxException e) {
			BaseLogger.error(e.getMessage());
		}
	}
	/**
	 * an addLike method to add like to likes list and change it from posts.txt
	 * @param nickname of who liked
	 */
	public void addLike(String nickname) {
		//adding it to likes list
		this.likes.add(nickname);
		//changing it from posts.txt
		String likesLine = "";
		for (int i = 0; i < this.likes.size(); i++) {
			likesLine += this.likes.get(i);
			if (i != this.likes.size()-1) {
				likesLine += ",";
			}
		}
		try {
			wordReplacer(this.photoPath, likesLine, User.class.getResource("/data/posts.txt").toURI(), "likes");
		} catch (URISyntaxException e) {
			BaseLogger.error(e.getMessage());
		}
	}
	/**
	 * an addDislike method to add dislike to dislikes list and change it from posts.txt
	 * @param nickname of who disliked
	 */
	public void addDislike(String nickname) {
		//adding it to likes list
		this.dislikes.add(nickname);
		//changing it from posts.txt
		String dislikesLine = "";
		for (int i = 0; i < this.dislikes.size(); i++) {
			dislikesLine += this.dislikes.get(i);
			if (i != this.dislikes.size()-1) {
				dislikesLine += ",";
			}
		}
		try {
			wordReplacer(this.photoPath, dislikesLine, User.class.getResource("/data/posts.txt").toURI(), "dislikes");
		} catch (URISyntaxException e) {
			BaseLogger.error(e.getMessage());
		}
	}
	/**
	 * a method to remove like from likes list and posts.txt
	 * @param nickname to remove
	 */
	public void removeLike(String nickname) {
		//removing from likes list
		if (this.likes.contains(nickname)) {
			this.likes.remove(nickname);
		//removing from posts.txt
			String likesLine = " ";
			for (int i = 0; i < this.likes.size(); i++) {
				likesLine += this.likes.get(i);
				if (i != this.likes.size()-1) {
					likesLine += ",";
				}
			}
			try {
				wordReplacer(this.photoPath, likesLine, User.class.getResource("/data/posts.txt").toURI(), "likes");
			} catch (URISyntaxException e) {
				BaseLogger.error(e.getMessage());
			}
		}
	}
	/**
	 * a method to remove dislike from dislikes list and posts.txt
	 * @param nickname to remove
	 */
	public void removeDislike(String nickname) {
		//removing from likes list
		if (this.dislikes.contains(nickname)) {
			this.dislikes.remove(nickname);
			//removing from posts.txt
			String dislikesLine = " ";
			for (int i = 0; i < this.dislikes.size(); i++) {
				dislikesLine += this.dislikes.get(i);
				if (i != this.dislikes.size()-1) {
					dislikesLine += ",";
				}
			}
			try {
				wordReplacer(this.photoPath, dislikesLine, User.class.getResource("/data/posts.txt").toURI(), "dislikes");
			} catch (URISyntaxException e) {
				BaseLogger.error(e.getMessage());
			}
		}
	}
	/**
	 * a wordReplacer method to replace fields from posts.txt file 
	 * @param photoPath the images path
	 * @param wordToInsert new field
	 * @param filePath where is it going to be changed
	 * @param field
	 */
	public static void wordReplacer(String photoPath, String wordToInsert, URI filePath, String field) {
		try {
			File file = new File(filePath);
			BufferedReader reader = new BufferedReader(new FileReader(file));
            FileWriter filewriter = new FileWriter(file, true);
			BufferedWriter writer = new BufferedWriter(filewriter);
			List<String> lines = new ArrayList<String>();
            int index = 0;
            //determining the fields index
            switch (field) {
            	case "comments":
            		index = 3;
            		break;
            	case "likes":
            		index = 4;
            		break;
            	case "dislikes":
            		index = 5;
            		break;
            }
            //removing it from posts.txt
            String line;
            while ((line = reader.readLine()) != null) {
            	String[] tokens = line.split("#");
            	String newLine = "";
            	if (tokens[1].matches(photoPath)) {
                	for (int i = 0; i < tokens.length; i++) {
                		if (i == index) {
                			newLine += wordToInsert;
                		}else {
                			newLine += tokens[i];
                		}
                		if (i != tokens.length-1) {
                			newLine += "#";
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
	 * a removePost method to remove posts from posts list and posts.txt
	 * @param post to remove
	 */
	public static void removePost(Post post) {
		try {
			//removing from posts
			Post.posts.remove(post);
	    	//removing from posts.txt
			File file1 = new File(User.class.getResource("/data/posts.txt").toURI());
			BufferedReader reader1 = new BufferedReader(new FileReader(file1));
	        FileWriter filewriter1 = new FileWriter(file1, true);
			BufferedWriter writer1 = new BufferedWriter(filewriter1);
			List<String> lines1 = new ArrayList<String>();
			String line1;
			while ((line1 = reader1.readLine()) != null) {
	        	String[] tokens1 = line1.split("#");
	        	String newLine1 = "";
	        	if (tokens1[1].matches(post.getPhotoPath())) {
	            	continue;
	            } else {
	            	newLine1 = line1;
	            }
	            lines1.add(newLine1);
	        }
	        FileWriter Xfilewriter1 = new FileWriter(file1);
			BufferedWriter Xwriter1 = new BufferedWriter(Xfilewriter1);
			Xwriter1.write("");
			Xwriter1.close();
	        for (int i = 0; i < lines1.size(); i++) {
	        	writer1.write(lines1.get(i));
	        	writer1.newLine();
	        }
	        reader1.close();
	        writer1.close();
	        User.removePhoto(post.getPhotoPath());
	        if (User.sharedImages.contains(post.getPhotoPath())) {
	        	User.sharedImages.remove(post.getPhotoPath());
	        }
		} catch (IOException | URISyntaxException e) {
			BaseLogger.error(e.getMessage());
		}
	}
}
