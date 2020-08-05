package routing;

import java.util.*;
import core.DTNHost;
import core.Connection;

public class StorageMD extends Metadata
{
    /* public static Map<String, StorageMetrics> storageMetadata;
    static {
    storageMetadata = new HashMap<String, StorageMetrics>(); 
    }*/

    public StorageMD(DTNHost h)
    {
        super(h);
       
    }
      
    @Override
    public void connUp(Connection con, DTNHost thisHost, double time) {
        DTNHost otherHost = con.getOtherNode(thisHost);;
        updateStorageMetricsFor(thisHost, otherHost,time);
        updateTransitiveStorageMetrics(otherHost, thisHost, time);  
    }
    
    public void updateStorageMetricsFor(DTNHost thisHost, DTNHost otherHost, double time) {
        MessageRouter thisRouter = thisHost.getRouter();
        Map<String, StorageMetrics> myStorageMetadata =
                ((Router10)thisRouter).getStorageMetadata(); 
        Map<String, StorageMetrics> myStorageMetadataCopy = new HashMap<String, StorageMetrics>(); // avoid ConcurrentModificationException
        if(myStorageMetadata.size() != 0) {
        for (Map.Entry<String, StorageMetrics> entry : myStorageMetadata.entrySet()) {
            if (entry.getKey()== otherHost.toString()){
                if(entry.getValue().getLastEncTime()<time) {
                    StorageMetrics sm = new StorageMetrics(otherHost, time);
                    myStorageMetadataCopy.put("StorageMetadata for " + otherHost.toString() + ": ", sm);
                }
                
            }else {
                StorageMetrics sm = new StorageMetrics(otherHost, time);
                myStorageMetadataCopy.put("StorageMetadata for " + otherHost.toString() + ": ", sm); 
            }
        }
        }
        for (Map.Entry<String, StorageMetrics> entry : myStorageMetadataCopy.entrySet()) {
            myStorageMetadata.put(entry.getKey(), entry.getValue());
        }
        StorageMetrics sm = new StorageMetrics(otherHost, time);
        myStorageMetadata.put("StorageMetadata for " + otherHost.toString() + ": ", sm); 

    }
    
    public void updateTransitiveStorageMetrics( DTNHost otherHost, DTNHost thisHost, double time ) {
        MessageRouter thisRouter = thisHost.getRouter();
        Map<String, StorageMetrics> myStorageMetadata =
                ((Router10)thisRouter).getStorageMetadata();
        MessageRouter otherRouter = otherHost.getRouter();
        Map<String, StorageMetrics> otherHostStorageMetadata =
                ((Router10)otherRouter).getStorageMetadata();
        for (Map.Entry<String, StorageMetrics> entry : otherHostStorageMetadata.entrySet()) {
            if (entry.getKey() == thisHost.toString()) {
                continue; 
            }
            StorageMetrics storageMetrics = myStorageMetadata.get(entry.getKey()); 
            if(storageMetrics != null) {
                if(storageMetrics.getLastEncTime() < entry.getValue().getLastEncTime()) {
                    myStorageMetadata.put(entry.getKey(), entry.getValue());
                }
            }else {
                myStorageMetadata.put(entry.getKey(), entry.getValue()); 
            }
            
        }
    }
    
   

    @Override
    public void connDown(Connection con, DTNHost otherHost, double time) {
        
        
    }
    

}
