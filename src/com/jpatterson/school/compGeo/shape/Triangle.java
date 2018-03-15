package com.jpatterson.school.compGeo.shape;

import com.jpatterson.school.compGeo.CompGeoUtils;
import com.jpatterson.school.compGeo.Point;
import java.util.Arrays;
import java.util.List;

public class Triangle implements Shape
{
	protected final Point p1;
	protected final Point p2;
	protected final Point p3;

	public Triangle(Point p1, Point p2, Point p3)
	{
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}

	@Override
	public List<Point> getPoints()
	{
		return Arrays.asList(p1, p2, p3);
	}
	
	public boolean contains(Point point)
	{
		return (CompGeoUtils.isCCW(p1, p2, point))
			&& (CompGeoUtils.isCCW(p2, p3, point))
			&& (CompGeoUtils.isCCW(p3, p1, point));
	}
}
