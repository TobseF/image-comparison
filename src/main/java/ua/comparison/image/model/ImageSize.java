package ua.comparison.image.model;

import java.util.Objects;

/**
 * The size in {@link #width} and {@link #height} of an image.
 */
public class ImageSize {
	private final int height;
	private final int width;

	public ImageSize(int width, int height) {
		this.height = height;
		this.width = width;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) { return true; }
		if (o == null || getClass() != o.getClass()) { return false; }
		ImageSize imageSize = (ImageSize) o;
		return height == imageSize.height && width == imageSize.width;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	@Override
	public int hashCode() {
		return Objects.hash(height, width);
	}

	@Override
	public String toString() {
		return "ImageSize{" + height + " x " + width + '}';
	}
}
