package com.github.ants280.compgeo.algorithm;

import com.github.ants280.compgeo.Point;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Test;

public class VoronoiDiagramTest
{
	@Test
	public void testGetVoronoiCells_onePoint()
	{
		Point p0 = new Point(7, 13);
		List<Point> points = Collections.singletonList(p0);
		int maxWidth = 20;
		int maxHeight = 20;
		VoronoiDiagram voronoiDiagram = new VoronoiDiagram(points, maxWidth, maxHeight);

		Map<Point, List<Point>> actualVoronoiCellPoints = voronoiDiagram.getVoronoiCells();
		List<Point> expectedP0VoronoiCellPoints = Arrays.asList(
				new Point(0, 0),
				new Point(20, 0),
				new Point(20, 20),
				new Point(0, 20));

		assertSame(1, actualVoronoiCellPoints.size());
		assertEquals(expectedP0VoronoiCellPoints, actualVoronoiCellPoints.get(p0));
	}

	@Test
	public void testGetVoronoiCells_twoPoints_horizontal_regular()
	{
		Point p0 = new Point(1, 1);
		Point p1 = new Point(3, 1);
		List<Point> points = Arrays.asList(p0, p1);
		int maxWidth = 4;
		int maxHeight = 2;
		VoronoiDiagram voronoiDiagram = new VoronoiDiagram(points, maxWidth, maxHeight);

		Map<Point, List<Point>> actualVoronoiCellPoints = voronoiDiagram.getVoronoiCells();
		List<Point> expectedP0VoronoiCellPoints = Arrays.asList(
				new Point(0, 0),
				new Point(2, 0),
				new Point(2, 2),
				new Point(0, 2));
		List<Point> expectedP1VoronoiCellPoints = Arrays.asList(
				new Point(2, 0),
				new Point(4, 0),
				new Point(4, 2),
				new Point(2, 2));

		assertSame(2, actualVoronoiCellPoints.size());
		assertEquals(expectedP0VoronoiCellPoints, actualVoronoiCellPoints.get(p0));
		assertEquals(expectedP1VoronoiCellPoints, actualVoronoiCellPoints.get(p1));
	}

	@Test
	public void testGetVoronoiCells_twoPoints_angle()
	{
		Point p0 = new Point(2, 2);
		Point p1 = new Point(1, 3);
		List<Point> points = Arrays.asList(p0, p1);
		int maxWidth = 4;
		int maxHeight = 4;
		VoronoiDiagram voronoiDiagram = new VoronoiDiagram(points, maxWidth, maxHeight);

		Map<Point, List<Point>> actualVoronoiCellPoints = voronoiDiagram.getVoronoiCells();
		List<Point> expectedP0VoronoiCellPoints = Arrays.asList(
				new Point(0, 0),
				new Point(4, 0),
				new Point(4, 4),
				new Point(3, 4),
				new Point(0, 1));
		List<Point> expectedP1VoronoiCellPoints = Arrays.asList(
				new Point(3, 4),
				new Point(0, 4),
				new Point(0, 1));

		assertSame(2, actualVoronoiCellPoints.size());
		assertEquals(expectedP0VoronoiCellPoints, actualVoronoiCellPoints.get(p0));
		assertEquals(expectedP1VoronoiCellPoints, actualVoronoiCellPoints.get(p1));
	}

	@Test
	public void testGetVoronoiCells_threePoints_vertical_regular()
	{
		Point p0 = new Point(1, 1);
		Point p1 = new Point(1, 3);
		Point p2 = new Point(1, 5);
		List<Point> points = Arrays.asList(p0, p1, p2);
		int maxWidth = 2;
		int maxHeight = 7;
		VoronoiDiagram voronoiDiagram = new VoronoiDiagram(points, maxWidth, maxHeight);

		Map<Point, List<Point>> actualVoronoiCellPoints = voronoiDiagram.getVoronoiCells();
		List<Point> expectedP0VoronoiCellPoints = Arrays.asList(
				new Point(0, 0),
				new Point(2, 0),
				new Point(2, 2),
				new Point(0, 2));
		List<Point> expectedP1VoronoiCellPoints = Arrays.asList(
				new Point(2, 2),
				new Point(2, 4),
				new Point(0, 4),
				new Point(0, 2));
		List<Point> expectedP2VoronoiCellPoints = Arrays.asList(
				new Point(2, 4),
				new Point(2, 7),
				new Point(0, 7),
				new Point(0, 4));

		assertSame(3, actualVoronoiCellPoints.size());
		assertEquals(expectedP0VoronoiCellPoints, actualVoronoiCellPoints.get(p0));
		assertEquals(expectedP1VoronoiCellPoints, actualVoronoiCellPoints.get(p1));
		assertEquals(expectedP2VoronoiCellPoints, actualVoronoiCellPoints.get(p2));
	}
}
