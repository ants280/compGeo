package com.github.ants280.compgeo.algorithm;

import com.github.ants280.compgeo.CompGeoUtils;
import com.github.ants280.compgeo.Point;
import com.github.ants280.compgeo.shape.Triangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DelaunayTriangulation
{
	private final Map<Point, Collection<Triangle>> points;
	private final Map<Edge, Collection<Triangle>> edges;
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
		this.points = new TreeMap<>();
		this.edges = new HashMap<>();
		this.p1 = new Point(-1d, -1d);
		this.p2 = new Point(maxX * 2d, -1d);
		this.p3 = new Point(-1d, maxY * 2d);
		init(points);
	}

	private void init(List<Point> points1)
	{
		Triangle initialTriangle = new Triangle(p1, p2, p3);
		points.put(p1, createMutableCollection(initialTriangle));
		points.put(p2, createMutableCollection(initialTriangle));
		points.put(p3, createMutableCollection(initialTriangle));

		edges.put(Edge.fromPoints(p1, p2), createMutableCollection(initialTriangle));
		edges.put(Edge.fromPoints(p2, p3), createMutableCollection(initialTriangle));
		edges.put(Edge.fromPoints(p3, p1), createMutableCollection(initialTriangle));

		points1.forEach(this::addPoint);
	}

	public void addPoint(Point point)
	{
		if (point.getX() < 0 || point.getX() > maxX || point.getY() < 0 || point.getY() > maxY)
		{
			throw new IllegalArgumentException(String.format("The point being added to the delaunay triangulation (%s) must lie within the [0,0] and [%d,%d] rectangle.", point, maxX, maxY));
		}

		if (points.containsKey(point))
		{
			return; // the point is already in the triangulation.
		}

		Triangle triangleContainingPoint = points.keySet()
				.stream()
				.collect(Collectors.toMap(
						Function.identity(),
						existingPoint -> CompGeoUtils.getDistance(point, existingPoint)))
				.entrySet()
				.stream()
				.sorted(Comparator.comparing(Map.Entry::getValue))
				.map(Map.Entry::getKey)
				.map(points::get)
				.flatMap(Collection::stream)
				.distinct()
				.filter(triangle -> triangle.contains(point))
				.findFirst()
				.orElseThrow(() -> new AssertionError("No triangle contained " + point));

		List<Triangle> splitTriangles = this.splitTriangles(triangleContainingPoint, point);

		this.addSplitTriangles(point, splitTriangles);

		List<Triangle> trianglesToFlip = new ArrayList<>(splitTriangles);
		trianglesToFlip.forEach(this::flipTrianglesAround);
	}

	private void addSplitTriangles(Point point, List<Triangle> splitTriangles)
	{
		points.put(point, splitTriangles);

		Map<Edge, Long> splitTriangleEdges = splitTriangles.stream()
				.map(Triangle::getEdges)
				.flatMap(Collection::stream)
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		for (Map.Entry<Edge, Long> splitTriangleEdgeEntry : splitTriangleEdges.entrySet())
		{
			Edge edge = splitTriangleEdgeEntry.getKey();
			int sharedCount = splitTriangleEdgeEntry.getValue().intValue();
			switch (sharedCount)
			{
				case 1:
					// the edge is not shared between the splitTriangles
					break;
				case 2:
					List<Triangle> trianglesWithEdge = splitTriangles.stream()
							.filter(triangle -> triangle.getEdges().contains(edge))
							.collect(Collectors.toList());
					edges.put(edge, new ArrayList<>(trianglesWithEdge));
					break;
				default:
					throw new IllegalArgumentException(String.format(
							"%s is shared between triangles unexpected number of times: %d",
							edge,
							sharedCount));
			}
		}
	}

	private List<Triangle> splitTriangles(Triangle triangleContainingPoint, Point point)
	{
		if (triangleContainingPoint.containsPointOnEdge(point))
		{
			Triangle otherTriangleContainingPoint = getOnlyElement(
					this.getSharedTriangleEdges(triangleContainingPoint)
							.entrySet()
							.stream()
							.filter(sharedTriangleEdgeEntry
									-> sharedTriangleEdgeEntry.getValue()
									.containsPoint(point))
							.map(Map.Entry::getKey));

			return this.splitTrianglesOnEdge(
					point,
					triangleContainingPoint,
					otherTriangleContainingPoint);
		}
		else
		{
			assert triangleContainingPoint.contains(point);

			return this.splitTriangle(triangleContainingPoint, point);
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
			Point point,
			Triangle... sourceTriangles)
	{
		assert sourceTriangles.length == 2;

		List<Triangle> splitTriangles = new ArrayList<>(4);
		for (Triangle sourceTriangle : sourceTriangles)
		{
			assert sourceTriangle.containsPointOnEdge(point);

			for (Edge sharedEdge : sourceTriangle.getEdges())
			{
				if (sharedEdge.containsPoint(point))
				{
					edges.remove(sharedEdge);
				}
				else
				{
					splitTriangles.add(this.createTriangleFromSharedEdge(point, sharedEdge, sourceTriangle));
				}
			}
		}

		assert splitTriangles.size() == 4 : splitTriangles.size();

		return splitTriangles;
	}

	private Triangle createTriangleFromSharedEdge(Point point, Edge sharedEdge, Triangle sourceTriangle)
	{
		assert sourceTriangle.getEdges().contains(sharedEdge);
		assert edges.containsKey(sharedEdge);

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
				.filter(triangle -> !triangle.getPoints().contains(p1)
				&& !triangle.getPoints().contains(p2)
				&& !triangle.getPoints().contains(p3))
				.collect(Collectors.toList());
	}

	private void flipTrianglesAround(Triangle sourceTriangle)
	{
		Map<Triangle, Edge> sharedEdgeTriangles = this.getSharedTriangleEdges(sourceTriangle);

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
		Map<Triangle, Edge> sharedTriangleEdges = new HashMap<>();

		for (Edge edge : triangle.getEdges())
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
				.orElseThrow(IllegalArgumentException::new);
	}

	private static Collection<Triangle> createMutableCollection(Triangle... triangles)
	{
		Collection<Triangle> mutableCollection = new ArrayList<>();

		Arrays.stream(triangles).forEach(mutableCollection::add);

		return mutableCollection;
	}
}
