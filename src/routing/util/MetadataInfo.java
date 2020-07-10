package routing.util;

import java.util.ArrayList;
import java.util.List;

import core.Connection;
import core.DTNHost;

public class MetadataInfo {
	private int dev_id;
	public ContactMD contact;
	private StorageMD storage;
	private List<Metadata> metadata_instances;

	public MetadataInfo(DTNHost h) {
		this.dev_id = h.getAddress();
		this.contact = new ContactMD(h);
		this.storage = new StorageMD(h);
		this.metadata_instances = new ArrayList<Metadata>();
		this.register(this.contact);
		this.register(this.storage);
	}
	
	public void register(Metadata md) {
		this.metadata_instances.add(md);
	}
	
	public void connUp(Connection con, double time) {
		for (Metadata m: this.metadata_instances) {
			m.connUp(con, time);
		}
	}

	public void connDown(Connection con, double time) {
		for (Metadata m: this.metadata_instances) {
			m.connDown(con, time);
		}
	}
	
	public int get_id() {
		return this.dev_id;
	}

}
