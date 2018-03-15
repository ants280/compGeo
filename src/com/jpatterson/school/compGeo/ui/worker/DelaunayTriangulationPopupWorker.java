package com.jpatterson.school.compGeo.ui.worker;

import com.jpatterson.school.compGeo.Point;
import com.jpatterson.school.compGeo.algorithm.DelaunayTriangulation;
import com.jpatterson.school.compGeo.ui.CompGeoFrame;
import com.jpatterson.school.compGeo.ui.PointUiUtils;
import java.awt.Polygon;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DelaunayTriangulationPopupWorker extends CompGeoPopupWorker<Collection<Polygon>>
{
	private final Collection<Point> points;
	private final DelaunayTriangulation delaunayTriangulation;

	public DelaunayTriangulationPopupWorker(Consumer<Collection<Polygon>> completedAction, CompGeoFrame frame)
	{
		super(completedAction, frame, "Delaunay Triangulation being created...");
		this.points = frame.getCanvas().getPoints();
		this.delaunayTriangulation = new DelaunayTriangulation(points);
	}

	@Override
	protected Collection<Polygon> doInBackground()
	{
		return delaunayTriangulation.getTriangulationTriangles()
			.stream()
			.map(this::createDelaunayTriangle)
			.collect(Collectors.toList());
	}

	private Polygon createDelaunayTriangle(List<Point> points)
	{
		assert points.size() == 3;

		return PointUiUtils.createPolygon(points);
	}
}
