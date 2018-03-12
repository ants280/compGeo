package com.jpatterson.school.compGeo;

import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class BinomialTest
{
	@Parameter(0)
	public int n;
	@Parameter(1)
	public int k;
	@Parameter(2)
	public Object expectedValue;

	@Parameters
	public static Iterable<Object[]> data()
	{
		return Arrays.asList(new Object[][] {
				new Object[] { 0, 0, 1L},
				new Object[] { 1, 1, 1L},
				new Object[] { 10, 10, 1L},
				new Object[] { 1, 0, 1L},
				new Object[] { 10, 0, 1L},
				new Object[] { 7, 1, 7L},
				new Object[] { 7, 6, 7L},
				new Object[] { 5, 2, 10L},
				new Object[] { 5, 3, 10L},
				new Object[] { 6, 3, 20L},
				new Object[] { 499, 4, 2552446876L},
			});
	}

	@Test
	public void test()
	{
		Binomial binomial = new Binomial();
		
		Long actualValue = binomial.of(n, k);
		
		Assert.assertEquals(expectedValue, actualValue);
	}
}
