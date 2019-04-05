package ua.comparison.image;

import org.junit.Ignore;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static ua.comparison.image.ImageComparisonTools.readImageFromResources;


/**
 * Unit-level testing for {@link ImageComparison} object.
 */
public class ImageComparisonUnitTest {

    /**
     * The most important test. Shown, that the changes in algorithm,
     * don't break the main behaviour and result as expected.
     */
    @Test
    public void testCorrectWorkingImage1Image2() throws IOException, URISyntaxException {
        //given
        BufferedImage expectedResultImage = readImageFromResources( "result1.png" );

        //when
        BufferedImage drawnDifferences = new ImageComparison( "image1.png", "image2.png" ).getImageComparison();

        //then
        assertImagesEqual(expectedResultImage, drawnDifferences);
    }

    /**
     * Ignored dou to resolving problem with overlapping.
     */
    @Ignore
    @Test
    public void testCorrectWorkingImage1Image() throws IOException, URISyntaxException {
        //given
        BufferedImage expectedResultImage = readImageFromResources( "result2.png" );

        //when
        BufferedImage drawnDifferences = new ImageComparison( "image1.png", "image3.png" ).getImageComparison();

        //then
        assertImagesEqual(expectedResultImage, drawnDifferences);
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

    /**
     * Test issue #11.
     */
    @Test
    public void testIssue11() throws IOException, URISyntaxException {
        // TODO: 3/13/2019 Implemented logic
        BufferedImage bufferedImage = new ImageComparison("image1.png", "image3.png").getImageComparison();
    }


    private static void assertImagesEqual(BufferedImage imgA, BufferedImage imgB) {
        if (imgA.getWidth() != imgB.getWidth() || imgA.getHeight() != imgB.getHeight()) {
            fail("Images have different dimensions");
        }

        int width  = imgA.getWidth();
        int height = imgA.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (imgA.getRGB(x, y) != imgB.getRGB(x, y)) {
                    fail("Images are different, found different pixel at: x = " + x + ", y = " + y);
                }
            }
        }
    }

}
