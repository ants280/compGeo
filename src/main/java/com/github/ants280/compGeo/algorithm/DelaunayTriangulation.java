package com.github.ants280.compGeo.algorithm;

import com.github.ants280.compGeo.CompGeoUtils;
import com.github.ants280.compGeo.Point;
import com.github.ants280.compGeo.shape.Triangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DelaunayTriangulation
{
	private final Set<Point> points;
	private final Set<Triangle> triangulationTriangles;
	private final int maxX;
	private final int maxY;
	private final Point p1;
	private final Point p2;
	private final Point p3;

	public DelaunayTriangulation(int maxX, int maxY)
	{
		this(Collections.emptyList(), maxX, maxY);
	}

	public DelaunayTriangulation(List<Point> points, int maxX, int maxY)
	{
		if (maxX < 0 || maxX > Integer.MAX_VALUE / 2 || maxY < 0 || maxY > Integer.MAX_VALUE / 2)
		{
			throw new IllegalArgumentException(String.format("Invalid max x/y: [%d,%d]", maxX, maxY));
		}
		this.maxX = maxX;
		this.maxY = maxY;
		this.p1 = new Point(-1, -1);
		this.p2 = new Point(maxX * 2, -1);
		this.p3 = new Point(-1, maxY * 2);
		this.points = new HashSet<>();
		Triangle initialTriangle = new Triangle(p1, p2, p3);
		this.triangulationTriangles = new HashSet<>(Collections.singletonList(initialTriangle));
		points.forEach(this::addPoint);
	}

	public void addPoint(Point point)
	{
		if (point.getX() < 0 || point.getX() > maxX || point.getY() < 0 || point.getY() > maxY)
		{
			throw new IllegalArgumentException(String.format("The point being added to the delaunay triangulation (%s) must lie within the [0,0] and [%d,%d] rectangle.", point, maxX, maxY));
		}

		if (!points.add(point))
		{
			return; // the point is already in the triangulation.
		}

		List<Triangle> trianglesContainingPoint = triangulationTriangles.stream()
				.filter(triangle -> triangle.contains(point))
				.collect(Collectors.toList());
		assert !trianglesContainingPoint.isEmpty();
		assert trianglesContainingPoint.size() <= 2;

		List<Triangle> splitTriangles = splitTriangles(trianglesContainingPoint, point);

		triangulationTriangles.removeAll(trianglesContainingPoint);
		triangulationTriangles.addAll(splitTriangles);
		// TODO: only need to flip triangles around splitTriangles (and then add those to splitTriangles).
		splitTriangles.forEach(this::flipTrianglesAround);
	}

	private List<Triangle> splitTriangles(List<Triangle> trianglesContainingPoint, Point point)
	{
		List<Triangle> splitTriangles = new ArrayList<>(); // usually size = 3, sometimes = 4.

		switch (trianglesContainingPoint.size())
		{
			case 1:
				List<Point> oldPoints = trianglesContainingPoint.get(0).getPoints();
				splitTriangles.add(new Triangle(oldPoints.get(0), oldPoints.get(1), point));
				splitTriangles.add(new Triangle(oldPoints.get(1), oldPoints.get(2), point));
				splitTriangles.add(new Triangle(oldPoints.get(2), oldPoints.get(0), point));
				break;
			case 2:
				for (Triangle halfTriangle : trianglesContainingPoint)
				{
					if (!halfTriangle.containsPointOnEdge(point))
					{
						throw new IllegalArgumentException(String.format("Expected point %s to be on the edge of %s.", point, halfTriangle));
					}
					oldPoints = halfTriangle.getPoints();
					if (CompGeoUtils.getDeterminant(oldPoints.get(0), oldPoints.get(1), point) != 0)
					{
						splitTriangles.add(new Triangle(oldPoints.get(0), oldPoints.get(1), point));
					}
					if (CompGeoUtils.getDeterminant(oldPoints.get(1), oldPoints.get(2), point) != 0)
					{
						splitTriangles.add(new Triangle(oldPoints.get(1), oldPoints.get(2), point));
					}
					if (CompGeoUtils.getDeterminant(oldPoints.get(2), oldPoints.get(0), point) != 0)
					{
						splitTriangles.add(new Triangle(oldPoints.get(2), oldPoints.get(0), point));
					}
				}
				assert splitTriangles.size() == 4;
				break;
			default:
				throw new IllegalArgumentException(String.format("Expected only one or two triangles to contain point %s.  Found %d.", point, trianglesContainingPoint.size()));
		}

		return splitTriangles;
	}

	public List<Triangle> getTriangulationTriangles()
	{
		return triangulationTriangles.stream()
				.collect(Collectors.toMap(Function.identity(), Triangle::getPoints))
				.entrySet()
				.stream()
				.filter(entry -> !entry.getValue().contains(p1))
				.filter(entry -> !entry.getValue().contains(p2))
				.filter(entry -> !entry.getValue().contains(p3))
				.map(Map.Entry::getKey)
				.collect(Collectors.toList());
	}

	private void flipTrianglesAround(Triangle sourceTriangle)
	{
		if (!triangulationTriangles.contains(sourceTriangle))
		{
			return; // no points flipped
		}

		for (Triangle otherTriangle : triangulationTriangles) // TODO: Need faster lookup of triangles next to sourceTriangle.
		{
			if (otherTriangle.equals(sourceTriangle))
			{
				continue;
			}

			List<Point> sharedPoints = sourceTriangle.getSharedPoints(otherTriangle); // This isn't the best for memory.
			assert sharedPoints.size() < 3;
			if (sharedPoints.size() == 2)
			{
				Point otherPoint = getOtherPoint(otherTriangle, sharedPoints);

				if (!sourceTriangle.containsPointInCircle(otherPoint))
				{
					continue;
				}

				Point sourceTrianglePoint = getOtherPoint(sourceTriangle, sharedPoints);

				Triangle t1 = new Triangle(sourceTrianglePoint, otherPoint, sharedPoints.get(0));
				Triangle t2 = new Triangle(sourceTrianglePoint, otherPoint, sharedPoints.get(1));

				triangulationTriangles.remove(sourceTriangle);
				triangulationTriangles.remove(otherTriangle);
				triangulationTriangles.add(t1);
				triangulationTriangles.add(t2);

				flipTrianglesAround(t1);
				flipTrianglesAround(t2);

				return; // points flipped
			}
		}

		// no points flipped
	}

	private Point getOtherPoint(Triangle triangle, List<Point> sharedPoints)
	{
		return triangle.getPoints()
				.stream()
				.filter(point -> !sharedPoints.contains(point))
				.findAny()
				.orElse(null);
	}
}
