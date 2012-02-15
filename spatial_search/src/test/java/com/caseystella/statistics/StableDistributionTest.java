package com.caseystella.statistics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.linear.RealVector;

import com.caseystella.AppTest;
import com.caseystella.KNN;
import com.caseystella.KNN.IBackingStore;
import com.caseystella.KNN.IHashCreator;
import com.caseystella.KNN.Payload;
import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;

public class StableDistributionTest extends TestCase 
{
	private static class InMemoryBackingStore implements IBackingStore
	{
		ListMultimap<Long, Payload> multimap;
		public InMemoryBackingStore()
		{
			Supplier<List<Payload>> supplier = new Supplier<List<Payload>>()
					{
				@Override
				public List<Payload> get() {
					return new ArrayList<Payload>();
				}
					};
			Map<Long, Collection<Payload>> backingMap = new HashMap<Long, Collection<Payload>>();
			multimap = Multimaps.<Long, Payload> newListMultimap( backingMap
		
												, supplier
												);
		}
		@Override
		public Iterable<Payload> getBucket(long key) {
			return multimap.get(key);
		}
		@Override
		public void persist(long key, Payload payload) {
			multimap.put(key, payload);
			
		}
	}
	/**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public StableDistributionTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( StableDistributionTest.class );
    }
   
    public void testL1Metric()
    {
    	KNN knn = new KNN(8
    					 , 3
    					 , 0
    					 , new IHashCreator()
    	{
    		@Override
    		public Function<RealVector, Long> construct(int hashDimension,
    				long seed) {
    			return new StableDistributionFunction(3, 1.0f, 0)
    			{
    				protected com.google.common.base.Function<org.apache.commons.math.random.RandomDataImpl,Double> getSampler(org.apache.commons.math.random.RandomDataImpl dataSampler) 
    				{
    					return new L1Sampler();
    				}
    			};
    		}
    	}, new InMemoryBackingStore()
		
    					 );
    	List<RealVector> vectors = new ArrayList<RealVector>();
    	for(double i = 0.0;i < 5;i += 0.1)
    	{
    		for(double j = 0.0;j < 5;j += 0.1)
        	{
    			for(double k = 0.0;k < 5;k += 0.1)
            	{
    				RealVector vec = new ArrayRealVector(new double[] { i, j, k });
    				Payload payload = new Payload(vec, new byte[] {} );
    				vectors.add(vec);
            		knn.insert(payload);
            	}
        	}
    	}
    	
    	RealVector query = new ArrayRealVector(new double[] { 0, 0, 0} );
    	Iterable<Payload> results = knn.query(query, KNN.L1, .3);
    	int hypothesisPayloadSize = 0;
    	for(Payload result : results)
    	{
    		hypothesisPayloadSize++;
    		double distance = KNN.L1.apply(result.getVector(), query);
    		System.out.println(distance);
    		Assert.assertTrue(distance < .3);
    	}
    	int actualNum = 0;
    	for(RealVector v : vectors)
    	{
    		double distance = KNN.L1.apply(v, query);
    		if(distance < .3)
    		{
    			actualNum++;
    		}
    	}
    	Assert.assertEquals(actualNum, hypothesisPayloadSize);
    }
    
    
    
}
