package routing;

import core.DTNHost;
import core.Connection;


public abstract class Metadata
{
  private String dev_id;
  
  public Metadata(DTNHost h) {
      this.dev_id = h.toString();
  }
  
  public abstract void connUp(DTNHost h, double time);
  
  public abstract void connDown(DTNHost h, double time);
}
