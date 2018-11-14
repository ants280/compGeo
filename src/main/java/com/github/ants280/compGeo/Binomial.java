package com.github.ants280.compGeo;

import java.util.HashMap;
import java.util.Map;

// (n k) = n! / k!(n-k)!
public class Binomial
{
	private final Map<BinomialValueKey, Long> cache;

	public Binomial()
	{
		cache = new HashMap<>();
	}

	public long of(int n, int k) throws IllegalArgumentException
	{
		validateParameters(n, k);

		return ofInternal(n, k);
	}

//	// https://en.wikipedia.org/wiki/Binomial_coefficient#Multiplicative_formula
//	private long ofInternal(int n, int k)
//	{
//		if (n == k || k == 0)
//		{
//			return 1;
//		}
//
//		BinomialValueKey binomialValueKey = new BinomialValueKey(n, k);
//		if (cache.containsKey(binomialValueKey))
//		{
//			return cache.get(binomialValueKey);
//		}
//		else
//		{
//			k = k > n / 2 ? n - k : k;
//			long numerator = 1;
//			long denominator = 1;
//			for (int i = 1; i <= k; i++)
//			{
//				numerator = Math.multiplyExact(numerator, n + 1 - i);
//				denominator =  Math.multiplyExact(denominator, i);
//			}
//			long binomialValue = numerator / denominator;
//			cache.put(binomialValueKey, binomialValue);
//			return binomialValue;
//		}
//	}
	// https://en.wikipedia.org/wiki/Binomial_coefficient#Recursive_formula
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
			long binomialValue = Math.addExact(ofInternal(n - 1, k - 1), ofInternal(n - 1, k));
			cache.put(binomialValueKey, binomialValue);
			return binomialValue;
		}
	}

	private static void validateParameters(int n, int k) throws IllegalArgumentException
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
			return (this == obj)
					|| ((obj != null)
					&& (this.getClass() == obj.getClass())
					&& (this.n == ((BinomialValueKey) obj).n)
					&& (this.k == ((BinomialValueKey) obj).k));
		}
	}
}
