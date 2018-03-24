package com.jpatterson.school.compGeo.ui;

import com.jpatterson.school.compGeo.CompGeoUtils;
import com.jpatterson.school.compGeo.Point;
import com.jpatterson.school.compGeo.shape.Triangle;
import java.awt.Polygon;

public class DelaunayTriangle
{
	private final Polygon triangle;
	private final Point circumcircleCenterPoint;
	private final double circumcircleRadius;

	public DelaunayTriangle(Triangle triangle)
	{
		this.triangle = PointUiUtils.createPolygon(triangle.getPoints());
		this.circumcircleCenterPoint = triangle.getCircumcircleCenterPoint();
		this.circumcircleRadius = CompGeoUtils.getDistance(triangle.getPoints().get(0), circumcircleCenterPoint);
	}

	public Polygon getTriangle()
	{
		return triangle;
	}

	public Point getCircumcircleCenterPoint()
	{
		return circumcircleCenterPoint;
	}

	public double getCircumcircleRadius()
	{
		return circumcircleRadius;
	}
}
