package com.jpatterson.school.compGeo.shape;

import com.jpatterson.school.compGeo.CompGeoUtils;
import com.jpatterson.school.compGeo.Point;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Triangle implements Shape
{
	protected final Point p1;
	protected final Point p2;
	protected final Point p3;
	private final Set<Point> pointsSet;

	public Triangle(Point p1, Point p2, Point p3)
	{
		if (CompGeoUtils.getDeterminant(p1, p2, p3) == 0d)
		{
			throw new IllegalArgumentException(String.format("Triangle points (%s,%s,%s) are colinear.", p1, p2, p3));
		}
		
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		
		this.pointsSet = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(p1, p2, p3)));
	}

	@Override
	public List<Point> getPoints()
	{
		return Arrays.asList(p1, p2, p3);
	}

	public Set<Point> getPointsSet()
	{
		return pointsSet;
	}
	
	public boolean contains(Point point)
	{
		double d1 = CompGeoUtils.getDeterminant(p1, p2, point);
		double d2 = CompGeoUtils.getDeterminant(p2, p3, point);
		double d3 = CompGeoUtils.getDeterminant(p3, p1, point);
		
		return (d1 <= 0 && d2 <= 0 && d3 <= 0) || (d1 >= 0 && d2 >= 0 && d3 >= 0);
	}
	
	public boolean isOnEdge(Point point)
	{
		double d1 = CompGeoUtils.getDeterminant(p1, p2, point);
		double d2 = CompGeoUtils.getDeterminant(p2, p3, point);
		double d3 = CompGeoUtils.getDeterminant(p3, p1, point);
		
		return d1 == 0 || d2 == 0 || d3 == 0;
	}
}
