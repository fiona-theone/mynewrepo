package routing;

import core.DTNHost;

import java.util.List;
import java.util.Map;

import core.Connection;


public abstract class Metadata
{
  private String dev_id;
  
  public Metadata(DTNHost h) {
      this.dev_id = h.toString();
  }
  
  public abstract void connUp(Connection con, DTNHost thisHost, double time);
  
  public abstract void connDown(Connection con, DTNHost thisHost, double time);
}
