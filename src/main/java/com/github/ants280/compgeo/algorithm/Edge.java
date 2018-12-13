package com.github.ants280.compgeo.algorithm;

import com.github.ants280.compgeo.CompGeoUtils;
import com.github.ants280.compgeo.Point;
import com.github.ants280.compgeo.line.ParametricLine;

public class Edge extends ParametricLine implements Comparable<Edge>
{
	private final double angle;

	private Edge(Point startPoint, Point endPoint)
	{
		super(startPoint, endPoint);
		Point rightStartPoint = new Point(startPoint.getX() + 1, startPoint.getY());
		this.angle = CompGeoUtils.getAngle(rightStartPoint, startPoint, endPoint);
	}

	public static Edge fromPoints(Point p1, Point p2)
	{
		return p1.compareTo(p2) > 0 ? new Edge(p1, p2) : new Edge(p2, p1);
	}

	private Double getAngle()
	{
		return angle;
	}

	@Override
	public int compareTo(Edge o)
	{
		int startPointCompareTo = this.getStartPoint().compareTo(o.getStartPoint());
		if (startPointCompareTo != 0)
		{
			return startPointCompareTo;
		}
		int angleCompareTo = this.getAngle().compareTo(o.getAngle());
		return angleCompareTo == 0 ? this.getEndPoint().compareTo(o.getEndPoint()) : angleCompareTo;
	}

	@Override
	public int hashCode()
	{
		int hash = 3;
		hash = 59 * hash + (int) (Double.doubleToLongBits(this.angle) ^ (Double.doubleToLongBits(this.angle) >>> 32));
		return hash;
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
