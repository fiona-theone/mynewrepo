package routing;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
        MessageRouter thisRouter = thisHost.getRouter();
        Map<String, StorageMetrics> myStorageMetadata =
                ((Router10)thisRouter).getStorageMetadata(); 
        ((Router10)thisRouter).setStorageMetadataSizeForThisHost(sizeOfBytes(myStorageMetadata)); 
        ((Router10)thisRouter).storeTotalMetadataSizeForThisHost();
        ((Router10)thisRouter).printSizeOfMetadataExchanged();
    }
    
    /* Update storage metadata for host we just met.
       First check if I have this host already in my map and the last encounter time 
    */
    public void updateStorageMetricsFor(DTNHost thisHost, DTNHost otherHost, double time) {
        MessageRouter thisRouter = thisHost.getRouter();
        Map<String, StorageMetrics> myStorageMetadata =
                ((Router10)thisRouter).getStorageMetadata(); 
        List<Integer> storageMetadataSizeExchangedForThisHost = ((Router10)thisRouter).getStorageMetadataSizeExchangedForThisHost();
          if(myStorageMetadata.size() != 0) {
            if(myStorageMetadata.containsKey(otherHost.toString())) {
                if(myStorageMetadata.get(otherHost.toString()).getLastEncTime() < time) {
                    StorageMetrics sm = new StorageMetrics(otherHost, time);
                    myStorageMetadata.put(otherHost.toString(), sm); 
                    //storageMetadataSizeExchangedForThisHost.add(sizeOfBytes(sm));
                    storageMetadataSizeExchangedForThisHost.add(32);
                }
            }else {
                  StorageMetrics sm = new StorageMetrics(otherHost, time);
                  myStorageMetadata.put(otherHost.toString(), sm); 
                  //storageMetadataSizeExchangedForThisHost.add(sizeOfBytes(sm));
                  storageMetadataSizeExchangedForThisHost.add(32);
            }
        }else {
        StorageMetrics sm = new StorageMetrics(otherHost, time);
        myStorageMetadata.put(otherHost.toString(), sm); 
        //storageMetadataSizeExchangedForThisHost.add(sizeOfBytes(sm));
        storageMetadataSizeExchangedForThisHost.add(32);
        }

    }
    
    /* Update transitive storage metadata
       For each storage metadata other host has, check if I already have this info 
       and if I have it check last encounter time 
    */
    
    public void updateTransitiveStorageMetrics( DTNHost otherHost, DTNHost thisHost, double time ) {
        MessageRouter thisRouter = thisHost.getRouter();
        List<Integer> storageMetadataSizeExchangedForThisHost = ((Router10)thisRouter).getStorageMetadataSizeExchangedForThisHost();
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
                    storageMetadataSizeExchangedForThisHost.add(32);
                }
            }else {
                myStorageMetadata.put(entry.getKey(), entry.getValue());   
                storageMetadataSizeExchangedForThisHost.add(32);
            }
            //storageMetadataSizeExchangedForThisHost.add(sizeOfBytes(storageMetrics));
        }
    }
    

    @Override
    public void connDown(Connection con, DTNHost otherHost, double time) {
        
        
    }
    
    public int sizeOfBytes(Object o) {
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        try{
            ObjectOutputStream oos=new ObjectOutputStream(baos);
            oos.writeObject(o );
            oos.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return baos.size();
    }

}
