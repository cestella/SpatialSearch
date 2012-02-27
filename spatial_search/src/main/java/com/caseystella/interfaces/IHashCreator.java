package com.caseystella.interfaces;

import org.apache.commons.math.MathException;


public interface IHashCreator
{
	public ILSH construct(int hashDimension, long seed) throws MathException;
}