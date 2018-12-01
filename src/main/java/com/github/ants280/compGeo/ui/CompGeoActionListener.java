package com.github.ants280.compgeo.ui;

import com.github.ants280.compgeo.Point;
import static com.github.ants280.compgeo.ui.CompGeoFrame.*;
import com.github.ants280.compgeo.ui.worker.BezierCurvePopupWorker;
import com.github.ants280.compgeo.ui.worker.CompGeoPopupWorker;
import com.github.ants280.compgeo.ui.worker.DelaunayTriangulationPopupWorker;
import com.github.ants280.compgeo.ui.worker.GrahamScanPopupWorker;
import com.github.ants280.compgeo.ui.worker.VoronoiDiagramPopupWorker;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import javax.imageio.ImageIO;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.filechooser.FileFilter;

public class CompGeoActionListener implements ActionListener
{
	private static final Random POINT_GENERATOR = new Random();
	private final Map<String, Runnable> commandMap;
	private final CompGeoFrame frame;

	public CompGeoActionListener(CompGeoFrame frame)
	{
		this.commandMap = new HashMap<>();
		this.frame = frame;
		CompGeoCanvas canvas = frame.getCanvas();

		commandMap.put(RANDOM_POINTS_MI, this::handleAddRandomPoints);
		commandMap.put(CLEAR_POINTS_MI, frame::clear);
		commandMap.put(TOGGLE_CONVEX_HULL_MI, this::toggleConvexHull);
		commandMap.put(TOGGLE_VORONOI_DIAGRAM_MI, this::toggleVoronoiDiagram);
		commandMap.put(TOGGLE_DELAUNAY_TRIANGULATION_MI, this::toggleDelaunayTriangulation);
		commandMap.put(TOGGLE_BEZIER_CURVE_MI, this::toggleBezierCurve);
		commandMap.put(SAVE_IMAGE, this::handleSaveImage);
		commandMap.put(SET_RADIUS_MI, this::handleSetRadius);
		commandMap.put(SET_NUMBER_RANDOM_POINTS_MI, this::handleSetNumberRandomPoints);
		commandMap.put(SET_CONVEX_HULL_COLOR_MI, this::handleSetConvexHullColor);
		commandMap.put(SET_DRAW_POINTS_MI, () -> flipPreferenceBooleanValue(CompGeoCanvas.DRAW_POINTS, canvas::flipDrawPoints, canvas::shouldDrawPoints));
		commandMap.put(SET_SMOOTH_EDGES_MI, () -> flipPreferenceBooleanValue(CompGeoCanvas.SMOOTH_EDGES, canvas::flipSmoothEdges, canvas::shouldSmoothEdges));
		commandMap.put(SET_COLOR_VORONOI_CELL_REGIONS_MI, () -> flipPreferenceBooleanValue(CompGeoCanvas.COLOR_VORONOI_CELL_REGIONS, canvas::flipColorVoronoiCellRegions, canvas::shouldColorVoronoiCellRegions));
		commandMap.put(SET_SHOW_POINTS_MI, () -> flipPreferenceBooleanValue(CompGeoCanvas.SHOW_POINTS_LABEL, canvas::flipShowPointsLabel, canvas::shouldShowPointsLabel));
		commandMap.put(SET_DRAW_DELAUNAY_CIRCUMCIRCLES_MI, () -> flipPreferenceBooleanValue(CompGeoCanvas.DRAW_DELAUNAY_CIRCUMCIRCLES, canvas::flipDrawDelaunayCircumcircles, canvas::shouldDrawDelaunayCircumcircles));
		commandMap.put(RESET_ALL_PREFERENCES_MI, this::handleResetAllPreferences);
		commandMap.put(HELP_MI, this::handleHelpItem);
		commandMap.put(ABOUT_MI, this::handleAboutItem);
		commandMap.put(EXIT_MI, this::handleExitItem);
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		String actionCommand = event.getActionCommand();

		if (!commandMap.containsKey(actionCommand))
		{
			throw new IllegalArgumentException("Unknown actionEvent: " + actionCommand);
		}

		commandMap.get(actionCommand).run();
	}

	private void handleAddRandomPoints()
	{
		int numberRandomPointsToAdd = frame.getCanvas().getNumberRandomPointsToAdd();
		CompGeoCanvas canvas = frame.getCanvas();
		if (!canvas.canAcceptPoints(numberRandomPointsToAdd))
		{
			return;
		}

		Collection<Point> canvasPoints = canvas.getPoints();
		Set<Point> existingPoints = new HashSet<>(canvasPoints);
		Set<Point> newPoints = new HashSet<>();
		for (int i = 0; i < numberRandomPointsToAdd; i++)
		{
			Point randomPoint;

			do
			{
				int x = POINT_GENERATOR.nextInt(canvas.getWidth());
				int y = POINT_GENERATOR.nextInt(canvas.getHeight());
				randomPoint = new Point(x, y);
			}
			while (!existingPoints.add(randomPoint));

			newPoints.add(randomPoint);
		}

		Point[] points = newPoints.toArray(new Point[0]);
		frame.addPoints(points);
	}

	private void toggleConvexHull()
	{
		if (frame.getCanvas().hasConvexHull())
		{
			frame.setConvexHullPoints(null);
		}
		else
		{
			CompGeoPopupWorker<List<Point>> compGeoPopupWorker
					= new GrahamScanPopupWorker(frame::setConvexHullPoints, frame);
			compGeoPopupWorker.start();
		}
	}

	private void toggleVoronoiDiagram()
	{
		if (frame.getCanvas().hasVoronoiCells())
		{
			frame.setVoronoiCells(null);
		}
		else
		{
			CompGeoPopupWorker<Collection<VoronoiCell>> compGeoPopupWorker
					= new VoronoiDiagramPopupWorker(frame::setVoronoiCells, frame);
			compGeoPopupWorker.start();
		}
	}

	private void toggleDelaunayTriangulation()
	{
		if (frame.getCanvas().hasDelaunayTriangulation())
		{
			frame.setDelaunayTriangulationTriangles(null);
		}
		else
		{
			CompGeoPopupWorker<Collection<DelaunayTriangle>> compGeoPopupWorker
					= new DelaunayTriangulationPopupWorker(frame::setDelaunayTriangulationTriangles, frame);
			compGeoPopupWorker.start();
		}
	}

	private void toggleBezierCurve()
	{
		if (frame.getCanvas().hasBezierCurvePoints())
		{
			frame.setBezierCurvePoints(null);
		}
		else
		{
			CompGeoPopupWorker<List<Point>> compGeoPopupWorker
					= new BezierCurvePopupWorker(frame::setBezierCurvePoints, frame);
			compGeoPopupWorker.start();
		}
	}

	private void handleSaveImage()
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new PngFileFilter());

		this.showSaveDialog(fileChooser);
	}

	private void handleSetRadius()
	{
		CompGeoCanvas canvas = frame.getCanvas();
		this.changePreferenceIntegerValue(
				canvas.getPointRadius(), 0, 15, 1,
				SET_RADIUS_MI,
				canvas::setPointRadius,
				CompGeoCanvas.POINT_RADIUS);
	}

	private void handleSetNumberRandomPoints()
	{
		CompGeoCanvas canvas = frame.getCanvas();
		this.changePreferenceIntegerValue(canvas.getNumberRandomPointsToAdd(), 1, 500, 10,
				SET_NUMBER_RANDOM_POINTS_MI,
				canvas::setNumberRandomPointsToAdd,
				CompGeoCanvas.RANDOM_POINT_COUNT);
	}

	private void handleSetConvexHullColor()
	{
		CompGeoCanvas canvas = frame.getCanvas();
		JColorChooser colorChooser = new JColorChooser(canvas.getConvexHullColor());

		int optionChoice = JOptionPane.showOptionDialog(frame, colorChooser, SET_CONVEX_HULL_COLOR_MI, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (JOptionPane.OK_OPTION == optionChoice)
		{
			Color newValue = colorChooser.getColor();
			canvas.setConvexHullColor(newValue);
			CompGeoCanvas.CONVEX_HULL_COLOR.setValue(newValue.getRGB());
		}
	}

	private static void flipPreferenceBooleanValue(CompGeoCanvasPreference<Boolean> preference, Runnable preferenceFlipper, BooleanSupplier preferenceSupplier)
	{
		preferenceFlipper.run();
		preference.setValue(preferenceSupplier.getAsBoolean());
	}

	private void handleResetAllPreferences()
	{
		frame.getCanvas().resetSavedPreferences();

		frame.reloadPreferences();
	}

	private void handleHelpItem()
	{
		JOptionPane.showMessageDialog(
				frame,
				"This program draws the smallest convex polygon around a group of points."
				+ "\n"
				+ "\nClick in the window to create points, or click \"" + RANDOM_POINTS_MI + "\" to draw "
				+ "\n" + frame.getCanvas().getNumberRandomPointsToAdd() + " random points in the window.  Click \"" + TOGGLE_CONVEX_HULL_MI + "\" to draw the"
				+ "\nsmallest convex polygon (the convex hull) around the points.  Click "
				+ "\n\"" + CLEAR_POINTS_MI + "\" to clear the points from the screen."
				+ "\n"
				+ "\nA Voronoi Diagram can also be generated by clicking \"" + TOGGLE_VORONOI_DIAGRAM_MI + "\"."
				+ "\nThis divides the canvas into cells around each point so that all locations in"
				+ "\nthe cell are closest to the point which is also in the cell."
				+ "\n"
				+ "\nA triangulation can be drawn by clicking \"" + TOGGLE_DELAUNAY_TRIANGULATION_MI + "\"."
				+ "\nThis divides the canvas into triangles between the points.  The semicircles"
				+ "\nformed by the points of each triangle contain none of the other points on"
				+ "\nthe canvas.  The center of each circle is a vertex on the Voronoi Triangulation."
				+ "\n"
				+ "\nA Bezier Curve can be drawn between the first point and most recently drawn"
				+ "\npoint by clicking \"" + TOGGLE_BEZIER_CURVE_MI + "\".  This curves toward intermediate points"
				+ "\nby a factor relative to the amount of the line that has been drawn.",
				"Help",
				JOptionPane.QUESTION_MESSAGE);
	}

	private void handleAboutItem()
	{
		JOptionPane.showMessageDialog(
				frame,
				"By: Jacob Patterson"
				+ "\n"
				+ "\nVersion " + CompGeo.VERSION
				+ "\nCopyright(©) 2012, 2017, 2018"
				+ "\n"
				+ "\nGraham Scan algorithm an adaptation of"
				+ "\nCormen, Leiserson, Rivest, and Stein's"
				+ "\nIntroduction To Algorithms, 2 Ed."
				+ "\n"
				+ "\nVoronoi Algorithm is brute-force and slow.",
				"About " + frame.getTitle(),
				JOptionPane.INFORMATION_MESSAGE);
	}

	private void handleExitItem()
	{
		frame.setVisible(false);
		Runtime.getRuntime().exit(0);
	}

	private void showSaveDialog(JFileChooser fileChooser)
	{
		if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION)
		{
			File outputFile = fileChooser.getSelectedFile();
			if (outputFile.exists()
					&& JOptionPane.showConfirmDialog(
							fileChooser,
							String.format("Overwrite file '%s'?", outputFile.getName()),
							"Confirm File Overwrite",
							JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE)
					!= JOptionPane.OK_OPTION)
			{
				showSaveDialog(fileChooser);
			}
			else
			{
				CompGeoCanvas canvas = frame.getCanvas();
				BufferedImage image = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
				canvas.paint(image.createGraphics());
				try
				{
					ImageIO.write(image, "png", outputFile);
				}
				catch (IOException ex)
				{
					throw new RuntimeException(ex);
				}
			}
		}
	}

	private void changePreferenceIntegerValue(
			int currentValue,
			int min,
			int max,
			int stepSize,
			String title,
			Consumer<Integer> valueConsumer,
			CompGeoCanvasPreference<Integer> preferenceType)
	{
		SpinnerNumberModel model = new SpinnerNumberModel(currentValue, min, max, stepSize);
		JSpinner spinner = new JSpinner(model);
		if (JOptionPane.showOptionDialog(
				frame,
				spinner,
				title,
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				null,
				null)
				== JOptionPane.OK_OPTION)
		{
			int newValue = Integer.parseInt(model.getValue().toString());
			valueConsumer.accept(newValue);
			preferenceType.setValue(newValue);
		}
	}

	private static class PngFileFilter extends FileFilter
	{
		@Override
		public boolean accept(File file)
		{
			return file.isDirectory() || "png".equalsIgnoreCase(getExtension(file));
		}

		@Override
		public String getDescription()
		{
			return "PNG (*.png)";
		}

		private static String getExtension(File file)
		{
			String fileName = file.getName();
			int lastIndexOfPeriod = fileName.lastIndexOf(".");
			if (lastIndexOfPeriod < 0 || lastIndexOfPeriod + 1 >= fileName.length())
			{
				return null;
			}
			return fileName.substring(lastIndexOfPeriod + 1);
		}
	}
}
