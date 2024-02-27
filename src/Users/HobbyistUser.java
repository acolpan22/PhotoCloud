package Users;

import PhotoCloudApp.BaseLogger;
import editing.ImageMatrix;
import exception.IncorrectInputException;
import exception.InvalidUserException;
/**
 * a HobbyistUser class to represent Hobbyist users
 * @author ahmet
 *
 */
public class HobbyistUser extends FreeUser{
	/**
	 * a constructor for a Hobbyist tier user
	 * sets userType to hobbyist
	 * and if parameters are not in a way we wanted throws exceptions
	 * @param nickname
	 * @param password
	 * @param name
	 * @param surname
	 * @param age
	 * @param email
	 * @param profilePhotoPath
	 * @param isAdmin
	 * @param count
	 * @throws InvalidUserException
	 * @throws IncorrectInputException
	 */
	public HobbyistUser(String nickname, String password, String name, String surname, String age, String email, String profilePhotoPath, boolean isAdmin, int count) throws InvalidUserException, IncorrectInputException {
		super(nickname, password, name, surname, age, email, profilePhotoPath, isAdmin, count);
		this.userType = UserTypes.Hobbyist;
	}
	/**
	 * login method for hobbyist user from UserInterface
	 */
	@Override
	public void login() {
		BaseLogger.info("Hobbyist user " + this.getNickname() + " logged in");
	}
	/**
	 * logout method for hobbyist user from UserInterface
	 */
	@Override
	public void logout() {
		BaseLogger.info("Hobbyist user " + this.getNickname() + " logged out");
	}
	/**
	 * created method for hobbyist user from UserInterface
	 */
	@Override
	public void created() {
		BaseLogger.info("Hobbyist user " + this.getNickname() + " created");
	}
	/**
	 * a adjustBrightness method to adjust the brightness of an image
	 * first we take an pixel from the image matrix one by one
	 * and then we add our modifier to each RGB value to increase or decrease the brightness
	 * the modifier can be between -255 to 255
	 * than we take those values and set them to our adjusted matrix
	 * @param imageMatrix an image matrix to represent our image as a pixel matrix
	 * @param brightnessModifier a modifier to adjust how much are we going to change pixel RGB values
	 * @return image matrix with an adjusted brightness
	 */
	public static ImageMatrix adjustBrightness(ImageMatrix imageMatrix, int brightnessModifier) {
	    int width = imageMatrix.getWidth();
	    int height = imageMatrix.getHeight();
	    ImageMatrix adjustedImage = new ImageMatrix(width, height);
	    for (int x = 0; x < width; x++) {
	        for (int y = 0; y < height; y++) {
	            int originalRed = imageMatrix.getRed(x, y);
	            int originalGreen = imageMatrix.getGreen(x, y);
	            int originalBlue = imageMatrix.getBlue(x, y);
	            int adjustedRed = Math.max(0, Math.min(originalRed + brightnessModifier, 255));
	            int adjustedGreen = Math.max(0, Math.min(originalGreen + brightnessModifier, 255));
	            int adjustedBlue = Math.max(0, Math.min(originalBlue + brightnessModifier, 255));
	            int adjustedRGB = ImageMatrix.convertRGB(adjustedRed, adjustedGreen, adjustedBlue);
	            adjustedImage.setRGB(x, y, adjustedRGB);
	        }
	    }
	    return adjustedImage;
	}
	/**
	 * a adjustContrast method to adjust contrast of images
	 * we have a formula to apply this contrast filter
	 * we find the factor and apply those factors according to formula i found online
	 * and then we set those adjusted RGB to each pixel one by one
	 * @param imageMatrix a image matrix to adjust it's contrast
	 * @param contrastModifier a contrast modifier that is between 0-100 to tell us how much we are going to change the image
	 * @return the adjuste image matrix
	 */
	public static ImageMatrix adjustContrast(ImageMatrix imageMatrix, int contrastModifier) {
	    double factor = (259 * (contrastModifier + 255)) / (255 * (259 - contrastModifier));
	    int width = imageMatrix.getWidth();
	    int height = imageMatrix.getHeight();
	    ImageMatrix adjustedImage = new ImageMatrix(width, height);
	    for (int x = 0; x < width; x++) {
	        for (int y = 0; y < height; y++) {
	            int red = imageMatrix.getRed(x, y);
	            int green = imageMatrix.getGreen(x, y);
	            int blue = imageMatrix.getBlue(x, y);
	            int adjustedRed = (int) (factor * (red - 128) + 128);
	            int adjustedGreen = (int) (factor * (green - 128) + 128);
	            int adjustedBlue = (int) (factor * (blue - 128) + 128);
	            adjustedRed = Math.max(0, Math.min(adjustedRed, 255));
	            adjustedGreen = Math.max(0, Math.min(adjustedGreen, 255));
	            adjustedBlue = Math.max(0, Math.min(adjustedBlue, 255));
	            int adjustedRGB = ImageMatrix.convertRGB(adjustedRed, adjustedGreen, adjustedBlue);
	            adjustedImage.setRGB(x, y, adjustedRGB);
	        }
	    }
	    return adjustedImage;
	}
}
