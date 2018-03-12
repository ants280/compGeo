package com.jpatterson.school.compGeo.ui;

import com.jpatterson.school.compGeo.Point;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CompGeoCanvas extends Canvas
{
	private static final long serialVersionUID = 5270647793920053227L;
	private final List<Point> points;
	private Polygon convexHull;
	private Collection<VoronoiCell> voronoiCells;
	private List<Point> bezierCurvePoints;
	private int pointRadius;
	private int numberRandomPointsToAdd;
	private Color convexHullColor;
	private boolean drawPoints;
	private boolean smoothEdges;
	private boolean colorVoronoiCellRegions;
	private boolean drawPointsLabel;

	public CompGeoCanvas()
	{
		super();

		// Using a HashSet leads to unpredictable point drawing, but is fast
//		this.points = new HashSet<>();
		// use a TreeSet so the points stay ordered.  This makes it is predictable which point will be drawn over others (the bottom-right point)
		//this.points = new TreeSet<>();
		// use a LinkedHashSet so the points stay ordered from insertion order.
//		this.points = new LinkedHashSet<>();
		this.points = new ArrayList<>();

		this.convexHull = null;
		this.voronoiCells = null;
		this.bezierCurvePoints = null;
		this.reloadPreferences();
	}

	public final void reloadPreferences()
	{
		this.pointRadius = CompGeoCanvasPreference.POINT_RADIUS.getValue();
		this.numberRandomPointsToAdd = CompGeoCanvasPreference.RANDOM_POINT_COUNT.getValue();
		this.convexHullColor = new Color(CompGeoCanvasPreference.CONVEX_HULL_COLOR.getValue(), true);
		this.drawPoints = CompGeoCanvasPreference.DRAW_POINTS.getValue();
		this.smoothEdges = CompGeoCanvasPreference.SMOOTH_EDGES.getValue();
		this.colorVoronoiCellRegions = CompGeoCanvasPreference.COLOR_VORONOI_CELL_REGIONS.getValue();
		this.drawPointsLabel = CompGeoCanvasPreference.SHOW_POINTS_LABEL.getValue();

		this.repaint();
	}

	/**
	 * {@inheritDoc} Redraws the ConvexHullCanvas when it is resized.
	 */
	@Override
	public void setSize(int width, int height)
	{
		super.setSize(width, height);
		this.repaint();
	}

	public boolean hasConvexHull()
	{
		return convexHull != null;
	}

	public boolean hasVoroniCells()
	{
		return voronoiCells != null;
	}

	public boolean hasBezierCurvePoints()
	{
		return bezierCurvePoints != null;
	}

	public void setConvexHull(Polygon convexHull)
	{
		this.convexHull = convexHull;
		this.repaint();
	}

	public void setVoronoiCells(Collection<VoronoiCell> voronoiCells)
	{
		this.voronoiCells = voronoiCells;
		this.repaint();
	}

	public void setBezierCurvePoints(List<Point> bezierCurvePoints)
	{
		this.bezierCurvePoints = bezierCurvePoints;
		this.repaint();
	}

	public int getPointRadius()
	{
		return pointRadius;
	}

	public void setPointRadius(int pointRadius)
	{
		if (pointRadius < 0)
		{
			throw new IllegalArgumentException("Invalid value: " + pointRadius);
		}
		this.pointRadius = pointRadius;

		this.repaint();
	}

	public int getNumberRandomPointsToAdd()
	{
		return numberRandomPointsToAdd;
	}

	public void setNumberRandomPointsToAdd(int numberRandomPointsToAdd)
	{
		if (numberRandomPointsToAdd <= 0)
		{
			throw new IllegalArgumentException("Invalid value: " + numberRandomPointsToAdd);
		}
		
		this.numberRandomPointsToAdd = numberRandomPointsToAdd;
	}

	public Color getConvexHullColor()
	{
		return convexHullColor;
	}

	public void setConvexHullColor(Color convexHullColor)
	{
		this.convexHullColor = convexHullColor;

		if (this.convexHull != null)
		{
			this.repaint();
		}
	}

	public boolean shouldDrawPoints()
	{
		return drawPoints;
	}

	public void flipDrawPoints()
	{
		this.drawPoints = !drawPoints;

		if (!points.isEmpty())
		{
			this.repaint();
		}
	}

	public boolean shouldSmoothEdges()
	{
		return smoothEdges;
	}

	public void flipSmoothEdges()
	{
		this.smoothEdges = !smoothEdges;

		if (!points.isEmpty())
		{
			this.repaint();
		}
	}

	public boolean shouldColorVoronoiCellRegoins()
	{
		return colorVoronoiCellRegions;
	}

	public void flipColorVoronoiCellRegions()
	{
		this.colorVoronoiCellRegions = !colorVoronoiCellRegions;

		if (voronoiCells != null)
		{
			this.repaint();
		}
	}

	public boolean shouldShowPointsLabel()
	{
		return drawPointsLabel;
	}

	public void flipShowPointsLabel()
	{
		this.drawPointsLabel = !drawPointsLabel;

		this.repaint();
	}

	/**
	 * {@inheritDoc} Contains Double-Buffering logic to make painting more
	 * smooth.
	 *
	 * @param g The Canvass Graphics to pain on.
	 */
	@Override
	public void update(Graphics g)
	{
		BufferedImage lastDrawnImage
			= (BufferedImage) this.createImage(this.getWidth(),
				this.getHeight());

		//Draws the shape onto the BufferedImage
		this.paint(lastDrawnImage.getGraphics());

		//Draws the BufferedImage onto the PaintPanel
		g.drawImage(lastDrawnImage, 0, 0, this);
	}

	@Override
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		RenderingHints rh = new RenderingHints(
			RenderingHints.KEY_ANTIALIASING,
			smoothEdges ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
		g2.setRenderingHints(rh);

		this.whiteoutRectangle(g);
		this.drawVoronoiCells(g);
		this.drawConvexHull(g);
		this.drawBezierCurvePoints(g);
		this.drawPoints(g);
		this.drawPointsLabel(g);
	}

	private void whiteoutRectangle(Graphics g)
	{
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
	}

	private void drawConvexHull(Graphics g)
	{
		if (convexHull != null)
		{
			g.setColor(convexHullColor);
			g.fillPolygon(convexHull);
			g.setColor(Color.BLUE);
			g.drawPolygon(convexHull);
		}
	}

	//TODO: Add Delanauy triangulation support.
	private void drawVoronoiCells(Graphics g)
	{
		if (voronoiCells != null)
		{
			g.setColor(Color.LIGHT_GRAY);
			voronoiCells.forEach(voronoiCell -> this.drawVoronoiCell(voronoiCell, g));

			if (colorVoronoiCellRegions)
			{
				voronoiCells.forEach(voronoiCell -> this.fillVoronoiCell(voronoiCell, g));
			}
		}
	}
	
	private void drawBezierCurvePoints(Graphics g)
	{
		if (bezierCurvePoints != null && !bezierCurvePoints.isEmpty())
		{
			g.setColor(Color.RED);
			
			GeneralPath polyLine = new GeneralPath();
			polyLine.moveTo(
				bezierCurvePoints.get(0).getX(),
				bezierCurvePoints.get(0).getY());
			for (int i = 1; i < bezierCurvePoints.size(); i++)
			{
				polyLine.lineTo(
					bezierCurvePoints.get(i).getX(),
					bezierCurvePoints.get(i).getY());
			}
			((Graphics2D) g).draw(polyLine);
		}
	}

	private void drawPoints(Graphics g)
	{
		if (drawPoints)
		{
			for (Point point : points)
			{
				g.setColor(Color.GRAY);
				g.fillOval(
					(int) point.getX() - pointRadius,
					(int) point.getY() - pointRadius,
					2 * pointRadius,
					2 * pointRadius);

				g.setColor(Color.BLACK);
				g.drawOval(
					(int) point.getX() - pointRadius,
					(int) point.getY() - pointRadius,
					2 * pointRadius,
					2 * pointRadius);
				g.drawLine(
					(int) point.getX(),
					(int) point.getY(),
					(int) point.getX(),
					(int) point.getY());
			}
		}
	}

	private void drawVoronoiCell(VoronoiCell voronoiCell, Graphics g)
	{
		g.drawPolygon(voronoiCell.getPolygon());
	}

	private void fillVoronoiCell(VoronoiCell voronoiCell, Graphics g)
	{
		g.setColor(voronoiCell.getColor());
		g.fillPolygon(voronoiCell.getPolygon());
	}

	private void drawPointsLabel(Graphics g)
	{
		if (drawPointsLabel)
		{
			g.setColor(Color.DARK_GRAY);
			FontMetrics fontMetrics = g.getFontMetrics();

			String pointCount = "Points: " + points.size() + " ";
			int pointCountWidth = pointCount.chars()
				.map(fontMetrics::charWidth)
				.sum();
			int pointCountHeight = fontMetrics.getHeight() / 2;

			int x = this.getWidth() - pointCountWidth;
			int y = this.getHeight() - pointCountHeight;
			g.drawString(pointCount, x, y);
		}
	}

	/**
	 * Removes the points
	 */
	public void clear()
	{
		if (!points.isEmpty())
		{
			points.clear();
			convexHull = null;
			voronoiCells = null;
			this.repaint();
		}
	}

	/**
	 * @return A copy of the points to avoid ConcurrentModificationErrors.
	 */
	public List<Point> getPoints()
	{
		return points.stream()
			.collect(Collectors.toList());
	}

	/**
	 * Adds a Point to the Set of Points to create the Convex Hull from. Only
	 * adds the Point to the Set if an equal Point has not already been added.
	 *
	 * @param additionalPoints The Points to add the Set of Points to create the
	 * Convex Hull from.
	 * @return True if the points were added to the Set of Point.
	 */
	public boolean addPoints(Point... additionalPoints)
	{
		if (!this.canAcceptPoints(additionalPoints.length))
		{
			return false;
		}

		boolean pointAdded = false;
		Set<Point> existingPoints = new HashSet<>(points);
		for (Point point : additionalPoints)
		{
			if (!existingPoints.contains(point) && this.canAcceptPoints(1))
			{
				points.add(point);
				pointAdded = true;
			}
		}

		if (pointAdded)
		{
			convexHull = null;
			voronoiCells = null;
			this.repaint();
		}
		return pointAdded;
	}

	/**
	 * Returns whether or not certain number of points can be added to the
	 * canvas.
	 *
	 * @param additionalPoints The number of points to be added to the canvas.
	 * @return whether or not certain number of points can be added to the
	 * canvas.
	 */
	public boolean canAcceptPoints(int additionalPoints)
	{
		return points.size() + additionalPoints <= this.getMaximumPoints();
	}

	public int getPointCount()
	{
		return points.size();
	}

	/**
	 * Gets the maximum number of Points to create a Convex Hull from.
	 *
	 * @return The maximum number of Points to create a Convex Hull from.
	 */
	public int getMaximumPoints()
	{
		return this.getWidth() * this.getHeight();
	}
}
