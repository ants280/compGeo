package com.jpatterson.school.compGeo.ui;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.prefs.Preferences;

public abstract class CompGeoCanvasPreference<T>
{
	private static final Preferences PREFERENCES_API = Preferences.userNodeForPackage(CompGeoCanvas.class);

	public static final CompGeoCanvasPreference<Integer> POINT_RADIUS;
	public static final CompGeoCanvasPreference<Integer> RANDOM_POINT_COUNT;
	public static final CompGeoCanvasPreference<Integer> CONVEX_HULL_COLOR;
	public static final CompGeoCanvasPreference<Boolean> DRAW_POINTS;
	public static final CompGeoCanvasPreference<Boolean> SMOOTH_EDGES;
	public static final CompGeoCanvasPreference<Boolean> COLOR_VORONOI_CELL_REGIONS;
	public static final CompGeoCanvasPreference<Boolean> SHOW_POINTS_LABEL;
	public static final CompGeoCanvasPreference<Boolean> DRAW_DELAUNAY_CIRCUMCIRCLES;

	static
	{
		POINT_RADIUS = new CompGeoCanvasIntegerPreference("POINT_RADIUS", 4);
		RANDOM_POINT_COUNT = new CompGeoCanvasIntegerPreference("RANDOM_POINT_COUNT", 3);
		CONVEX_HULL_COLOR = new CompGeoCanvasIntegerPreference("CONVEX_HULL_COLOR", 0x7fff0000); // new Color(255, 0, 0, 127) (transparent red)
		DRAW_POINTS = new CompGeoCanvasBooleanPreference("DRAW_POINTS", true);
		SMOOTH_EDGES = new CompGeoCanvasBooleanPreference("SMOOTH_EDGES", true);
		COLOR_VORONOI_CELL_REGIONS = new CompGeoCanvasBooleanPreference("COLOR_VORONOI_CELL_REGIONS", true);
		SHOW_POINTS_LABEL = new CompGeoCanvasBooleanPreference("SHOW_POINTS_LABEL", true);
		DRAW_DELAUNAY_CIRCUMCIRCLES = new CompGeoCanvasBooleanPreference("DRAW_DELAUNAY_CIRCUMCIRCLES", true);
	}

	protected static final Set<CompGeoCanvasPreference<?>> ALL_PREFERENCES
		= new HashSet<>(Arrays.asList(
			POINT_RADIUS, RANDOM_POINT_COUNT, CONVEX_HULL_COLOR,
			DRAW_POINTS, SMOOTH_EDGES, COLOR_VORONOI_CELL_REGIONS, SHOW_POINTS_LABEL, DRAW_DELAUNAY_CIRCUMCIRCLES));

	private final String preferenceName;
	private final T defaultValue;
	private final BiFunction<String, T, T> preferenceGetter;
	private final BiConsumer<String, T> preferenceSetter;

	private CompGeoCanvasPreference(
		String preferenceName,
		T defaultValue,
		BiFunction<String, T, T> preferenceGetter,
		BiConsumer<String, T> preferenceSetter)
	{
		this.preferenceName = preferenceName;
		this.defaultValue = defaultValue;
		this.preferenceGetter = preferenceGetter;
		this.preferenceSetter = preferenceSetter;
	}

	public T getValue()
	{
		return preferenceGetter.apply(preferenceName, defaultValue);
	}

	public void setValue(T value)
	{
		preferenceSetter.accept(preferenceName, value);
	}

	private void setDefaultValue()
	{
		this.setValue(defaultValue);
	}

	private static class CompGeoCanvasIntegerPreference extends CompGeoCanvasPreference<Integer>
	{
		public CompGeoCanvasIntegerPreference(String preferenceName, Integer defaultValue)
		{
			super(preferenceName, defaultValue, PREFERENCES_API::getInt, PREFERENCES_API::putInt);
		}
	}

	private static class CompGeoCanvasBooleanPreference extends CompGeoCanvasPreference<Boolean>
	{
		public CompGeoCanvasBooleanPreference(String preferenceName, Boolean defaultValue)
		{
			super(preferenceName, defaultValue, PREFERENCES_API::getBoolean, PREFERENCES_API::putBoolean);
		}
	}

	public static void resetSavedPreferences()
	{
		for (CompGeoCanvasPreference preference : ALL_PREFERENCES)
		{
			preference.setDefaultValue();
		}
	}
}
