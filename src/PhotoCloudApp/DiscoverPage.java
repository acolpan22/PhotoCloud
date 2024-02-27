package PhotoCloudApp;

import java.awt.Color;
import java.awt.Font;
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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import Sharing.Post;
import Sharing.PostPage;
import Sharing.SharingPage;
import Users.User;
import button.ResizableButtonWithLabel;
/**
 * a DiscoverPage to show shared images and navigating through the application
 * @author ahmet
 *
 */
public class DiscoverPage extends JFrame implements ActionListener{
	//fields
	private User user;
	
	private JButton logout;
	private JButton profile;
	private JButton upload;
	
	private JComboBox users;
	private JButton goToUser;
	
	/**
	 * a constructor for the page
	 * @param user the user who is surfing the application
	 */
	public DiscoverPage(User user) {
		this.user = user;
		
		JPanel panel2 = new JPanel();
		panel2.setBounds(180, 0, 1500, 927);
		panel2.setLayout(new GridLayout());
		
		JPanel panel1 = new JPanel();
		panel1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel1.setBounds(0, 0, 180, 927);
		panel1.setLayout(null);
		//displaying shared images
		if (User.sharedImages.size() == 0) {
			JLabel noPhotosLabel = new JLabel("No shared photos on PhotoCloud");
			noPhotosLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 25));
			noPhotosLabel.setHorizontalAlignment(JLabel.CENTER);
			panel2.add(noPhotosLabel);
		} else {
			for (String i: User.sharedImages) {
				User owner = User.getUserByPhoto(i);
				ResizableButtonWithLabel button = new ResizableButtonWithLabel(new ImageIcon(i), owner.getNickname() + " (" + owner.getUserType() + ")");
				button.getButton().setBorder(BorderFactory.createEmptyBorder());
				button.getButton().setToolTipText("Click for full view");
				button.getButton().addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						if (e.getSource() == button.getButton()) {
							new PostPage(i, user);
						}
					}
				});
				panel2.add(button);
			}
		}
		
		JPanel panel3 = new JPanel();
		panel3.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel3.setBounds(0, 0, 180, 120);
		panel3.setLayout(new GridLayout(2,1));
		panel1.add(panel3);
		
		logout = new JButton("Log out");
		logout.setFont(new Font("Lucida Grande", Font.BOLD, 25));
		logout.setBounds(0, 827, 180, 100);
		logout.addActionListener(this);
		panel1.add(logout);
		
		profile = new JButton("Profile Page");
		profile.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		profile.setBounds(0, 120, 180, 40);
		profile.setBorder(BorderFactory.createEmptyBorder());
		profile.addActionListener(this);
		panel1.add(profile);
		
		upload = new JButton("Upload photo");
		upload.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		upload.setBounds(0, 160, 180, 40);
		upload.setBorder(BorderFactory.createEmptyBorder());
		upload.addActionListener(this);
		panel1.add(upload);
		
		Object[] userArray = User.users.toArray();
		users = new JComboBox(userArray);
		users.setBounds(0, 250, 180, 50);
		panel1.add(users);
		
		goToUser = new JButton("Go to user");
		goToUser.setBounds(15, 300, 150, 50);
		goToUser.addActionListener(this);
		panel1.add(goToUser);
		
		JLabel accountIcon = new JLabel();
		accountIcon.setHorizontalAlignment(SwingConstants.CENTER);
		accountIcon.setIcon(new ImageIcon(new ImageIcon(user.getProfilePhotoPath()).getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
		panel3.add(accountIcon);
		
		JLabel accountName = new JLabel();
		accountName.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		accountName.setHorizontalAlignment(SwingConstants.CENTER);
		accountName.setText(user.getNickname());
		panel3.add(accountName);
		
		this.setVisible(true);
		this.setSize(1680, 955);
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("Discover Page");
		getContentPane().add(panel1);
		getContentPane().add(panel2);
		}
	/**
	 * an action listener to handle buttons
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		//button to logout from the account
		if (e.getSource() == logout) {
			user.logout();
			this.dispose();
			new LoginPage();
		//profile page button to navigate to profile page
		} else if (e.getSource() == profile) {
			this.dispose();
			new ProfilePage(user, user);
		//upload button to upload images to the application
		} else if (e.getSource() == upload) {
			JFileChooser fc = new JFileChooser();
			fc.addChoosableFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png"));
            fc.setAcceptAllFileFilterUsed(false);
			int response = fc.showOpenDialog(null);
			if (response == JFileChooser.APPROVE_OPTION) {
				try {
					//copying the image to images directory
					String filePath = fc.getSelectedFile().getAbsolutePath();
					Path source = Paths.get(filePath);
					Path destination = Paths.get(new File("res/images/").getAbsolutePath().toString() + "/" + this.user.getNickname() + this.user.count + User.getExtension(filePath));
					Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
					this.user.userImages.add(destination.toString());
					this.user.updateCount();
					//writing image's owner and path to images
					File file = new File(User.class.getResource("/data/images.txt").toURI());
					FileWriter filewriter = new FileWriter(file, true);
					BufferedWriter writer = new BufferedWriter(filewriter);
					writer.write(this.user.getNickname() + "," + destination.toString());
					writer.newLine();
					writer.close();
					this.dispose();
					new ProfilePage(this.user, this.user);
					BaseLogger.info(this.user.getNickname() + " uploaded a photo: " + destination.toString());
				}catch (IOException | URISyntaxException e2) {
					BaseLogger.error(e2.getMessage());
					}
				}
		//to navigate user profiles through the whole application
		} else if (e.getSource() == goToUser) {
			this.dispose();
			new ProfilePage((User) users.getSelectedItem(), this.user);
		}
	}	
}
