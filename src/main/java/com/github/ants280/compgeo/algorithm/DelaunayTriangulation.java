package com.github.ants280.compgeo.algorithm;

import com.github.ants280.compgeo.CompGeoUtils;
import com.github.ants280.compgeo.Point;
import com.github.ants280.compgeo.shape.Triangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DelaunayTriangulation
{
	private final Map<Point, Collection<Triangle>> points;
	private final Map<Edge, Collection<Triangle>> edges;

	private final Point p1;
	private final Point p2;
	private final Point p3;

	public DelaunayTriangulation()
	{
		this(Collections.emptyList());
	}

	public DelaunayTriangulation(List<Point> points)
	{
		this.points = new HashMap<>();
		this.edges = new HashMap<>();
		this.p1 = new Point(Double.MAX_VALUE, Double.MAX_VALUE);
		this.p2 = new Point(-Double.MAX_VALUE, Double.MAX_VALUE);
		this.p3 = new Point(Double.MAX_VALUE, -Double.MAX_VALUE);
		init(points);
	}

	private void init(List<Point> points1)
	{
		Triangle initialTriangle = new Triangle(p1, p2, p3);
		points.put(p1, new ArrayList<>(Arrays.asList(initialTriangle)));
		points.put(p2, new ArrayList<>(Arrays.asList(initialTriangle)));
		points.put(p3, new ArrayList<>(Arrays.asList(initialTriangle)));

		edges.put(Edge.fromPoints(p1, p2), createMutableCollection(initialTriangle));
		edges.put(Edge.fromPoints(p2, p3), createMutableCollection(initialTriangle));
		edges.put(Edge.fromPoints(p3, p1), createMutableCollection(initialTriangle));

		points1.forEach(this::addPoint);
	}

	public void addPoint(Point point)
	{
		if (point.getX() < 0 || point.getY() < 0)
		{
			throw new IllegalArgumentException(
					"The point have positive coordinates : " + point);
		}

		if (!points.containsKey(point))
		{
			return; // the point is already in the triangulation.
		}

		List<Triangle> trianglesContainingPoint = points.values()
				.stream()
				.flatMap(Collection::stream)
				.distinct()
				.filter(triangle -> triangle.contains(point))
				.collect(Collectors.toList());
		assert !trianglesContainingPoint.isEmpty();
		assert trianglesContainingPoint.size() <= 2;

		List<Triangle> splitTriangles = splitTriangles(trianglesContainingPoint, point);

		splitTriangles.forEach(this::flipTrianglesAround);
	}

	private List<Triangle> splitTriangles(List<Triangle> trianglesContainingPoint, Point point)
	{
		switch (trianglesContainingPoint.size())
		{
			case 1:
				return splitTriangle(trianglesContainingPoint.get(0), point);
			case 2:
				return splitTrianglesOnEdge(trianglesContainingPoint, point);
			default:
				throw new IllegalArgumentException(String.format("Expected only one or two triangles to contain point %s.  Found %d.", point, trianglesContainingPoint.size()));
		}
	}

	private List<Triangle> splitTriangle(Triangle sourceTriangle, Point point)
	{
		assert sourceTriangle.contains(point);
		assert !sourceTriangle.containsPointOnEdge(point);

		List<Triangle> splitTriangles = new ArrayList<>(3);
		for (Edge sharedEdge : sourceTriangle.getEdges())
		{
			splitTriangles.add(this.createTriangleFromSharedEdge(point, sharedEdge, sourceTriangle));
		}

		assert splitTriangles.size() == 3;
		return splitTriangles;
	}

	private List<Triangle> splitTrianglesOnEdge(
			List<Triangle> sourceTriangles,
			Point point)
	{
		assert sourceTriangles.size() == 2;

		List<Triangle> splitTriangles = new ArrayList<>(4);
		for (Triangle sourceTriangle : sourceTriangles)
		{
			assert sourceTriangle.containsPointOnEdge(point);

			for (Edge sharedEdge : sourceTriangle.getEdges())
			{
				if (CompGeoUtils.getDeterminant(point, sharedEdge.getStartPoint(), sharedEdge.getEndPoint()) < CompGeoUtils.DELTA)
				{
					continue;
				}

				splitTriangles.add(this.createTriangleFromSharedEdge(point, sharedEdge, sourceTriangle));
			}
		}

		assert splitTriangles.size() == 4 : splitTriangles.size();

		return splitTriangles;
	}

	private Triangle createTriangleFromSharedEdge(Point point, Edge sharedEdge, Triangle sourceTriangle)
	{
		Triangle tN = new Triangle(point, sharedEdge.getStartPoint(), sharedEdge.getEndPoint());

		points.get(sharedEdge.getStartPoint()).remove(sourceTriangle);
		points.get(sharedEdge.getEndPoint()).remove(sourceTriangle);
		points.get(sharedEdge.getStartPoint()).add(tN);
		points.get(sharedEdge.getEndPoint()).add(tN);

		edges.get(sharedEdge).remove(sourceTriangle);
		edges.get(sharedEdge).add(tN);

		return tN;
	}

	public List<Triangle> getTriangulationTriangles()
	{
		return points.entrySet()
				.stream()
				.filter(entry -> !entry.getKey().equals(p1)
				&& !entry.getKey().equals(p2)
				&& !entry.getKey().equals(p3))
				.map(Map.Entry::getValue)
				.flatMap(Collection::stream)
				.distinct()
				.collect(Collectors.toList());
	}

	private void flipTrianglesAround(Triangle sourceTriangle)
	{
		Map<Triangle, Edge> sharedEdgeTriangles = getSharedTriangleEdges(sourceTriangle);

		for (Map.Entry<Triangle, Edge> sharedTriangleEdgeEntry : sharedEdgeTriangles.entrySet())
		{
			Triangle otherTriangle = sharedTriangleEdgeEntry.getKey();
			Edge sharedEdge = sharedTriangleEdgeEntry.getValue();
			Point otherTrianglePoint = this.getOtherPoint(sharedEdge, otherTriangle);

			if (sourceTriangle.containsPointInCircle(otherTrianglePoint))
			{
				Point sourceTrianglePoint = getOtherPoint(sharedEdge, sourceTriangle);

				Triangle t1 = new Triangle(sourceTrianglePoint, otherTrianglePoint, sharedEdge.getStartPoint());
				Triangle t2 = new Triangle(sourceTrianglePoint, otherTrianglePoint, sharedEdge.getEndPoint());
				Edge t12Edge = Edge.fromPoints(sourceTrianglePoint, otherTrianglePoint);
				Edge startSourceEdge = Edge.fromPoints(sharedEdge.getStartPoint(), sourceTrianglePoint);
				Edge startOtherEdge = Edge.fromPoints(sharedEdge.getStartPoint(), otherTrianglePoint);
				Edge endSourceEdge = Edge.fromPoints(sharedEdge.getEndPoint(), sourceTrianglePoint);
				Edge endOtherEdge = Edge.fromPoints(sharedEdge.getEndPoint(), otherTrianglePoint);

				points.get(sourceTrianglePoint).remove(sourceTriangle);
				points.get(otherTrianglePoint).remove(otherTriangle);
				points.get(sharedEdge.getStartPoint()).remove(sourceTriangle);
				points.get(sharedEdge.getStartPoint()).remove(otherTriangle);
				points.get(sharedEdge.getEndPoint()).remove(sourceTriangle);
				points.get(sharedEdge.getEndPoint()).remove(otherTriangle);
				edges.remove(sharedEdge);
				edges.get(startSourceEdge).remove(sourceTriangle);
				edges.get(endSourceEdge).remove(sourceTriangle);
				edges.get(startOtherEdge).remove(otherTriangle);
				edges.get(endOtherEdge).remove(otherTriangle);

				points.get(sourceTrianglePoint).add(t1);
				points.get(sourceTrianglePoint).add(t2);
				points.get(otherTrianglePoint).add(t1);
				points.get(otherTrianglePoint).add(t2);
				points.get(sharedEdge.getStartPoint()).add(t1);
				points.get(sharedEdge.getEndPoint()).add(t2);
				edges.put(t12Edge, createMutableCollection(t1, t2));
				edges.get(startSourceEdge).add(t1);
				edges.get(startOtherEdge).add(t1);
				edges.get(endSourceEdge).add(t2);
				edges.get(endOtherEdge).add(t2);

				this.flipTrianglesAround(t1);
				this.flipTrianglesAround(t2);

				return; // points flipped
			}
		}

		// no points flipped
	}

	private Map<Triangle, Edge> getSharedTriangleEdges(Triangle triangle)
	{
		List<Point> trianglePoints = triangle.getPoints();
		Edge e1 = Edge.fromPoints(trianglePoints.get(0), trianglePoints.get(1));
		Edge e2 = Edge.fromPoints(trianglePoints.get(1), trianglePoints.get(2));
		Edge e3 = Edge.fromPoints(trianglePoints.get(2), trianglePoints.get(0));

		Map<Triangle, Edge> sharedTriangleEdges = new HashMap<>();

		for (Edge edge : Arrays.asList(e1, e2, e3))
		{
			Collection<Triangle> edgeTriangles = edges.get(edge);

			if (edgeTriangles.size() != 1)
			{
				assert edgeTriangles.size() == 2 : edgeTriangles.size();

				Triangle otherTriangleForEdge = getOnlyElement(
						edgeTriangles.stream()
								.filter(Predicate.isEqual(triangle).negate()));
				sharedTriangleEdges.put(otherTriangleForEdge, edge);
			}
		}

		return sharedTriangleEdges;
	}

	/**
	 * Get the point in the Triangle not on the Edge.
	 *
	 * @param edge
	 * @param triangle
	 * @return The point in the Triangle not on the Edge.
	 */
	private Point getOtherPoint(Edge edge, Triangle triangle)
	{
		return getOnlyElement(triangle.getPoints()
				.stream()
				.filter(point -> !point.equals(edge.getStartPoint())
				&& !point.equals(edge.getEndPoint())));
	}

	private static <E> E getOnlyElement(Stream<E> stream)
	{
		return stream.reduce((a, b) ->
		{
			throw new IllegalArgumentException();
		})
				.orElseThrow(() -> new IllegalArgumentException());
	}

	private static Collection<Triangle> createMutableCollection(Triangle... triangles)
	{
		Collection<Triangle> mutableCollection = new ArrayList<>();

		Arrays.stream(triangles).forEach(mutableCollection::add);

		return mutableCollection;
	}
}
