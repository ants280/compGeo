package com.github.ants280.compgeo;

import org.junit.Assert;
import org.junit.Test;

public class VectorTest
{
	@Test
	public void testToString()
	{
		Object vector = new Vector(new Point(0, 0), new Point(1, 2));

		String toString = vector.toString();

		Assert.assertTrue(toString.startsWith("["));
		Assert.assertTrue(toString.endsWith("]"));
		Assert.assertEquals(1, toString.length() % 2); // should be odd
		Assert.assertEquals(',', toString.charAt(toString.length() / 2));
	}
}
