package com.jpatterson.school.compGeo.algorithm;

import com.jpatterson.school.compGeo.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class GrahamScanTest
{
	private final List<Point> points;
	private final List<Integer> expectedConvexHullPointIndexes;

	public GrahamScanTest(
		List<Point> points, List<Integer> expectedConvexHullPointIndexes)
	{
		this.points = points;
		this.expectedConvexHullPointIndexes = expectedConvexHullPointIndexes;
	}

	@Parameters
	public static Iterable<Object[]> data()
	{
		return Arrays.asList(
			createTestCase(Collections.emptyList(), Collections.emptyList()),
			createTestCase(Collections.singletonList(new Point(0, 0)), Collections.singletonList(0)),
			createTestCase(Arrays.asList(new Point(0, 0), new Point(0, 1)), Arrays.asList(0, 1)),
			createTestCase(Arrays.asList(new Point(0, 0), new Point(0, 0)), Collections.singletonList(0)),
			createTestCase(Arrays.asList(new Point(0, 0), new Point(1, 0), new Point(0, 1)), Arrays.asList(0, 1, 2)),
			createTestCase(Arrays.asList(new Point(0, 0), new Point(1, 0), new Point(2, 0)), Arrays.asList(0, 2)),
			createTestCase(Arrays.asList(new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(0, 1)), Arrays.asList(0, 1, 2, 3)),
			createTestCase(Arrays.asList(new Point(0, 0), new Point(2, 0), new Point(1, 1), new Point(0, 2)), Arrays.asList(0, 1, 3)),
			createTestCase(Arrays.asList(new Point(0, 0), new Point(1, 0), new Point(0, 2), new Point(0, 3)), Arrays.asList(0, 1, 3)),
			// Should not form a concave pentagon: if interiorPoint checks the last in list instead of first
			createTestCase(Arrays.asList(new Point(4, 0), new Point(8, 4), new Point(5, 5), new Point(4, 8), new Point(0, 4)), Arrays.asList(0, 1, 3, 4)));
	}

	private static Object[] createTestCase(List<Point> points, List<Integer> expectedConvexHullPointIndexes)
	{
		return new Object[]
		{
			points, expectedConvexHullPointIndexes
		};
	}

	@Test
	public void testGetConvexHullPoints()
	{
		List<Point> shuffledPoints = new ArrayList<>(points);
		long seed = System.currentTimeMillis();
		Random random = new Random(seed);
		Collections.shuffle(shuffledPoints, random); // make sure the ordering works!  Should at least print out the order of points used if fails.  To see the use of this, don't sort on GrahamScan.getFarthestPoint()

		GrahamScan grahamScan = new GrahamScan(shuffledPoints);
		List<Point> actualConvexHullPoints = grahamScan.getConvexHullPoints();
		List<Point> expectedConvexHullPoints = expectedConvexHullPointIndexes.stream()
			.mapToInt(Integer::intValue)
			.mapToObj(points::get)
			.collect(Collectors.toList());

		assertEquals("Convex hull test failed with seed=" + seed,
			expectedConvexHullPoints, actualConvexHullPoints);
	}
}
