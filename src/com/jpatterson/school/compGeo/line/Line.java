package com.jpatterson.school.compGeo.line;

import com.jpatterson.school.compGeo.Point;

public interface Line<E>
{
	Point getIntersectionPoint(E otherLine);
}
