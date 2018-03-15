package com.jpatterson.school.compGeo.ui;

import com.jpatterson.school.compGeo.Point;
import java.awt.Polygon;
import java.util.List;

public class DelaunayTriangle
{
	private final Polygon polygon;

	public DelaunayTriangle(List<Point> points)
	{
		assert points.size() == 3;

		this.polygon = PointUiUtils.createPolygon(points);
	}

	public Polygon getPolygon()
	{
		return polygon;
	}
}
