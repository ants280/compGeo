package com.github.ants280.compGeo.line;

import com.github.ants280.compGeo.Point;

public interface Line<E>
{
	Point getIntersectionPoint(E otherLine);
}
