package com.jpatterson.school.compGeo.ui;

import java.awt.Window;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public final class CompGeo
{
	public static final String VERSION = "4.0.1";

	public static void main(final String[] args)
	{
		// Set the menu of the ConvexHullFrame on the mac menu.
		if (System.getProperty("mrj.version") != null)
		{
			System.setProperty("apple.laf.useScreenMenuBar", "true");
		}

		SwingUtilities.invokeLater(new CompGeo()::run);
	}

	private CompGeo()
	{
	}

	private void run()
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
			if (!System.getProperty("os.name").toLowerCase().contains("mac"))
			{
				UIManager.setLookAndFeel(
					"com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			}
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex)
		{
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}
	}
}
