package com.jpatterson.school.compGeo.ui;

import com.jpatterson.school.compGeo.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.DoubleToIntFunction;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

public class PointUiUtils
{
	private static final DoubleToIntFunction TRUNCATE_DOUBLE_FUNCTION = x -> (int) x;
	
	private PointUiUtils()
	{
	}

	public static Polygon createPolygon(List<Point> points)
	{
		int[] xpoints = getPointValues(points, Point::getX);
		int[] ypoints = getPointValues(points, Point::getY);
		int npoints = points.size();
		
		return new Polygon(xpoints, ypoints, npoints);
	}
	
	public static Double getMaxPointValue(Collection<Point> points, Function<Point, Double> valueExtractor)
	{
		return points.stream()
			.sorted(Comparator.comparing(valueExtractor).reversed())
			.findFirst()
			.map(valueExtractor)
			.get();
	}
	
	public static Point getPoint(MouseEvent event)
	{
		return new Point(event.getX(), event.getY());
	}
	
	private static int[] getPointValues(List<Point> points, ToDoubleFunction<Point> valueExtractor)
	{
		return points.stream()
			.mapToDouble(valueExtractor)
			.mapToInt(TRUNCATE_DOUBLE_FUNCTION)
			.toArray();
	}
}
