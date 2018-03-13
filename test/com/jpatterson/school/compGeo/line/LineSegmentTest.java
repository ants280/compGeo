package com.jpatterson.school.compGeo.line;

import com.jpatterson.school.compGeo.Point;
import static org.junit.Assert.*;
import org.junit.Test;

public class LineSegmentTest
{
	@Test
	public void testGetIntersectionPoint_simple()
	{
		Point startPoint = new Point(1, 1);
		Point endPoint = new Point(3, 3);
		LineSegment lineSegment = new LineSegment(startPoint, endPoint);
		ParametricLine line = new ParametricLine(new Point(0, 4), new Point(1, 3));

		Point actualIntersectionPoint = lineSegment.getIntersectionPoint(line);
		Point expectedIntersectionPoint = new Point(2, 2);

		assertEquals(expectedIntersectionPoint, actualIntersectionPoint);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetIntersectionPoint_tooShort()
	{
		Point startPoint = new Point(4, 4);
		Point endPoint = new Point(3, 3);
		LineSegment lineSegment = new LineSegment(startPoint, endPoint);
		ParametricLine line = new ParametricLine(new Point(0, 4), new Point(1, 3));

		lineSegment.getIntersectionPoint(line);

	}
}
