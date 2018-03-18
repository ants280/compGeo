package com.jpatterson.school.compGeo.shape;

import com.jpatterson.school.compGeo.Point;
import junit.framework.Assert;
import org.junit.Test;

public class TriangleTest
{
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_colinearPoints()
	{
		Point p1 = new Point(1, 1);
		Point p2 = new Point(3, 3);
		Point p3 = new Point(2, 2);
		Triangle triangle = new Triangle(p1, p2, p3);
		
		Assert.fail("Exception should be thrown");
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
}
