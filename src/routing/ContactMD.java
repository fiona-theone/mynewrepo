package routing;

import java.util.Map;

import core.Connection;
import core.DTNHost;
import util.Tuple;

public class ContactMD extends Metadata
{
    public static Map<String, Tuple<?,?>> contactMetadata;
    private double last_conn_start;
    private Map<String, Tuple<Double,Double>> contactHistory;

    public ContactMD(DTNHost h) {
        super(h);
        last_conn_start = 0.0;
    }

    @Override
    public void connUp(DTNHost h, double time) {
        last_conn_start = time;
        
    }

    @Override
    public void connDown(DTNHost h, double time) {
        
        
    }
    
    public double get_last_start() {
        return this.last_conn_start;
    }
}
