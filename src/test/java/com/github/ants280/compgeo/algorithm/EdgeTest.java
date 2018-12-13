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
}
