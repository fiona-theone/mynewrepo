package routing;

import core.DTNHost;
import util.Tuple;

public class ContactMetrics
{ 
    private DTNHost host;
    private String hostName;
    private Double contactDuration;
    private Double startEncTime;
    
    
    public ContactMetrics(DTNHost h, Double time)
    {
         super();
        // TODO Auto-generated constructor stub
         setHost(h);
         setHostName(h.toString());
         setStartEncTime(time);  
         setContactDuration(time);
    }
    

    public void setHost(DTNHost host)
    {
        this.host = host;
    }

    public void setHostName(String hostName)
    {
        this.hostName = hostName;
    }

    public void setContactDuration(Double endContactTime)
    {
        this.contactDuration = endContactTime-getStartEncTime();
    }

    public void setStartEncTime(Double startEncTime)
    {
        this.startEncTime = startEncTime;
    }
    


    public DTNHost getHost()
    {
        return host;
    }



    public String getHostName()
    {
        return hostName;
    }



    public Double getContactDuration()
    {
        return contactDuration;
    }



    public Double getStartEncTime()
    {
        return startEncTime;
    }

    
}
