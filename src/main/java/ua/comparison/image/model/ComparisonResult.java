package ua.comparison.image.model;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class ComparisonResult {

	private final List<Rectangle> differences;

	public ComparisonResult(List<Rectangle> differences) {
		this.differences = differences;
	}

	public List<Rectangle> getDifferences() {
		return differences;
	}


	public boolean hasDifferences() {
		return !differences.isEmpty();
	}

	/**
	 *
	 * @return sorted list differences, starting with the biggest.
	 */
	public List<Rectangle> getMaxDifferences(int limit){
		return getSortedDifferences().limit(limit).collect(toList());
	}

	/**
	 * @return sorted list differences, starting with the biggest.
	 */
	public Optional<Rectangle> getMaxDifference(){
		return getSortedDifferences().findFirst();
	}

	private Stream<Rectangle> getSortedDifferences() {
		return differences.stream().sorted();
	}

	public int getMaxDifferenceSize(){
		return getMaxDifference().map(Rectangle::getSize).orElse(0);
	}
}
