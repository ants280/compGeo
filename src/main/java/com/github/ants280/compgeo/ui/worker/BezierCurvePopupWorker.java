package com.github.ants280.compgeo.ui.worker;

import com.github.ants280.compgeo.Point;
import com.github.ants280.compgeo.algorithm.BezierCurve;
import com.github.ants280.compgeo.ui.CompGeoFrame;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BezierCurvePopupWorker extends CompGeoPopupWorker<List<Point>>
{
	private final BezierCurve bezierCurve;
	private static final int POINTS_PER_CALCULATION = 10;
	// It might be useful to recalculate with higher precision if the points are only slightly past the max difference.
	private static final Double MAX_POINT_DIFFERENCE = 0.5d;

	public BezierCurvePopupWorker(Consumer<List<Point>> completedAction, CompGeoFrame frame)
	{
		super(completedAction, frame, "Bezier Curve being created...");
		this.bezierCurve = new BezierCurve(frame.getCanvas().getPoints());
	}

	@Override
	protected List<Point> doInBackground()
	{
		return getBezierCurvePoints(0, 1, 1);
	}

	private List<Point> getBezierCurvePoints(double tMin, double tMax, double calculationPercentage)
	{
		List<Point> points = bezierCurve.getPoints(tMin, tMax, POINTS_PER_CALCULATION, MAX_POINT_DIFFERENCE);
		if (points != null)
		{
			updateProgress(calculationPercentage);

			return points;
		}

		points = new ArrayList<>(POINTS_PER_CALCULATION * 2); // move if recursion occurs
		double tMid = (tMin + tMax) / 2;
		List<Point> points1 = getBezierCurvePoints(tMin, tMid, calculationPercentage / 2);
		List<Point> points2 = getBezierCurvePoints(tMid, tMax, calculationPercentage / 2);

		if (!points1.get(points1.size() - 1).equals(points2.get(0)))
		{
			throw new RuntimeException(String.format(
					"Last point of first half of points should equal first point of last half of points. %s, tMin=%f, tMax=%f",
					bezierCurve, tMin, tMax));
		}

		points.addAll(points1);
		points.addAll(points2.subList(1, points2.size()));
		return points;
	}
}
