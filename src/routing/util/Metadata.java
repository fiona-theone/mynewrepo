package routing.util;

import java.util.ArrayList;
import java.util.List;

import core.Connection;
import core.DTNHost;

public abstract class Metadata {
	private int dev_id;

	public Metadata(DTNHost h) {
		this.dev_id = h.getAddress();
	}
	
	public abstract void connUp(Connection con, double time);
	public abstract void connDown(Connection con, double time);
}
