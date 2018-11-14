package com.github.ants280.compGeo.line;

import com.github.ants280.compGeo.Point;

/**
 * A line perpendicular to the specified points.
 */
public class BisectorLine extends ParametricLine
{
	public BisectorLine(Point startPoint, Point endPoint)
	{
		super(
				new Point(
						((startPoint.getX() + endPoint.getX()) - (endPoint.getY() - startPoint.getY())) / 2,
						((startPoint.getY() + endPoint.getY()) + (endPoint.getX() - startPoint.getX())) / 2),
				new Point(
						((startPoint.getX() + endPoint.getX()) + (endPoint.getY() - startPoint.getY())) / 2,
						((startPoint.getY() + endPoint.getY()) - (endPoint.getX() - startPoint.getX())) / 2));
	}

	@Override
	public String toString()
	{
		return String.format("BisectorLine{%s}", super.toString());
	}
}
