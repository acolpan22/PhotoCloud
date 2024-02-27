package Users;

import PhotoCloudApp.BaseLogger;
import editing.ImageMatrix;
import exception.IncorrectInputException;
import exception.InvalidUserException;
/**
 * a ProfessionalUser class to represent professional tier users
 * @author ahmet
 *
 */
public class ProfessionalUser extends HobbyistUser{
	/**
	 * a contstructor for our professional user
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
	public ProfessionalUser(String nickname, String password, String name, String surname, String age, String email, String profilePhotoPath, boolean isAdmin, int count) throws InvalidUserException, IncorrectInputException {
		super(nickname, password, name, surname, age, email, profilePhotoPath, isAdmin, count);
		this.userType = UserTypes.Professional;
	}
	/**
	 * login method for professional user from UserInterface
	 */
	@Override
	public void login() {
		BaseLogger.info("Professional user " + this.getNickname() + " logged in");
	}
	/**
	 * logout method for professional user from UserInterface
	 */
	@Override
	public void logout() {
		BaseLogger.info("Professional user " + this.getNickname() + " logged out");
	}
	/**
	 * created method for professional user from UserInterface
	 */
	@Override
	public void created() {
		BaseLogger.info("Professional user " + this.getNickname() + " created");
	}
	/**
	 * a grayscaleFilter method to apply grayscale filter to the images
	 * first we take the modifier and divide it by 100 to change the RGB values correctly
	 * and then we take all the pixels one by one and multiply it by our modifier and set all of the RGB to the same value
	 * if the modifier is 100, the image is fully grayscaled
	 * if it is not then it is less grayscaled
	 * then we set the grayscale value to all of the pixels
	 * @param imageMatrix an image matrix to apply grayscale filter
	 * @param intensityModifier a modifier to tell us how much do we need to grayscale the image
	 * @return the grayscaled image matrix
	 */
	public static ImageMatrix grayscaleFilter(ImageMatrix imageMatrix, int intensityModifier) {
	    int width = imageMatrix.getWidth();
	    int height = imageMatrix.getHeight();
	    double modifier = (double) intensityModifier;
	    modifier = modifier / 100;
	    ImageMatrix grayscaleImage = new ImageMatrix(width, height);
	    for (int x = 0; x < width; x++) {
	        for (int y = 0; y < height; y++) {
	            int originalRed = imageMatrix.getRed(x, y);
	            int originalGreen = imageMatrix.getGreen(x, y);
	            int originalBlue = imageMatrix.getBlue(x, y);
	            int grayscaleValue = (int) ((modifier * originalRed) + (modifier * originalGreen) + (modifier * originalBlue)) / 3;
	            int grayscaleRGB = ImageMatrix.convertRGB(grayscaleValue, grayscaleValue, grayscaleValue);
	            grayscaleImage.setRGB(x, y, grayscaleRGB);
	        }
	    }
	    return grayscaleImage;
	}
	/**
	 * an edgeDetectionFilter to apply edge detection filter
	 * this filter grayscales and blurs the image first to get a more realistic result
	 * we have pewitt horizontal and vertical operator
	 * it iterates over pixels excluding the border pixels
	 * it initializes the variables to store the sum of products
	 * applies convolution by multiplying the values of the horizontalOperator and verticalOperator matrices to the adjacent pixels
	 * and adds to the sum variables
	 * after that it calculates the gradient values by using square root of the sum of squares for the vertical and horizontal gradients
	 * then sets those values to our filtered pixels
	 * @param imageMatrix an image matrix to apply edge detection filter
	 * @return an image matrix that applied edge detection filter
	 */
	public static ImageMatrix edgeDetectionFilter(ImageMatrix imageMatrix) {
	    ImageMatrix grayscaleImage = grayscaleFilter(imageMatrix, 100);
	    ImageMatrix blurredImage = FreeUser.blurFilter(grayscaleImage, 3);
	    int width = imageMatrix.getWidth();
	    int height = imageMatrix.getHeight();
	    ImageMatrix edgeImage = new ImageMatrix(width, height);
	    
	    int[][] horizontalOperator = {
	            {-1, 0, 1},
	            {-1, 0, 1},
	            {-1, 0, 1}
	    };

	    int[][] verticalOperator = {
	            {-1, -1, -1},
	            {0, 0, 0},
	            {1, 1, 1}
	    };

	    for (int x = 1; x < width - 1; x++) {
	        for (int y = 1; y < height - 1; y++) {
	            int gxRed = 0;
	            int gxGreen = 0;
	            int gxBlue = 0;
	            int gyRed = 0;
	            int gyGreen = 0;
	            int gyBlue = 0;
	            for (int i = -1; i <= 1; i++) {
	                for (int j = -1; j <= 1; j++) {
	                    int neighborRed = blurredImage.getRed(x + i, y + j);
	                    int neighborGreen = blurredImage.getGreen(x + i, y + j);
	                    int neighborBlue = blurredImage.getBlue(x + i, y + j);
	                    gxRed += horizontalOperator[i + 1][j + 1] * neighborRed;
	                    gxGreen += horizontalOperator[i + 1][j + 1] * neighborGreen;
	                    gxBlue += horizontalOperator[i + 1][j + 1] * neighborBlue;
	                    gyRed += verticalOperator[i + 1][j + 1] * neighborRed;
	                    gyGreen += verticalOperator[i + 1][j + 1] * neighborGreen;
	                    gyBlue += verticalOperator[i + 1][j + 1] * neighborBlue;
	                }
	            }
	            int gradientRed = (int) Math.sqrt(gxRed * gxRed + gyRed * gyRed);
	            int gradientGreen = (int) Math.sqrt(gxGreen * gxGreen + gyGreen * gyGreen);
	            int gradientBlue = (int) Math.sqrt(gxBlue * gxBlue + gyBlue * gyBlue);
	            int edgeRGB = ImageMatrix.convertRGB(gradientRed, gradientGreen, gradientBlue);
	            edgeImage.setRGB(x, y, edgeRGB);
	        }
	    }
	    return edgeImage;
	}
}
