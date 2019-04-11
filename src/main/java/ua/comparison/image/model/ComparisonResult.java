package ua.comparison.image.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class ComparisonResult {

	public final CheckResult checkResult;
	private final List<Rectangle> differences;

	public enum CheckResult {
		ImageSizeMissmatch,
		Match,
		ImageMissmatch
	}

	private ComparisonResult(CheckResult checkResult, List<Rectangle> differences) {
		this.checkResult = checkResult;
		this.differences = differences;
	}

	public ComparisonResult(List<Rectangle> differences) {
		this(differences.isEmpty() ? CheckResult.Match : CheckResult.ImageMissmatch, differences);
	}

	public static ComparisonResult sizeMissmatch() {
		return new ComparisonResult(CheckResult.ImageSizeMissmatch, new ArrayList<>());
	}

	public CheckResult getCheckResult() {
		return checkResult;
	}

	public List<Rectangle> getDifferences() {
		return differences;
	}

	/**
	 * @return sorted list differences, starting with the biggest.
	 */
	public Optional<Rectangle> getMaxDifference() {
		return getSortedDifferences().findFirst();
	}

	public int getMaxDifferenceSize() {
		return getMaxDifference().map(Rectangle::getSize).orElse(0);
	}

	/**
	 * @return sorted list differences, starting with the biggest.
	 */
	public List<Rectangle> getMaxDifferences(int limit) {
		return getSortedDifferences().limit(limit).collect(toList());
	}

	public boolean hasDifferences() {
		return !differences.isEmpty();
	}

	private Stream<Rectangle> getSortedDifferences() {
		return differences.stream().sorted();
	}

	@Override
	public String toString() {
		return "ComparisonResult{" + "checkResult=" + checkResult + ", differences=" + differences.size() + '}';
	}
}
