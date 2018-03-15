package com.jpatterson.school.compGeo;

import static com.jpatterson.school.compGeo.CompGeoUtils.DELTA;
import static java.lang.Math.PI;
import static org.junit.Assert.*;
import org.junit.Test;

public class CompGeoUtilsTest
{
	@Test
	public void testGetAngle_noAngle()
	{
		Point p0 = new Point(1, 0);
		Point p1 = new Point(0, 0);
		Point p2 = new Point(0, 0);

		double actualAngle = CompGeoUtils.getAngle(p0, p1, p2);
		double expectedAngle = -1d;

		assertEquals(expectedAngle, actualAngle, DELTA);
	}

	@Test
	public void testGetAngle_rightAngle()
	{
		Point p0 = new Point(1, 0);
		Point p1 = new Point(0, 0);
		Point p2 = new Point(0, 1);

		double actualAngle = CompGeoUtils.getAngle(p0, p1, p2);
		double expectedAngle = PI / 2;

		assertEquals(expectedAngle, actualAngle, DELTA);
	}

	@Test
	public void testGetAngle_line()
	{
		Point p0 = new Point(0, -1);
		Point p1 = new Point(0, 0);
		Point p2 = new Point(0, 1);

		double actualAngle = CompGeoUtils.getAngle(p0, p1, p2);
		double expectedAngle = PI;

		assertEquals(expectedAngle, actualAngle, DELTA);
	}

	@Test
	public void testGetAngle_obtuseRightAngle()
	{
		Point p0 = new Point(-1, 0);
		Point p1 = new Point(0, 0);
		Point p2 = new Point(0, 1);

		double actualAngle = CompGeoUtils.getAngle(p0, p1, p2);
		double expectedAngle = PI / 2;

		assertEquals("angles between 0 and PI should only be returned",
			expectedAngle, actualAngle, DELTA);
	}

	/**
	 * p2
	 * |
	 * |
	 * p1---p0
	 */
	@Test
	public void testGetDeterminant_clockwise()
	{
		Point p0 = new Point(1, 0);
		Point p1 = new Point(0, 0);
		Point p2 = new Point(0, 1);

		double expectedDeterminant = CompGeoUtils.getDeterminant(p0, p1, p2);

		assertTrue("The determinant should be greater than 0 because the points are in clockwise direction",
			expectedDeterminant > 0);
	}

	/**
	 * p0
	 * |
	 * |
	 * p1---p2
	 */
	@Test
	public void testGetDeterminant_counterClockwise()
	{
		Point p0 = new Point(0, 1);
		Point p1 = new Point(0, 0);
		Point p2 = new Point(1, 0);

		double expectedDeterminant = CompGeoUtils.getDeterminant(p0, p1, p2);

		assertTrue("The determinant should be less than 0 because the points are in counterClockwise direction",
			expectedDeterminant < 0);
	}

	/**
	 * p2--p0
	 * |
	 * |
	 * p1
	 */	
	@Test
	public void testIsCCW_swap()
	{
		Point p0 = new Point(1, 1);
		Point p1 = new Point(0, 0);
		Point p2 = new Point(0, 1);

		boolean ccw1 = CompGeoUtils.isCCW(p0, p1, p2);
		assertFalse(ccw1);
		boolean ccw2 = CompGeoUtils.isCCW(p2, p1, p0);
		assertTrue(ccw2);
	}


	@Test
	public void testGetDistance_zero()
	{
		Point p0 = new Point(183, -17);
		Point p1 = new Point(183, -17);

		double actualDistance = CompGeoUtils.getDistance(p0, p1);
		double expectedDistance = 0d;

		assertEquals(expectedDistance, actualDistance, DELTA);
	}

	@Test
	public void testGetDistance_one()
	{
		Point p0 = new Point(-6, 9);
		Point p1 = new Point(-6, 10);

		double actualDistance = CompGeoUtils.getDistance(p0, p1);
		double expectedDistance = 1d;

		assertEquals(expectedDistance, actualDistance, DELTA);
	}

	@Test
	public void testGetDistance_one_swapped()
	{
		Point p0 = new Point(-6, 10);
		Point p1 = new Point(-6, 9);

		double actualDistance = CompGeoUtils.getDistance(p0, p1);
		double expectedDistance = 1d;

		assertEquals(expectedDistance, actualDistance, DELTA);
	}

	@Test
	public void testGetDistance_root_two()
	{
		Point p0 = new Point(0, 0);
		Point p1 = new Point(1, 1);

		double actualDistance = CompGeoUtils.getDistance(p0, p1);
		double expectedDistance = Math.sqrt(2d);

		assertEquals(expectedDistance, actualDistance, DELTA);
	}
}
