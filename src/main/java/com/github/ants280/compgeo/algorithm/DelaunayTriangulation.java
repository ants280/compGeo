package com.github.ants280.compgeo.algorithm;

import com.github.ants280.compgeo.CompGeoUtils;
import com.github.ants280.compgeo.Point;
import com.github.ants280.compgeo.line.ParametricLine;
import com.github.ants280.compgeo.shape.Triangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DelaunayTriangulation
{
	private final Map<Point, Collection<Edge>> verticies;

	private final Point p1;
	private final Point p2;
	private final Point p3;

	public DelaunayTriangulation()
	{
		this(Collections.emptyList());
	}

	public DelaunayTriangulation(List<Point> points)
	{
		this.verticies = new HashMap<>();
		this.p1 = new Point(Double.MAX_VALUE, Double.MAX_VALUE);
		this.p2 = new Point(-Double.MAX_VALUE, Double.MAX_VALUE);
		this.p3 = new Point(Double.MAX_VALUE, -Double.MAX_VALUE);
		points.forEach(this::addPoint);
	}

	public void addPoint(Point point)
	{
		if (point.getX() < 0 || point.getY() < 0)
		{
			throw new IllegalArgumentException(
					"The point have positive coordinates : " + point);
		}

		if (!verticies.containsKey(point))
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

				if (sourceTriangle.containsPointInCircle(otherPoint))
				{
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

	private static class Edge extends ParametricLine implements Comparable<Edge>
	{
		private final double angle;

		public Edge(Point startPoint, Point endPoint)
		{
			super(startPoint, endPoint);

			Point rightStartPoint = new Point(
					startPoint.getX() + 1,
					startPoint.getY());
			this.angle = CompGeoUtils.getAngle(
					rightStartPoint,
					startPoint,
					endPoint);
		}

		private Double getAngle()
		{
			return angle;
		}

		@Override
		public int compareTo(Edge o)
		{
			int startPointCompareTo
					= this.getStartPoint().compareTo(o.getStartPoint());

			if (startPointCompareTo != 0)
			{
				return startPointCompareTo;
			}

			int angleCompareTo = this.getAngle().compareTo(o.getAngle());

			return angleCompareTo == 0
					? this.getEndPoint().compareTo(o.getEndPoint())
					: angleCompareTo;
		}
	}
}
