package com.jpatterson.school.compGeo.ui.worker;

import com.jpatterson.school.compGeo.Point;
import com.jpatterson.school.compGeo.algorithm.DelaunayTriangulation;
import com.jpatterson.school.compGeo.ui.CompGeoFrame;
import java.awt.Polygon;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;

public class DelaunayTriangulationPopupWorker extends CompGeoPopupWorker<Collection<Polygon>>
{
	private final Collection<Point> points;
	private final DelaunayTriangulation delaunayTriangulation;
	private final double progressPerPoint;

	public DelaunayTriangulationPopupWorker(Consumer<Collection<Polygon>> completedAction, CompGeoFrame frame)
	{
		super(completedAction, frame, "Delaunay Triangulation being created...");
		this.points = frame.getCanvas().getPoints();
		this.delaunayTriangulation = new DelaunayTriangulation(
			points,
			frame.getCanvas().getWidth(),
			frame.getCanvas().getHeight());
		this.progressPerPoint = 1d / points.size();
	}

	@Override
	protected Collection<Polygon> doInBackground()
	{
		return Collections.singleton(new Polygon(new int[] { 1, 50, 1 }, new int[] { 1, 1, 50 },  3)); // TODO
	}
}
