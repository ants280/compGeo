package com.github.ants280.compgeo.ui.worker;

import com.github.ants280.compgeo.Point;
import com.github.ants280.compgeo.algorithm.VoronoiDiagram;
import com.github.ants280.compgeo.ui.CompGeoFrame;
import com.github.ants280.compgeo.ui.VoronoiCell;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class VoronoiDiagramPopupWorker extends CompGeoPopupWorker<Collection<VoronoiCell>>
{
	private final Collection<Point> points;
	private final VoronoiDiagram voronoiDiagram;
	private final double progressPerPoint;

	public VoronoiDiagramPopupWorker(Consumer<Collection<VoronoiCell>> completedAction, CompGeoFrame frame)
	{
		super(completedAction, frame, "Voronoi Diagram being created...");
		this.points = frame.getCanvas().getPoints();
		this.voronoiDiagram = new VoronoiDiagram(
				points,
				frame.getCanvas().getWidth() - 1,
				frame.getCanvas().getHeight() - 1);
		this.progressPerPoint = 1d / points.size();
	}

	@Override
	protected Collection<VoronoiCell> doInBackground()
	{
		return points.stream()
				.map(this::createVoronoiCell)
				.collect(Collectors.toList());
	}

	private VoronoiCell createVoronoiCell(Point point)
	{
		updateProgress(progressPerPoint);

		return new VoronoiCell(voronoiDiagram.getVoronoiCellPoints(point));
	}
}
