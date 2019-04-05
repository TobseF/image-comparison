package ua.comparison.image.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ComparisonResultTest {

	@Test
	public void getMaxDifferences() {
		List<Rectangle> rectangles = new ArrayList<>();
		rectangles.add(newRectangle(5));
		rectangles.add(newRectangle(2));
		rectangles.add(newRectangle(8));
		rectangles.add(newRectangle(6));
		ComparisonResult result = new ComparisonResult(rectangles);
		List<Rectangle> sortedResult = result.getMaxDifferences(4);
		List<Rectangle> sorted = new ArrayList<>();
		sorted.add(newRectangle(8));
		sorted.add(newRectangle(6));
		sorted.add(newRectangle(5));
		sorted.add(newRectangle(2));
		assertEquals(sorted, sortedResult);
		assertEquals(3, result.getMaxDifferences(3).size());
	}

	public Rectangle newRectangle(int size){
		Rectangle rectangle = new Rectangle();
		rectangle.setMaxX(size);
		rectangle.setMaxY(size);
		return rectangle;
	}
}