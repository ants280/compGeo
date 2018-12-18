package com.github.ants280.compgeo.ui;

import com.github.ants280.compgeo.Point;
import java.awt.event.ComponentListener;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;

@RunWith(Parameterized.class)
public class CompGeoComponentListenerTest
{
	private final CompGeoFrame mockCompGeoFrame;
	private final boolean expectedFrameClear;
	private final boolean expectedFrameUpdatePointControls;

	public CompGeoComponentListenerTest(
			List<Point> points,
			int canvasWidth,
			int canvasHeight,
			boolean hasBackgroundImage,
			boolean expectedFrameClear,
			boolean expectedFrameUpdatePointControls)
	{

		this.mockCompGeoFrame = Mockito.mock(CompGeoFrame.class);
		this.expectedFrameClear = expectedFrameClear;
		this.expectedFrameUpdatePointControls = expectedFrameUpdatePointControls;

		CompGeoCanvas mockCompGeoCanvas = Mockito.mock(CompGeoCanvas.class);
		Mockito.when(mockCompGeoCanvas.hasBackgroundImage()).thenReturn(hasBackgroundImage);
		Mockito.when(mockCompGeoFrame.getCanvas()).thenReturn(mockCompGeoCanvas);
		Mockito.when(mockCompGeoCanvas.getPoints()).thenReturn(points);
		Mockito.when(mockCompGeoCanvas.getWidth()).thenReturn(canvasWidth);
		Mockito.when(mockCompGeoCanvas.getHeight()).thenReturn(canvasHeight);
	}

	@Parameterized.Parameters(name = "{index}: points:{0},width:{1},height:{2},hasBackgroundImage:{3}expectFramePointsCleared:{4},expectedFrameUpdatePointControls:{5}")
	public static Iterable<Object[]> data()
	{
		return Arrays.asList(
				createTestCase(Arrays.asList(new Point(1, 1), new Point(1, 100), new Point(2, 2)), 15, 30, false, true, false),
				createTestCase(Arrays.asList(new Point(1, 1), new Point(1, 100), new Point(2, 2)), 15, 30, true, false, false),
				createTestCase(Arrays.asList(new Point(1, 1), new Point(100, 1), new Point(2, 2)), 15, 30, false, true, false),
				createTestCase(Arrays.asList(new Point(1, 1), new Point(100, 1), new Point(2, 2)), 15, 30, true, false, false),
				createTestCase(Arrays.asList(new Point(10, 20)), 15, 30, false, false, true),
				createTestCase(Arrays.asList(new Point(10, 20)), 15, 30, true, false, false),
				createTestCase(Arrays.asList(), 100, 100, false, false, true),
				createTestCase(Arrays.asList(), 100, 100, true, false, false));
	}

	private static Object[] createTestCase(
			List<Point> points,
			int canvasWidth,
			int canvasHeight,
			boolean hasBackgroundImage,
			boolean expectedFrameClear,
			boolean expectedFrameUpdatePointControls)
	{
		return new Object[]
		{
			points,
			canvasWidth,
			canvasHeight,
			hasBackgroundImage,
			expectedFrameClear,
			expectedFrameUpdatePointControls
		};
	}

	@Test
	public void testComponentResized()
	{
		Assert.assertTrue(
				"only one should be expected",
				mockCompGeoFrame.getCanvas().hasBackgroundImage()
				^ expectedFrameClear
				^ expectedFrameUpdatePointControls);
		ComponentListener componentListener
				= new CompGeoComponentListener(mockCompGeoFrame);

		// Passing null for ComponentEvent because it is not used.
		componentListener.componentResized(null);

		Mockito.verify(
				mockCompGeoFrame,
				Mockito.times(expectedFrameClear ? 1 : 0))
				.clear();
		Mockito.verify(
				mockCompGeoFrame,
				Mockito.times(expectedFrameUpdatePointControls ? 1 : 0))
				.updatePointControls();
	}
}
