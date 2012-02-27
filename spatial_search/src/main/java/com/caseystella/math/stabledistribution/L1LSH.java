package com.caseystella.math.stabledistribution;

import org.apache.commons.math.MathException;
import org.apache.commons.math.random.RandomDataImpl;
import org.apache.commons.math.random.RandomGenerator;

import com.caseystella.interfaces.IDistanceMetric;
import com.caseystella.math.L1DistanceMetric;
import com.google.common.base.Function;

public class L1LSH extends AbstractStableDistributionFunction 
{
	protected static class L1Sampler implements ISampler
	{
		/**
		 * {@inheritDoc}
		 * @throws MathException 
		 * 
		 * @see Function#apply(RandomDataImpl)
		 */
		public double apply(RandomDataImpl randomData) throws MathException {
			
			return randomData.nextCauchy(0, 1);
			
		}

	}
	private static ISampler sampler = new L1Sampler();
	private static IDistanceMetric metric = new L1DistanceMetric();
	/**
	 * Constructs a new instance.
	 * @throws MathException 
	 */
	public L1LSH(int dim, float w, RandomGenerator rand) throws MathException {
		super(dim, w, rand);
	}

	public L1LSH(int dim, float w, long seed) throws MathException {
		
		super(dim, w, seed);
	}
	
	@Override
	public IDistanceMetric getMetric() {
		return metric;
	}

	@Override
	protected ISampler getSampler(
			RandomDataImpl dataSampler) {
		return sampler;
	}

}
