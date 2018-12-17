package com.github.ants280.compgeo.ui;

import com.github.ants280.compgeo.Point;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.util.*;

// TODO Extend JComponent rather than Canvas.
public class CompGeoCanvas extends Canvas
{
	private static final long serialVersionUID = 1L;
	private final transient List<Point> points;
	private Polygon convexHull;
	private transient Collection<VoronoiCell> voronoiCells;
	private transient Collection<DelaunayTriangle> delaunayTriangulationTriangles;
	private transient List<Point> bezierCurvePoints;
	private BufferedImage backgroundImage;
	private int pointRadius;
	private int numberRandomPointsToAdd;
	private Color convexHullColor;
	private boolean drawPoints;
	private boolean smoothEdges;
	private boolean colorVoronoiCellRegions;
	private boolean drawPointsLabel;
	private boolean drawDelaunayCircumcircles;

	public static final CompGeoCanvasPreference<Integer> POINT_RADIUS = new CompGeoCanvasPreference.CompGeoCanvasIntegerPreference("POINT_RADIUS", 4);
	public static final CompGeoCanvasPreference<Integer> RANDOM_POINT_COUNT = new CompGeoCanvasPreference.CompGeoCanvasIntegerPreference("RANDOM_POINT_COUNT", 3);
	public static final CompGeoCanvasPreference<Integer> CONVEX_HULL_COLOR = new CompGeoCanvasPreference.CompGeoCanvasIntegerPreference("CONVEX_HULL_COLOR", 0x7fff0000);
	public static final CompGeoCanvasPreference<Boolean> DRAW_POINTS = new CompGeoCanvasPreference.CompGeoCanvasBooleanPreference("DRAW_POINTS", true);
	public static final CompGeoCanvasPreference<Boolean> SMOOTH_EDGES = new CompGeoCanvasPreference.CompGeoCanvasBooleanPreference("SMOOTH_EDGES", true);
	public static final CompGeoCanvasPreference<Boolean> COLOR_VORONOI_CELL_REGIONS = new CompGeoCanvasPreference.CompGeoCanvasBooleanPreference("COLOR_VORONOI_CELL_REGIONS", true);
	public static final CompGeoCanvasPreference<Boolean> SHOW_POINTS_LABEL = new CompGeoCanvasPreference.CompGeoCanvasBooleanPreference("SHOW_POINTS_LABEL", true);
	public static final CompGeoCanvasPreference<Boolean> DRAW_DELAUNAY_CIRCUMCIRCLES = new CompGeoCanvasPreference.CompGeoCanvasBooleanPreference("DRAW_DELAUNAY_CIRCUMCIRCLES", true);

	protected static final Set<CompGeoCanvasPreference<?>> ALL_PREFERENCES
			= new HashSet<>(Arrays.asList(
					POINT_RADIUS, RANDOM_POINT_COUNT, CONVEX_HULL_COLOR,
					DRAW_POINTS, SMOOTH_EDGES, COLOR_VORONOI_CELL_REGIONS, SHOW_POINTS_LABEL, DRAW_DELAUNAY_CIRCUMCIRCLES));

	public CompGeoCanvas()
	{
		super();

		this.points = new ArrayList<>();
		this.convexHull = null;
		this.voronoiCells = null;
		this.delaunayTriangulationTriangles = null;
		this.bezierCurvePoints = null;
		this.backgroundImage = null;

		init();
	}

	private void init()
	{
		this.reloadPreferences();
	}

	public void reloadPreferences()
	{
		this.pointRadius = POINT_RADIUS.getValue();
		this.numberRandomPointsToAdd = RANDOM_POINT_COUNT.getValue();
		this.convexHullColor = new Color(CONVEX_HULL_COLOR.getValue(), true);
		this.drawPoints = DRAW_POINTS.getValue();
		this.smoothEdges = SMOOTH_EDGES.getValue();
		this.colorVoronoiCellRegions = COLOR_VORONOI_CELL_REGIONS.getValue();
		this.drawPointsLabel = SHOW_POINTS_LABEL.getValue();
		this.drawDelaunayCircumcircles = DRAW_DELAUNAY_CIRCUMCIRCLES.getValue();

		this.repaint();
	}

	public void resetSavedPreferences()
	{
		ALL_PREFERENCES.stream().forEach(CompGeoCanvasPreference::setDefaultValue);
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

	public boolean hasVoronoiCells()
	{
		return voronoiCells != null;
	}

	public boolean hasDelaunayTriangulation()
	{
		return delaunayTriangulationTriangles != null;
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
		this.voronoiCells = voronoiCells == null
				? null
				: Collections.unmodifiableCollection(voronoiCells);
		this.repaint();
	}

	public void setDelaunayTriangulationTriangles(Collection<DelaunayTriangle> delaunayTriangulationTriangles)
	{
		this.delaunayTriangulationTriangles = delaunayTriangulationTriangles == null
				? null
				: Collections.unmodifiableCollection(delaunayTriangulationTriangles);
		this.repaint();
	}

	public void setBezierCurvePoints(List<Point> bezierCurvePoints)
	{
		this.bezierCurvePoints = bezierCurvePoints == null
				? null
				: Collections.unmodifiableList(bezierCurvePoints);
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

	public boolean shouldColorVoronoiCellRegions()
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

	public boolean shouldDrawDelaunayCircumcircles()
	{
		return drawDelaunayCircumcircles;
	}

	public void flipDrawDelaunayCircumcircles()
	{
		this.drawDelaunayCircumcircles = !drawDelaunayCircumcircles;

		if (this.delaunayTriangulationTriangles != null)
		{
			this.repaint();
		}
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

		// Draws the shape onto the BufferedImage
		this.paint(lastDrawnImage.getGraphics());

		// Draws the BufferedImage onto the PaintPanel
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
		this.drawDelaunayTriangulation(g);
		this.drawBezierCurvePoints(g);
		this.drawPoints(g);
		this.drawPointsLabel(g);
	}

	private void whiteoutRectangle(Graphics g)
	{
		if (backgroundImage == null)
		{
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
		else
		{
			// TODO: investigate usage of last argument (ImageObserver).
			g.drawImage(backgroundImage, 0, 0, backgroundImage.getWidth(), backgroundImage.getHeight(), this);
		}
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

	private void drawDelaunayTriangulation(Graphics g)
	{
		if (delaunayTriangulationTriangles != null)
		{
			delaunayTriangulationTriangles.stream()
					.forEach(delaunayTriangulationTriangle
							-> this.drawDelaunayTriangulation(g, delaunayTriangulationTriangle));
		}
	}

	private void drawDelaunayTriangulation(Graphics g, DelaunayTriangle delaunayTriangulationTriangle)
	{
		g.setColor(Color.DARK_GRAY);
		g.drawPolygon(delaunayTriangulationTriangle.getTriangle());

		if (drawDelaunayCircumcircles)
		{
			g.setColor(Color.LIGHT_GRAY);

			g.drawLine(
					(int) delaunayTriangulationTriangle.getCircumcircleCenterPoint().getX() - pointRadius,
					(int) delaunayTriangulationTriangle.getCircumcircleCenterPoint().getY() - pointRadius,
					(int) delaunayTriangulationTriangle.getCircumcircleCenterPoint().getX() + pointRadius,
					(int) delaunayTriangulationTriangle.getCircumcircleCenterPoint().getY() + pointRadius);
			g.drawLine(
					(int) delaunayTriangulationTriangle.getCircumcircleCenterPoint().getX() - pointRadius,
					(int) delaunayTriangulationTriangle.getCircumcircleCenterPoint().getY() + pointRadius,
					(int) delaunayTriangulationTriangle.getCircumcircleCenterPoint().getX() + pointRadius,
					(int) delaunayTriangulationTriangle.getCircumcircleCenterPoint().getY() - pointRadius);

			int x = (int) (delaunayTriangulationTriangle.getCircumcircleCenterPoint().getX() - delaunayTriangulationTriangle.getCircumcircleRadius());
			int y = (int) (delaunayTriangulationTriangle.getCircumcircleCenterPoint().getY() - delaunayTriangulationTriangle.getCircumcircleRadius());
			int diameter = (int) (delaunayTriangulationTriangle.getCircumcircleRadius() * 2);
			g.drawOval(x, y, diameter, diameter);
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
			points.stream().forEach(point -> this.drawPoint(g, point));
		}
	}

	private void drawPoint(Graphics g, Point point)
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
			delaunayTriangulationTriangles = null;
			bezierCurvePoints = null;
			this.repaint();
		}
	}

	/**
	 * @return A copy of the points to avoid ConcurrentModificationErrors.
	 */
	public List<Point> getPoints()
	{
		return new ArrayList<>(points);
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
			delaunayTriangulationTriangles = null;
			bezierCurvePoints = null;
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

	public void setBackgroundImage(BufferedImage image)
	{
		this.clear();

		this.backgroundImage = image;

		this.setSize(new Dimension(image.getWidth(), image.getHeight()));

		this.repaint();
	}
}
