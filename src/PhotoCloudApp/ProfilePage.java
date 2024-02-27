package PhotoCloudApp;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Users.User;
import button.ResizableButton;
import editing.PhotoEditingPage;
import exception.IncorrectInputException;
import exception.InvalidUserException;

import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import Sharing.Post;
import Sharing.PostPage;
import Sharing.SharingPage;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
/**
 * a ProfilePage class to show user information
 * @author ahmet
 *
 */
public class ProfilePage extends JFrame implements ActionListener{
	//fields
	private User owner;
	private User visitor;
	
	private JPanel infoPanel;
	private JPanel imagesPanel;
	
	private JPanel infoLeft;
	private JPanel infoRight;
	
	private JButton profilePicture;
	private JLabel userNickname;
	
	private JLabel password;
	private JButton passwordEdit;
	
	private JLabel name;
	private JButton nameEdit;
	
	private JLabel surname;
	private JButton surnameEdit;
	
	private JLabel age;
	private JButton ageEdit;
	
	private JLabel email;
	private JButton emailEdit;
	
	private JButton backToDiscover;
	private JButton logout;
	
	/**
	 * a constructor to create the window
	 * @param owner of the profile page
	 * @param visitor who visit the page
	 */
	public ProfilePage(User owner, User visitor) {
		this.owner = owner;
		this.visitor = visitor;
		
		imagesPanel = new JPanel();
		imagesPanel.setLayout(new GridLayout());
		imagesPanel.setBounds(0, 350, 1680, 655);
		//if the user is viewing their own profile page
		if (owner.getNickname().matches(visitor.getNickname())) {	
			if (owner.userImages.size() == 0) {
				JLabel noPhotosLabel = new JLabel("You didn't upload any images yet");
				noPhotosLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 25));
				noPhotosLabel.setHorizontalAlignment(JLabel.CENTER);
				imagesPanel.add(noPhotosLabel);
			} else {
				//listing images one by one
				for (String i: owner.userImages) {
					ResizableButton button = new ResizableButton(new ImageIcon(i));
					button.setBorder(BorderFactory.createEmptyBorder());
					button.setToolTipText("Click for full view");
					button.addActionListener(new ActionListener() {
						//adding action listener to images
						@Override
						public void actionPerformed(ActionEvent e) {
							if (e.getSource() == button) {
								if (User.sharedImages.contains(i)) {
									int answer = JOptionPane.showConfirmDialog(null, "Do you want to edit this photo?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
									if (answer == JOptionPane.YES_OPTION) {
										new PhotoEditingPage(i);
									} else if (answer == JOptionPane.NO_OPTION) {
									new PostPage(i, visitor);
									}
								} else {
									int answer = JOptionPane.showConfirmDialog(null, "Do you want to edit this photo?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
									if (answer == JOptionPane.YES_OPTION) {
										new PhotoEditingPage(i);
									} else if (answer == JOptionPane.NO_OPTION) {
										int answer1 = JOptionPane.showConfirmDialog(null, "Do you want to share this photo?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
										if (answer1 == JOptionPane.YES_OPTION){
											new SharingPage(i, visitor);
										} else if (answer1 == JOptionPane.NO_OPTION){
											new BigPicturePage(i);
										}
									}
								}
							}
						}
					});
					imagesPanel.add(button);
				}
			}
			
			infoPanel = new JPanel();
			infoPanel.setLayout(null);
			infoPanel.setBounds(0, 0, 1680, 350);
			
			infoLeft = new JPanel();
			infoLeft.setLayout(new GridLayout(2,1));
			infoLeft.setBounds(0, 0, 800, 350);
			infoLeft.setBorder(new LineBorder(new Color(0, 0, 0)));
			infoPanel.add(infoLeft);
			
			profilePicture = new JButton();
			profilePicture.setVerticalAlignment(SwingConstants.BOTTOM);
			profilePicture.setIcon(new ImageIcon(new ImageIcon(owner.getProfilePhotoPath()).getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
			profilePicture.setBorder(BorderFactory.createEmptyBorder());
			profilePicture.setToolTipText("Click to change profile photo");
			profilePicture.addActionListener(this);
			infoLeft.add(profilePicture);
			
			userNickname = new JLabel(owner.getNickname() + " (" + owner.getUserType() + ")");
			userNickname.setFont(new Font("Lucida Grande", Font.PLAIN, 25));
			userNickname.setHorizontalAlignment(SwingConstants.CENTER);
			infoLeft.add(userNickname);
			
			infoRight = new JPanel();
			infoRight.setLayout(null);
			infoRight.setBounds(800, 0, 880, 350);
			infoRight.setBorder(new LineBorder(new Color(0, 0, 0)));
			infoPanel.add(infoRight);
			
			password = new JLabel("Password: " + owner.getPassword());
			password.setBounds(100, 0, 400, 70);
			infoRight.add(password);
			
			passwordEdit = new JButton("Edit");
			passwordEdit.setBounds(500, 0, 80, 70);
			passwordEdit.addActionListener(this);
			infoRight.add(passwordEdit);
			
			name = new JLabel("Name: " + owner.getName());
			name.setBounds(100, 70, 400, 70);
			infoRight.add(name);
			
			nameEdit = new JButton("Edit");
			nameEdit.setBounds(500, 70, 80, 70);
			nameEdit.addActionListener(this);
			infoRight.add(nameEdit);
			
			surname = new JLabel("Surname: " + owner.getSurname());
			surname.setBounds(100, 140, 400, 70);
			infoRight.add(surname);
			
			surnameEdit = new JButton("Edit");
			surnameEdit.setBounds(500, 140, 80, 70);
			surnameEdit.addActionListener(this);
			infoRight.add(surnameEdit);
			
			age = new JLabel("Age: " + owner.getAge());
			age.setBounds(100, 210, 400, 70);
			infoRight.add(age);
			
			ageEdit = new JButton("Edit");
			ageEdit.setBounds(500, 210, 80, 70);
			ageEdit.addActionListener(this);
			infoRight.add(ageEdit);
			
			email = new JLabel("Email: " + owner.getEmail());
			email.setBounds(100, 280, 400, 70);
			infoRight.add(email);
			
			emailEdit = new JButton("Edit");
			emailEdit.setBounds(500, 280, 80, 70);
			emailEdit.addActionListener(this);
			infoRight.add(emailEdit);
			
			backToDiscover = new JButton("Discover Page");
			backToDiscover.setBounds(700, 70, 100, 80);
			backToDiscover.addActionListener(this);
			infoRight.add(backToDiscover);
			
			logout = new JButton("Logout");
			logout.setBounds(700, 220, 100, 80);
			logout.setFont(new Font("Lucida Grande", Font.BOLD, 25));
			logout.addActionListener(this);
			infoRight.add(logout);
			
		} else {
			boolean condition = false;
			for (String i: User.sharedImages) {
				for (String a : owner.userImages) {
					if (i.matches(a)) {
						condition = true;
						break;
					}
				}
				if (condition) {
					break;
				}
			}
			if (!condition) {
				JLabel noPhotosLabel = new JLabel(owner.getNickname() + " didn't upload any images yet");
				noPhotosLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 25));
				noPhotosLabel.setHorizontalAlignment(JLabel.CENTER);
				imagesPanel.add(noPhotosLabel);
			} else {
				//adding images one by one
				for (String i: owner.userImages) {
					if (User.sharedImages.contains(i)) {
						ResizableButton button = new ResizableButton(new ImageIcon(i));
						button.setBorder(BorderFactory.createEmptyBorder());
						button.setToolTipText("Click for full view");
						button.addActionListener(new ActionListener() {
							//adding action listener to images one by one
							@Override
							public void actionPerformed(ActionEvent e) {
								if (e.getSource() == button) {
									new PostPage(i, visitor);
								}
							}
						});
						imagesPanel.add(button);
					}
				}
			}
			
			infoPanel = new JPanel();
			infoPanel.setLayout(null);
			infoPanel.setBounds(0, 0, 1680, 350);
			
			infoLeft = new JPanel();
			infoLeft.setLayout(new GridLayout(2,1));
			infoLeft.setBounds(0, 0, 800, 350);
			infoLeft.setBorder(new LineBorder(new Color(0, 0, 0)));
			infoPanel.add(infoLeft);
			
			JLabel profilePictureLabel = new JLabel();
			profilePictureLabel.setVerticalAlignment(SwingConstants.CENTER);
			profilePictureLabel.setHorizontalAlignment(SwingConstants.CENTER);
			profilePictureLabel.setIcon(new ImageIcon(new ImageIcon(owner.getProfilePhotoPath()).getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
			infoLeft.add(profilePictureLabel);
			
			userNickname = new JLabel(owner.getNickname() + " (" + owner.getUserType() + ")");
			userNickname.setFont(new Font("Lucida Grande", Font.PLAIN, 25));
			userNickname.setHorizontalAlignment(SwingConstants.CENTER);
			infoLeft.add(userNickname);
			
			infoRight = new JPanel();
			infoRight.setLayout(null);
			infoRight.setBounds(800, 0, 880, 350);
			infoRight.setBorder(new LineBorder(new Color(0, 0, 0)));
			infoPanel.add(infoRight);
			
			name = new JLabel("Name: " + owner.getName());
			name.setBounds(100, 70, 400, 70);
			infoRight.add(name);
			
			surname = new JLabel("Surname: " + owner.getSurname());
			surname.setBounds(100, 140, 400, 70);
			infoRight.add(surname);
			
			age = new JLabel("Age: " + owner.getAge());
			age.setBounds(100, 210, 400, 70);
			infoRight.add(age);
			
			backToDiscover = new JButton("Discover Page");
			backToDiscover.setBounds(700, 70, 100, 80);
			backToDiscover.addActionListener(this);
			infoRight.add(backToDiscover);
			
			logout = new JButton("Logout");
			logout.setBounds(700, 220, 100, 80);
			logout.setFont(new Font("Lucida Grande", Font.BOLD, 25));
			logout.addActionListener(this);
			infoRight.add(logout);
			
			
		}
		this.pack();
		this.setVisible(true);
		this.setSize(1680, 955);
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("Profile Page");
		this.add(infoPanel);
		this.add(imagesPanel);
		
	}
	/**
	 * action listener to handle buttons and more
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		//to change password of the user
		if (e.getSource() == passwordEdit) {
			String newPassword;
			while (true) {
				newPassword = JOptionPane.showInputDialog("<html>enter a new password that<br/>-contains at least 8 characters and at most 20 characters<br/>-contains at least one digit<br/>-contains at least one upper case letter<br/>-contains at least one lower case letter<br/>-contains at least one special character<br/>-doesn’t contain any white space<html/>");
				try {
					if (newPassword == null) {
						break;
					}
					if (User.validatePassword(newPassword)) {
						this.owner.setPassword(newPassword);
						password.setText("Password: " + newPassword);
						break;
					}
				} catch (IncorrectInputException e1) {
					BaseLogger.error(e1.getMessage());
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		//to change name of the user
		} else if (e.getSource() == nameEdit) {
			String newName;
			while (true) {
				newName = JOptionPane.showInputDialog("Enter your new name");
				try {
					if (newName == null) {
						break;
					}
					if (User.validateName(newName)) {
						this.owner.setName(newName);
						name.setText("Name: " + newName);
						break;
					}
				} catch (IncorrectInputException e1) {
					BaseLogger.error(e1.getMessage());
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		//to change surname of the user
		} else if (e.getSource() == surnameEdit) {
			String newSurname;
			while (true) {
				newSurname = JOptionPane.showInputDialog("Enter your new surname");
				try {
					if (newSurname == null) {
						break;
					}
					if (User.validateSurname(newSurname)) {
						this.owner.setSurname(newSurname);
						surname.setText("Surname: " + newSurname);
						break;
					}
				} catch (IncorrectInputException e1) {
					BaseLogger.error(e1.getMessage());
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		//to change age of the user
		} else if (e.getSource() == ageEdit) {
			String newAge;
			while (true) {
				newAge = JOptionPane.showInputDialog("Enter your new age");
				try {
					if (newAge == null) {
						break;
					}
					if (User.validateAge(newAge)) {
						this.owner.setAge(newAge);
						age.setText("Age: " + newAge);
						break;
					}
				} catch (IncorrectInputException e1) {
					BaseLogger.error(e1.getMessage());
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		//to change email of the person
		} else if (e.getSource() == emailEdit) {
			String newEmail;
			while (true) {
				newEmail = JOptionPane.showInputDialog("Enter your new email");
				try {
					if (newEmail == null) {
						break;
					}
					if (User.validateEmail(newEmail)) {
						this.owner.setEmail(newEmail);
						email.setText("Email: " + newEmail);
						break;
					}
				} catch (IncorrectInputException | InvalidUserException e1) {
					BaseLogger.error(e1.getMessage());
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		//to change profile picture of the person
		} else if (e.getSource() == profilePicture) {
			JFileChooser fc = new JFileChooser();
			fc.addChoosableFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png"));
            fc.setAcceptAllFileFilterUsed(false);
			int response = fc.showOpenDialog(null);
			if (response == JFileChooser.APPROVE_OPTION) {
				String filePath = fc.getSelectedFile().getAbsolutePath();
				try {
					Path source = Paths.get(filePath);
					Path destination = Paths.get(new File("res/images/").getAbsolutePath().toString() + "/" + this.owner.getNickname() + User.getExtension(filePath));
					Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
					this.owner.setProfilePhotoPath(destination.toString());
					profilePicture.setIcon(new ImageIcon(new ImageIcon(this.owner.getProfilePhotoPath()).getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
				} catch (IOException e1) {
					BaseLogger.error(e1.getMessage());
				}
			}
		//to go back to discover page
		} else if (e.getSource() == backToDiscover) {
			this.dispose();
			new DiscoverPage(this.visitor);
		//to logout from the account
		} else if (e.getSource() == logout) {
			owner.logout();
			this.dispose();
			new LoginPage();
		}
	}
}
