package com.jpatterson.school.compGeo.ui;

import com.jpatterson.school.compGeo.Point;
import static com.jpatterson.school.compGeo.ui.CompGeoFrame.*;
import com.jpatterson.school.compGeo.ui.worker.BezierCurvePopupWorker;
import com.jpatterson.school.compGeo.ui.worker.CompGeoPopupWorker;
import com.jpatterson.school.compGeo.ui.worker.GrahamScanPopupWorker;
import com.jpatterson.school.compGeo.ui.worker.VoronoiDiagramPopupWorker;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
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
	private final CompGeoFrame frame;

	public CompGeoActionListener(CompGeoFrame frame)
	{
		this.frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		String actionCommand = event.getActionCommand();
		CompGeoCanvas canvas = frame.getCanvas();
		switch (actionCommand)
		{
			case RANDOM_POINTS_MI:
				this.handleAddRandomPoints();
				break;
			case CLEAR_POINTS_MI:
				frame.clear();
				break;
			case TOGGLE_CONVEX_HULL_MI:
				this.toggleConvexHull();
				break;
			case TOGGLE_VORONOI_DIAGRAM_MI:
				this.toggleVoronoiDiagram();
				break;
			case TOGGLE_BEZIER_CURVE_MI:
				this.toggleBezierCurve();
				break;
			case SAVE_IMAGE:
				this.handleSaveImage();
				break;
			case SET_RADIUS_MI:
				this.handleSetRadius();
				break;
			case SET_NUMBER_RANDOM_POINTS_MI:
				this.handleSetNumberRandomPoints();
				break;
			case SET_CONVEX_HULL_COLOR_MI:
				this.handleSetConvexHullColor();
				break;
			case SET_DRAW_POINTS_MI:
				flipPreferenceBooleanValue(
					CompGeoCanvasPreference.DRAW_POINTS,
					canvas::flipDrawPoints,
					canvas::shouldDrawPoints);
				break;
			case SET_SMOOTH_EDGES_MI:
				flipPreferenceBooleanValue(
					CompGeoCanvasPreference.SMOOTH_EDGES,
					canvas::flipSmoothEdges,
					canvas::shouldSmoothEdges);
				break;
			case SET_COLOR_VORONOI_CELL_REGIONS_MI:
				flipPreferenceBooleanValue(
					CompGeoCanvasPreference.COLOR_VORONOI_CELL_REGIONS,
					canvas::flipColorVoronoiCellRegions,
					canvas::shouldColorVoronoiCellRegoins);
				break;
			case SET_SHOW_POINTS_MI:
				flipPreferenceBooleanValue(
					CompGeoCanvasPreference.SHOW_POINTS_LABEL,
					canvas::flipShowPointsLabel,
					canvas::shouldShowPointsLabel);
				break;
			case RESET_ALL_PREFERENCES_MI:
				this.handleResetAllPreferences();
				break;
			case HELP_MI:
				this.handleHelpItem();
				break;
			case ABOUT_MI:
				this.handleAboutItem();
				break;
			case EXIT_MI:
				this.handleExitItem();
				break;
			default:
				throw new IllegalArgumentException("Unknown actionEvent: " + event.getActionCommand());
		}
	}

	private void handleAddRandomPoints()
	{
		int numberRandomPointsToAdd = frame.getCanvas().getNumberRandomPointsToAdd();
		CompGeoCanvas canvas = frame.getCanvas();
		if (!canvas.canAcceptPoints(numberRandomPointsToAdd))
		{
			return;
		}

		Point randomPoint;
		Collection<Point> canvasPoints = canvas.getPoints();
		Set<Point> existingPoints = new HashSet<>(canvasPoints);
		for (int i = 0; i < numberRandomPointsToAdd; i++)
		{
			do
			{
				int x = POINT_GENERATOR.nextInt(canvas.getWidth());
				int y = POINT_GENERATOR.nextInt(canvas.getHeight());
				randomPoint = new Point(x, y);
			}
			while (!existingPoints.add(randomPoint));
		}
		Set<Point> newPoints = existingPoints;

		newPoints.removeAll(canvasPoints);
		frame.addPoints(newPoints.stream().toArray(Point[]::new));
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
		if (frame.getCanvas().hasVoroniCells())
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
			CompGeoCanvasPreference.POINT_RADIUS);
	}

	private void handleSetNumberRandomPoints()
	{
		CompGeoCanvas canvas = frame.getCanvas();
		this.changePreferenceIntegerValue(canvas.getNumberRandomPointsToAdd(), 1, 500, 10,
			SET_NUMBER_RANDOM_POINTS_MI,
			canvas::setNumberRandomPointsToAdd,
			CompGeoCanvasPreference.RANDOM_POINT_COUNT);
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
			CompGeoCanvasPreference.CONVEX_HULL_COLOR.setValue(newValue.getRGB());
		}
	}

	private static void flipPreferenceBooleanValue(CompGeoCanvasPreference<Boolean> preference, Runnable preferenceFlipper, Supplier<Boolean> preferenceSupplier)
	{
		preferenceFlipper.run();
		preference.setValue(preferenceSupplier.get());
	}

	private void handleResetAllPreferences()
	{
		CompGeoCanvasPreference.resetSavedPreferences();

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
			+ "\nA Bezier Curve can be drawn between the first point and most recently drawn"
			+ "\npoint by clicking \"" + TOGGLE_BEZIER_CURVE_MI + "\".  This curves toward intermediate points"
			+ "\nby a factor relative to the amount of the line that has been dwawn.",
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
			+ "\nCopyright(©) 2012, 2017"
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
			return fileName.substring(lastIndexOfPeriod + 1, fileName.length());
		}
	}
}