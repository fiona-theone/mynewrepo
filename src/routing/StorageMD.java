package routing;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
    
    /* Update storage metadata for host we just met.
       First check if I have this host already in my map and the last encounter time 
    */
    public void updateStorageMetricsFor(DTNHost thisHost, DTNHost otherHost, double time) {
        MessageRouter thisRouter = thisHost.getRouter();
        Map<String, StorageMetrics> myStorageMetadata =
                ((Router10)thisRouter).getStorageMetadata(); 
          if(myStorageMetadata.size() != 0) {
            if(myStorageMetadata.containsKey(otherHost.toString())) {
                if(myStorageMetadata.get(otherHost.toString()).getLastEncTime() < time) {
                    StorageMetrics sm = new StorageMetrics(otherHost, time);
                    myStorageMetadata.put(otherHost.toString(), sm); 
                   // updateMetadataSize();
                }
            }else {
                  StorageMetrics sm = new StorageMetrics(otherHost, time);
                  myStorageMetadata.put(otherHost.toString(), sm); 
                //updateMetadataSize();
            }
        }else {
        StorageMetrics sm = new StorageMetrics(otherHost, time);
        myStorageMetadata.put(otherHost.toString(), sm); 
        //updateMetadataSize();
        }

    }
    
    /* Update transitive storage metadata
       For each storage metadata other host has, check if I already have this info 
       and if I have it check last encounter time 
    */
    
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
                    //updateMetadataSize();
                }
            }else {
                myStorageMetadata.put(entry.getKey(), entry.getValue()); 
                //updateMetadataSize();
            }
            
        }
    }
    
    /* Write to file every time an update happens
 */
    
   public void updateMetadataSize(String data, int noOfLines) {
       FileWriter fr = null;
       BufferedWriter br = null;
       String dataWithNewLine=data+System.getProperty("line.separator");
       try{
           fr = new FileWriter(Router10.file);
           br = new BufferedWriter(fr);
           for(int i = noOfLines; i>0; i--){
               br.write(dataWithNewLine);
           }
       } catch (IOException e) {
           e.printStackTrace();
       }finally{
           try {
               br.close();
               fr.close();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
   }

    @Override
    public void connDown(Connection con, DTNHost otherHost, double time) {
        
        
    }
    

}
