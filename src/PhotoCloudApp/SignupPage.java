package PhotoCloudApp;

import java.awt.Color;
import java.awt.Font;
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

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import Users.FreeUser;
import Users.HobbyistUser;
import Users.ProfessionalUser;
import Users.User;
import Users.UserTypes;
import exception.IncorrectInputException;
import exception.InvalidUserException;

import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * a SignupPage class to get new users
 * @author ahmet
 *
 */
public class SignupPage extends JFrame implements ActionListener{
	//fields
	private JLabel label1;
	private JLabel label2;
	private JLabel label3;
	private JLabel label4;
	private JLabel label5;
	private JLabel label6;
	private JLabel label7;
	private JLabel label8;
	private JLabel label9;
	private JLabel label10;
	private JLabel label11;
	private JLabel label12;
	private JTextField nickname;
	private JPasswordField password;
	private JTextField name;
	private JTextField surname;
	private JTextField age;
	private JTextField email;
	private JComboBox comboBox;
	private JButton fileChooserButton;
	private JButton submitButton;
	private JButton backToLogin;
	private JCheckBox admin;
	private File file;
	private String filePath;
	
	/**
	 * a constructor for the SignupPage
	 */
	public SignupPage(){
		label1 = new JLabel("Enter nickname:");
		label1.setBounds(392, 170, 250, 40);
		
		label2 = new JLabel("Enter password:");
		label2.setBounds(392, 220, 250, 40);

		label3 = new JLabel("Enter name:");
		label3.setBounds(392, 270, 250, 40);
		
		label4 = new JLabel("Enter surname:");
		label4.setBounds(392, 320, 250, 40);
		
		label5 = new JLabel("Enter age:");
		label5.setBounds(392, 370, 250, 40);
		
		label6 = new JLabel("Enter email:");
		label6.setBounds(392, 420, 250, 40);
		
		label7 = new JLabel("Select a profile picture (optional):");
		label7.setBounds(392, 470, 250, 40);
		
		label8 = new JLabel("Select a user type:");
		label8.setBounds(392, 520, 250, 40);
		
		label10 = new JLabel();
		label10.setHorizontalAlignment(SwingConstants.CENTER);
		label10.setBounds(642, 23, 200, 100);
		label10.setIcon(new ImageIcon(SignupPage.class.getResource("/appIcons/Hotpot.png")));

		label9 = new JLabel("Welcome!");
		label9.setBounds(422, 48, 165, 50);
		label9.setFont(new Font("Noteworthy", Font.PLAIN, 45));
		
		label11 = new JLabel();
		label11.setBounds(800,470,400,40);
		label11.setVisible(false);
		
		//using html brackets to create a new line in JLabel
		label12 = new JLabel("<html>make sure that your password<br/>-contains at least 8 characters and at most 20 characters<br/>-contains at least one digit<br/>-contains at least one upper case letter<br/>-contains at least one lower case letter<br/>-contains at least one special character<br/>-doesn’t contain any white space<html/>");
		label12.setForeground(new Color(115, 114, 120));
		label12.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 13));
		label12.setHorizontalAlignment(SwingConstants.CENTER);
		label12.setLocation(899, 205);
		label12.setSize(281, 139);
		
		
		nickname = new JTextField();
		nickname.setBounds(537,170,350,40);
		nickname.addActionListener(this);
		
		password = new JPasswordField();
		password.setBounds(537,220,350,40);
		password.addActionListener(this);
		
		name = new JTextField();
		name.setBounds(537,270,350,40);
		name.addActionListener(this);
		
		surname = new JTextField();
		surname.setBounds(537,320,350,40);
		surname.addActionListener(this);
		
		age = new JTextField();
		age.setBounds(537,370,350,40);
		age.addActionListener(this);
		
		email = new JTextField();
		email.setBounds(537,420,350,40);
		email.addActionListener(this);
		
		fileChooserButton = new JButton("Select Image");
		fileChooserButton .setBounds(654,470,100,40);
		fileChooserButton.addActionListener(this);
		
		UserTypes[] types = {UserTypes.Free, UserTypes.Hobbyist, UserTypes.Professional};
		comboBox = new JComboBox(types);
		comboBox.setBounds(537, 520, 250, 40);
		
		
		admin = new JCheckBox("Is it administirator?");
		admin.setBounds(837, 520, 200, 40);
		admin.addActionListener(this);
		
		submitButton = new JButton("Sign up");
		submitButton.setBounds(630, 590, 150, 50);
		submitButton.addActionListener(this);
		
		backToLogin = new JButton("Do you have an account?");
		backToLogin.setHorizontalAlignment(SwingConstants.LEFT);
		backToLogin.setForeground(new Color(72, 167, 247));
		backToLogin.setBounds(837, 555, 200, 20);
		backToLogin.addActionListener(this);
		backToLogin.setBorder(BorderFactory.createEmptyBorder());
		
		this.setVisible(true);
		this.setSize(1280, 720);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setTitle("Sign up");
		this.setLocationRelativeTo(null);
		
		this.setLayout(null);
		this.add(label1);
		this.add(label2);
		this.add(label3);
		this.add(label4);
		this.add(label5);
		this.add(label6);
		this.add(label7);
		this.add(label8);
		this.add(label9);
		this.add(label10);
		this.add(label11);
		this.add(label12);
		this.add(nickname);
		this.add(password);
		this.add(name);
		this.add(surname);
		this.add(age);
		this.add(email);
		this.add(backToLogin);
		//if there is not an admin yet it shows a is it admin button
		if (!User.verifyAdmin()) {
			this.add(admin);
		}
		this.add(comboBox);
		this.add(fileChooserButton);
		this.add(submitButton);
	}
	/**
	 * an action listener to handle buttons and more
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		//a button to go back to login page
		if (e.getSource() == backToLogin) {
			this.dispose();
			new LoginPage();
		//to choose files from the device
		} else if (e.getSource() == fileChooserButton) {
			JFileChooser fc = new JFileChooser();
			fc.addChoosableFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png"));
            fc.setAcceptAllFileFilterUsed(false);
			int response = fc.showOpenDialog(null);
			if (response == JFileChooser.APPROVE_OPTION) {
				filePath = fc.getSelectedFile().getAbsolutePath();
				label11.setText(filePath);
				label11.setVisible(true);
			}
		//to create a new user
		} else if (e.getSource() == submitButton){
			boolean isAdmin = admin.isSelected();
			UserTypes type;
			if (isAdmin) {
				type = UserTypes.Professional;
			} else {
				type = (UserTypes) comboBox.getSelectedItem();
			}
			//if the user did not chose a profile picture it sets a default one
			if (filePath == null) {
				try {
					User user = null;
					if (type == UserTypes.Free) {
						user = new FreeUser(nickname.getText(), new String(password.getPassword()), name.getText(), surname.getText(), age.getText(), email.getText(), SignupPage.class.getResource("/appIcons/blank_pp.png").toURI().toString().substring(5), isAdmin, 0);
					} else if (type == UserTypes.Hobbyist) {
						user = new HobbyistUser(nickname.getText(), new String(password.getPassword()), name.getText(), surname.getText(), age.getText(), email.getText(), SignupPage.class.getResource("/appIcons/blank_pp.png").toURI().toString().substring(5), isAdmin, 0);
					} else if (type == UserTypes.Professional) {
						user = new ProfessionalUser(nickname.getText(), new String(password.getPassword()), name.getText(), surname.getText(), age.getText(), email.getText(), SignupPage.class.getResource("/appIcons/blank_pp.png").toURI().toString().substring(5), isAdmin, 0);
					}
					try {
						//writing new user information to users.txt
						File file = new File(User.class.getResource("/data/users.txt").toURI());
						FileWriter filewriter = new FileWriter(file, true);
						BufferedWriter writer = new BufferedWriter(filewriter);
						writer.write(nickname.getText() + "," + new String(password.getPassword()) + "," + name.getText() + "," + surname.getText() + "," + age.getText() + "," + email.getText() + "," + SignupPage.class.getResource("/appIcons/blank_pp.png").toURI().toString().substring(5) + "," + type + "," + isAdmin + "," + 0);
						writer.newLine();
						writer.close();
					} catch (IOException | URISyntaxException e2) {
						BaseLogger.error("User could not saved");
					}
					user.created();
					user.login();
					this.dispose();
					new DiscoverPage(user);
				} catch (InvalidUserException | IncorrectInputException | URISyntaxException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			//if not sets the preferred one as profile picture
			} else {
				try {
					User user = null;
					if (type == UserTypes.Free) {
						user = new FreeUser(nickname.getText(), new String(password.getPassword()), name.getText(), surname.getText(), age.getText(), email.getText(), filePath, isAdmin, 0);
					} else if (type == UserTypes.Hobbyist) {
						user = new HobbyistUser(nickname.getText(), new String(password.getPassword()), name.getText(), surname.getText(), age.getText(), email.getText(), filePath, isAdmin, 0);
					} else if (type == UserTypes.Professional) {
						user = new ProfessionalUser(nickname.getText(), new String(password.getPassword()), name.getText(), surname.getText(), age.getText(), email.getText(), filePath, isAdmin, 0);
					}
					try {
						//writing new user information to users.txt
						File file = new File(User.class.getResource("/data/users.txt").toURI());
						Path source = Paths.get(filePath);
						Path destination = Paths.get(new File("res/images/").getAbsolutePath().toString() + "/" + nickname.getText() + User.getExtension(filePath));
						Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
						FileWriter filewriter = new FileWriter(file, true);
						BufferedWriter writer = new BufferedWriter(filewriter);
						writer.write(nickname.getText() + "," + new String(password.getPassword()) + "," + name.getText() + "," + surname.getText() + "," + age.getText() + "," + email.getText() + "," + destination.toString() + "," + type + "," + isAdmin + "," + 0);
						writer.newLine();
						writer.close();
					} catch (IOException | URISyntaxException e2) {
						BaseLogger.error("User could not saved");
					}
					user.created();
					user.login();
					this.dispose();
					new DiscoverPage(user);
				} catch (InvalidUserException | IncorrectInputException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
}