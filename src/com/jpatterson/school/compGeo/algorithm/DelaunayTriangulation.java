package com.jpatterson.school.compGeo.algorithm;

import com.jpatterson.school.compGeo.Point;
import com.jpatterson.school.compGeo.shape.Triangle;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DelaunayTriangulation
{
	private final Set<Point> points;
	private final List<Triangle> triangulationTriangles;
	private final int maxX;
	private final int maxY;
	private final Point p1;
	private final Point p2;
	private final Point p3;

	public DelaunayTriangulation(int maxX, int maxY)
	{
		this(Collections.emptyList(), maxX, maxY);
	}

	public DelaunayTriangulation(List<Point> points, int maxX, int maxY)
	{
		if (maxX < 0 || maxX > Integer.MAX_VALUE / 2 || maxY < 0 || maxY > Integer.MAX_VALUE / 2)
		{
			throw new IllegalArgumentException(String.format("Invalid max x/y: [%d,%d]", maxX, maxY));
		}
		this.maxX = maxX;
		this.maxY = maxY;
		this.p1 = new Point(-1, -1);
		this.p2 = new Point(maxX * 2, -1);
		this.p3 = new Point(-1, maxY * 2);
		this.points = new HashSet<>();
		Triangle initialTriangle = new Triangle(p1, p2, p3);
		this.triangulationTriangles = new LinkedList<>(Collections.singletonList(initialTriangle));
		points.forEach(this::addPoint);
	}

	public void addPoint(Point point)
	{
		if (point.getX() < 0 || point.getX() > maxX || point.getY() < 0 || point.getY() > maxY)
		{
			throw new IllegalArgumentException(String.format("The point being added to the delaunay triangulation (%s) must lie within the [0,0] and [%d,%d] rectangle.", point, maxX, maxY));
		}

		if (!points.add(point))
		{
			return; // the point is already in the triangulation.
		}
	}

	public List<Triangle> getTriangulationTriangles()
	{
		return triangulationTriangles.stream()
			.collect(Collectors.toMap(Function.identity(), Triangle::getPoints))
			.entrySet()
			.stream()
			.filter(entry -> !entry.getValue().contains(p1))
			.filter(entry -> !entry.getValue().contains(p2))
			.filter(entry -> !entry.getValue().contains(p3))
			.map(Map.Entry::getKey)
			.collect(Collectors.toList());
	}

	private void flipTriangles()
	{

	}
}
