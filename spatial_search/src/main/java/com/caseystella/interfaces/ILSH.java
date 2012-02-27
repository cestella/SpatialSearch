package com.caseystella.interfaces;

import org.apache.commons.math.linear.RealVector;

public interface ILSH 
{
	public IDistanceMetric getMetric();
	public long apply(RealVector vector);
}
