package ua.comparison.image;

import ua.comparison.image.model.ImageSize;
import ua.comparison.image.model.Rectangle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Tools for the {@link ImageComparison} object.
 */
public class ImageComparisonTools {

	/**
	 * Create a {@link Rectangle} object.
	 *
	 * @param matrix
	 * 		the matrix of the Conformity pixels.
	 * @param counter
	 * 		the number from marks regions.
	 * @return the {@link Rectangle} object.
	 */
	public static Rectangle createRectangle(int[][] matrix, int counter) {
		Rectangle rectangle = Rectangle.createDefault();

		//todo refactor it to make it faster.
		for (int y = 0; y < matrix.length; y++) {
			for (int x = 0; x < matrix[0].length; x++) {
				if (matrix[y][x] == counter) {
					if (x < rectangle.getMinX()) {
						rectangle.setMinX(x);
					}
					if (x > rectangle.getMaxX()) {
						rectangle.setMaxX(x);
					}

					if (y < rectangle.getMinY()) {
						rectangle.setMinY(y);
					}
					if (y > rectangle.getMaxY()) {
						rectangle.setMaxY(y);
					}
				}
			}
		}
		return rectangle;
	}

	/**
	 * Check images for equals their widths and heights.
	 *
	 * @param image1
	 *        {@link BufferedImage} object of the first image.
	 * @param image2
	 *        {@link BufferedImage} object of the second image.
	 */
	public static boolean hasSameImageSize(BufferedImage image1, BufferedImage image2) {
		return getSize(image1).equals(getSize(image2));
	}

	/**
	 * Say if the two pixels equal or not. The rule is the difference between two pixels
	 * need to be more then 10%.
	 *
	 * @param rgb1
	 * 		the RGB value of the Pixel of the Image1.
	 * @param rgb2
	 * 		the RGB value of the Pixel of the Image2.
	 * @return {@code true} if they' are difference, {@code false} otherwise.
	 */
	public static boolean isDifferent(int rgb1, int rgb2) {
		int red1 = (rgb1 >> 16) & 0xff;
		int green1 = (rgb1 >> 8) & 0xff;
		int blue1 = (rgb1) & 0xff;
		int red2 = (rgb2 >> 16) & 0xff;
		int green2 = (rgb2 >> 8) & 0xff;
		int blue2 = (rgb2) & 0xff;
		double result = Math.sqrt(Math.pow(red2 - red1, 2) + Math.pow(green2 - green1, 2) + Math.pow(blue2 - blue1, 2)) / Math.sqrt(Math.pow(255, 2) * 3);
		return result > 0.1;
	}

	/**
	 * Read image from the provided path.
	 *
	 * @param path
	 * 		the path where contains image.
	 * @return the {@link BufferedImage} object of this specific image.
	 */
	public static BufferedImage readImageFromResources(String path) throws IOException, URISyntaxException {
		return ImageIO.read(new File(ImageComparison.class.getClassLoader().getResource(path).toURI().getPath()));
	}

	/**
	 * Save image to the provided path.
	 *
	 * @param path
	 * 		the path to the saving image.
	 * @param image
	 * 		the {@link BufferedImage} object of this specific image.
	 */
	public static void saveImage(File path, BufferedImage image) throws IOException {
		File dir = path.getParentFile();
		// make dir if it's not using from Gradle.
		boolean dirExists = dir == null || dir.isDirectory() || dir.mkdirs();
		if (!dirExists) {
			throw new RuntimeException("Unable to create directory " + dir);
		}
		ImageIO.write(image, "png", path);
	}

	/**
	 * Make a copy of the {@link BufferedImage} object.
	 *
	 * @param image
	 * 		the provided image.
	 * @return copy of the provided image.
	 */
	static BufferedImage deepCopy(BufferedImage image) {
		ColorModel cm = image.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = image.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	/**
	 * @return the size of the provided Image
	 */
	static ImageSize getSize(BufferedImage image) {
		return new ImageSize(image.getWidth(), image.getHeight());
	}

	/**
	 * Populate binary matrix by "0" and "1". If the pixels are difference set it as "1", otherwise "0".
	 *
	 * @param image1
	 *        {@link BufferedImage} object of the first image.
	 * @param image2
	 *        {@link BufferedImage} object of the second image.
	 * @return populated binary matrix.
	 */
	static int[][] populateTheMatrixOfTheDifferences(BufferedImage image1, BufferedImage image2) {
		int[][] matrix = new int[image1.getWidth()][image1.getHeight()];
		for (int y = 0; y < image1.getHeight(); y++) {
			for (int x = 0; x < image1.getWidth(); x++) {
				matrix[x][y] = isDifferent(image1.getRGB(x, y), image2.getRGB(x, y)) ? 1 : 0;
			}
		}
		return matrix;
	}
}
