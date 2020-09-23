package routing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.Connection;
import core.DTNHost;
import core.Settings;
import core.SimClock;
import routing.util.RoutingInfo;

public class Router10 extends ActiveRouter
{
    
    private List<ContactMetrics> cm;
    private Map<String, StorageMetrics> storageMetadata;
    private Map<String, ContactHistory> contactMetadata;
    private int storageMetadataSizeForThisHost;
    private int contactMetadataSizeForThisHost;
    private List<Integer> storageMetadataSizeExchangedForThisHost;
    private List<Integer> contactMetadataSizeExchangedForThisHost;
  
    
    public Router10(ActiveRouter r)
    {
        super(r);
        // TODO Auto-generated constructor stub
    }

    public Router10(Settings s)
    {
        super(s);
        // TODO Auto-generated constructor stub
        init();
    }
    
    protected Router10(Router10 r) {
        super(r);
        init();
    }
    
    public void init() {
        this.cm = new ArrayList<ContactMetrics>();
        this.storageMetadata = new HashMap<String, StorageMetrics>();
        this.contactMetadata = new HashMap<String, ContactHistory>();
        this.storageMetadataSizeForThisHost = 0;
        this.storageMetadataSizeForThisHost = 0;
        this.storageMetadataSizeExchangedForThisHost = new ArrayList<Integer>();
        this.contactMetadataSizeExchangedForThisHost = new ArrayList<Integer>();
    }
    
    @Override
    public void changedConnection(Connection con) {
        DTNHost otherHost = con.getOtherNode(getHost());;
        MetadataInfo metadataInfo = new MetadataInfo(otherHost);
        if (con.isUp()) {
            System.out.println(SimClock.getTime());
            metadataInfo.connUp(con , getHost(), SimClock.getTime()); 
        }else {
            System.out.println(SimClock.getTime());
            metadataInfo.connDown(con, getHost(), SimClock.getTime()); 
        }
    }
    
    public List<ContactMetrics> getListOfAllContactMetrics() {
        return this.cm;
    }
    
    public Map<String, StorageMetrics> getStorageMetadata() {
        return this.storageMetadata;
    }
    
    public Map<String, ContactHistory> getContactMetadata() {
        return this.contactMetadata;
    }
    
   
    
    public int getStorageMetadataSizeForThisHost()
    {
        return storageMetadataSizeForThisHost;
    }

    public void setStorageMetadataSizeForThisHost(int storageMetadataSizeForThisHost)
    {
        this.storageMetadataSizeForThisHost = storageMetadataSizeForThisHost;
    }

    public int getContactMetadataSizeForThisHost()
    {
        return contactMetadataSizeForThisHost;
    }

    public void setContactMetadataSizeForThisHost(int contactMetadataSizeForThisHost)
    {
        this.contactMetadataSizeForThisHost = contactMetadataSizeForThisHost;
    }

    public List<Integer> getStorageMetadataSizeExchangedForThisHost()
    {
        return storageMetadataSizeExchangedForThisHost;
    }

    public List<Integer> getContactMetadataSizeExchangedForThisHost()
    {
        return contactMetadataSizeExchangedForThisHost;
    }

    @Override
    public RoutingInfo getRoutingInfo() {
    RoutingInfo top = super.getRoutingInfo();
    RoutingInfo ri = new RoutingInfo("Metadata Metrics");

     for (String key: getStorageMetadata().keySet()){
       ri.addMoreInfo(new RoutingInfo(String.format("%s ", showStorageInfo(key))));
  
     }
     for (String key: getContactMetadata().keySet()){
       ri.addMoreInfo(new RoutingInfo(String.format("%s ", showContactInfo(key))));
     }
    top.addMoreInfo(ri);

        return top;
    }
    
    private String showStorageInfo(String s) {
        String info = s.toString() + ": " + this.storageMetadata.get(s) + " " ;
        return info;
            }
            
            private String showContactInfo(String s) {
                String info = s + ": " + this.contactMetadata.get(s);
                return info;
                    }

            
       @Override
        public Router10 replicate() {
        Router10 r = new Router10(this);
        return r;
      }
       
       public void storeTotalMetadataSizeForThisHost() {
      
           int totalSumForThisHost = 0;
           totalSumForThisHost += getStorageMetadataSizeForThisHost();
           totalSumForThisHost += getStorageMetadataSizeForThisHost();
           MetadataSize.metadataSizeOfAllHosts.put(getHost().toString(), totalSumForThisHost);
           MetadataSize.calculateTotalMetadataSize();
       }
       
       public void printSizeOfMetadataExchanged() {
           int sum = 0;
           for(Integer size : storageMetadataSizeExchangedForThisHost) {
               sum += size;
           }
           for(Integer size : contactMetadataSizeExchangedForThisHost) {
               sum += size;
           }
           MetadataSize.metadataSizeOfAllHostsExchanged.put(getHost().toString(), sum);
           MetadataSize.calculateTotalMetadataSizeExchnagedAtRuntime();
       }
}
