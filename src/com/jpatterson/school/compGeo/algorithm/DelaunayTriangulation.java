package com.jpatterson.school.compGeo.algorithm;

import com.jpatterson.school.compGeo.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DelaunayTriangulation
{
	private final List<Point> points;
	private final List<List<Point>> triangulationTriangles;

	public DelaunayTriangulation()
	{
		this(Collections.emptyList());
	}
	
	public DelaunayTriangulation(List<Point> points)
	{
		this.points = new ArrayList<>(points);
		this.triangulationTriangles = new ArrayList<>();
	}

	public void addPoint(Point point)
	{
		points.add(point);
		
		// TODO: add another triangle...
	}

	public List<List<Point>> getTriangulationTriangles()
	{
		return triangulationTriangles.stream()
			.map(ArrayList::new)
			.collect(Collectors.toList());
	}
}
