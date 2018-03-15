package com.jpatterson.school.compGeo.ui;

import com.jpatterson.school.compGeo.shape.Triangle;
import java.awt.Polygon;

public class DelaunayTriangle
{
	private final Polygon polygon;

	public DelaunayTriangle(Triangle triangle)
	{
		this.polygon = PointUiUtils.createPolygon(triangle.getPoints());
	}

	public Polygon getPolygon()
	{
		return polygon;
	}
}
