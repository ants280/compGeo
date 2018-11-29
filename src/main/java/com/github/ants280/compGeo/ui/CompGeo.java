package com.github.ants280.compgeo.ui;

import java.awt.Window;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public final class CompGeo implements Runnable
{
	// NOTE: The version only shows as non-null when `mvn package` is run.
	public static final String VERSION = CompGeo.class.getPackage().getSpecificationVersion();

	public static void main(final String[] args)
	{
		// Set the menu of the ConvexHullFrame on the mac menu.
		if (System.getProperty("mrj.version") != null)
		{
			System.setProperty("apple.laf.useScreenMenuBar", "true");
		}

		SwingUtilities.invokeLater(new CompGeo());
	}

	private CompGeo()
	{
	}

	@Override
	public void run()
	{
		setLookAndFeel();

		Window frame = new CompGeoFrame();
		Thread.setDefaultUncaughtExceptionHandler(
				new CompGeoUncaughtExceptionHandler(frame));

		// Center the Window on the screen.
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private static void setLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex)
		{
			Logger.getLogger(CompGeo.class.getName())
					.log(
							Level.SEVERE,
							"Could not set system Look-And-Feel",
							ex);
		}
	}
}
