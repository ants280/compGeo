package com.github.ants280.compgeo.ui.worker;

import com.github.ants280.compgeo.Point;
import com.github.ants280.compgeo.algorithm.VoronoiDiagram;
import com.github.ants280.compgeo.ui.VoronoiCell;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.swing.JFrame;

public class VoronoiDiagramPopupWorker extends CompGeoPopupWorker<Collection<VoronoiCell>>
{
	private final Collection<Point> points;
	private final VoronoiDiagram voronoiDiagram;
	private final double progressPerPoint;

	public VoronoiDiagramPopupWorker(
			Consumer<Collection<VoronoiCell>> completedAction,
			JFrame frame,
			List<Point> points,
			int width,
			int height)
	{
		super(completedAction, frame, "Voronoi Diagram being created...");
		this.points = points;
		this.voronoiDiagram = new VoronoiDiagram(
				points,
				width - 1,
				height - 1);
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
