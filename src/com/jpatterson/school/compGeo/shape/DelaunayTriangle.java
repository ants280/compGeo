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
}
