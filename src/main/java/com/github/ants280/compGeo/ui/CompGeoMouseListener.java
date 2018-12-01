package com.github.ants280.compgeo.ui;

import com.github.ants280.compgeo.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CompGeoMouseListener extends MouseAdapter
{
	private final CompGeoFrame frame;

	public CompGeoMouseListener(CompGeoFrame frame)
	{
		this.frame = frame;
	}

	@Override
	public void mouseReleased(MouseEvent event)
	{
		if (event.getButton() == MouseEvent.BUTTON1) // left
		{
			Point point = PointUiUtils.getPoint(event);
			frame.addPoints(point);
		}
	}
}
