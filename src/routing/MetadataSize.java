package routing;

import java.util.HashMap;
import java.util.Map;

public class MetadataSize
{
    public static Map<String, Integer> metadataSizeOfAllHosts;
    static {
        metadataSizeOfAllHosts = new HashMap<String, Integer>();
        }
    
    public static void calculateTotalMetadataSize() {
        int totalMetadataSizeForAllHosts = 0;
        for (Map.Entry<String, Integer> entry : metadataSizeOfAllHosts.entrySet()) {
            totalMetadataSizeForAllHosts += entry.getValue();
        }
        System.out.println("The total metadata size after the last transitive update is: " + totalMetadataSizeForAllHosts) ;
    }
}
