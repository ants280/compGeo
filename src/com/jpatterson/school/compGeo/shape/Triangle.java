package com.jpatterson.school.compGeo.shape;

import com.jpatterson.school.compGeo.CompGeoUtils;
import com.jpatterson.school.compGeo.Point;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Triangle implements Shape
{
	protected final Point p1;
	protected final Point p2;
	protected final Point p3;
	private transient final List<Point> pointsList;
	private transient final Set<Point> pointsSet;

	public Triangle(Point p1, Point p2, Point p3)
	{
		if (CompGeoUtils.getDeterminant(p1, p2, p3) == 0d)
		{
			throw new IllegalArgumentException(String.format("Triangle points (%s,%s,%s) are colinear.", p1, p2, p3));
		}
		
		List<Point> tempPointsList = Arrays.asList(p1, p2, p3);
		Collections.sort(tempPointsList);
		
		this.p1 = tempPointsList.get(0);
		this.p2 = tempPointsList.get(1);
		this.p3 = tempPointsList.get(2);
		this.pointsList = Collections.unmodifiableList(tempPointsList);
		this.pointsSet = Collections.unmodifiableSet(new HashSet<>(pointsList));
	}

	@Override
	public List<Point> getPoints()
	{
		return pointsList;
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
	
	public boolean containsPointOnEdge(Point point)
	{
		double d1 = CompGeoUtils.getDeterminant(p1, p2, point);
		double d2 = CompGeoUtils.getDeterminant(p2, p3, point);
		double d3 = CompGeoUtils.getDeterminant(p3, p1, point);
		
		return d1 == 0 || d2 == 0 || d3 == 0;
	}

	public boolean containsPointInCircle(Point point)
	{
		throw new UnsupportedOperationException("Not supported yet.");
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
