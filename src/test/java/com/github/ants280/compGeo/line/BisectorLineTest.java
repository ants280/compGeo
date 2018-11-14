package com.jpatterson.school.compGeo.line;

import com.jpatterson.school.compGeo.Point;
import static org.junit.Assert.*;
import org.junit.Test;

public class BisectorLineTest
{
	@Test
	public void testGetLineBetween_simple()
	{
		Point p0 = new Point(1, 1);
		Point p1 = new Point(2, 2);

		BisectorLine lineBetween = new BisectorLine(p0, p1);
		Point expectedStartPoint = new Point(1, 2);
		Point expectedEndPoint = new Point(2, 1);

		assertEquals(expectedStartPoint, lineBetween.getStartPoint());
		assertEquals(expectedEndPoint, lineBetween.getEndPoint());
	}

	@Test
	public void testGetLineBetween_complex()
	{
		Point p0 = new Point(1, 1);
		Point p1 = new Point(7, 3);

		BisectorLine lineBetween = new BisectorLine(p0, p1);
		Point expectedStartPoint = new Point(3, 5);
		Point expectedEndPoint = new Point(5, -1);

		assertEquals(expectedStartPoint, lineBetween.getStartPoint());
		assertEquals(expectedEndPoint, lineBetween.getEndPoint());
	}

	@Test
	public void testGetLineBetween_verticalPoints()
	{
		Point p0 = new Point(1, 1);
		Point p1 = new Point(1, 3);

		BisectorLine lineBetween = new BisectorLine(p0, p1);
		Point expectedStartPoint = new Point(0, 2);
		Point expectedEndPoint = new Point(2, 2);

		assertEquals(expectedStartPoint, lineBetween.getStartPoint());
		assertEquals(expectedEndPoint, lineBetween.getEndPoint());
	}
}
