package com.github.ants280.compgeo;

import org.junit.Assert;
import org.junit.Test;

public class PointTest
{
	@Test
	public void testCompareTo_sameY_lowerX()
	{
		Point p0 = new Point(0, 0);
		Point p1 = new Point(1, 0);

		int actualCompareToValue = p0.compareTo(p1);
		Assert.assertTrue(
				"p0 should be < p1 because it has a larger x cordinate when the y coordinates are the same",
				actualCompareToValue < 0);
	}

	@Test
	public void testCompareTo_sameY_higherX()
	{
		Point p0 = new Point(2, 0);
		Point p1 = new Point(1, 0);

		int actualCompareToValue = p0.compareTo(p1);
		Assert.assertTrue(
				"p0 should be > p1 because it has a larger x cordinate when the y coordinates are the same",
				actualCompareToValue > 0);
	}

	@Test
	public void testEquals_same()
	{
		Object p0 = new Point(0, 1);
		Object p1 = p0;

		boolean actualEquals = p0.equals(p1);
		boolean expectedEquals = true;

		Assert.assertEquals(expectedEquals, actualEquals);
	}

	@Test
	public void testEquals_null()
	{
		Object p0 = new Point(0, 1);
		Object p1 = null;

		boolean actualEquals = p0.equals(p1);
		boolean expectedEquals = false;

		Assert.assertEquals(expectedEquals, actualEquals);
	}

	@Test
	public void testEquals_wrongObject()
	{
		Object p0 = new Point(0, 1);
		Object p1 = "(0.000000,1.000000)";

		boolean actualEquals = p0.equals(p1);
		boolean expectedEquals = true;

		Assert.assertEquals(expectedEquals, actualEquals);
	}

	@Test
	public void testEquals_differentX()
	{
		Object p0 = new Point(0, 1);
		Object p1 = new Point(2, 1);

		boolean actualEquals = p0.equals(p1);
		boolean expectedEquals = false;

		Assert.assertEquals(expectedEquals, actualEquals);
	}

	@Test
	public void testEquals_differentY()
	{
		Object p0 = new Point(0, 1);
		Object p1 = new Point(0, 2);

		boolean actualEquals = p0.equals(p1);
		boolean expectedEquals = false;

		Assert.assertEquals(expectedEquals, actualEquals);
	}

	@Test
	public void testEquals_eq()
	{
		Object p0 = new Point(0, 1);
		Object p1 = new Point(0, 1);

		boolean actualEquals = p0.equals(p1);
		boolean expectedEquals = true;

		Assert.assertEquals(expectedEquals, actualEquals);
	}
}
