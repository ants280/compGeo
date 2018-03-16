package com.jpatterson.school.compGeo.shape;

import com.jpatterson.school.compGeo.Point;
import java.util.Arrays;
import java.util.Collection;

public class DelaunayTriangle extends CCWTriangle
{
	private DelaunayTriangle t1; // on edge p1 - p2
	private DelaunayTriangle t2; // on edge p2 - p3
	private DelaunayTriangle t3; // on edge p3 - p1

	public DelaunayTriangle(Point p1, Point p2, Point p3, DelaunayTriangle t1, DelaunayTriangle t2, DelaunayTriangle t3)
	{
		super(p1, p2, p3);
		this.t1 = t1;
		this.t2 = t2;
		this.t3 = t3;
	}

	// used as intermediate constructor for splitting DelaunayTriangles
	private DelaunayTriangle(Point p1, Point p2, Point p3)
	{
		this(p1, p2, p3, null, null, null);
	}

	public Collection<DelaunayTriangle> split(Point p)
	{
		if (!contains(p))
		{
			throw new IllegalArgumentException(String.format("DelaunayTriangle cannot be split by %s because the point is not inside it.", p));
		}

		DelaunayTriangle s1 = new DelaunayTriangle(p1, p2, p);
		DelaunayTriangle s2 = new DelaunayTriangle(p, p2, p3);
		DelaunayTriangle s3 = new DelaunayTriangle(p1, p, p3);
		
		s1.t1 = this.t1;
		s1.t2 = s2;
		s1.t3 = s3;
		
		s2.t1 = s1;
		s2.t2 = this.t2;
		s2.t3 = s3;
		
		s3.t1 = s1;
		s3.t2 = s2;
		s3.t3 = this.t3;

		return Arrays.asList(s1, s2, s3);
	}
	
	// Adapted from Incremental Delaunay Triangulation, Dani Lischinski, ACM 1993
	/*
	inline Real TriArea(const Point2d& a, const Point2d& b, const Point2d& c)
// Returns twice the area of the oriented triangle (a, b, c), i.e., the
// area is positive if the triangle is oriented counterclockwise.
{
return (b.x - a.x)*(c.y - a.y) - (b.y - a.y)*(c.x - a.x);
}
int InCircle(const Point2d& a, const Point2d& b,
const Point2d& c, const Point2d& d)
// Returns TRUE if the point d is inside the circle defined by the
// points a, b, c. See Guibas and Stolfi (1985) p.107.
{
return (a.x*a.x + a.y*a.y) * TriArea(b, c, d) -
(b.x*b.x + b.y*b.y) * TriArea(a, c, d) +
(c.x*c.x + c.y*c.y) * TriArea(a, b, d) -
(d.x*d.x + d.y*d.y) * TriArea(a, b, c) > 0;
}
	*/
	
}
