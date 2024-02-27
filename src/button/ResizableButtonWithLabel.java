package button;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
/**
 * a ResizableButtonWithLabel class to show image thumbnails and user info on discover page
 * @author ahmet
 *
 */
public class ResizableButtonWithLabel extends JPanel {
    //fields
	private final JButton button;
    private final JLabel label;
    
    /**
     * a constructor to create resizable button
     * @param icon
     * @param labelText
     */
    public ResizableButtonWithLabel(ImageIcon icon, String labelText) {
        setLayout(new BorderLayout());

        button = new JButton() {
            //using 2d graphics to draw images on buttons without getting it's resolutions going bad
        	@Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int width = getWidth();
                int height = getHeight();
                int iconWidth = icon.getIconWidth();
                int iconHeight = icon.getIconHeight();
                double widthRatio = (double) width / iconWidth;
                double heightRatio = (double) height / iconHeight;
                double scaleFactor = Math.min(widthRatio, heightRatio);
                int scaledWidth = (int) (iconWidth * scaleFactor);
                int scaledHeight = (int) (iconHeight * scaleFactor);
                int x = (width - scaledWidth) / 2;
                int y = (height - scaledHeight) / 2;
                Image image = icon.getImage();
                g.drawImage(image, x, y, scaledWidth, scaledHeight, this);
            }
        };
        label = new JLabel(labelText, SwingConstants.CENTER);
        label.setFont(new Font("Lucida Grande", Font.PLAIN, 25));
        
        add(button, BorderLayout.CENTER);
        add(label, BorderLayout.NORTH);
    }
    /**
     * to adjust button size according to their components width and height
     */
    @Override
    public Dimension getPreferredSize() {
        Dimension buttonSize = button.getPreferredSize();
        Dimension labelSize = label.getPreferredSize();

        int width = Math.max(buttonSize.width, labelSize.width);
        int height = buttonSize.height + labelSize.height;

        return new Dimension(width, height);
    }
    /**
     * a getter method to retrieve the button
     * @return button
     */
    public JButton getButton() {
    	return button;
    }
}

