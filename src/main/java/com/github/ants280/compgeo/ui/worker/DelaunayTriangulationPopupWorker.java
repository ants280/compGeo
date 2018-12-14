package com.github.ants280.compgeo.ui.worker;

import com.github.ants280.compgeo.Point;
import com.github.ants280.compgeo.algorithm.DelaunayTriangulation;
import com.github.ants280.compgeo.ui.DelaunayTriangle;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.swing.JFrame;

public class DelaunayTriangulationPopupWorker extends CompGeoPopupWorker<Collection<DelaunayTriangle>>
{
	private final Collection<Point> points;
	private final DelaunayTriangulation delaunayTriangulation;
	private final double progressPerPoint;

	public DelaunayTriangulationPopupWorker(
			Consumer<Collection<DelaunayTriangle>> completedAction,
			JFrame frame,
			List<Point> points,
			int width,
			int height)
	{
		super(completedAction, frame, "Delaunay Triangulation being created...");
		this.points = points;
		this.delaunayTriangulation = new DelaunayTriangulation(width, height);
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
