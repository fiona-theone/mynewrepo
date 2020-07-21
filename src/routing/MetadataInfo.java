package routing;

import java.util.*;

import core.Connection;
import core.DTNHost;

public class MetadataInfo
{
    private String dev_id;
    public ContactMD contact;
    private StorageMD storage;
    private List<Metadata> metadata_instances;
    
    public MetadataInfo(DTNHost h) {
        this.dev_id = h.toString();
        this.contact = new ContactMD(h);
        this.storage = new StorageMD(h);
        this.metadata_instances = new ArrayList<Metadata>();
        this.register(this.contact);
        this.register(this.storage);
    }
    
    public void register(Metadata md) {
        this.metadata_instances.add(md);
    }
    
    public void connUp(DTNHost h, double time) {
        for (Metadata m: this.metadata_instances) {
                m.connUp(h, time);
        }
    }

    public void connDown(DTNHost thisHost, DTNHost otherHost, double time) {
        for (Metadata m: this.metadata_instances) {
            m.connDown(thisHost, otherHost, time);
        }
    }
    
    public String get_id() {
        return this.dev_id;
    }
}
