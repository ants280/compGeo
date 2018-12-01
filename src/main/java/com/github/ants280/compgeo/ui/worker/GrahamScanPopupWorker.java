package com.github.ants280.compgeo.ui.worker;

import com.github.ants280.compgeo.Point;
import com.github.ants280.compgeo.algorithm.GrahamScan;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.JFrame;

public class GrahamScanPopupWorker extends CompGeoPopupWorker<List<Point>>
{
	private final GrahamScan grahamScan;

	public GrahamScanPopupWorker(
			Consumer<List<Point>> completedAction,
			JFrame frame,
			List<Point> points)
	{
		super(completedAction, frame, "Convex Hull being created...");
		this.grahamScan = new GrahamScan(points);
	}

	@Override
	protected List<Point> doInBackground()
	{
		return grahamScan.getConvexHullPoints();
	}
}
