package routing;

import java.util.HashMap;
import java.util.Map;

public class MetadataSize
{
    public static Map<String, Integer> metadataSizeOfAllHosts;
    static {
        metadataSizeOfAllHosts = new HashMap<String, Integer>();
        }
    public static Map<String, Integer> metadataSizeOfAllHostsExchanged;
    static {
        metadataSizeOfAllHostsExchanged = new HashMap<String, Integer>();
        }
    
    public static void calculateTotalMetadataSize() {
        int totalMetadataSizeForAllHosts = 0;
        for (Map.Entry<String, Integer> entry : metadataSizeOfAllHosts.entrySet()) {
            totalMetadataSizeForAllHosts += entry.getValue();
        }
        System.out.println("The total metadata size after the last transitive update is: " + totalMetadataSizeForAllHosts) ;
    }
    public static void calculateTotalMetadataSizeExchnagedAtRuntime() {
        int totalMetadataSizeExchangedForAllHosts = 0;
        for (Map.Entry<String, Integer> entry : metadataSizeOfAllHostsExchanged.entrySet()) {
            totalMetadataSizeExchangedForAllHosts += entry.getValue();
        }
        System.out.println("The total metadata size exchanged at runtime is: " + totalMetadataSizeExchangedForAllHosts) ;
    }
}
