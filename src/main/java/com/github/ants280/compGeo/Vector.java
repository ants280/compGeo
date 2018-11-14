package com.github.ants280.compGeo;

public class Vector extends Point
{
	public Vector(Point end, Point start)
	{
		super(end.x - start.x, end.y - start.y);
	}

	/**
	 * @return The Vector, in the form of "[x,y]".
	 */
	@Override
	public String toString()
	{
		return String.format("[%f,%f]", x, y);
	}

	public double getLength()
	{
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}

	public double getDotProduct(Vector other)
	{
		return this.x * other.x + this.y * other.y;
	}

	/**
	 * Gets the determinant with another Vector. Counter-clockwise orientation
	 * between two Vectors should return a positive integer, clockwise
	 * orientation should return a negative number.
	 *
	 * @param other The other Vector to computer the determinant with.
	 * @return The determinant with the other Vector.
	 */
	public double getDeterminant(Vector other)
	{
		return this.x * other.y - other.x * this.y;
	}
}
