package com.github.ants280.compgeo;

/**
 * A RuntimeException that is caused by a predictable reason.
 */
public class CompGeoException extends RuntimeException
{
	public CompGeoException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
