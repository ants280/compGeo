package com.github.ants280.compgeo.ui;

import com.github.ants280.compgeo.Point;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Polygon;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class CompGeoFrame extends JFrame
{
	private static final long serialVersionUID = -8857520805111704416L;
	public static final String RANDOM_POINTS_MI = "Draw Random Points";
	public static final String CLEAR_POINTS_MI = "Clear Points";
	public static final String TOGGLE_CONVEX_HULL_MI = "Show Convex Hull";
	public static final String TOGGLE_VORONOI_DIAGRAM_MI = "Show Voronoi Diagram";
	public static final String TOGGLE_DELAUNAY_TRIANGULATION_MI = "Show Delaunay Triangulation";
	public static final String TOGGLE_BEZIER_CURVE_MI = "Show Bezier Curve";
	public static final String SAVE_IMAGE = "Save Image...";
	public static final String SET_RADIUS_MI = "Set point radius...";
	public static final String SET_NUMBER_RANDOM_POINTS_MI = "Set number of random points...";
	public static final String SET_CONVEX_HULL_COLOR_MI = "Set convex hull color...";
	public static final String SET_DRAW_POINTS_MI = "Draw Points";
	public static final String SET_SMOOTH_EDGES_MI = "Smooth Edges";
	public static final String SET_COLOR_VORONOI_CELL_REGIONS_MI = "Color Voronoi Cell Regions";
	public static final String SET_SHOW_POINTS_MI = "Show Points Label";
	public static final String SET_DRAW_DELAUNAY_CIRCUMCIRCLES_MI = "Draw Delaunay circumcircles";
	public static final String RESET_ALL_PREFERENCES_MI = "Reset Edited Values";
	public static final String HELP_MI = "Help";
	public static final String ABOUT_MI = "About";
	public static final String EXIT_MI = "Exit";
	private static final int MAX_BEZIER_CURVE_POINTS = 67;
	private static final String BEZIER_CURVE_DISABLED_MESSAGE = String.format("The bezier curve only works when there are less than %d points.", MAX_BEZIER_CURVE_POINTS);
	private final JMenuItem clearPointsMenuItem;
	private final JMenuItem randomPointsMenuItem;
	private final JCheckBoxMenuItem convexHullMenuItem;
	private final JCheckBoxMenuItem voronoiDiagramMenuItem;
	private final JCheckBoxMenuItem delaunayTriangulationMenuItem;
	private final JCheckBoxMenuItem bezierCurveMenuItem;
	private final JMenuItem saveAsImageMenuItem;
	private final JCheckBoxMenuItem drawPointsMenuItem;
	private final JCheckBoxMenuItem smoothEdgesMenuItem;
	private final JCheckBoxMenuItem colorVoronoiCellRegionsMenuItem;
	private final JCheckBoxMenuItem showPointsLabelMenuItem;
	private final JCheckBoxMenuItem drawDelaunayCircumcirclesMenuItem;
	private final CompGeoCanvas canvas;

	public CompGeoFrame()
	{
		super("Comp Geo Demo");

		this.randomPointsMenuItem = new JMenuItem(RANDOM_POINTS_MI);
		this.clearPointsMenuItem = new JMenuItem(CLEAR_POINTS_MI);
		this.convexHullMenuItem = new JCheckBoxMenuItem(TOGGLE_CONVEX_HULL_MI, false);
		this.voronoiDiagramMenuItem = new JCheckBoxMenuItem(TOGGLE_VORONOI_DIAGRAM_MI, false);
		this.delaunayTriangulationMenuItem = new JCheckBoxMenuItem(TOGGLE_DELAUNAY_TRIANGULATION_MI, false);
		this.bezierCurveMenuItem = new JCheckBoxMenuItem(TOGGLE_BEZIER_CURVE_MI, false);
		this.saveAsImageMenuItem = new JMenuItem(SAVE_IMAGE, KeyEvent.VK_S);
		this.drawPointsMenuItem = new JCheckBoxMenuItem(SET_DRAW_POINTS_MI, true);
		this.smoothEdgesMenuItem = new JCheckBoxMenuItem(SET_SMOOTH_EDGES_MI, true);
		this.colorVoronoiCellRegionsMenuItem = new JCheckBoxMenuItem(SET_COLOR_VORONOI_CELL_REGIONS_MI, true);
		this.showPointsLabelMenuItem = new JCheckBoxMenuItem(SET_SHOW_POINTS_MI, true);
		this.drawDelaunayCircumcirclesMenuItem = new JCheckBoxMenuItem(SET_DRAW_DELAUNAY_CIRCUMCIRCLES_MI, true);

		this.canvas = new CompGeoCanvas();
		this.addMenu(new CompGeoActionListener(this));
		this.addCanvas(new CompGeoMouseListener(this));
		this.initSize();
	}

	private void addMenu(ActionListener actionListener)
	{
		clearPointsMenuItem.setEnabled(false);
		convexHullMenuItem.setEnabled(false);
		voronoiDiagramMenuItem.setEnabled(false);
		delaunayTriangulationMenuItem.setEnabled(false);
		bezierCurveMenuItem.setEnabled(false);
		JMenuItem exitMenuItem = new JMenuItem(EXIT_MI, KeyEvent.VK_X);
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.ALT_DOWN_MASK));

		JMenuItem setRadiusMenuItem = new JMenuItem(SET_RADIUS_MI, KeyEvent.VK_R);
		JMenuItem setNumPointsMenuItem = new JMenuItem(SET_NUMBER_RANDOM_POINTS_MI, KeyEvent.VK_N);
		JMenuItem setConvexHullColorMenuItem = new JMenuItem(SET_CONVEX_HULL_COLOR_MI, KeyEvent.VK_C);
		drawPointsMenuItem.setSelected(canvas.shouldDrawPoints());
		smoothEdgesMenuItem.setSelected(canvas.shouldSmoothEdges());
		colorVoronoiCellRegionsMenuItem.setSelected(canvas.shouldColorVoronoiCellRegions());
		showPointsLabelMenuItem.setSelected(canvas.shouldShowPointsLabel());
		drawDelaunayCircumcirclesMenuItem.setSelected(canvas.shouldDrawDelaunayCircumcircles());
		JMenuItem resetAllPreferencesMenuItem = new JMenuItem(RESET_ALL_PREFERENCES_MI);
		resetAllPreferencesMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK));
		saveAsImageMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
		saveAsImageMenuItem.setEnabled(false);

		JMenuItem helpMenuItem = new JMenuItem(HELP_MI, KeyEvent.VK_H);
		helpMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, KeyEvent.ALT_DOWN_MASK));
		JMenuItem aboutMenuItem = new JMenuItem(ABOUT_MI, KeyEvent.VK_A);
		aboutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.ALT_DOWN_MASK));

		JMenu actionMenu = new JMenu("Action");
		actionMenu.add(randomPointsMenuItem);
		actionMenu.add(clearPointsMenuItem);
		actionMenu.addSeparator();
		actionMenu.add(convexHullMenuItem);
		actionMenu.add(voronoiDiagramMenuItem);
		actionMenu.add(delaunayTriangulationMenuItem);
		actionMenu.add(bezierCurveMenuItem);
		actionMenu.addSeparator();
		actionMenu.add(exitMenuItem);

		JMenu imageMenu = new JMenu("Image");
		imageMenu.add(setRadiusMenuItem);
		imageMenu.add(setNumPointsMenuItem);
		imageMenu.add(setConvexHullColorMenuItem);
		imageMenu.addSeparator();
		imageMenu.add(drawPointsMenuItem);
		imageMenu.add(smoothEdgesMenuItem);
		imageMenu.add(colorVoronoiCellRegionsMenuItem);
		imageMenu.add(showPointsLabelMenuItem);
		imageMenu.add(drawDelaunayCircumcirclesMenuItem);
		imageMenu.addSeparator();
		imageMenu.add(resetAllPreferencesMenuItem);
		imageMenu.add(saveAsImageMenuItem);

		JMenu helpMenu = new JMenu("Help");
		helpMenu.add(helpMenuItem);
		helpMenu.add(aboutMenuItem);

		JMenuBar mainMenu = new JMenuBar();
		mainMenu.add(actionMenu);
		mainMenu.add(imageMenu);
		mainMenu.add(helpMenu);

		for (int i = 0; i < mainMenu.getMenuCount(); i++)
		{
			for (Component menuComponent : mainMenu.getMenu(i).getMenuComponents())
			{
				if (menuComponent instanceof AbstractButton)
				{
					((AbstractButton) menuComponent).addActionListener(actionListener);
				}
			}
		}

		addIncrementalAccelerators(actionMenu, KeyEvent.ALT_DOWN_MASK);
		addIncrementalAccelerators(imageMenu, KeyEvent.CTRL_DOWN_MASK);

		this.setJMenuBar(mainMenu);
	}

	private void addIncrementalAccelerators(JMenu menu, int modifiers)
	{
		int i = 1;

		for (Component menuComponent : menu.getMenuComponents())
		{
			if (menuComponent instanceof JMenuItem)
			{
				JMenuItem menuItem = (JMenuItem) menuComponent;
				if (menuItem.getAccelerator() == null)
				{
					menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0 + i, modifiers));
					i++;
				}
			}
		}
	}

	private void addCanvas(MouseListener mouseListener)
	{
		this.add(canvas);
		canvas.setSize(600, 400);
		canvas.addMouseListener(mouseListener);
	}

	private void initSize()
	{
		this.setMinimumSize(new Dimension(400, 100));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addComponentListener(new CompGeoComponentListener(this));
		this.pack();
	}

	public CompGeoCanvas getCanvas()
	{
		return canvas;
	}

	public void setConvexHullPoints(List<Point> convexHullPoints)
	{
		Polygon convexHull = convexHullPoints == null ? null : PointUiUtils.createPolygon(convexHullPoints);
		canvas.setConvexHull(convexHull);

		convexHullMenuItem.setEnabled(canvas.getPointCount() > 0);
		convexHullMenuItem.setSelected(convexHull != null);
	}

	public void setVoronoiCells(Collection<VoronoiCell> voronoiCells)
	{
		canvas.setVoronoiCells(voronoiCells);

		voronoiDiagramMenuItem.setEnabled(canvas.getPointCount() > 0);
		voronoiDiagramMenuItem.setSelected(voronoiCells != null);
	}

	public void setDelaunayTriangulationTriangles(Collection<DelaunayTriangle> delaunayTriangulationTriangles)
	{
		canvas.setDelaunayTriangulationTriangles(delaunayTriangulationTriangles);

		delaunayTriangulationMenuItem.setEnabled(canvas.getPointCount() > 0);
		delaunayTriangulationMenuItem.setSelected(delaunayTriangulationTriangles != null);
	}

	public void setBezierCurvePoints(List<Point> bezierCurvePoints)
	{
		canvas.setBezierCurvePoints(bezierCurvePoints);

		bezierCurveMenuItem.setEnabled(canvas.getPointCount() > 1);
		bezierCurveMenuItem.setSelected(bezierCurvePoints != null);
	}

	public void updatePointControls()
	{
		boolean hasPoints = canvas.getPointCount() > 0;
		clearPointsMenuItem.setEnabled(hasPoints);
		saveAsImageMenuItem.setEnabled(hasPoints);
		randomPointsMenuItem.setEnabled(canvas.canAcceptPoints(1));
	}

	/**
	 * Adds a Point to the Set of Points to create the Convex Hull from. Only
	 * adds the Point to the Set if an equal Point has not already been added.
	 *
	 * @param points The Point to add the Set of Points to create the Convex
	 * Hull from.
	 */
	public void addPoints(Point... points)
	{
		boolean pointAdded = canvas.addPoints(points);

		if (pointAdded)
		{
			clearDiagrams();
			this.updatePointControls();

			if (canvas.getPointCount() > MAX_BEZIER_CURVE_POINTS)
			{
				bezierCurveMenuItem.setToolTipText(BEZIER_CURVE_DISABLED_MESSAGE); // Investigate this number (67) and why things break for larger point counts.
				bezierCurveMenuItem.setEnabled(false);
			}
		}

	}

	public void clear()
	{
		if (canvas.getPointCount() > MAX_BEZIER_CURVE_POINTS) // Remove the warning tooltip.
		{
			bezierCurveMenuItem.setToolTipText(null);
			bezierCurveMenuItem.setEnabled(true);
		}

		canvas.clear();

		clearDiagrams();
		this.updatePointControls();
	}

	private void clearDiagrams()
	{
		this.setConvexHullPoints(null);
		this.setVoronoiCells(null);
		this.setDelaunayTriangulationTriangles(null);
		this.setBezierCurvePoints(null);
	}

	public void reloadPreferences()
	{
		canvas.reloadPreferences();

		drawPointsMenuItem.setSelected(canvas.shouldDrawPoints());
		smoothEdgesMenuItem.setSelected(canvas.shouldSmoothEdges());
		colorVoronoiCellRegionsMenuItem.setSelected(canvas.shouldColorVoronoiCellRegions());
		showPointsLabelMenuItem.setSelected(canvas.shouldShowPointsLabel());
		drawDelaunayCircumcirclesMenuItem.setSelected(canvas.shouldDrawDelaunayCircumcircles());
	}
}
