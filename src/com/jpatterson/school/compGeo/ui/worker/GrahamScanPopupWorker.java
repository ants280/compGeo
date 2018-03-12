package com.jpatterson.school.compGeo.ui.worker;

import com.jpatterson.school.compGeo.Point;
import com.jpatterson.school.compGeo.algorithm.GrahamScan;
import com.jpatterson.school.compGeo.ui.CompGeoFrame;
import java.util.List;
import java.util.function.Consumer;

public class GrahamScanPopupWorker extends CompGeoPopupWorker<List<Point>>
{
	private final GrahamScan grahamScan;

	public GrahamScanPopupWorker(Consumer<List<Point>> completedAction, CompGeoFrame frame)
	{
		super(completedAction, frame, "Convex Hull being created...");
		this.grahamScan = new GrahamScan(frame.getCanvas().getPoints());
	}

	@Override
	protected List<Point> doInBackground()
	{
		return grahamScan.getConvexHullPoints();
	}
}
