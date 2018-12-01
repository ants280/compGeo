package com.github.ants280.compgeo.ui;

import com.github.ants280.compgeo.CompGeoUtils;
import com.github.ants280.compgeo.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
		Point point = PointUiUtils.getPoint(event);
		switch (event.getButton())
		{
			case MouseEvent.BUTTON1: // left
				frame.addPoints(point);
				break;
			case MouseEvent.BUTTON3: // right
				this.printPointDistances(point);
				break;
			default:
				break;
		}
	}

	private void printPointDistances(Point point)
	{
		System.out.printf("Distances from %s to points:%n", point);

		frame.getCanvas().getPoints()
				.stream()
				.collect(Collectors.toMap(
						Function.identity(),
						otherPoint -> CompGeoUtils.getDistance(point, otherPoint)))
				.entrySet()
				.stream()
				.sorted(Comparator.comparing(Map.Entry::getValue))
				.forEach(entry -> System.out.printf(
				"\t%s is at distance %.2f%n",
				entry.getKey(),
				entry.getValue()));
	}
}
