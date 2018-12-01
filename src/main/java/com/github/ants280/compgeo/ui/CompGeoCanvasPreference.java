package com.github.ants280.compgeo.ui;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.prefs.Preferences;

public abstract class CompGeoCanvasPreference<T>
{
	private static final Preferences PREFERENCES_API = Preferences.userNodeForPackage(CompGeoCanvas.class);

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

	public void setDefaultValue()
	{
		this.setValue(defaultValue);
	}

	public static class CompGeoCanvasIntegerPreference extends CompGeoCanvasPreference<Integer>
	{
		public CompGeoCanvasIntegerPreference(String preferenceName, Integer defaultValue)
		{
			super(preferenceName, defaultValue, PREFERENCES_API::getInt, PREFERENCES_API::putInt);
		}
	}

	public static class CompGeoCanvasBooleanPreference extends CompGeoCanvasPreference<Boolean>
	{
		public CompGeoCanvasBooleanPreference(String preferenceName, Boolean defaultValue)
		{
			super(preferenceName, defaultValue, PREFERENCES_API::getBoolean, PREFERENCES_API::putBoolean);
		}
	}
}
