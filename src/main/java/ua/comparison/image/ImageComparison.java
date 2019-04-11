package ua.comparison.image;

import ua.comparison.image.model.ComparisonResult;
import ua.comparison.image.model.Rectangle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static java.awt.Color.RED;
import static java.nio.file.Files.createTempFile;
import static java.util.Optional.ofNullable;
import static ua.comparison.image.ImageComparisonTools.createRectangle;
import static ua.comparison.image.ImageComparisonTools.deepCopy;
import static ua.comparison.image.ImageComparisonTools.hasNotSameImageSize;
import static ua.comparison.image.ImageComparisonTools.populateTheMatrixOfTheDifferences;
import static ua.comparison.image.ImageComparisonTools.readImageFromResources;
import static ua.comparison.image.ImageComparisonTools.saveImage;
import static ua.comparison.image.model.Rectangle.defaultRectangle;

public class ImageComparison {

	/**
	 * Prefix of the name of the result image.
	 */
	private static final String NAME_PREFIX = "image-comparison";
	/**
	 * Suffix of the name of of the result image.
	 */
	private static final String NAME_SUFFIX = ".png";
	/**
	 * The threshold which means the max distance between non-equal pixels.
	 * Could be changed according size and requirements to the image.
	 */
	public static int threshold = 5;

	/**
	 * The minimum area size of a difference rectangle.
	 */
	public static int minimumRectangleSize = 1;

	/**
	 * The number which marks how many rectangles. Beginning from 2.
	 */
	private int counter = 2;
	/**
	 * The number of the marking specific rectangle.
	 */
	private int regionCount = counter;
	/**
	 * First image for comparing
	 */
	private final BufferedImage image1;
	/**
	 * Second image for comparing
	 */
	private final BufferedImage image2;
	private int[][] matrix;
	private ComparisonResult comparisonResult;

	ImageComparison(String image1, String image2) throws IOException, URISyntaxException {
		this(readImageFromResources(image1), readImageFromResources(image2));
	}

	/**
	 * Create a new instance of {@link ImageComparison} that can compare the given images.
	 *
	 * @param image1
	 * 		first image to be compared
	 * @param image2
	 * 		second image to be compared
	 */
	public ImageComparison(BufferedImage image1, BufferedImage image2) {
		this.image1 = image1;
		this.image2 = image2;
	}

	/**
	 * Calculate regions which cover the difference pixels.
	 *
	 * @return the result od the comparison
	 */
	public ComparisonResult compareImages() {

		if (hasNotSameImageSize(image1, image2)) {
			comparisonResult = ComparisonResult.sizeMissmatch();
			return comparisonResult;
		}

		matrix = populateTheMatrixOfTheDifferences(image1, image2);

		List<Rectangle> rectangles = new ArrayList<>();

		groupRegions();

		while (counter <= regionCount) {
			Rectangle rectangle = createRectangle(matrix, counter);
			if (!rectangle.equals(defaultRectangle) && rectangle.getSize() >= minimumRectangleSize) {
				rectangles.add(rectangle);
			}
			counter++;
		}
		comparisonResult = new ComparisonResult(rectangles);
		return comparisonResult;
	}

	public BufferedImage getImage1() {
		return image1;
	}

	public BufferedImage getImage2() {
		return image2;
	}

	/**
	 * @return the result of the drawing.
	 */
	public BufferedImage getImageComparison() {
		return getImageComparison(Integer.MAX_VALUE);
	}

	/**
	 * @param maximalDifferences
	 * 		maximal differences which should be drawn - Beginning with the biggest.
	 * @return the result of the drawing.
	 */
	public BufferedImage getImageComparison(int maximalDifferences) {
		BufferedImage outImg = deepCopy(image2);
		Graphics2D graphics = outImg.createGraphics();
		graphics.setColor(RED);
		getComparisonResult().getMaxDifferences(maximalDifferences)
							 .forEach(rectangle -> graphics.drawRect(rectangle.getMinY(), rectangle.getMinX(), rectangle.getWidth(), rectangle.getHeight()));
		return outImg;
	}

	/**
	 * Write an image which visualizes the differences between the two images into to provided file.
	 *
	 * @param maximalDifferences
	 * 		maximal differences which should be drawn - Beginning with the biggest. This limit helps to speedup the drawing process.
	 * @return the result of the drawing.
	 * @see #writeImageComparison(File, int)
	 */
	public BufferedImage writeImageComparison(File destination, int maximalDifferences) throws IOException {
		BufferedImage outImg = getImageComparison(maximalDifferences);
		//save the image:
		saveImage(ofNullable(destination).orElse(createTempFile(NAME_PREFIX, NAME_SUFFIX).toFile()), outImg);
		return outImg;
	}

	/**
	 * Write an image which visualizes the differences between the two images. It will be saved to the temp folder.
	 *
	 * @return the result of the drawing.
	 * @see #writeImageComparison(File)
	 */
	public BufferedImage writeImageComparison() throws IOException {
		return writeImageComparison(null);
	}

	/**
	 * Write an image which visualizes the differences between the two images into to provided file.
	 *
	 * @return the result of the drawing.
	 * @see #writeImageComparison(File, int)
	 */
	public BufferedImage writeImageComparison(File destination) throws IOException {
		return writeImageComparison(destination, Integer.MAX_VALUE);
	}

	private ComparisonResult getComparisonResult() {
		if (comparisonResult == null) {
			comparisonResult = compareImages();
		}
		return comparisonResult;
	}

	/**
	 * Group rectangle regions in matrix.
	 */
	private void groupRegions() {
		for (int row = 0; row < matrix.length; row++) {
			for (int col = 0; col < matrix[row].length; col++) {
				if (matrix[row][col] == 1) {
					joinToRegion(row, col);
					regionCount++;
				}
			}
		}
	}

	/**
	 * The recursive method which go to all directions and finds difference
	 * in binary matrix using {@code threshold} for setting max distance between values which equal "1".
	 * and set the {@code groupCount} to matrix.
	 *
	 * @param row
	 * 		the value of the row.
	 * @param col
	 * 		the value of the column.
	 */
	private void joinToRegion(int row, int col) {
		if (row < 0 || row >= matrix.length || col < 0 || col >= matrix[row].length || matrix[row][col] != 1) {
			return;
		}

		matrix[row][col] = regionCount;

		for (int i = 0; i < threshold; i++) {
			joinToRegion(row + 1 + i, col);
			joinToRegion(row, col + 1 + i);

			joinToRegion(row + 1 + i, col - 1 - i);
			joinToRegion(row - 1 - i, col + 1 + i);
			joinToRegion(row + 1 + i, col + 1 + i);
		}
	}
}
