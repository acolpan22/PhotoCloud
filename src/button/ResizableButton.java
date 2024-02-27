package button;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
/**
 * a ResizableButton class to represent image thumbnails on profile page
 * @author ahmet
 *
 */
public class ResizableButton extends JButton{
	//fields
	private final ImageIcon icon;
	/**
	 * a constructor for the button
	 * @param icon
	 */
    public ResizableButton(ImageIcon icon) {
        this.icon = icon;
    }
    /**
     * using 2d graphics to draw images on buttons without getting it's resolutions going bad
     */
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
}
