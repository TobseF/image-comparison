package ua.comparison.image;

import org.junit.Test;
import ua.comparison.image.model.ImageSize;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static ua.comparison.image.ImageComparisonTools.readImageFromResources;

/**
 * Unit-level testing for {@link ImageComparison} object.
 */
public class ImageComparisonUnitTest {

	private static void assertImagesEqual(BufferedImage imgA, BufferedImage imgB) {
		if (imgA.getWidth() != imgB.getWidth() || imgA.getHeight() != imgB.getHeight()) {
			fail("Images have different dimensions");
		}

		int width = imgA.getWidth();
		int height = imgA.getHeight();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (imgA.getRGB(x, y) != imgB.getRGB(x, y)) {
					fail("Images are different, found different pixel at: x = " + x + ", y = " + y);
				}
			}
		}
	}

	/**
	 * The most important test. Shown, that the changes in algorithm,
	 * don't break the main behaviour and result as expected.
	 */
	@Test
	public void testCorrectWorkingImage1Image2() throws IOException, URISyntaxException {
		//given
		BufferedImage expectedResultImage = readImageFromResources("result1.png");

		//when
		BufferedImage drawnDifferences = new ImageComparison("image1.png", "image2.png").getImageComparison();

		//then
		assertImagesEqual(expectedResultImage, drawnDifferences);
	}

	@Test
	public void testImageSize() {
		assertEquals(new ImageSize(10, 10), new ImageSize(10, 10));
		assertNotEquals(new ImageSize(10, 10), new ImageSize(20, 20));
	}

	/**
	 * Test issue #17. It was StackOverFlowError.
	 */
	@Test
	public void testIssue17() throws IOException, URISyntaxException {
		BufferedImage bufferedImage = new ImageComparison("b1#17.png", "b2#17.png").getImageComparison();
		assertNotNull(bufferedImage);
	}

	/**
	 * Test issue #21. It was StackOverFlowError.
	 */
	@Test
	public void testIssue21() throws IOException, URISyntaxException {
		BufferedImage bufferedImage = new ImageComparison("b1#21.png", "b2#21.png").getImageComparison();
		assertNotNull(bufferedImage);
	}

}
