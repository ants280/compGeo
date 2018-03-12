package com.jpatterson.school.compGeo.algorithm;

import com.jpatterson.school.compGeo.CompGeoUtils;
import com.jpatterson.school.compGeo.Point;
import com.jpatterson.school.compGeo.line.BisectorLine;
import com.jpatterson.school.compGeo.line.LineSegment;
import com.jpatterson.school.compGeo.line.ParametricLine;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class VoronoiDiagram
{
	private final Collection<Point> points;
	private final int maxWidth;
	private final int maxHeight;

	public VoronoiDiagram(Collection<Point> points, int maxWidth, int maxHeight)
	{
		this.points = points;
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
	}

	public Map<Point, List<Point>> getVoronoiCells()
	{
		return points.stream()
			.collect(Collectors.toMap(
				Function.identity(),
				this::getVoronoiCellPoints));
	}

	public List<Point> getVoronoiCellPoints(Point point)
	{
		try
		{
			List<Point> voroniCellPoints = new LinkedList<>(Arrays.asList(
				new Point(0, 0),
				new Point(maxWidth, 0),
				new Point(maxWidth, maxHeight),
				new Point(0, maxHeight)));

			for (Point otherPoint : points)
			{
				if (!otherPoint.equals(point))
				{
					splitVoronoiCellPoints(
						voroniCellPoints,
						new BisectorLine(point, otherPoint),
						point);
				}
			}

			return voroniCellPoints;
		}
		catch (Exception ex)
		{
			throw new RuntimeException("Problem getting voroni cell for point " + point + " for points: " + points, ex);
		}
	}

	private void splitVoronoiCellPoints(List<Point> voronoiCellPoints, ParametricLine splitLine, Point pointInVoronoiCell)
	{
		if (voronoiCellPoints.isEmpty())
		{
			return;
		}

		boolean pointInVoroniCellCcwToSplitLine = isCcw(splitLine, pointInVoronoiCell);

		int i = 0;
		Point firstPoint = voronoiCellPoints.get(0);
		Point previousPoint;
		if (pointInVoroniCellCcwToSplitLine != isCcw(splitLine, voronoiCellPoints.get(i)))
		{
			do
			{
				previousPoint = voronoiCellPoints.remove(i);
			}
			while (i < voronoiCellPoints.size() && pointInVoroniCellCcwToSplitLine != isCcw(splitLine, voronoiCellPoints.get(i)));

			if (i < voronoiCellPoints.size())
			{
				voronoiCellPoints.add(
					i,
					getIntersectionPoint(
						splitLine,
						previousPoint,
						voronoiCellPoints.get(i)));
				previousPoint = voronoiCellPoints.get(i);
				i++;

				while (i < voronoiCellPoints.size() && pointInVoroniCellCcwToSplitLine == isCcw(splitLine, voronoiCellPoints.get(i)))
				{
					previousPoint = voronoiCellPoints.get(i);
					i++;
				}

				voronoiCellPoints.add(
					i,
					getIntersectionPoint(
						splitLine,
						previousPoint,
						i == voronoiCellPoints.size() ? firstPoint : voronoiCellPoints.get(i)));
				i++;

				// Remove remaining points.  They should all be out of the voronoi cell.
				while (i < voronoiCellPoints.size())
				{
					voronoiCellPoints.remove(i);
				}
			}
		}
		else
		{
			do
			{
				i++;
			}
			while (i < voronoiCellPoints.size() && pointInVoroniCellCcwToSplitLine == isCcw(splitLine, voronoiCellPoints.get(i)));

			if (i < voronoiCellPoints.size())
			{
				voronoiCellPoints.add(
					i,
					getIntersectionPoint(
						splitLine,
						voronoiCellPoints.get(i - 1),
						voronoiCellPoints.get(i)));
				previousPoint = voronoiCellPoints.get(i);
				i++;

				do
				{
					previousPoint = voronoiCellPoints.remove(i);
				}
				while (i < voronoiCellPoints.size() && pointInVoroniCellCcwToSplitLine != isCcw(splitLine, voronoiCellPoints.get(i)));

				voronoiCellPoints.add(
					i,
					getIntersectionPoint(
						splitLine,
						previousPoint,
						i == voronoiCellPoints.size() ? firstPoint : voronoiCellPoints.get(i)));

				// keep remaining points.  they are all in the voronoi cell.
			}
		}
	}

	/**
	 * Determines whether the point is in counter-clockwise orientation to the
	 * specified line segment.
	 *
	 * @param line The line segment to test the point against.
	 * @param point The point in question.
	 * @return Whether or not the point is in counter-clockwise orientation to
	 * the specified line segment.
	 */
	private static boolean isCcw(ParametricLine line, Point point)
	{
		return (CompGeoUtils.getDeterminant(line.getStartPoint(), line.getEndPoint(), point) > 0);
	}

	private static Point getIntersectionPoint(ParametricLine splitLine, Point startPoint, Point endPoint)
	{
		return new LineSegment(startPoint, endPoint)
			.getIntersectionPoint(splitLine);
	}

	@Override
	public String toString()
	{
		return String.format("VoronoiDiagram{points=%s, maxWidth=%s, maxHeight=%s}", points, maxWidth, maxHeight);
	}
}
