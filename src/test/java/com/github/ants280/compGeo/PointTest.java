package com.github.ants280.compGeo;

import static org.junit.Assert.*;
import org.junit.Test;

public class PointTest
{
	@Test
	public void testCompareTo_sameY_lowerX()
	{
		Point p0 = new Point(0, 0);
		Point p1 = new Point(1, 0);

		int actualCompareToValue = p0.compareTo(p1);
		assertTrue("p0 should be < p1 because it has a larger x cordinate when the y coordinates are the same",
				actualCompareToValue < 0);
	}

	@Test
	public void testCompareTo_sameY_higherX()
	{
		Point p0 = new Point(2, 0);
		Point p1 = new Point(1, 0);

		int actualCompareToValue = p0.compareTo(p1);
		assertTrue("p0 should be > p1 because it has a larger x cordinate when the y coordinates are the same",
				actualCompareToValue > 0);
	}
}
