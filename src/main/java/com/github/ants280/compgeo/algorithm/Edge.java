package com.github.ants280.compgeo.algorithm;

import com.github.ants280.compgeo.CompGeoUtils;
import com.github.ants280.compgeo.Point;
import com.github.ants280.compgeo.line.ParametricLine;

public class Edge extends ParametricLine implements Comparable<Edge>
{
	private final Double angle;
	private final int hashCode;

	private Edge(Point startPoint, Point endPoint)
	{
		super(startPoint, endPoint);
		Point rightStartPoint = new Point(startPoint.getX() + 1, startPoint.getY());
		this.angle = CompGeoUtils.getAngle(rightStartPoint, startPoint, endPoint);
		this.hashCode = super.hashCode();
	}

	public static Edge fromPoints(Point p1, Point p2)
	{
		return p1.compareTo(p2) > 0 ? new Edge(p1, p2) : new Edge(p2, p1);
	}

	public boolean containsPoint(Point point)
	{
		return CompGeoUtils.getDeterminant(this.getStartPoint(), this.getEndPoint(), point) == 0d;
	}

	@Override
	public int compareTo(Edge o)
	{
		int startPointCompareTo = this.getStartPoint().compareTo(o.getStartPoint());
		if (startPointCompareTo != 0)
		{
			return startPointCompareTo;
		}
		int angleCompareTo = this.angle.compareTo(o.angle);
		return angleCompareTo == 0 ? this.getEndPoint().compareTo(o.getEndPoint()) : angleCompareTo;
	}

	@Override
	public int hashCode()
	{
		return hashCode;
	}

	@Override
	public boolean equals(Object obj)
	{
		return this == obj
				|| obj != null
				&& this.getClass() == obj.getClass()
				&& super.equals(obj);
	}
}
