package com.jpatterson.school.compGeo.algorithm;

import com.jpatterson.school.compGeo.Point;
import java.util.Collection;

public class DelaunayTriangulation
{
	private final Collection<Point> points;
	private final int maxWidth;
	private final int maxHeight;

	public DelaunayTriangulation(Collection<Point> points, int maxWidth, int maxHeight)
	{
		this.points = points;
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
	}
}
