package com.github.ants280.compGeo.ui;

import com.github.ants280.compGeo.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Collection;

public class CompGeoComponentListener extends ComponentAdapter
{
	private final CompGeoFrame frame;

	public CompGeoComponentListener(CompGeoFrame frame)
	{
		this.frame = frame;
	}

	@Override
	public void componentResized(ComponentEvent event)
	{
		CompGeoCanvas canvas = frame.getCanvas();
		Collection<Point> points = canvas.getPoints();

		if (!points.isEmpty()
				&& ((PointUiUtils.getMaxPointValue(points, Point::getX) > canvas.getWidth())
				|| (PointUiUtils.getMaxPointValue(points, Point::getY) > canvas.getHeight())))
		{
			frame.clear();
		}
		else
		{
			frame.updatePointControls();
		}
	}
}
