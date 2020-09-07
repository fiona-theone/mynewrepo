package routing;

import java.io.Serializable;
import java.util.Map;

import core.DTNHost;
import util.Tuple;

public class StorageMetrics implements Serializable
{
   private long bufferSize;
   private double bufferOccupancy;
   private double lastEncTime;
   
   
    public StorageMetrics(DTNHost h, Double time) 
    {
         super();
        // TODO Auto-generated constructor stub
         this.bufferSize = h.getRouter().getBufferSize();
         this.bufferOccupancy = h.getBufferOccupancy();
         this.lastEncTime = time;
    }


    public long getBufferSize()
    {
        return bufferSize;
    }


    public void setBufferSize(long bufferSize)
    {
        this.bufferSize = bufferSize;
    }


    public double getBufferOccupancy()
    {
        return bufferOccupancy;
    }


    public void setBufferOccupancy(double bufferOccupancy)
    {
        this.bufferOccupancy = bufferOccupancy;
    }


    public double getLastEncTime()
    {
        return lastEncTime;
    }


    public void setLastEncTime(double lastEncTime)
    {
        this.lastEncTime = lastEncTime;
    }


    @Override
    public String toString()
    {
        return "StorageMetrics [bufferSize=" + bufferSize + ", bufferOccupancy=" + bufferOccupancy + ", lastEncTime=" + lastEncTime + "]";
    }
      
  
   
}
