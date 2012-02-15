package com.caseystella;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.math.linear.RealVector;

import com.google.common.base.Function;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(payload);
		result = prime * result + ((vector == null) ? 0 : vector.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Payload other = (Payload) obj;
		if (!Arrays.equals(payload, other.payload))
			return false;
		if (vector == null) {
			if (other.vector != null)
				return false;
		} else if (!vector.equals(other.vector))
			return false;
		return true;
	}


   }
   public static interface IDistanceMetric
   {
	   public double apply(RealVector v1, RealVector v2);
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

   public static IDistanceMetric L1 = new IDistanceMetric()
   {
	   @Override
	public double apply(RealVector v1, RealVector v2) {
		return v1.getL1Distance(v2);
	}
   };
   public static IDistanceMetric L2 = new IDistanceMetric()
   {
	   @Override
	public double apply(RealVector v1, RealVector v2) {
		return v1.getDistance(v2);
	}
   };
   
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
   
   public Iterable< Payload> query(RealVector q, IDistanceMetric metric, double limit)
   {
      Set<Payload> results = new HashSet<Payload>(); 
      for(Function<RealVector, Long> hash : hashes)
      {
         //find the thing in the bucket
         Iterable<Payload> values = backingStore.getBucket(hash.apply(q));
         for(Payload value : values)
         {
            if(metric.apply(q, value.getVector()) < limit)
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
