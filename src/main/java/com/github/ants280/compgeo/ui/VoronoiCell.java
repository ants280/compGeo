package com.github.ants280.compgeo.ui;

import com.github.ants280.compgeo.Point;
import java.awt.Color;
import java.awt.Polygon;
import java.util.List;
import java.util.Random;

public class VoronoiCell
{
	private static final Random COLOR_GENERATOR = new Random();
	private static final int MAX_COLOR_VALUE = 256;
	private static final int MIN_ALPHA_VALUE = MAX_COLOR_VALUE / 4;
	private final Polygon polygon;
	private final Color color;

	public VoronoiCell(List<Point> points)
	{
		this.polygon = PointUiUtils.createPolygon(points);
		this.color = createRandomColor();
	}

	public Polygon getPolygon()
	{
		return polygon;
	}

	public Color getColor()
	{
		return color;
	}

	private static Color createRandomColor()
	{
		int r = COLOR_GENERATOR.nextInt(MAX_COLOR_VALUE);
		int g = COLOR_GENERATOR.nextInt(MAX_COLOR_VALUE);
		int b = COLOR_GENERATOR.nextInt(MAX_COLOR_VALUE);
		int a = COLOR_GENERATOR.nextInt(MAX_COLOR_VALUE - MIN_ALPHA_VALUE) + MIN_ALPHA_VALUE;

		return new Color(r, g, b, a);
	}
}
