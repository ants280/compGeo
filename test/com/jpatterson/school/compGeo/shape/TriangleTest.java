package com.jpatterson.school.compGeo.shape;

import com.jpatterson.school.compGeo.Point;
import org.junit.Assert;
import org.junit.Test;

public class TriangleTest
{
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_colinearPoints()
	{
		Point p1 = new Point(1, 1);
		Point p2 = new Point(3, 3);
		Point p3 = new Point(2, 2);

		Assert.fail("Exception should be thrown for triangle: " + new Triangle(p1, p2, p3));
	}

	@Test
	public void testContains_ccw()
	{
		Point p1 = new Point(1, 1);
		Point p2 = new Point(5, 1);
		Point p3 = new Point(1, 5);
		Point p4 = new Point(4, 4); // should not be in triangle
		Point p5 = new Point(3, 3); // should be in triangle (on edge)
		Point p6 = new Point(2, 2); // should be in triangle
		Triangle triangle = new Triangle(p1, p2, p3);

		Assert.assertTrue(!triangle.contains(p4));
		Assert.assertTrue(triangle.contains(p5));
		Assert.assertTrue(triangle.contains(p6));
	}

	@Test
	public void testContains_notCCW()
	{
		Point p1 = new Point(1, 1);
		Point p2 = new Point(5, 1);
		Point p3 = new Point(1, 5);
		Point p4 = new Point(4, 4); // should not be in triangle
		Point p5 = new Point(3, 3); // should be in triangle (on edge)
		Point p6 = new Point(2, 2); // should be in triangle
		Triangle triangle = new Triangle(p3, p2, p1);

		Assert.assertTrue(!triangle.contains(p4));
		Assert.assertTrue(triangle.contains(p5));
		Assert.assertTrue(triangle.contains(p6));
	}

	@Test
	public void testContainsPointOnEdge()
	{
		Point p1 = new Point(1, 1);
		Point p2 = new Point(5, 1);
		Point p3 = new Point(1, 5);
		Point p4 = new Point(4, 4); // should not be on edge
		Point p5 = new Point(3, 3); // should be on edge
		Point p6 = new Point(2, 2); // should not be on edge
		Point p7 = new Point(2, 1); // should be on edge
		Point p8 = new Point(1, 4); // should be on edge
		Triangle triangle = new Triangle(p1, p2, p3);

		Assert.assertTrue(!triangle.containsPointOnEdge(p4));
		Assert.assertTrue(triangle.containsPointOnEdge(p5));
		Assert.assertTrue(!triangle.containsPointOnEdge(p6));
		Assert.assertTrue(triangle.containsPointOnEdge(p7));
		Assert.assertTrue(triangle.containsPointOnEdge(p8));
	}

	@Test
	public void testContainsPointInCircle()
	{
		Point p1 = new Point(0, 0);
		Point p2 = new Point(3, 0);
		Point p3 = new Point(3, 3);
		Point p4 = new Point(1, 2);
		Triangle triangle = new Triangle(p1, p2, p3);

		Assert.assertTrue(triangle.containsPointInCircle(p4));
	}

	@Test
	public void testContainsPointInCircle2()
	{
		Point p1 = new Point(0, 0);
		Point p2 = new Point(3, 0);
		Point p3 = new Point(3, 3);
		Point p4 = new Point(1, 4);
		Triangle triangle = new Triangle(p1, p2, p3);

		Assert.assertTrue(!triangle.containsPointInCircle(p4));
	}

	@Test
	public void testContainsPointInCircle3()
	{
		Point p1 = new Point(2000, 0);
		Point p2 = new Point(1, 1);
		Point p3 = new Point(0, 2000);
		Point p4 = new Point(0, 0);
		Triangle triangle = new Triangle(p1, p2, p3);

		Assert.assertTrue(!triangle.containsPointInCircle(p4));
	}

	@Test
	public void testContainsPointInCircle4()
	{
		Point p1 = new Point(0, 0);
		Point p2 = new Point(6, 0);
		Point p3 = new Point(1, 1);
		Point p4 = new Point(2, 1);
		Triangle triangle = new Triangle(p1, p2, p3);

		Assert.assertTrue(triangle.containsPointInCircle(p4));
	}
}