package com.jpatterson.school.compGeo.ui;

import com.jpatterson.school.compGeo.Point;
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
	static final String RANDOM_POINTS_MI = "Draw Random Points";
	static final String CLEAR_POINTS_MI = "Clear Points";
	static final String TOGGLE_CONVEX_HULL_MI = "Show Convex Hull";
	static final String TOGGLE_VORONOI_DIAGRAM_MI = "Show Voronoi Diagram";
	static final String TOGGLE_DELAUNAY_TRIANGULATION_MI = "Show Delaunay Triangulation";
	static final String TOGGLE_BEZIER_CURVE_MI = "Show Bezier Curve";
	static final String SAVE_IMAGE = "Save Image...";
	static final String SET_RADIUS_MI = "Set point radius...";
	static final String SET_NUMBER_RANDOM_POINTS_MI = "Set number of random points...";
	static final String SET_CONVEX_HULL_COLOR_MI = "Set convex hull color...";
	static final String SET_DRAW_POINTS_MI = "Draw Points";
	static final String SET_SMOOTH_EDGES_MI = "Smooth Edges";
	static final String SET_COLOR_VORONOI_CELL_REGIONS_MI = "Color Voronoi Cell Regions";
	static final String SET_SHOW_POINTS_MI = "Show Points Label";
	static final String SET_DRAW_DELAUNAY_CIRCUMCIRCLES_MI = "Draw Delaunay circumcircles";
	static final String RESET_ALL_PREFERENCES_MI = "Reset Edited Values";
	static final String HELP_MI = "Help";
	static final String ABOUT_MI = "About";
	static final String EXIT_MI = "Exit";
	private static final int MAX_BEZIER_CURVE_POINTS = 67;
	private static final String BEZIER_CURVE_DISABLED_MESSAGE = String.format("The bezier curve only works when there are less than %d points.", MAX_BEZIER_CURVE_POINTS);
	private final JMenuItem clearPoints_MI;
	private final JMenuItem randomPoints_MI;
	private final JCheckBoxMenuItem convexHull_MI;
	private final JCheckBoxMenuItem voronoiDiagram_MI;
	private final JCheckBoxMenuItem delaunayTriangulation_MI;
	private final JCheckBoxMenuItem bezierCurve_MI;
	private final JMenuItem saveAsImage_MI;
	private final JCheckBoxMenuItem drawPoints_MI;
	private final JCheckBoxMenuItem smoothEdges_MI;
	private final JCheckBoxMenuItem colorVoronoiCellRegoins_MI;
	private final JCheckBoxMenuItem showPointsLabel_MI;
	private final JCheckBoxMenuItem drawDelaunayCircumcircles_MI;
	private final CompGeoCanvas canvas;

	CompGeoFrame()
	{
		super("Comp Geo Demo");

		this.randomPoints_MI = new JMenuItem(RANDOM_POINTS_MI);
		this.clearPoints_MI = new JMenuItem(CLEAR_POINTS_MI);
		this.convexHull_MI = new JCheckBoxMenuItem(TOGGLE_CONVEX_HULL_MI, false);
		this.voronoiDiagram_MI = new JCheckBoxMenuItem(TOGGLE_VORONOI_DIAGRAM_MI, false);
		this.delaunayTriangulation_MI = new JCheckBoxMenuItem(TOGGLE_DELAUNAY_TRIANGULATION_MI, false);
		this.bezierCurve_MI = new JCheckBoxMenuItem(TOGGLE_BEZIER_CURVE_MI, false);
		this.saveAsImage_MI = new JMenuItem(SAVE_IMAGE, KeyEvent.VK_S);
		this.drawPoints_MI = new JCheckBoxMenuItem(SET_DRAW_POINTS_MI, true);
		this.smoothEdges_MI = new JCheckBoxMenuItem(SET_SMOOTH_EDGES_MI, true);
		this.colorVoronoiCellRegoins_MI = new JCheckBoxMenuItem(SET_COLOR_VORONOI_CELL_REGIONS_MI, true);
		this.showPointsLabel_MI = new JCheckBoxMenuItem(SET_SHOW_POINTS_MI, true);
		this.drawDelaunayCircumcircles_MI = new JCheckBoxMenuItem(SET_DRAW_DELAUNAY_CIRCUMCIRCLES_MI, true);

		this.canvas = new CompGeoCanvas();
		this.addMenu(new CompGeoActionListener(this));
		this.addCanvas(new CompGeoMouseListener(this));
		this.initSize();
	}

	private void addMenu(ActionListener actionListener)
	{
		clearPoints_MI.setEnabled(false);
		convexHull_MI.setEnabled(false);
		voronoiDiagram_MI.setEnabled(false);
		delaunayTriangulation_MI.setEnabled(false);
		bezierCurve_MI.setEnabled(false);
		JMenuItem exit_MI = new JMenuItem(EXIT_MI, KeyEvent.VK_X);
		exit_MI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.ALT_MASK));

		JMenuItem setRadius_MI = new JMenuItem(SET_RADIUS_MI, KeyEvent.VK_R);
		JMenuItem setNumPoints_MI = new JMenuItem(SET_NUMBER_RANDOM_POINTS_MI, KeyEvent.VK_N);
		JMenuItem setConvexHullColor_MI = new JMenuItem(SET_CONVEX_HULL_COLOR_MI, KeyEvent.VK_C);
		drawPoints_MI.setSelected(canvas.shouldDrawPoints());
		smoothEdges_MI.setSelected(canvas.shouldSmoothEdges());
		colorVoronoiCellRegoins_MI.setSelected(canvas.shouldColorVoronoiCellRegoins());
		showPointsLabel_MI.setSelected(canvas.shouldShowPointsLabel());
		drawDelaunayCircumcircles_MI.setSelected(canvas.shouldDrawDelaunayCircumcircles());
		JMenuItem resetAllPreferences_MI = new JMenuItem(RESET_ALL_PREFERENCES_MI);
		resetAllPreferences_MI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK));
		saveAsImage_MI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
		saveAsImage_MI.setEnabled(false);

		JMenuItem help_MI = new JMenuItem(HELP_MI, KeyEvent.VK_H);
		help_MI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, KeyEvent.ALT_DOWN_MASK));
		JMenuItem about_MI = new JMenuItem(ABOUT_MI, KeyEvent.VK_A);
		about_MI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.ALT_DOWN_MASK));

		JMenu actionMenu = new JMenu("Action");
		actionMenu.add(randomPoints_MI);
		actionMenu.add(clearPoints_MI);
		actionMenu.addSeparator();
		actionMenu.add(convexHull_MI);
		actionMenu.add(voronoiDiagram_MI);
		actionMenu.add(delaunayTriangulation_MI);
		actionMenu.add(bezierCurve_MI);
		actionMenu.addSeparator();
		actionMenu.add(exit_MI);

		JMenu imageMenu = new JMenu("Image");
		imageMenu.add(setRadius_MI);
		imageMenu.add(setNumPoints_MI);
		imageMenu.add(setConvexHullColor_MI);
		imageMenu.addSeparator();
		imageMenu.add(drawPoints_MI);
		imageMenu.add(smoothEdges_MI);
		imageMenu.add(colorVoronoiCellRegoins_MI);
		imageMenu.add(showPointsLabel_MI);
		imageMenu.add(drawDelaunayCircumcircles_MI);
		imageMenu.addSeparator();
		imageMenu.add(resetAllPreferences_MI);
		imageMenu.add(saveAsImage_MI);

		JMenu helpMenu = new JMenu("Help");
		helpMenu.add(help_MI);
		helpMenu.add(about_MI);

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
		int i = 0;

		for (Component menuComponent : menu.getMenuComponents())
		{
			if (menuComponent instanceof JMenuItem)
			{
				JMenuItem menuItem = (JMenuItem) menuComponent;
				if (menuItem.getAccelerator() == null)
				{
					menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0 + ++i, modifiers));
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

	void setConvexHullPoints(List<Point> convexHullPoints)
	{
		Polygon convexHull = convexHullPoints == null ? null : PointUiUtils.createPolygon(convexHullPoints);
		canvas.setConvexHull(convexHull);

		convexHull_MI.setEnabled(canvas.getPointCount() > 0);
		convexHull_MI.setSelected(convexHull != null);
	}

	void setVoronoiCells(Collection<VoronoiCell> voronoiCells)
	{
		canvas.setVoronoiCells(voronoiCells);

		voronoiDiagram_MI.setEnabled(canvas.getPointCount() > 0);
		voronoiDiagram_MI.setSelected(voronoiCells != null);
	}

	void setDelaunayTriangulationTriangles(Collection<DelaunayTriangle> delaunayTriangulationTriangles)
	{
		canvas.setDelaunayTriangulationTriangles(delaunayTriangulationTriangles);

		delaunayTriangulation_MI.setEnabled(canvas.getPointCount() > 0);
		delaunayTriangulation_MI.setSelected(delaunayTriangulationTriangles != null);
	}

	void setBezierCurvePoints(List<Point> bezierCurvePoints)
	{
		canvas.setBezierCurvePoints(bezierCurvePoints);

		bezierCurve_MI.setEnabled(canvas.getPointCount() > 1);
		bezierCurve_MI.setSelected(bezierCurvePoints != null);
	}

	void updatePointControls()
	{
		boolean hasPoints = canvas.getPointCount() > 0;
		clearPoints_MI.setEnabled(hasPoints);
		saveAsImage_MI.setEnabled(hasPoints);
		randomPoints_MI.setEnabled(canvas.canAcceptPoints(1));
	}

	/**
	 * Adds a Point to the Set of Points to create the Convex Hull from. Only
	 * adds the Point to the Set if an equal Point has not already been added.
	 *
	 * @param points The Point to add the Set of Points to create the Convex
	 * Hull from.
	 * @return True if the point was added to the Set of Point, otherwise false.
	 */
	boolean addPoints(Point... points)
	{
		boolean pointAdded = canvas.addPoints(points);

		if (pointAdded)
		{
			clearDiagrams();
			this.updatePointControls();

			if (canvas.getPointCount() > MAX_BEZIER_CURVE_POINTS)
			{
				bezierCurve_MI.setToolTipText(BEZIER_CURVE_DISABLED_MESSAGE); // Investigate this number (67) and why things break for larger point counts.
				bezierCurve_MI.setEnabled(false);
			}
		}

		return pointAdded;
	}

	void clear()
	{
		if (canvas.getPointCount() > MAX_BEZIER_CURVE_POINTS) // Remove the warning tooltip.
		{
			bezierCurve_MI.setToolTipText(null);
			bezierCurve_MI.setEnabled(true);
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

	void reloadPreferences()
	{
		canvas.reloadPreferences();

		drawPoints_MI.setSelected(canvas.shouldDrawPoints());
		smoothEdges_MI.setSelected(canvas.shouldSmoothEdges());
		colorVoronoiCellRegoins_MI.setSelected(canvas.shouldColorVoronoiCellRegoins());
		showPointsLabel_MI.setSelected(canvas.shouldShowPointsLabel());
		drawDelaunayCircumcircles_MI.setSelected(canvas.shouldDrawDelaunayCircumcircles());
	}
}
