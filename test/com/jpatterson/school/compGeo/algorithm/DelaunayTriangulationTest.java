package com.jpatterson.school.compGeo.algorithm;

import com.jpatterson.school.compGeo.Point;
import junit.framework.TestCase;
import org.junit.Assert;

public class DelaunayTriangulationTest extends TestCase
{
	private DelaunayTriangulation delaunayTriangulation;

	@Override
	protected void setUp() throws Exception
	{
		delaunayTriangulation = new DelaunayTriangulation();
	}

	public void testAddPoint0()
	{
		Assert.assertSame(0, delaunayTriangulation.getTriangulationTriangles().size());
	}

	public void testAddPoint1()
	{
		delaunayTriangulation.addPoint(new Point(1, 1));
		Assert.assertSame(0, delaunayTriangulation.getTriangulationTriangles().size());
	}

	public void testAddPoint2()
	{
		delaunayTriangulation.addPoint(new Point(1, 1));
		delaunayTriangulation.addPoint(new Point(2, 1));
		Assert.assertSame(0, delaunayTriangulation.getTriangulationTriangles().size());
	}

//	public void testAddPoint3()
//	{
//		delaunayTriangulation.addPoint(new Point(1, 1));
//		delaunayTriangulation.addPoint(new Point(2, 1));
//		delaunayTriangulation.addPoint(new Point(1, 2));
//		Assert.assertSame(1, delaunayTriangulation.getTriangulationTriangles().size());
//	}
//
//	public void testAddPoint4()
//	{
//		delaunayTriangulation.addPoint(new Point(1, 1));
//		delaunayTriangulation.addPoint(new Point(2, 1));
//		delaunayTriangulation.addPoint(new Point(1, 2));
//		delaunayTriangulation.addPoint(new Point(3, 3));
//		Assert.assertSame(2, delaunayTriangulation.getTriangulationTriangles().size());
//	}
}
