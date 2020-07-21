package routing;

import core.Connection;
import core.DTNHost;
import core.Settings;
import core.SimClock;

public class Router10 extends ActiveRouter
{

    public Router10(ActiveRouter r)
    {
        super(r);
        // TODO Auto-generated constructor stub
    }

    public Router10(Settings s)
    {
        super(s);
        // TODO Auto-generated constructor stub
    }
    
    
    
    @Override
    public Router10 replicate() {
        Router10 r = new Router10(this);
        return r;
    }
    
    @Override
    public void changedConnection(Connection con) {
        DTNHost otherHost = con.getOtherNode(getHost());
        MetadataInfo metadataInfo = new MetadataInfo(otherHost);
        if (con.isUp()) {
            metadataInfo.connUp(otherHost, SimClock.getTime());   
        }else {
            metadataInfo.connUp(otherHost, SimClock.getTime()); 
        }
    }

}
