package com.jpatterson.school.compGeo.line;

import static com.jpatterson.school.compGeo.CompGeoUtils.DELTA;
import com.jpatterson.school.compGeo.Point;

public class LineSegment extends ParametricLine
{
	public LineSegment(Point startPoint, Point endPoint)
	{
		super(startPoint, endPoint);
	}
	
	/**
	 * @param line The line to test for intersection
	 * @return The intersection point, or null if the LineSegment is to short to
	 * intersect with the Line.
	 */
	@Override
	public Point getIntersectionPoint(ParametricLine line)
	{
		Point intersectionPoint = super.getIntersectionPoint(line);
		
		if ((intersectionPoint.getX() - DELTA > startPoint.getX() && intersectionPoint.getX() - DELTA > endPoint.getX())
			|| (intersectionPoint.getX() + DELTA < startPoint.getX() && intersectionPoint.getX() + DELTA < endPoint.getX())
			|| (intersectionPoint.getY() - DELTA > startPoint.getY() && intersectionPoint.getY() - DELTA> endPoint.getY()
			|| (intersectionPoint.getY() + DELTA < startPoint.getY() && intersectionPoint.getY() + DELTA < endPoint.getY())))
		{
			throw new IllegalArgumentException(String.format("Line %s does not intersect with line segment %s", line, this));
		}
		
		return intersectionPoint;
	}

	@Override
	public String toString()
	{
		return String.format("LineSegment{%s}", super.toString());
	}
}
