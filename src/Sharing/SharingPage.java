package Sharing;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import PhotoCloudApp.BaseLogger;
import Users.User;
/**
 * a SharingPage class to open a window to share a post
 * @author ahmet
 *
 */
public class SharingPage extends JFrame implements ActionListener {
	//fields
	private String filePath;
	private User visitor;
	
	private JPanel image;
	private JLabel descriptionLabel = new JLabel("Write a description for your photo (optional):");
	
	private JTextField description = new JTextField();
	
	private JButton share = new JButton("Share");
	private JButton cancel = new JButton("Cancel");
	
	/**
	 * constructor to create the window
	 * @param filePath image's path
	 * @param visitor the user who wants to post
	 */
	public SharingPage(String filePath, User visitor) {
		this.filePath = filePath;
		this.visitor = visitor;
		
		this.filePath = filePath;
		image = new JPanel() {
            @Override
            /**
             * using 2d graphics to draw the image without it's resoulution going bad
             */
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    BufferedImage image = ImageIO.read(new File(filePath));
                    int maxWidth = getWidth() - 20;
                    int maxHeight = getHeight() - 20; 
                    int imageWidth = image.getWidth();
                    int imageHeight = image.getHeight();

                    double widthRatio = (double) maxWidth / imageWidth;
                    double heightRatio = (double) maxHeight / imageHeight;
                    double scaleFactor = Math.min(widthRatio, heightRatio);

                    int scaledWidth = (int) (imageWidth * scaleFactor);
                    int scaledHeight = (int) (imageHeight * scaleFactor);

                    int x = (getWidth() - scaledWidth) / 2; 
                    int y = (getHeight() - scaledHeight) / 2;

                    g.drawImage(image, x, y, scaledWidth, scaledHeight, null);
                } catch (IOException e) {
                    BaseLogger.error(e.getMessage());
                }
            }
        };
		image.setBounds(50, 50, 700, 350);
		 
		descriptionLabel.setBounds(50, 450, 300, 50);
		 
		description.setBounds(350, 450, 350, 50);
		description.addActionListener(this);
		 
		share.setBounds(400, 600, 100, 60);
		share.addActionListener(this);
		
		cancel.setBounds(250, 600, 100, 60);
		cancel.addActionListener(this);
		 
		this.setVisible(true);
		this.setSize(800, 800);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setTitle("Share");
		this.setLocationRelativeTo(null);
		this.setLayout(null);
		this.setAlwaysOnTop(true);
		this.add(image);
		this.add(descriptionLabel);
		this.add(description);
		this.add(share);
		this.add(cancel);
	}
	/**
	 * an action listener to handle button clicks and more
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		//to share the post when clicked share button
		if (e.getSource() == share) {
			String text = description.getText();
			if (text.isEmpty()) {
				//creating a new post
				Post post = new Post(visitor, filePath, "");
				try {
					// adding it to posts.txt
					File file = new File(User.class.getResource("/data/posts.txt").toURI());
					FileWriter filewriter = new FileWriter(file, true);
					BufferedWriter writer = new BufferedWriter(filewriter);
					String commentsLine = " ";
					String likesLine = " ";
					String dislikesLine = " ";
					int count = 0;
					if (post.comments.size() != 0) {
						for (Map.Entry<String, String> i: post.comments.entrySet()){
							commentsLine += i.getKey() + ":" + i.getValue();
							count += 1;
							if (count != post.comments.size()){
								commentsLine += ",";
							}
						}
					}
					for (int i = 0; i < post.likes.size(); i++) {
						likesLine += post.likes.get(i);
						if (i != post.likes.size()-1) {
							likesLine += ",";
						}
					}
					for (int i = 0; i < post.dislikes.size(); i++) {
						dislikesLine += post.dislikes.get(i);
						if (i != post.dislikes.size()-1) {
							dislikesLine += ",";
						}
					}
					writer.write(post.getOwner().getNickname() + "#" + post.getPhotoPath() + "#" + " " + "#" + commentsLine + "#" + likesLine + "#" + dislikesLine);
					writer.newLine();
					writer.close();
					BaseLogger.info(post.getOwner().getNickname() + " shared a photo: " + post.getPhotoPath());
				} catch (IOException | URISyntaxException e1) {
					BaseLogger.error(e1.getMessage());
				}
			} else {
				//creating a post
				Post post = new Post(visitor, filePath, text);
				try {
					//adding it to posts.txt
					File file = new File(User.class.getResource("/data/posts.txt").toURI());
					FileWriter filewriter = new FileWriter(file, true);
					BufferedWriter writer = new BufferedWriter(filewriter);
					String commentsLine = " ";
					String likesLine = " ";
					String dislikesLine = " ";
					int count = 0;
					if (post.comments.size() != 0) {
						for (Map.Entry<String, String> i: post.comments.entrySet()){
							commentsLine += i.getKey() + ":" + i.getValue();
							count += 1;
							if (count != post.comments.size()){
								commentsLine += ",";
							}
						}
					}
					for (int i = 0; i < post.likes.size(); i++) {
						likesLine += post.likes.get(i);
						if (i != post.likes.size()-1) {
							likesLine += ",";
						}
					}
					for (int i = 0; i < post.dislikes.size(); i++) {
						dislikesLine += post.dislikes.get(i);
						if (i != post.dislikes.size()-1) {
							dislikesLine += ",";
						}
					}
					writer.write(post.getOwner().getNickname() + "#" + post.getPhotoPath() + "#" + post.getDescription() + "#" + commentsLine + "#" + likesLine + "#" + dislikesLine);
					writer.newLine();
					writer.close();
					BaseLogger.info(post.getOwner().getNickname() + " shared a photo: " + post.getPhotoPath());
				} catch (IOException | URISyntaxException e1) {
					BaseLogger.error(e1.getMessage());
				}
			}
			this.dispose();
		//to close the window when hits cancel
		} else if (e.getSource() == cancel) {
			this.dispose();
		}
		
	}
}
