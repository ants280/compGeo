package com.jpatterson.school.compGeo.ui;

import com.jpatterson.school.compGeo.Point;
import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.*;
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

		Double expectedMaxPointValue = 5d;
		Double actualMaxPointValue = PointUiUtils.getMaxPointValue(points, Point::getX);

		assertEquals(expectedMaxPointValue, actualMaxPointValue);
	}
}
