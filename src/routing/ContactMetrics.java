package routing;

import core.DTNHost;
import util.Tuple;

public class ContactMetrics
{ 
    private Tuple<String,String> hostName;
    private Tuple<String,Double> contactDuration;
    private Tuple<String,Double> startEncTime;
    
    
    public ContactMetrics(DTNHost h, Double time)
    {
         super();
        // TODO Auto-generated constructor stub
         hostName = new Tuple<String, String>("HostName",h.toString());
         startEncTime = new Tuple<String, Double>("StartEncounterTime",time);
    
    }
    
    public String getHostName() {
        return hostName.getValue();
    }
    
    public double getStartEncTime() {
        return startEncTime.getValue();
    }

    public void setContactDuration(DTNHost h, DTNHost neighbour, Double endContactTime) {
        contactDuration = new Tuple<String, Double>("ContactDuration of "+h.toString()+ "with"+ neighbour.toString() +"is:",endContactTime-getStartEncTime());
    }
    
    public double getContactDuration() {
        return contactDuration.getValue();
    }
    
}
