package com.caseystella;

import java.lang.Iterable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.math.linear.RealVector;

import com.google.common.base.Function;

import com.google.common.collect.Iterables;

public class KNN
{
   public static interface IHashCreator
   {
      public Function<RealVector, Long> construct(int hashDimension, long seed);
   }

   private Iterable<Function<RealVector, Long>> hashes;
   public KNN( int numHashes
             , int hashDimension
             , long seed
             , IHashCreator creator
             )
   {
      List<Function<RealVector, Long > > hashList = new ArrayList<Function<RealVector,Long>> (numHashes);
      for(int i = 0;i < numHashes;++i)
      {
         hashList.add(creator.construct(hashDimension, seed));
      }
      hashes = hashList;
   }
   
   public Iterable<byte[]> query(RealVector q, Function<RealVector, Double> metric, double limit)
   {
      for(Function<RealVector, Long> hash : hashes)
      {
         //find the thing in the bucket
         
      }
   }

   public <T> Iterable<T> query( RealVector q
                               , Function<byte[], T> transformer
                               )
   {
       return Iterables.transform(query(q), transformer);
   }
}
