package com.github.ants280.compgeo.algorithm;

import com.github.ants280.compgeo.Point;
import org.junit.Assert;
import org.junit.Test;

public class EdgeTest
{
	@Test
	public void testEquals()
	{
		double v = Integer.valueOf(Integer.MAX_VALUE).doubleValue();
		Edge edge1 = Edge.fromPoints(new Point(v, v), new Point(-v, v));
		Edge edge2 = Edge.fromPoints(new Point(v, v), new Point(-v, v));

		boolean equals1 = edge1.equals(edge2);
		boolean equals2 = edge2.equals(edge1);

		Assert.assertTrue(equals1);
		Assert.assertTrue(equals2);
	}

	@Test
	public void testContainsPoint_sameSlopeNotOnLine()
	{
		Edge edge = Edge.fromPoints(new Point(0, 0), new Point(2, 2));
		Point point = new Point(3, 3);

		boolean containsPoint = edge.containsPoint(point);

		Assert.assertFalse(containsPoint);
	}

	@Test
	public void testContainsPoint_onLine()
	{
		Edge edge = Edge.fromPoints(new Point(0, 0), new Point(4, 4));
		Point point = new Point(3, 3);

		boolean containsPoint = edge.containsPoint(point);

		Assert.assertTrue(containsPoint);
	}

	@Test
	public void testContainsPoint_isStartPoint()
	{
		Edge edge = Edge.fromPoints(new Point(1, 1), new Point(4, 4));
		Point point = new Point(1, 1);

		boolean containsPoint = edge.containsPoint(point);

		Assert.assertTrue(containsPoint);
	}

	@Test
	public void testContainsPoint_isEndPoint()
	{
		Edge edge = Edge.fromPoints(new Point(0, 0), new Point(5, 5));
		Point point = new Point(4, 4);

		boolean containsPoint = edge.containsPoint(point);

		Assert.assertTrue(containsPoint);
	}
}
