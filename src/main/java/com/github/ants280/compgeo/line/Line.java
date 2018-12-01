package com.github.ants280.compgeo.line;

import com.github.ants280.compgeo.Point;

public interface Line<E>
{
	Point getIntersectionPoint(E otherLine);
}
