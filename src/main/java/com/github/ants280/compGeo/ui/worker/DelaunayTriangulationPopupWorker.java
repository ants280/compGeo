package com.github.ants280.compGeo.ui.worker;

import com.github.ants280.compGeo.Point;
import com.github.ants280.compGeo.algorithm.DelaunayTriangulation;
import com.github.ants280.compGeo.ui.CompGeoFrame;
import com.github.ants280.compGeo.ui.DelaunayTriangle;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DelaunayTriangulationPopupWorker extends CompGeoPopupWorker<Collection<DelaunayTriangle>>
{
	private final Collection<Point> points;
	private final DelaunayTriangulation delaunayTriangulation;
	private final double progressPerPoint;

	public DelaunayTriangulationPopupWorker(Consumer<Collection<DelaunayTriangle>> completedAction, CompGeoFrame frame)
	{
		super(completedAction, frame, "Delaunay Triangulation being created...");
		this.points = frame.getCanvas().getPoints();
		this.delaunayTriangulation = new DelaunayTriangulation(frame.getCanvas().getWidth(), frame.getCanvas().getHeight());
		this.progressPerPoint = 1d / points.size();
	}

	@Override
	protected Collection<DelaunayTriangle> doInBackground()
	{
		points.forEach(this::addPointToTriangulation);

		return delaunayTriangulation.getTriangulationTriangles()
				.stream()
				.map(DelaunayTriangle::new)
				.collect(Collectors.toList());
	}

	private void addPointToTriangulation(Point point)
	{
		updateProgress(progressPerPoint);

		delaunayTriangulation.addPoint(point);
	}
}
