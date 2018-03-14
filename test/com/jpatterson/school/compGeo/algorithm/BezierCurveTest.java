package com.jpatterson.school.compGeo.algorithm;

import com.jpatterson.school.compGeo.CompGeoUtils;
import com.jpatterson.school.compGeo.Point;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import org.junit.Assert;
import org.junit.Test;

public class BezierCurveTest
{
	@Test
	public void testGetPoints_linear()
	{
		BezierCurve bezierCurve = new BezierCurve(new Point(1, 1), new Point(7, 4));
		List<Point> actualBezierCurvePoints = bezierCurve.getPoints(0, 1, 3);
		assertPointsAre(actualBezierCurvePoints, new Point(1, 1), new Point(3, 2), new Point(5, 3), new Point(7, 4));
	}

	@Test
	public void testGetPoints_linear_middle()
	{
		BezierCurve bezierCurve = new BezierCurve(new Point(1, 1), new Point(7, 4));
		List<Point> actualBezierCurvePoints = bezierCurve.getPoints(1 / 3d, 2 / 3d, 1);
		assertPointsAre(actualBezierCurvePoints, new Point(3, 2), new Point(5, 3));
	}

	@Test
	public void testGetPoints_linear_NoSteps()
	{
		BezierCurve bezierCurve = new BezierCurve(new Point(1, 1), new Point(7, 4));
		List<Point> actualBezierCurvePoints = bezierCurve.getPoints(.5d, 1, 0);
		assertPointsAre(actualBezierCurvePoints, new Point(4, 2.5d));
	}

	@Test
	public void testGetPoints_linear_EqualTMinAndTMax()
	{
		BezierCurve bezierCurve = new BezierCurve(new Point(1, 1), new Point(7, 4));
		int steps = 5;
		List<Point> actualBezierCurvePoints = bezierCurve.getPoints(1, 1, steps);
		assertPointsAre(actualBezierCurvePoints, Collections.nCopies(steps + 1, new Point(7, 4)).toArray(new Point[0]));
	}

	@Test
	public void testGetPoints_linear_toFarApart()
	{
		BezierCurve bezierCurve = new BezierCurve(new Point(1, 1), new Point(7, 4));
		List<Point> actualBezierCurvePoints = bezierCurve.getPoints(1 / 3d, 2 / 3d, 1, 2d);
		Assert.assertNull(
			"The points should be null because they are more than 2 apart ([3,2]&[5,3] are sqrt(5) apart",
			actualBezierCurvePoints);
	}

	private void assertPointsAre(List<Point> actualBezierCurvePoints, Point... expectedBezierCurvePoints)
	{
		Assert.assertNotNull(actualBezierCurvePoints);
		Assert.assertSame(actualBezierCurvePoints.size(), expectedBezierCurvePoints.length);

		for (int i = 0; i < expectedBezierCurvePoints.length; i++)
		{
			Point expectedBezierCurvePoint = expectedBezierCurvePoints[i];
			Point actualBezierCurvePoint = actualBezierCurvePoints.get(i);

			assertCoordinatesAreClose(expectedBezierCurvePoint, actualBezierCurvePoint, Point::getX);
			assertCoordinatesAreClose(expectedBezierCurvePoint, actualBezierCurvePoint, Point::getY);
		}
	}

	private void assertCoordinatesAreClose(Point expectedPoint, Point actualPoint, Function<Point, Double> coordinateFunction)
	{
		Assert.assertEquals(
			coordinateFunction.apply(expectedPoint),
			coordinateFunction.apply(actualPoint),
			CompGeoUtils.DELTA);
	}
}
