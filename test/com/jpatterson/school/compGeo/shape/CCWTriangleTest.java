package com.jpatterson.school.compGeo.shape;

import com.jpatterson.school.compGeo.CompGeoUtils;
import com.jpatterson.school.compGeo.Point;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class CCWTriangleTest
{
	@Test
	public void testConstructor_CCW()
	{
		Point p1 = new Point(0, 0);
		Point p2 = new Point(1, 0);
		Point p3 = new Point(0, 1);

		Triangle triangle = new CCWTriangle(p1, p2, p3);
		List<Point> points = triangle.getPoints();

		Assert.assertSame(3, points.size());
		Assert.assertTrue(CompGeoUtils.isCCW(points.get(0), points.get(1), points.get(2)));
	}

	@Test
	public void testConstructor_notCCW()
	{
		Point p1 = new Point(0, 0);
		Point p2 = new Point(0, 1);
		Point p3 = new Point(1, 0);

		Triangle triangle = new CCWTriangle(p1, p2, p3);
		List<Point> points = triangle.getPoints();

		Assert.assertSame(3, points.size());
		Assert.assertTrue(CompGeoUtils.isCCW(points.get(0), points.get(1), points.get(2)));
	}

	public void testGetArea()
	{
	}
}
