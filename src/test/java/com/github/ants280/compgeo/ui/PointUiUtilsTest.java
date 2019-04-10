package com.github.ants280.compgeo.ui;

import com.github.ants280.compgeo.Point;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.function.ToDoubleFunction;
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

	@Test(expected = Exception.class)
	public void testGenMaxPointValue_noPoints()
	{
		Collection<Point> points = Collections.emptyList();
		ToDoubleFunction<Point> valueExtractor = point -> -1d;

		double maxPointValue = PointUiUtils.getMaxPointValue(points, valueExtractor);

		Assert.fail("Should have crashed.  Got maxPointValue = " + maxPointValue);
	}
}
