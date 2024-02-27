package Users;

import PhotoCloudApp.BaseLogger;
import editing.ImageMatrix;
import exception.IncorrectInputException;
import exception.InvalidUserException;

/**
 * FreeUser class to represent free users
 * @author ahmet
 *
 */
public class FreeUser extends User{
	/**
	 * constructor for a free tier user 
	 * sets userType to free
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
	public FreeUser(String nickname, String password, String name, String surname, String age, String email, String profilePhotoPath, boolean isAdmin, int count) throws InvalidUserException, IncorrectInputException {
		super(nickname, password, name, surname, age, email, profilePhotoPath, isAdmin, count);
		this.userType = UserTypes.Free;
	}
	/**
	 * login method for free user from the UserInterface
	 */
	@Override
	public void login() {
		BaseLogger.info("Free user " + this.getNickname() + " logged in");
	}
	/**
	 * logout method for free user from the UserInterface
	 */
	@Override
	public void logout() {
		BaseLogger.info("Free user " + this.getNickname() + " logged out");
	}
	/**
	 * created method for free user from the UserInterface
	 */
	@Override
	public void created() {
		BaseLogger.info("Free user " + this.getNickname() + " created");
	}
	/**
	 * a blurFilter method to apply blur filter on image matrices
	 * it gets all of the pixels one by one and sums up an average value for its adjacent RGB values
	 * then sets those average values to pixel's RGB values
	 * @param imageMatrix a matrix of our image to change its pixels
	 * @param kernelSize it is the degree of how much we are going to change things in pixel values
	 * @return a matrix of the blurred image
	 */
	public static ImageMatrix blurFilter(ImageMatrix imageMatrix, int kernelSize) {
        int width = imageMatrix.getWidth();
        int height = imageMatrix.getHeight();
        ImageMatrix blurredImage = new ImageMatrix(width, height);
        int halfKernelSize = kernelSize / 2;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int avgRed = 0;
                int avgGreen = 0;
                int avgBlue = 0;
                int count = 0;
                for (int i = x - halfKernelSize; i <= x + halfKernelSize; i++) {
                    for (int j = y - halfKernelSize; j <= y + halfKernelSize; j++) {
                        if (i >= 0 && i < width && j >= 0 && j < height) {
                            avgRed += imageMatrix.getRed(i, j);
                            avgGreen += imageMatrix.getGreen(i, j);
                            avgBlue += imageMatrix.getBlue(i, j);
                            count++;
                        }
                    }
                }
                avgRed /= count;
                avgGreen /= count;
                avgBlue /= count;
                int blurredRGB = ImageMatrix.convertRGB(avgRed, avgGreen, avgBlue);
                blurredImage.setRGB(x, y, blurredRGB);
            }
        }
        return blurredImage;
    }
	/**
	 * a sharpen filter to apply sharpenning filter to image matrices
	 * it takes all of the pixels one by one and subsract its blurred value than sums up with its original value
	 * it checks if all of the RGB values are in boundaries
	 * then sets those RGB values to sharpened pixels
	 * @param imageMatrix a matrix of our image to change its pixels
	 * @param kernelSize a value to tell us how much are we going to manipulate pixels
	 * @return a matrix of the sharpened image
	 */
	public static ImageMatrix sharpenFilter(ImageMatrix imageMatrix, int kernelSize) {
	    ImageMatrix blurredImage = blurFilter(imageMatrix, kernelSize);
	    int width = imageMatrix.getWidth();
	    int height = imageMatrix.getHeight();
	    ImageMatrix sharpenedImage = new ImageMatrix(width, height);
	    for (int x = 0; x < width; x++) {
	        for (int y = 0; y < height; y++) {
	            int originalRed = imageMatrix.getRed(x, y);
	            int originalGreen = imageMatrix.getGreen(x, y);
	            int originalBlue = imageMatrix.getBlue(x, y);
	            int blurredRed = blurredImage.getRed(x, y);
	            int blurredGreen = blurredImage.getGreen(x, y);
	            int blurredBlue = blurredImage.getBlue(x, y);
	            int sharpenedRed = originalRed - blurredRed + originalRed;
	            int sharpenedGreen = originalGreen - blurredGreen + originalGreen;
	            int sharpenedBlue = originalBlue - blurredBlue + originalBlue;
	            sharpenedRed = Math.max(0, Math.min(sharpenedRed, 255));
	            sharpenedGreen = Math.max(0, Math.min(sharpenedGreen, 255));
	            sharpenedBlue = Math.max(0, Math.min(sharpenedBlue, 255));
	            int sharpenedRGB = ImageMatrix.convertRGB(sharpenedRed, sharpenedGreen, sharpenedBlue);
	            sharpenedImage.setRGB(x, y, sharpenedRGB);
	        }
	    }
	    return sharpenedImage;
	}
}
