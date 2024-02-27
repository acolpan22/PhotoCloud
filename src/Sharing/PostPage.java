package Sharing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import PhotoCloudApp.BaseLogger;
import PhotoCloudApp.DiscoverPage;
import PhotoCloudApp.ProfilePage;
import Users.User;
/**
 * a PostPage to show post and its fields and more
 * @author ahmet
 *
 */
public class PostPage extends JFrame implements ActionListener{
	//page's fields
	private User visitor;
	private String filePath;
	private Post post;
	
	private JPanel image;
	private JPanel commentsPanel;
	private JPanel info;
	private JPanel profile;
	private JPanel descriptionBox;
	private JPanel comments;
	
	private JLabel profilePhoto;
	private JLabel description;
	private JLabel commentLabel;
	
	private JTextField newCommentText;
	private JLabel enterComment;
	
	private JButton back;
	private JButton nickname;
	private JButton reset;
	private JButton send;
	
	private JRadioButton like;
	private JRadioButton dislike;
	
	private JLabel likeCount;
	private JLabel dislikeCount;
	
	private ButtonGroup group;
	
	private JButton delete;
	
	/**
	 * a constructor for the page
	 * @param filePath the image's path of the post
	 * @param visitor who visits this page
	 */
	public PostPage(String filePath, User visitor) {
		this.visitor = visitor;
		this.filePath = filePath;
		this.post = Post.getPost(filePath);
		
		image = new JPanel() {
            @Override
            //drawing the image using 2d graphics without getting its resolution ratio going bad
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
        image.setPreferredSize(new Dimension(500, 500));
		
        info = new JPanel();
        info.setPreferredSize(new Dimension(180, 720));
        info.setBorder(new LineBorder(new Color(0, 0, 0)));
        info.setLayout(null);
        
        profile = new JPanel();
        profile.setBounds(0, 0, 180, 200);
        profile.setLayout(new GridLayout(2, 1));
        profile.setBorder(new LineBorder(new Color(0, 0, 0)));
        info.add(profile);
        
        profilePhoto = new JLabel();
        profilePhoto.setHorizontalAlignment(SwingConstants.CENTER);
        profilePhoto.setIcon(new ImageIcon(new ImageIcon(User.getUserByPhoto(filePath).getProfilePhotoPath()).getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
		profile.add(profilePhoto);
		
		nickname = new JButton();
		nickname.setHorizontalAlignment(SwingConstants.CENTER);
		nickname.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		nickname.setText(User.getUserByPhoto(filePath).getNickname());
		nickname.setBorder(BorderFactory.createEmptyBorder());
		nickname.setToolTipText("Click to visit profile");
		nickname.addActionListener(this);
		profile.add(nickname);

        descriptionBox = new JPanel();
        descriptionBox.setLayout(new FlowLayout(FlowLayout.CENTER));
        info.add(descriptionBox);
        
        //setting description to create a new line using html brackets after coming to panel border
        description = new JLabel();
        String text;
        if (!Post.getPost(filePath).getDescription().matches(" ")) {
        	text = "<html>";
	        int length = Post.getPost(filePath).getDescription().length();
	        int numOfSubstrings = (int) Math.ceil((double) length / 28);
	        String[] substrings = new String[numOfSubstrings];
	
	        for (int i = 0; i < numOfSubstrings; i++) {
	            int startIndex = i * 28;
	            int endIndex = Math.min(startIndex + 28, length);
	            substrings[i] = Post.getPost(filePath).getDescription().substring(startIndex, endIndex);
	        }
	        for (int i = 0; i < numOfSubstrings; i++) {
	        	text += substrings[i];
	        	if (i != numOfSubstrings-1) {
	        		text += "<br/>";
	        	}
	        }
	        text += "</html>";
	        descriptionBox.setBorder(new LineBorder(new Color(0, 0, 0)));
	        descriptionBox.setBounds(0, 200, 180, 150);
	    } else {
	    	text = " ";
	    }
        description.setText(text);
        description.setVerticalAlignment(SwingConstants.CENTER);
        description.setHorizontalAlignment(SwingConstants.CENTER);
        description.setPreferredSize(new Dimension(170, 150));
        descriptionBox.add(description);
        
        group = new ButtonGroup();
        
        like = new JRadioButton();
        like.setIcon(new ImageIcon(PostPage.class.getResource("/appIcons/like.png")));
        like.setBounds(50, 400, 30, 30);
        //to disable like button if the visitor liked this post before
        if (post.likes.contains(visitor.getNickname())) {
        	like.setEnabled(false);
        }
        like.addActionListener(this);
        group.add(like);
        info.add(like);
        
        dislike = new JRadioButton();
        dislike.setIcon(new ImageIcon(PostPage.class.getResource("/appIcons/dislike.png")));
        dislike.setBounds(100, 400, 30, 30);
        //to disable dislike button if the visitor disliked this post before
        if (post.dislikes.contains(visitor.getNickname())) {
        	dislike.setEnabled(false);
        }
        dislike.addActionListener(this);
        group.add(dislike);
        info.add(dislike);
        
        likeCount = new JLabel(String.valueOf(this.post.likes.size()));
        likeCount.setBounds(70, 430, 10, 10);
        info.add(likeCount);
        
        dislikeCount = new JLabel(String.valueOf(this.post.dislikes.size()));
        dislikeCount.setBounds(110, 430, 10, 10);
        info.add(dislikeCount);
        
        reset = new JButton("reset");
        reset.setBounds(55, 450, 70, 40);
        reset.addActionListener(this);
        info.add(reset);
        
        //to give permission to delete the post if the visior is admin or the owner of this post
        if ((this.visitor.isAdmin) || (this.visitor.getNickname().matches(this.post.getOwner().getNickname())) ) {
        	delete = new JButton("Delete this post");
        	delete.setBounds(15, 500, 150, 50);
        	delete.addActionListener(this);
        	info.add(delete);
        }
		
		back = new JButton("Back");
		back.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		back.setBounds(10, 600, 160, 60);
		back.addActionListener(this);
		info.add(back);
        
		commentsPanel = new JPanel();
		commentsPanel.setPreferredSize(new Dimension(300, 720));
		commentsPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		commentsPanel.setLayout(null);
        
        commentLabel = new JLabel("Comments:");
        commentLabel.setFont(new Font("Lucida Grande", Font.BOLD, 30));
        commentLabel.setBounds(10, 0, 290, 40);
        commentsPanel.add(commentLabel);
        
        newCommentText = new JTextField();
        newCommentText.setBounds(10, 500, 280, 100);
        newCommentText.setHorizontalAlignment(JTextField.LEFT);
        commentsPanel.add(newCommentText);
        
        enterComment = new JLabel("Enter your comment:");
        enterComment.setBounds(20, 600, 150, 100);
        commentsPanel.add(enterComment);
        
        send = new JButton("Send");
        send.setBounds(200, 630, 60, 40);
        send.addActionListener(this);
        commentsPanel.add(send);
        
        comments = new JPanel();
        comments.setBounds(0, 40, 300, 450);
        comments.setBorder(new LineBorder(new Color(0, 0, 0)));
        comments.setLayout(new GridLayout(10,1));
        commentsPanel.add(comments);
        
       if (Post.getPost(filePath).comments.isEmpty()) {
    	   JLabel label = new JLabel("There are no comments");
    	   label.setPreferredSize(new Dimension(300, 50));
    	   label.setVerticalAlignment(SwingConstants.CENTER);
    	   label.setHorizontalAlignment(SwingConstants.CENTER);
    	   label.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
    	   comments.add(label);
       } else {
    	   for (Map.Entry<String, String> i: Post.getPost(filePath).comments.entrySet()) {
    		   JLabel label = new JLabel();
    		   label.setIcon(new ImageIcon(new ImageIcon(User.getUser(i.getKey()).getProfilePhotoPath()).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
    		   label.setText(User.getUser(i.getKey()).getNickname() + ": " + i.getValue());
    		   label.setHorizontalTextPosition(JLabel.RIGHT);
    		   label.setVerticalTextPosition(JLabel.CENTER);
    		   label.setVerticalAlignment(JLabel.CENTER);
    		   label.setHorizontalAlignment(JLabel.CENTER);
    		   comments.add(label);
    	   }
       }
        
		this.setVisible(true);
		this.setSize(1280, 720);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setTitle("Post");
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
		this.setAlwaysOnTop(true);
		this.add(info, BorderLayout.WEST);
		this.add(image, BorderLayout.CENTER);
		this.add(commentsPanel, BorderLayout.EAST);
	}
	/**
	 * to handle button clicks
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		//if the reset button is pressed it resets the like and dislike button
		if (e.getSource() == reset) {
			this.post.removeLike(this.visitor.getNickname());
			this.post.removeDislike(this.visitor.getNickname());
			group.clearSelection();
			likeCount.setText(String.valueOf(this.post.likes.size()));
			dislikeCount.setText(String.valueOf(this.post.dislikes.size()));
			like.setEnabled(true);
			dislike.setEnabled(true);
		//back button to close the page
		} else if (e.getSource() == back) {
			this.dispose();
		//to transfer you to profile page of the post's owner
		} else if (e.getSource() == nickname) {
			Window[] windows = getWindows();
			for (Window i: windows) {
				i.dispose();
			}
			new ProfilePage(User.getUserByPhoto(filePath), this.visitor);
		// to like the post 
		} else if (e.getSource() == like) {
				this.post.removeDislike(this.visitor.getNickname());
				this.post.addLike(this.visitor.getNickname());
				likeCount.setText(String.valueOf(this.post.likes.size()));
				dislikeCount.setText(String.valueOf(this.post.dislikes.size()));
				dislike.setEnabled(true);
				like.setEnabled(false);
		// to dislike the post
		} else if (e.getSource() == dislike) {
			this.post.removeLike(this.visitor.getNickname());
			this.post.addDislike(this.visitor.getNickname());
			likeCount.setText(String.valueOf(this.post.likes.size()));
			dislikeCount.setText(String.valueOf(this.post.dislikes.size()));
			dislike.setEnabled(false);
			like.setEnabled(true);
		//to send a comment
		} else if (e.getSource() == send) {
			if (!newCommentText.getText().isEmpty()) {
				post.addComment(visitor.getNickname(), newCommentText.getText());
				this.dispose();
				new PostPage(this.filePath, this.visitor);
			}
		//to delete the post
		} else if (e.getSource() == delete) {
			Post.removePost(this.post);
			Window[] windows = getWindows();
			for (Window i: windows) {
				i.dispose();
			}
			new DiscoverPage(this.visitor);
		}
	}
}
