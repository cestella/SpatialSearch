package com.caseystella.statistics;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.math.MathException;
import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.linear.RealVector;

import com.caseystella.KNN;
import com.caseystella.KNN.Payload;
import com.caseystella.interfaces.IHashCreator;
import com.caseystella.interfaces.ILSH;
import com.caseystella.math.stabledistribution.L1LSH;
import com.caseystella.math.stabledistribution.L2LSH;
import com.caseystella.util.InMemoryBackingStore;

public class StableDistributionTest extends TestCase 
{
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
    
    private static void driver( KNN knn)
    {
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
    	Iterable<Payload> results = knn.query(query, .3);
    	int hypothesisPayloadSize = 0;
    	for(Payload result : results)
    	{
    		hypothesisPayloadSize++;
    		double distance = knn.getUnderlyingMetric().apply(result.getVector(), query);
    		//System.out.println(distance);
    		Assert.assertTrue(distance < .3);
    	}
    	int actualNum = 0;
    	for(RealVector v : vectors)
    	{
    		double distance = knn.getUnderlyingMetric().apply(v, query);
    		if(distance < .3)
    		{
    			actualNum++;
    		}
    	}
    	Assert.assertEquals(actualNum, hypothesisPayloadSize);
    }
   
    public void testL1Metric() throws MathException
    {
    	KNN knn = new KNN(3
    					 , 3
    					 , 0
    					 , new IHashCreator()
    	{
    		@Override
    		public ILSH construct(int hashDimension,
    				long seed) throws MathException {
    			return new L1LSH(3, 1.0f, seed);
    			
    		}
    	}, new InMemoryBackingStore()
		
    					 );
    	driver(knn);
    }
    
    public void testL2Metric() throws MathException
    {
    	KNN knn = new KNN(3
    					 , 3
    					 , 0
    					 , new IHashCreator()
    	{
    		@Override
    		public ILSH construct(int hashDimension,
    				long seed) throws MathException {
    			return new L2LSH(3, 1.0f, seed);
    			
    		}
    	}, new InMemoryBackingStore()
		
    					 );
    	driver(knn);
    }
    
}
