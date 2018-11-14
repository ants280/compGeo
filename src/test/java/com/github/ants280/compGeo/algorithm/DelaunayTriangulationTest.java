package com.github.ants280.compGeo.algorithm;

import com.github.ants280.compGeo.Point;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DelaunayTriangulationTest
{
	private DelaunayTriangulation delaunayTriangulation;

	@Before
	public void setUp()
	{
		delaunayTriangulation = new DelaunayTriangulation(1000, 1000);
	}

	@Test
	public void testAddPoint0()
	{
		Assert.assertSame(0, delaunayTriangulation.getTriangulationTriangles().size());
	}

	@Test
	public void testAddPoint1()
	{
		delaunayTriangulation.addPoint(new Point(1, 1));
		Assert.assertSame(0, delaunayTriangulation.getTriangulationTriangles().size());
	}

	@Test
	public void testAddPoint2()
	{
		delaunayTriangulation.addPoint(new Point(1, 1));
		delaunayTriangulation.addPoint(new Point(2, 1));
		Assert.assertSame(0, delaunayTriangulation.getTriangulationTriangles().size());
	}

	@Test
	public void testAddPoint3()
	{
		delaunayTriangulation.addPoint(new Point(1, 1));
		delaunayTriangulation.addPoint(new Point(2, 1));
		delaunayTriangulation.addPoint(new Point(1, 2));
		Assert.assertSame(1, delaunayTriangulation.getTriangulationTriangles().size());
	}

	@Test
	public void testAddPoint4()
	{
		delaunayTriangulation.addPoint(new Point(1, 1));
		delaunayTriangulation.addPoint(new Point(2, 1));
		delaunayTriangulation.addPoint(new Point(1, 2));
		delaunayTriangulation.addPoint(new Point(3, 3));
		Assert.assertSame(2, delaunayTriangulation.getTriangulationTriangles().size());
	}

	@Test
	public void testAddPoint4_square()
	{
		List<Point> points = Arrays.asList(new Point(1, 1), new Point(2, 1), new Point(1, 2), new Point(2, 2));
		delaunayTriangulation = new DelaunayTriangulation(points, 3, 3);
		Assert.assertSame(2, delaunayTriangulation.getTriangulationTriangles().size());
	}

	@Test
	public void testAddPointOrigin()
	{
		delaunayTriangulation.addPoint(new Point(0, 0));
		Assert.assertSame(0, delaunayTriangulation.getTriangulationTriangles().size());
	}
}
