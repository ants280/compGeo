package com.jpatterson.school.compGeo.shape;

import com.jpatterson.school.compGeo.Point;
import java.util.Arrays;
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
		List<Point> actualPoints = triangle.getPoints();

		List<Point> expectedPoints = Arrays.asList(p1, p2, p3);
		Assert.assertEquals(expectedPoints, actualPoints);
	}

	@Test
	public void testConstructor_notCCW()
	{
		Point p1 = new Point(0, 0);
		Point p2 = new Point(0, 1);
		Point p3 = new Point(1, 0);

		Triangle triangle = new CCWTriangle(p1, p2, p3);
		List<Point> actualPoints = triangle.getPoints();

		List<Point> expectedPoints = Arrays.asList(p3, p2, p1);
		Assert.assertEquals(expectedPoints, actualPoints);
	}
}
