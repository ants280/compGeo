package com.github.ants280.compgeo.ui;

import com.github.ants280.compgeo.Point;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Assert;
import org.junit.Test;

public class PointUiUtilsTest
{
	@Test
	public void testGetMaxPointValue()
	{
		Collection<Point> points = Arrays.asList(
				new Point(3d, 4d),
				new Point(5d, 6d),
				new Point(1d, 2d));

		double expectedMaxPointValue = 5d;
		double actualMaxPointValue = PointUiUtils.getMaxPointValue(points, Point::getX);
		double delta = 0d;

		Assert.assertEquals(expectedMaxPointValue, actualMaxPointValue, delta);
	}
}
