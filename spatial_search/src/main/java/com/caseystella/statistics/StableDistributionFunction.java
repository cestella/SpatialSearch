package com.caseystella.statistics;

import org.apache.commons.math.MathException;

import org.apache.commons.math.linear.RealVector;

import org.apache.commons.math.random.JDKRandomGenerator;
import org.apache.commons.math.random.RandomDataImpl;
import org.apache.commons.math.random.RandomGenerator;

import com.google.common.base.Function;

public abstract class StableDistributionFunction implements Function<RealVector, Long>
{
   public static class L1Sampler implements Function< RandomDataImpl
                                                    , Double
                                                    >
   {
      /**
       * {@inheritDoc}
       * @see Function#apply(RandomDataImpl)
       */
      public Double apply(RandomDataImpl randomData)
      {
         try
         {
            return randomData.nextCauchy(0,1); 
         }
         catch(MathException me)
         {
            //I really hate this :(
            throw new RuntimeException(me);
         }
      }
         
   }

   public static class L2Sampler implements Function< RandomDataImpl
                                                    , Double
                                                    >
   {

      /**
       * {@inheritDoc}
       * @see Function#apply(RandomDataImpl)
       */
      public Double apply(RandomDataImpl randomData)
      {
            return randomData.nextGaussian(0,1); 
      }
         
   }


   private double[] a;
   private double b;
   private float w;
   private int dim;

   /**
    * Constructs a new instance.
    */
   public StableDistributionFunction(int dim, float w, RandomGenerator rand)
   {
      reset(dim, w, rand); 
   }

   public StableDistributionFunction(int dim, float w, int seed)
   {
      RandomGenerator generator = new JDKRandomGenerator();
      generator.setSeed(seed);
      reset(dim
           ,w
           ,generator
           );
   }

   public void reset(int dim, float w, RandomGenerator rand)
   {
      RandomDataImpl dataSampler = new RandomDataImpl(rand);
      Function<RandomDataImpl, Double> sampler = getSampler(dataSampler);      
      this.a = new double[dim];
      this.dim = dim;
      this.w = w;
      for(int i = 0;i < dim;++i)
      {
         a[i] = sampler.apply(dataSampler);
      }
      b = dataSampler.nextUniform(0, w);
   }


   protected abstract Function<RandomDataImpl, Double> 
   getSampler(RandomDataImpl dataSampler);
   
   public Long apply(RealVector vector)
   {
      double ret = b;
      //inner product
      for(int i = 0;i < dim;++i)
      {
         ret += vector.getEntry(i)*a[i];
      }
      return (long)Math.floor(ret/w);
   } 
}
