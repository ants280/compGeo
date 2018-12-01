package com.github.ants280.compgeo;

import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class BinomialTest
{
	private final int n;
	private final int k;
	private final Object expectedValue;

	public BinomialTest(int n, int k, Object expectedValue)
	{
		this.n = n;
		this.k = k;
		this.expectedValue = expectedValue;
	}

	@Parameters(name = "{index}: binomial.of(n:{0},  k:{1}) = expectedValue:{2}")
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
