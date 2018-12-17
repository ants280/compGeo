package com.github.ants280.compgeo;

import org.junit.Assert;
import org.junit.Test;

public class CompGeoExceptionTest
{
	@Test
	public void testGetMessage()
	{
		String message = "message :)";
		Throwable cause = null;
		Exception compGeoException = new CompGeoException(message, cause);

		String actualMessage = compGeoException.getMessage();

		Assert.assertEquals(message, actualMessage);
	}

	@Test
	public void testGetCause()
	{
		String message = null;
		Throwable cause = new IllegalArgumentException(":(");
		Exception compGeoException = new CompGeoException(message, cause);

		Throwable actualCause = compGeoException.getCause();

		Assert.assertEquals(cause, actualCause);
	}
}
