package com.github.ants280.compgeo.line;

import static com.github.ants280.compgeo.CompGeoUtils.DELTA;
import com.github.ants280.compgeo.Point;
import static org.junit.Assert.*;
import org.junit.Test;

public class ParametricLineTest
{
	@Test
	public void testGetIntersectionPoint_basic()
	{
		ParametricLine lineA = new ParametricLine(new Point(0, -8), new Point(1, -5));
		ParametricLine lineB = new ParametricLine(new Point(0, 4), new Point(1, 3));

		Point actualIntersectionPoint = lineA.getIntersectionPoint(lineB);
		Point expectedIntersectionPoint = new Point(3, 1);

		assertEquals(expectedIntersectionPoint, actualIntersectionPoint);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetIntersectionPoint_horizontalParallel()
	{
		ParametricLine lineA = new ParametricLine(new Point(0, 1), new Point(1, 1));
		ParametricLine lineB = new ParametricLine(new Point(0, 2), new Point(2, 2));

		Point actualIntersectionPoint = lineA.getIntersectionPoint(lineB);

		fail("Expect no intersection point, but got " + actualIntersectionPoint);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetIntersectionPoint_verticalParallel()
	{
		ParametricLine lineA = new ParametricLine(new Point(1, 0), new Point(1, 1));
		ParametricLine lineB = new ParametricLine(new Point(2, 0), new Point(2, 2));

		Point actualIntersectionPoint = lineA.getIntersectionPoint(lineB);

		fail("Expect no intersection point, but got " + actualIntersectionPoint);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetIntersectionPoint_slantParallel()
	{
		ParametricLine lineA = new ParametricLine(new Point(0, 1), new Point(1, 18));
		ParametricLine lineB = new ParametricLine(new Point(0, 2), new Point(1, 19));

		Point actualIntersectionPoint = lineA.getIntersectionPoint(lineB);

		fail("Expect no intersection point, but got " + actualIntersectionPoint);
	}

	@Test
	public void testGetIntersectionPoint_horizontalAndSlant()
	{
		ParametricLine lineA = new ParametricLine(new Point(0, -4), new Point(1, -4));
		ParametricLine lineB = new ParametricLine(new Point(0, 3), new Point(-1, 1));

		Point actualIntersectionPoint = lineA.getIntersectionPoint(lineB);
		Point expectedIntersectionPoint = new Point(-3.5d, -4); //exact
		//Point expectedIntersectionPoint = new Point(-3, -4); //truncated

		assertEquals(expectedIntersectionPoint, actualIntersectionPoint);
	}

	@Test
	public void testGetIntersectionPoint_verticalAndSlant()
	{
		ParametricLine lineA = new ParametricLine(new Point(1, 0), new Point(1, -5));
		ParametricLine lineB = new ParametricLine(new Point(0, 3), new Point(1, 5));

		Point actualIntersectionPoint = lineA.getIntersectionPoint(lineB);
		Point expectedIntersectionPoint = new Point(1, 5);

		assertEquals(expectedIntersectionPoint, actualIntersectionPoint);
	}

	@Test
	public void testGetIntersectionPoint_verticalAndSlant_reversed()
	{
		ParametricLine lineB = new ParametricLine(new Point(1, 0), new Point(1, -5));
		ParametricLine lineA = new ParametricLine(new Point(0, 3), new Point(1, 5));

		Point actualIntersectionPoint = lineA.getIntersectionPoint(lineB);
		Point expectedIntersectionPoint = new Point(1, 5);

		assertEquals(expectedIntersectionPoint, actualIntersectionPoint);
	}

	@Test
	public void testGetIntersectionPoint_sameStartPoint()
	{
		ParametricLine lineA = new ParametricLine(new Point(0, 0), new Point(1, 3));
		ParametricLine lineB = new ParametricLine(new Point(0, 0), new Point(1, 4));

		Point actualIntersectionPoint = lineA.getIntersectionPoint(lineB);
		Point expectedIntersectionPoint = new Point(0, 0);

		assertEquals(expectedIntersectionPoint, actualIntersectionPoint);
	}

	@Test
	public void testGetIntersectionPoint_equalIntSlopes()
	{
		//LineSegment{Line{startPoint=(599,101),endPoint=(0,101)}} BisectorLine{Line{startPoint=(243,99),endPoint=(477,139)}}
		ParametricLine lineA = new ParametricLine(new Point(599, 101), new Point(0, 101)); // m = 0
		ParametricLine lineB = new ParametricLine(new Point(243, 99), new Point(477, 139)); // m = 0.17094017094017094

		Point actualIntersectionPoint = lineA.getIntersectionPoint(lineB);
		//Point expectedIntersectionPoint = new Point(254, 101);
		Point expectedIntersectionPoint = new Point(254.7d, 101.0d);

		//assertEquals(expectedIntersectionPoint, actualIntersectionPoint); // Rounding errors.  See below
		//junit.framework.AssertionFailedError: expected:<(254.0,101.0)> but was:<(254.70000000000002,101.0)>
		assertEquals(expectedIntersectionPoint.getX(), actualIntersectionPoint.getX(), DELTA);
		assertEquals(expectedIntersectionPoint.getY(), actualIntersectionPoint.getY(), DELTA);
	}
}
