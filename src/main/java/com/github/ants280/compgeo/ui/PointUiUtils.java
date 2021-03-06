package com.github.ants280.compgeo.ui;

import com.github.ants280.compgeo.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;
import java.util.function.DoubleToIntFunction;
import java.util.function.ToDoubleFunction;

public class PointUiUtils
{
	private static final DoubleToIntFunction TRUNCATE_DOUBLE_FUNCTION
			= x -> (int) x;

	private PointUiUtils()
	{
	}

	public static Polygon createPolygon(List<Point> points)
	{
		int[] xPoints = getPointValues(points, Point::getX);
		int[] yPoints = getPointValues(points, Point::getY);
		int nPoints = points.size();

		return new Polygon(xPoints, yPoints, nPoints);
	}

	public static double getMaxPointValue(
			Collection<Point> points,
			ToDoubleFunction<Point> valueExtractor)
	{
		return points.stream()
				.mapToDouble(valueExtractor)
				.max()
				.orElseThrow(IllegalArgumentException::new);
	}

	public static Point getPoint(MouseEvent event)
	{
		return new Point(event.getX(), event.getY());
	}

	private static int[] getPointValues(
			List<Point> points,
			ToDoubleFunction<Point> valueExtractor)
	{
		return points.stream()
				.mapToDouble(valueExtractor)
				.mapToInt(TRUNCATE_DOUBLE_FUNCTION)
				.toArray();
	}
}
