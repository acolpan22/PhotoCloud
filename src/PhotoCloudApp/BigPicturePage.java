package PhotoCloudApp;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Users.User;

/**
 * a BigPicturePage to show full size images
 * @author ahmet
 *
 */
public class BigPicturePage extends JFrame implements ActionListener{
	//fields
	private String filePath;
	
	private JPanel panel;
	
	private JButton button;
	
	/**
	 * a constructor for the page
	 * @param filePath the image's path
	 */
	public BigPicturePage(String filePath){
		this.filePath = filePath;
		panel = new JPanel() {
            //using 2d graphics to draw images on buttons without getting it's resolutions going bad
			@Override
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
        panel.setPreferredSize(new Dimension(1000, 800));
        
        button = new JButton("Delete this photo");
        button.setBounds(450, 25, 100, 50);
        button.addActionListener(this);
		
		this.setVisible(true);
		this.setSize(1000, 900);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setAlwaysOnTop(true);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setLayout(new FlowLayout());
		this.add(panel);
		this.add(button);
	}
	/**
	 * an action listener to handle button
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		//delete button to delete image from everywhere
		if (e.getSource() == button) {
			User user = User.getUserByPhoto(filePath);
			User.removePhoto(filePath);
			Window[] windows = getWindows();
			for (Window i: windows) {
				i.dispose();
			}
			new ProfilePage(user, user);
		}
		
	}
}
