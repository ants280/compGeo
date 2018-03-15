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

	public DelaunayTriangulation()
	{
		this(Collections.emptyList());
	}

	public DelaunayTriangulation(List<Point> points)
	{
		this.points = new ArrayList<>(points);
		this.triangulationTriangles = new LinkedList<>();
	}

	public void addPoint(Point point)
	{
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
