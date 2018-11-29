package com.github.ants280.compgeo;

/**
 * An Set of utilities for doing math on Points.
 *
 * @author Jacob Patterson (jacob.patterson@gmail.com)
 * @version 1.2
 */
public class CompGeoUtils
{
	public static final double DELTA = 0.000001d;

	/**
	 * Creates a new CompGeoUtils
	 */
	private CompGeoUtils()
	{
	}

	/**
	 * Gets the smallest angle between Point p0 and Point p2 at Point p1. Uses
	 * the dot product of two Vectors (from p1) to calculate the angle. Returns
	 * 0 if p0 or p2 is the same as p1.
	 *
	 * @param p0 The first Point.
	 * @param p1 The vertex of p0 and p2.
	 * @param p2 The second Point.
	 * @return The angle between p0 and p2 at p1.
	 */
	public static double getAngle(Point p0, Point p1, Point p2)
	{
		if (p0.equals(p1) || p2.equals(p1))
		{
			return -1d;
		}

		Vector v0 = new Vector(p0, p1);
		Vector v1 = new Vector(p2, p1);

		return Math.acos(v0.getDotProduct(v1)
				/ (v0.getLength() * v1.getLength()));
	}

	/**
	 * Gets the determinant between three Points. Used to tell if three Points
	 * are in clockwise (negative) are counter-clockwise order.
	 *
	 * @param p0 The first Point.
	 * @param p1 The second Point.
	 * @param p2 The third Point.
	 * @return The determinant between the three Points.
	 */
	public static double getDeterminant(Point p0, Point p1, Point p2)
	{
		Vector v0 = new Vector(p0, p1);
		Vector v1 = new Vector(p2, p1);

		return v0.getDeterminant(v1);
	}

	/**
	 * Gets the distance between two Points. Uses the Pythagorean Theorem.
	 *
	 * @param p0 The first Point.
	 * @param p1 The second Point.
	 * @return The distance between the two Points.
	 */
	public static double getDistance(Point p0, Point p1)
	{
		return Math.sqrt(Math.pow(p1.getX() - p0.getX(), 2)
				+ Math.pow(p1.getY() - p0.getY(), 2));
	}

	public static IllegalArgumentException createIllegalArgumentException(String message, Object... variables)
	{
		StringBuilder messageBuilder = new StringBuilder(message);

		if (variables != null && variables.length > 0)
		{
			messageBuilder.append(".  Found ");

			for (int i = 0; i < variables.length; i++)
			{
				messageBuilder.append("%s");
				if (i + 1 < variables.length)
				{
					messageBuilder.append(", ");
				}
			}
		}

		return new IllegalArgumentException(String.format(messageBuilder.toString(), variables));
	}
}
