package editing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import PhotoCloudApp.BaseLogger;
import PhotoCloudApp.Main;
import PhotoCloudApp.ProfilePage;
import Users.FreeUser;
import Users.HobbyistUser;
import Users.ProfessionalUser;
import Users.User;
import Users.UserTypes;
/**
 * a PhotoEditingPage to open a window to edit images
 * @author ahmet
 *
 */
public class PhotoEditingPage extends JFrame implements ActionListener, ChangeListener{
	//fields
	private String photoPath;
	private UserTypes userType;
	private BufferedImage photo;
	
	private JPanel image;
	private JPanel editPanel;
	
	private JComboBox blurBox;
	private JComboBox sharpenBox;
	private JSlider grayscaleSlider;
	private JCheckBox edgedetectionBox;
	private JSlider brightnessSlider;
	private JSlider contrastSlider;
	
	
	private JLabel blur;
	private JLabel sharpen;
	private JLabel grayscale;
	private JLabel grayscaleValue;
	private JLabel edgedetection;
	private JLabel brightness;
	private JLabel brightnessValue;
	private JLabel contrast;
	private JLabel contrastValue;
	
	private JButton edit;
	private JButton cancel;
	
	public PhotoEditingPage(String photoPath) {
		this.photoPath = photoPath;
		this.userType = User.getUserByPhoto(photoPath).getUserType();
		try {
			this.photo = ImageIO.read(new File(photoPath));
			BaseLogger.info(photoPath + " file has been read to edit");
		} catch (IOException e1) {
			BaseLogger.error(e1.getMessage());
		}
		
		image = new JPanel() {
            //using 2d graphics to draw the image without it's resolution going bad
			@Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    BufferedImage image = ImageIO.read(new File(photoPath));
                    
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
		image.setBounds(0, 0, 1680, 500);
		
		editPanel = new JPanel();
		editPanel.setBounds(0, 500, 1680, 455);
		editPanel.setLayout(null);
		
		blur = new JLabel("Apply blur filter:");
		editPanel.add(blur);
		
		String[] percentages = {"%0", "%20", "%40", "%60", "%80", "%100"};
		
		blurBox = new JComboBox(percentages);
		blurBox.addActionListener(this);
		editPanel.add(blurBox);
		
		sharpen = new JLabel("Apply sharpen filter:"); 
		editPanel.add(sharpen);
		
		sharpenBox = new JComboBox(percentages);
		sharpenBox.addActionListener(this);
		editPanel.add(sharpenBox);
		
		grayscale = new JLabel("Apply grayscale filter:");
		editPanel.add(grayscale);
		
		grayscaleSlider = new JSlider(0, 100, 0);
		grayscaleSlider.setPaintTrack(true);
		grayscaleSlider.setMajorTickSpacing(100);
		grayscaleSlider.setPaintLabels(true);
		grayscaleSlider.addChangeListener(this);
		editPanel.add(grayscaleSlider);
		
		grayscaleValue = new JLabel("%" + String.valueOf(grayscaleSlider.getValue()));
		editPanel.add(grayscaleValue);
		
		edgedetection = new JLabel("Do you want to apply edge detection ?");
		editPanel.add(edgedetection);
		
		edgedetectionBox = new JCheckBox();
		edgedetectionBox.addActionListener(this);
		editPanel.add(edgedetectionBox);
		
		brightness = new  JLabel("Adjust brightness:");
		editPanel.add(brightness);
		
		brightnessSlider = new JSlider(-100, 100, 0);
		brightnessSlider.setPaintTrack(true);
		brightnessSlider.setMajorTickSpacing(100);
		brightnessSlider.setPaintLabels(true);
		brightnessSlider.addChangeListener(this);
		editPanel.add(brightnessSlider);
		
		brightnessValue = new JLabel("%" + String.valueOf(brightnessSlider.getValue()));
		editPanel.add(brightnessValue);
		
		contrast = new JLabel("Adjust contrast:");
		editPanel.add(contrast);
		
		contrastSlider = new JSlider(0,100,0);
		contrastSlider.setPaintTrack(true);
		contrastSlider.setMajorTickSpacing(100);
		contrastSlider.setPaintLabels(true);
		contrastSlider.addChangeListener(this);
		editPanel.add(contrastSlider);
		
		contrastValue = new JLabel("%" + String.valueOf(contrastSlider.getValue()));
		editPanel.add(contrastValue);
		// placing filter options according to user types
		if (userType == UserTypes.Free) {
			blur.setBounds(690, 0, 200, 100);
			blurBox.setBounds(890, 25, 100, 50);
			sharpen.setBounds(690, 50, 200, 100);
			sharpenBox.setBounds(890, 75, 100, 50);
		} else if (userType == UserTypes.Hobbyist) {
			blur.setBounds(690, 0, 200, 100);
			blurBox.setBounds(890, 25, 100, 50);
			sharpen.setBounds(690, 50, 200, 100);
			sharpenBox.setBounds(890, 75, 100, 50);
			brightness.setBounds(640, 100, 200, 100);
			brightnessSlider.setBounds(800, 125, 200, 50);
			brightnessValue.setBounds(1010, 132, 50, 30);
			contrast.setBounds(640, 150, 200, 100);
			contrastSlider.setBounds(800, 175, 200, 50);
			contrastValue.setBounds(1010, 182, 50, 30);
		} else if (userType == UserTypes.Professional) {
			blur.setBounds(690, 0, 200, 100);
			blurBox.setBounds(890, 25, 100, 50);
			sharpen.setBounds(690, 50, 200, 100);
			sharpenBox.setBounds(890, 75, 100, 50);
			brightness.setBounds(640, 100, 200, 100);
			brightnessSlider.setBounds(800, 125, 200, 50);
			brightnessValue.setBounds(1010, 132, 50, 30);
			contrast.setBounds(640, 150, 200, 100);
			contrastSlider.setBounds(800, 175, 200, 50);
			contrastValue.setBounds(1010, 182, 50, 30);
			grayscale.setBounds(640, 200, 200, 100);
			grayscaleSlider.setBounds(800, 225, 200, 50);
			grayscaleValue.setBounds(1010, 235, 50, 30);
			edgedetection.setBounds(640, 250, 250, 100);
			edgedetectionBox.setBounds(940, 275, 100, 50);
		}
        
		cancel = new JButton("Cancel");
		cancel.setBounds(690, 350, 100, 60);
		cancel.addActionListener(this);
		editPanel.add(cancel);
		
		edit = new JButton("Edit");
		edit.setBounds(890, 350, 100, 60);
		edit.addActionListener(this);
		editPanel.add(edit);
		
		this.setVisible(true);
		this.setSize(1680, 955);
		this.setLocationRelativeTo(null);
		this.setAlwaysOnTop(true);
		this.setLayout(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("Edit");
		this.add(image);
		this.add(editPanel);
	}
	/**
	 * action listener to handle button clicks and more
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		//cancel button to close the window
		if (e.getSource() == cancel) {
			this.dispose();
		//edit button to retrieve information from sliders and boxex to apply filters
		} else if (e.getSource() == edit) {
			ImageMatrix matrix = new ImageMatrix(photo);
			String fileName = "";
			// creating the new file's path
			for (char i: photoPath.substring(photoPath.indexOf("/images") + 8).toCharArray()) {
				if (i == '.') {
					break;
				} else {
					fileName += i;
				}
			}
			String extension = User.getExtension(photoPath);
			String selectedBlur = (String) blurBox.getSelectedItem();
			String selectedSharpen = (String) sharpenBox.getSelectedItem();
			int grayscaleModifier = grayscaleSlider.getValue();
			boolean edgedetectionSelected = edgedetectionBox.isSelected();
			int brightnessModifier = brightnessSlider.getValue();
			int contrastModifier = (int)((double) contrastSlider.getValue() * 2.55);
			//applying filters that chosen
			if (!selectedBlur.matches("%0")) {
				int kernelSize = 0;
				switch (selectedBlur) {
					case "%20":
						kernelSize = 3;
						break;
					case "%40":
						kernelSize = 5;
						break;
					case "%60":
						kernelSize = 7;
						break;
					case "%80":
						kernelSize = 9;
						break;
					case "%100":
						kernelSize = 11;
						break;
				}
				long startTime = System.currentTimeMillis();
				matrix = FreeUser.blurFilter(matrix, kernelSize);
				long finishTime = System.currentTimeMillis();
				BaseLogger.info("Blurring filter applied to " + photoPath + " file, took: " + String.valueOf(finishTime - startTime) + "ms");
				fileName += "_blurred_" + kernelSize + "_" + kernelSize;
			}
			if (!selectedSharpen.matches("%0")) {
				int kernelSize = 0;
				switch (selectedSharpen) {
					case "%20":
						kernelSize = 3;
						break;
					case "%40":
						kernelSize = 5;
						break;
					case "%60":
						kernelSize = 7;
						break;
					case "%80":
						kernelSize = 9;
						break;
					case "%100":
						kernelSize = 11;
						break;
				}
				long startTime = System.currentTimeMillis();
				matrix = FreeUser.sharpenFilter(matrix, kernelSize);
				long finishTime = System.currentTimeMillis();
				BaseLogger.info("Sharpenning filter applied to " + photoPath + " file, took: " + String.valueOf(finishTime - startTime) + "ms");
				fileName += "_sharpened_" + kernelSize + "_" + kernelSize;
			}
			if (grayscaleModifier != 0) {
				long startTime = System.currentTimeMillis();
				matrix = ProfessionalUser.grayscaleFilter(matrix, grayscaleModifier);
				long finishTime = System.currentTimeMillis();
				BaseLogger.info("Grayscaling filter applied to " + photoPath + " file, took: " + String.valueOf(finishTime - startTime) + "ms");
				fileName += "_grayscaled_" + grayscaleModifier;
			}
			if (edgedetectionSelected) {
				long startTime = System.currentTimeMillis();
				matrix = ProfessionalUser.edgeDetectionFilter(matrix);
				long finishTime = System.currentTimeMillis();
				BaseLogger.info("Edge detection filter applied to " + photoPath + " file, took: " + String.valueOf(finishTime - startTime) + "ms");
				fileName += "_edgedetection_";
			}
			if (brightnessModifier != 0) {
				long startTime = System.currentTimeMillis();
				matrix = HobbyistUser.adjustBrightness(matrix, brightnessModifier);
				long finishTime = System.currentTimeMillis();
				BaseLogger.info("Brightness adjusted on " + photoPath + " file, took: " + String.valueOf(finishTime - startTime) + "ms");
				fileName += "_brightness_" + brightnessModifier;
			}
			if (contrastModifier != 0) {
				long startTime = System.currentTimeMillis();
				matrix = HobbyistUser.adjustContrast(matrix, contrastModifier);
				long finishTime = System.currentTimeMillis();
				BaseLogger.info("Contrast adjusted on " + photoPath + " file, took: " + String.valueOf(finishTime - startTime) + "ms");
				fileName += "_contrast_" + contrastModifier;
			}
			//if any filter is applied creating a new image
			if (!fileName.matches("")) {
				try {
					//writing the new image's owner and path to images.txt
					File file = new File(PhotoEditingPage.class.getResource("/data/images.txt").toURI());
					FileWriter filewriter = new FileWriter(file, true);
					BufferedWriter writer = new BufferedWriter(filewriter);
					String newFilePath = new File("res/images/").getAbsolutePath().toString() + "/" + fileName + extension;
					//creating a new image
					File tempFile = File.createTempFile("temp", null);
			        ImageIO.write(matrix.getBufferedImage(), "png", tempFile);
			        byte[] imageBytes = Files.readAllBytes(tempFile.toPath());
			        tempFile.delete();
					Files.write(Path.of(newFilePath), imageBytes, StandardOpenOption.CREATE);
					writer.write(User.getUserByPhoto(photoPath).getNickname() + "," + newFilePath);
					writer.newLine();
					writer.close();
					//adding image to user's images
					User.getUserByPhoto(this.photoPath).userImages.add(newFilePath);
					this.dispose();
				} catch (IOException | URISyntaxException e1) {
					BaseLogger.error(e1.getMessage());
				}
			}
			Window[] windows = getWindows();
			for (Window i: windows) {
				i.dispose();
			}
			new ProfilePage(User.getUserByPhoto(photoPath), User.getUserByPhoto(photoPath));
		}
		
	}
	/**
	 * change listener to show slider inputs on the screen when it is moved
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == grayscaleSlider) {
			grayscaleValue.setText("%" + String.valueOf(grayscaleSlider.getValue()));
		} else if (e.getSource() == brightnessSlider) {
			brightnessValue.setText("%" + String.valueOf(brightnessSlider.getValue()));
		} else if (e.getSource() == contrastSlider) {
			contrastValue.setText("%" + String.valueOf(contrastSlider.getValue()));
		}
	}
}
