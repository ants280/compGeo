package com.github.ants280.compgeo;

import java.util.HashMap;
import java.util.Map;

public class Binomial
{
	private final Map<BinomialValueKey, Long> cache;

	public Binomial()
	{
		cache = new HashMap<>();
	}

	public long of(int n, int k)
	{
		validateParameters(n, k);

		return ofInternal(n, k);
	}

	/**
	 * Calculates the binomial using the recursive formula rather than the
	 * multiplicative one. See
	 * {@see https://en.wikipedia.org/wiki/Binomial_coefficient}.
	 *
	 * @param n The n argument of the binomial
	 * @param k The k argument of the binomial
	 * @return The binomial of n and k : (n k) == n! / k!(n-k)!
	 */
	private long ofInternal(int n, int k)
	{
		if (n == k || k == 0)
		{
			return 1;
		}

		BinomialValueKey binomialValueKey = new BinomialValueKey(n, k);
		if (cache.containsKey(binomialValueKey))
		{
			return cache.get(binomialValueKey);
		}
		else
		{
			long binomialValue = Math.addExact(
					ofInternal(n - 1, k - 1),
					ofInternal(n - 1, k));
			cache.put(binomialValueKey, binomialValue);
			return binomialValue;
		}
	}

	private static void validateParameters(int n, int k)
	{
		if (n < 0)
		{
			throw CompGeoUtils.createIllegalArgumentException("n must be >= 0", n);
		}
		if (k < 0)
		{
			throw CompGeoUtils.createIllegalArgumentException("k must be >= 0", k);
		}
		if (n < k)
		{
			throw CompGeoUtils.createIllegalArgumentException("n must be > k", n, k);
		}
	}

	private static class BinomialValueKey
	{
		private final int n;
		private final int k;

		public BinomialValueKey(int n, int k)
		{
			this.n = n;
			this.k = k;
		}

		private int getN()
		{
			return n;
		}

		private int getK()
		{
			return k;
		}

		@Override
		public int hashCode()
		{
			int hash = 5;
			hash = 41 * hash + this.n;
			hash = 41 * hash + this.k;
			return hash;
		}

		@Override
		public boolean equals(Object obj)
		{
			return this == obj
					|| obj != null
					&& this.getClass() == obj.getClass()
					&& this.n == ((BinomialValueKey) obj).getN()
					&& this.k == ((BinomialValueKey) obj).getK();
		}
	}
}
