package com.github.ants280.compgeo;

public class Point implements Comparable<Point>
{
	protected final double x;
	protected final double y;

	public Point(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public double getX()
	{
		return x;
	}

	public double getY()
	{
		return y;
	}

	/**
	 * @return The Point, in the form of "(x,y)".
	 */
	@Override
	public String toString()
	{
		return String.format("%f,%f)", x, y);
	}

	/**
	 * Compares the points by y coordinates. If the Point's y coordinates are
	 * equal, compares by x coordinates.
	 *
	 * @param other The point to compare this point to.
	 * @return The lowest or leftmost point.
	 */
	@Override
	public int compareTo(Point other)
	{
		double diffY = this.getY() - other.getY();

		if (diffY == 0d)
		{
			double diffX = this.getX() - other.getX();
			if (diffX == 0)
			{
				return 0;
			}

			return diffX > 0 ? 1 : -1;
		}
		else
		{
			return diffY > 0 ? 1 : -1;
		}
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof Point
				&& this.x == ((Point) obj).x
				&& this.y == ((Point) obj).y;
		// TODO: Improve equals() to below:
//		return this == obj
//				|| obj != null
//				&& this.getClass() == obj.getClass()
//				&& this.x == ((Point) obj).x
//				&& this.y == ((Point) obj).y;
	}

	@Override
	public int hashCode()
	{
		int hash = 5;
		hash = 43 * hash + Double.hashCode(this.x);
		hash = 43 * hash + Double.hashCode(this.y);
		return hash;
	}
}
