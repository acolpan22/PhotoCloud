package PhotoCloudApp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import Users.User;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import java.awt.Color;
import java.awt.event.KeyAdapter;

/**
 * A java class to represent login page of the application.
 * @author ahmet
 *
 */
public class LoginPage extends JFrame implements ActionListener {
	
	private JButton submit;
	private JButton toSignupPage;
	private JTextField nicknameField;
	private JPasswordField passwordField;
	private JLabel label1;
	private JLabel label2;
	private JLabel label3;
	private JLabel label4;
	private JButton quit;
	private JLabel asd;
	
	/**
	 * Constructor for login page which holds all of the elements
	 */
	public LoginPage() {
		submit = new JButton("Log in");
		submit.setBounds(245, 345, 150, 70);
		submit.addActionListener(this);
		
		toSignupPage = new JButton("Don't have an account?");
		toSignupPage.setForeground(new Color(72, 167, 247));
		toSignupPage.setBounds(330, 320, 150, 20);
		toSignupPage.addActionListener(this);
		toSignupPage.setBorder(BorderFactory.createEmptyBorder());
		
		label1 = new JLabel("Nickname:");
		label1.setBounds(118, 220, 70, 50);
		
		label2 = new JLabel("Password:");
		label2.setBounds(123, 277, 65, 50);
		
		label3 = new JLabel();
		label3.setBounds(200, 6, 250, 150);
		label3.setIcon(new ImageIcon(LoginPage.class.getResource("/appIcons/Hotpot.png")));
		
		label4 = new JLabel("PhotoCloud");
		label4.setBounds(228, 168, 200, 60);
		label4.setFont(new Font("Noteworthy", Font.PLAIN, 45));
		
		nicknameField = new JTextField();
		nicknameField.setBounds(200, 225, 250, 40);
		nicknameField.addActionListener(this);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(200, 282, 250, 40);
		passwordField.addActionListener(this);
		
		quit = new JButton("Quit");
		quit.setBounds(534, 396, 100, 50);
		quit.addActionListener(this);
		getContentPane().add(quit);
		
		//this is useless just to cover some error
		asd = new JLabel("");
		asd.setBounds(534, 396, 100, 50);
		this.add(asd);
		
		this.setVisible(true);
		this.setSize(640, 480);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setTitle("Log in");
		this.setLocationRelativeTo(null);
		this.setLayout(null);
		
		this.add(submit);
		this.add(nicknameField);
		this.add(passwordField);
		this.add(label1);
		this.add(label2);
		this.add(label3);
		this.add(label4);
		this.add(toSignupPage);
	}
	/**
	 * actionPerformed overridden method to handle events
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		//submit button to check user information and login
		if (e.getSource() == submit) {
			String nickname = nicknameField.getText();
			String password = new String(passwordField.getPassword());
			if (User.verifyUser(nickname, password)) {
				BaseLogger.info(nickname + " logged in.");
				this.dispose();
				User user = User.getUser(nickname);
				new DiscoverPage(user);
			} else {
				JOptionPane.showMessageDialog(null, "You entered wrong nickname or password. Try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
			}
		//toSignupPage button to navigate to signup page
		} else if (e.getSource() == toSignupPage) {
			this.dispose();
			new SignupPage();
		//quit button to safely quit the application and calculate runtime
		} else if (e.getSource() == quit) {
			long endTime = System.currentTimeMillis();
            long runtimeMillis = endTime - Main.startTime;
            long minutes = runtimeMillis / (60 * 1000);
            long seconds = (runtimeMillis % (60 * 1000)) / 1000;
            long milliseconds = runtimeMillis % 1000;
            BaseLogger.info("PhotoCloud has closed");
            BaseLogger.info("Application runtime is " + minutes + " minutes " + seconds + " seconds " + milliseconds + " milliseconds");
            this.dispose();
		}
	}
}
