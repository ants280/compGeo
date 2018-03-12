package com.jpatterson.school.compGeo.line;

import com.jpatterson.school.compGeo.Point;

public class ParametricLine implements Line<ParametricLine>
{
	protected final Point startPoint;
	protected final Point endPoint;

	public ParametricLine(Point startPoint, Point endPoint)
	{
		this.startPoint = startPoint;
		this.endPoint = endPoint;
	}

	public Point getStartPoint()
	{
		return startPoint;
	}

	public Point getEndPoint()
	{
		return endPoint;
	}
	
	@Override
	public Point getIntersectionPoint(ParametricLine otherLine)
	{
		Double thisLineM = getM(startPoint, endPoint);
		Double otherLineM = getM(otherLine.startPoint, otherLine.endPoint);
		double thisLineB = getB(startPoint, thisLineM);
		double otherLineB = getB(otherLine.startPoint, otherLineM);
			
		if ((thisLineM == null && otherLineM == null)
			|| (thisLineM != null && otherLineM != null
				&& thisLineM.doubleValue() == otherLineM.doubleValue()))
		{
			throw new IllegalArgumentException("Equal slopes: " + this + " " + otherLine);
		}
		
		double x =
			(thisLineM == null)
				? thisLineB
				: (otherLineM == null)
					? otherLineB
					: (thisLineB - otherLineB) / (otherLineM - thisLineM);
		double y = (thisLineM == null)
			? otherLineM * x + otherLineB
			: thisLineM * x + thisLineB;
		
		return new Point( x, y);
	}

	@Override
	public String toString()
	{
		return String.format("Line{startPoint=%s, endPoint=%s}", startPoint, endPoint);
	}
	
	private static Double getM(Point startPoint, Point endPoint)
	{
		return (startPoint.getX() == endPoint.getX())
			? null
			: (0.0 + endPoint.getY() - startPoint.getY()) / (endPoint.getX() - startPoint.getX());
	}
	
	private static double getB(Point startPoint, Double m)
	{
		return (m == null)
			? startPoint.getX()
			: startPoint.getY() - (m * startPoint.getX());
	}
}
