package com.github.ants280.compGeo.algorithm;

import com.github.ants280.compGeo.CompGeoUtils;
import com.github.ants280.compGeo.Point;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

public class GrahamScan
{
	private final Collection<Point> points;

	public GrahamScan(Collection<Point> points)
	{
		this.points = points;
	}

	public List<Point> getConvexHullPoints()
	{
		if (points.isEmpty())
		{
			return Collections.emptyList();
		}

		Map<Double, List<Point>> ccwPoints = getCcwPoints();
		List<List<Point>> sortedCcwPoints = getSortedCcwPoints(ccwPoints);
		List<Point> exteriorCcwPoints = getExteriorCcwPoints(sortedCcwPoints);
		return getNecessaryConvexHullPoints(exteriorCcwPoints);
	}

	private Map<Double, List<Point>> getCcwPoints()
	{
		Point lowestPoint = getLowestPoint();
		Point rightOfLowestPoint = new Point(lowestPoint.getX() + 1, lowestPoint.getY());
		Function<Point, Double> getAngleToLowestPoint
				= point -> CompGeoUtils.getAngle(rightOfLowestPoint, lowestPoint, point);

		return points.stream()
				.collect(Collectors.groupingBy(getAngleToLowestPoint));
	}

	private List<List<Point>> getSortedCcwPoints(Map<Double, List<Point>> ccwPoints)
	{
		return ccwPoints.entrySet()
				.stream()
				.sorted(Comparator.comparing(Map.Entry::getKey))
				.map(Map.Entry::getValue)
				.collect(Collectors.toList());
	}

	private List<Point> getExteriorCcwPoints(List<List<Point>> sortedCcwPoints)
	{
		Point lowestPoint = getLowestPoint();
		Function<List<Point>, Point> getFarthestPointAtAngle
				= pointsAtAngle -> getFarthestPoint(pointsAtAngle, lowestPoint);

		return sortedCcwPoints.stream()
				.map(getFarthestPointAtAngle)
				.collect(Collectors.toList());
	}

	private static List<Point> getNecessaryConvexHullPoints(List<Point> ccwExteriorPoints)
	{
		if (ccwExteriorPoints.size() <= 3)
		{
			return ccwExteriorPoints;
		}
		else
		{
			LinkedList<Point> necessaryConvexHullPoints = new LinkedList<>(ccwExteriorPoints.subList(0, 3));
			for (int i = 3; i < ccwExteriorPoints.size(); i++)
			{
				while (isInteriorPoint(ccwExteriorPoints.get(i), necessaryConvexHullPoints))
				{
					necessaryConvexHullPoints.removeLast();
				}
				necessaryConvexHullPoints.add(ccwExteriorPoints.get(i));
			}
			return necessaryConvexHullPoints;
		}
	}

	private Point getLowestPoint()
	{
		return points.stream()
				.min(Point::compareTo)
				.orElse(null);
	}

	private static Point getFarthestPoint(List<Point> pointsAtAngle, Point sourcePoint)
	{
		ToDoubleFunction<Point> getDistanceToSourcePoint
				= point -> CompGeoUtils.getDistance(point, sourcePoint);

		return pointsAtAngle.stream()
				.max(Comparator.comparingDouble(getDistanceToSourcePoint))
				.orElse(null);
	}

	/**
	 * Returns whether or not nextPoint is inside of rectangle formed by
	 * necessaryPoints.
	 *
	 * @param point The point to test
	 * @param necessaryPoints The points which have been determined to make the
	 * convex hull.
	 * @return Whether or not nextPoint is inside of rectangle formed by
	 * necessaryPoints.
	 */
	private static boolean isInteriorPoint(Point point, List<Point> necessaryPoints)
	{
		Point secondToLastPoint = necessaryPoints.get(necessaryPoints.size() - 2);
		Point lastPoint = necessaryPoints.get(necessaryPoints.size() - 1);

		return CompGeoUtils.getDeterminant(secondToLastPoint, lastPoint, point) >= 0;
	}

	@Override
	public String toString()
	{
		return String.format("GrahamScan{points=%s}", points);
	}
}
