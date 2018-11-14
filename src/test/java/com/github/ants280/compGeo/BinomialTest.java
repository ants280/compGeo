package com.jpatterson.school.compGeo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;

@RunWith(Parameterized.class)
public class BinomialTest
{
	@Parameter() // default is 0
	public int n;
	@Parameter(1)
	public int k;
	@Parameter(2)
	public Object expectedValue;

	@Parameters
	public static Iterable<Object[]> data()
	{
		return Arrays.asList(
			createTestCase(0, 0, 1L),
			createTestCase(1, 1, 1L),
			createTestCase(10, 10, 1L),
			createTestCase(1, 0, 1L),
			createTestCase(10, 0, 1L),
			createTestCase(7, 1, 7L),
			createTestCase(7, 6, 7L),
			createTestCase(5, 2, 10L),
			createTestCase(5, 3, 10L),
			createTestCase(6, 3, 20L),
			createTestCase(499, 4, 2552446876L));
	}

	private static Object[] createTestCase(int n, int k, Object expectedValue)
	{
		return new Object[]
		{
			n, k, expectedValue
		};
	}

	@Test
	public void test()
	{
		Binomial binomial = new Binomial();

		Long actualValue = binomial.of(n, k);

		Assert.assertEquals(expectedValue, actualValue);
	}
}
