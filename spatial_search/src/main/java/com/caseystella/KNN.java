package com.caseystella;

import java.lang.Iterable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.math.linear.RealVector;

import com.google.common.base.Function;

import com.google.common.collect.Iterables;

import com.google.common.primitives.Longs;

import com.sun.tools.javac.util.Pair;

public class KNN
{
   public static class Payload
   {
      private RealVector vector;
      private byte[] payload;

      public Payload(RealVector vector, byte[] payload)
      {
         this.vector = vector;
         this.payload = payload;
      }

      /**
       * Gets the vector for this instance.
       *
       * @return The vector.
       */
      public RealVector getVector()
      {
         return vector;
      }

      /**
       * Gets the payload for this instance.
       *
       * @return The payload.
       */
      public byte[] getPayload()
      {
         return payload;
      }


   }
   public static interface IHashCreator
   {
      public Function<RealVector, Long> construct(int hashDimension, long seed);
   }

   public static interface IBackingStore
   {
      public void persist(long key, Payload payload);
      public Iterable<Payload> getBucket(long key);
   }

   private Iterable<Function<RealVector, Long>> hashes;
   private IBackingStore backingStore;
   public KNN( int numHashes
             , int hashDimension
             , long seed
             , IHashCreator creator
             , IBackingStore backingStore
             )
   {
      this.backingStore = backingStore;
      List<Function<RealVector, Long > > hashList = new ArrayList<Function<RealVector,Long>> (numHashes);
      for(int i = 0;i < numHashes;++i)
      {
         hashList.add(creator.construct(hashDimension, seed));
      }
      hashes = hashList;
   }
   
   public Iterable< Payload> query(RealVector q, Function<RealVector, Double> metric, double limit)
   {
      List<Payload> results = new ArrayList<Payload>(); 
      for(Function<RealVector, Long> hash : hashes)
      {
         //find the thing in the bucket
         Iterable<Payload> values = backingStore.getBucket(hash.apply(q));
         for(Payload value : values)
         {
            if(metric.apply(value.getVector()) < limit)
            {
               results.add(value);
            }
         } 
      }
      return results;
   }
   
   public void insert(Payload payload)
   {
      for(Function<RealVector, Long> hash : hashes)
      {
         long key = hash.apply(payload.getVector());
         backingStore.persist(key, payload);
      }
   }
}
