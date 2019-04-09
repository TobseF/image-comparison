package ua.comparison.image;

import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static ua.comparison.image.ImageComparisonTools.readImageFromResources;

/**
 * Unit-level testing for {@link ImageComparisonTools} object.
 */
public class ImageComparisonToolsUnitTest {

	@Test
	public void testCheckCorrectImageSize() {
		BufferedImage image1 = new BufferedImage(10, 10, 10);
		BufferedImage image2 = new BufferedImage(12, 12, 10);
		assertFalse(ImageComparisonTools.hasSameImageSize(image1, image2));
	}

	@Test
	public void testSaveImage() throws IOException, URISyntaxException {
		BufferedImage image = readImageFromResources("result1.png");
		String path = "build/test/correct/save/image.png";
		ImageComparisonTools.saveImage(new File(path), image);
		assertTrue(new File(path).exists());
	}
}
