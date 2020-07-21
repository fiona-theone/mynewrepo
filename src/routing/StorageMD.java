package routing;

import java.util.*;
import java.util.Map.Entry;

import util.Tuple;

import core.DTNHost;
import core.Message;
import core.Connection;

public class StorageMD extends Metadata
{
    public static Map<String, Tuple<?,?>> storageMetadata;
    
    static {
        storageMetadata = new HashMap<String, Tuple<?,?>>(); 
    }

    public StorageMD(DTNHost h)
    {
        super(h);
       
    }
      
    @Override
    public void connUp(DTNHost h, double time) {
        BufferSize bs = new BufferSize(h);  
        BufferOccupancy bo = new BufferOccupancy(h);
        LastContactTime ct = new LastContactTime(time);
        storageMetadata.put(h.toString(),new Tuple<String, Double>(bs.getName(),bs.getValue()));
        storageMetadata.put(h.toString(),new Tuple<String, Double>(bo.getName(),bo.getValue()));
        storageMetadata.put(h.toString(),new Tuple<String, Double>(ct.getName(),ct.getValue()));
        
    }
    
   

    @Override
    public void connDown(DTNHost thisHost, DTNHost otherHost, double time) {
        
        
    }

}
