package routing.util;

import core.Connection;
import core.DTNHost;

public class ContactMD extends Metadata {
	double last_conn_start;

	public ContactMD(DTNHost h) {
		super(h);
		last_conn_start = 0.0;
	}

	@Override
	public void connUp(Connection con, double time) {
		last_conn_start = time;
		
	}

	@Override
	public void connDown(Connection con, double time) {
		// TODO Auto-generated method stub
		
	}
	
	public double get_last_start() {
		return this.last_conn_start;
	}
	

}
