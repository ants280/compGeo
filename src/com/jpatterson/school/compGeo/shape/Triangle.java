package com.jpatterson.school.compGeo.shape;

import com.jpatterson.school.compGeo.CompGeoUtils;
import com.jpatterson.school.compGeo.Point;
import com.jpatterson.school.compGeo.line.BisectorLine;
import com.jpatterson.school.compGeo.line.ParametricLine;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Triangle implements Shape
{
	protected final Point p1;
	protected final Point p2;
	protected final Point p3;
	private transient final List<Point> pointsList;

	public Triangle(Point p1, Point p2, Point p3)
	{
		if (CompGeoUtils.getDeterminant(p1, p2, p3) == 0d)
		{
			throw new IllegalArgumentException(String.format("Triangle points (%s,%s,%s) are colinear.", p1, p2, p3));
		}

		List<Point> tempPointsList = Arrays.asList(p1, p2, p3);
		Collections.sort(tempPointsList);
		boolean ccw = CompGeoUtils.getDeterminant(tempPointsList.get(0), tempPointsList.get(1), tempPointsList.get(2)) < 0;
		this.p1 = tempPointsList.get(0);
		this.p2 = ccw ? tempPointsList.get(1) : tempPointsList.get(2);
		this.p3 = ccw ? tempPointsList.get(2) : tempPointsList.get(1);
		this.pointsList = Collections.unmodifiableList(Arrays.asList(this.p1, this.p2, this.p3));
	}

	@Override
	public List<Point> getPoints()
	{
		return pointsList;
	}

	public boolean contains(Point point)
	{
		double d1 = CompGeoUtils.getDeterminant(p1, p2, point);
		double d2 = CompGeoUtils.getDeterminant(p2, p3, point);
		double d3 = CompGeoUtils.getDeterminant(p3, p1, point);

		return (d1 <= 0 && d2 <= 0 && d3 <= 0) || (d1 >= 0 && d2 >= 0 && d3 >= 0);
	}

	public boolean containsPointOnEdge(Point point)
	{
		double d1 = CompGeoUtils.getDeterminant(p1, p2, point);
		double d2 = CompGeoUtils.getDeterminant(p2, p3, point);
		double d3 = CompGeoUtils.getDeterminant(p3, p1, point);

		return d1 == 0 || d2 == 0 || d3 == 0;
	}
	
	public List<Point> getSharedPoints(Triangle other)
	{
		return pointsList
			.stream()
			.filter(point -> other.getPoints().contains(point))
			.collect(Collectors.toList());
	}

	// Next two methods (getTwiceArea, containsPointInCircle) copied from "Incremental Delaunay Triangulation", ACM 1993, Dani Lischinski.
	private double getTwiceArea(Point a, Point b, Point c)
	{
		return (b.getX() - a.getX()) * (c.getY() - a.getY()) - (b.getY() - a.getY()) * (c.getX() - a.getX());
	}

	public boolean containsPointInCircle(Point p4)
	{
		return (p1.getX() * p1.getX() + p1.getY() * p1.getY()) * getTwiceArea(p2, p3, p4)
			- (p2.getX() * p2.getX() + p2.getY() * p2.getY()) * getTwiceArea(p1, p3, p4)
			+ (p3.getX() * p3.getX() + p3.getY() * p3.getY()) * getTwiceArea(p1, p2, p4)
			- (p4.getX() * p4.getX() + p4.getY() * p4.getY()) * getTwiceArea(p1, p2, p3) > 0;
	}

	public Point getCircumcircleCenterPoint()
	{
		ParametricLine b1 = new BisectorLine(p1, p2);
		ParametricLine b2 = new BisectorLine(p2, p3);

		return b1.getIntersectionPoint(b2);
	}

	@Override
	public int hashCode()
	{
		int hash = 5;
		hash = 97 * hash + Objects.hashCode(this.p1);
		hash = 97 * hash + Objects.hashCode(this.p2);
		hash = 97 * hash + Objects.hashCode(this.p3);
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final Triangle other = (Triangle) obj;
		if (!Objects.equals(this.p1, other.p1))
		{
			return false;
		}
		if (!Objects.equals(this.p2, other.p2))
		{
			return false;
		}
		if (!Objects.equals(this.p3, other.p3))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return "Triangle{" + "p1=" + p1 + ", p2=" + p2 + ", p3=" + p3 + '}';
	}
}
