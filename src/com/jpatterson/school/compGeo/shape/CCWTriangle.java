package com.jpatterson.school.compGeo.shape;

import com.jpatterson.school.compGeo.CompGeoUtils;
import com.jpatterson.school.compGeo.Point;

public class CCWTriangle extends Triangle
{
	public CCWTriangle(Point p1, Point p2, Point p3)
	{
		super(
			CompGeoUtils.isCCW(p1, p2, p3) ? p1 : p3,
			p2,
			CompGeoUtils.isCCW(p1, p2, p3) ? p3 : p1);
	}
}
