package com.jpatterson.school.compGeo.algorithm;

import com.jpatterson.school.compGeo.Point;
import com.jpatterson.school.compGeo.shape.DelaunayTriangle;
import com.jpatterson.school.compGeo.shape.Triangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DelaunayTriangulation
{
	private final List<Point> points;
	private final List<DelaunayTriangle> triangulationTriangles;
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
		this.p1 = new Point(0, 0);
		this.p2 = new Point(maxX * 2, 0);
		this.p3 = new Point(0, maxY * 2);
		this.points = new ArrayList<>();
		DelaunayTriangle initialTriangle = new DelaunayTriangle(p1, p2, p3, null, null, null);
		this.triangulationTriangles = new LinkedList<>(Collections.singletonList(initialTriangle));
		points.forEach(this::addPoint);
	}

	public void addPoint(Point point)
	{
		if (point.getX() < 0 || point.getX() > maxX || point.getY() < 0 || point.getY() > maxY)
		{
			throw new IllegalArgumentException(String.format("The point being added to the delaunay triangulation (%s) must lie within the [0,0] and [%d,%d] rectangle.", point, maxX, maxY));
		}
		
		points.add(point);

//		if (points.size() >= 3)
//		{
//			Point p1 = points.get(points.size() - 1);
//			Point p2 = points.get(points.size() - 2);
//			triangulationTriangles.add(new ArrayList<>(Arrays.asList(point, p1, p2)));
//			if (points.size() > 3)
//			{
//				flipTriangles();
//			}
//		}
	}

	public List<Triangle> getTriangulationTriangles()
	{
		return new ArrayList<>(triangulationTriangles);
	}

	private void flipTriangles()
	{

	}
}
