package com.jpatterson.school.compGeo.ui;

import com.jpatterson.school.compGeo.Point;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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

	@Test
	public void testGetMaxPointValue_noValue()
	{
		Collection<Point> points = Collections.emptyList();

		Double actualMaxPointValue = PointUiUtils.getMaxPointValue(points, Point::getX);

		assertNull(actualMaxPointValue);
	}
}
