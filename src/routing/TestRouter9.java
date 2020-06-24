package routing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.Connection;
import core.DTNHost;
import core.Message;
import core.Settings;
import core.SimClock;
import util.Tuple;
import routing.util.RoutingInfo;

public class TestRouter9 extends ActiveRouter
{
    List<ContactMetadata> contactMetrics;
    private Map<DTNHost, StorageMetadata> storageMetrics;
    private Map<String, UniqueContactMetadata> contactMetadata;
    
   
    public TestRouter9(Settings s) {
        super(s);
    }
   
    protected TestRouter9(TestRouter9 r) {
        super(r);
        initBufferSizes();
    }
   
    private void initBufferSizes() {
        this.contactMetrics = new ArrayList<ContactMetadata>();
        this.storageMetrics = new HashMap<DTNHost, StorageMetadata>();
        this.contactMetadata = new HashMap<String, UniqueContactMetadata>();
    }
   
    @Override
    public void changedConnection(Connection con) {
        DTNHost otherHost = con.getOtherNode(getHost());;
        if (con.isUp()) {
       
            updateMetricsFor(otherHost);
            updateTransitiveMetrics((TestRouter9)otherHost.getRouter());
        }else {
          //System.out.println(SimClock.getTime());
            updateContactDurationFor(otherHost);
            updateUniqueContactMetricsFor(otherHost);    
        }
        
    }
   
    private void updateMetricsFor(DTNHost host) {

        StorageMetadata sm = new StorageMetadata();
        ContactMetadata cm = new ContactMetadata();
        sm.update(host);
        cm.update(host);
        contactMetrics.add(cm);
        storageMetrics.put(host, sm);        
        
    }
    
      private void updateContactDurationFor(DTNHost host) {
          for(ContactMetadata contact : contactMetrics) {
              if(contact.getHostName()==host.toString()) {
                 double duration =  SimClock.getTime() - contact.getStartEncounterTime();
                 contact.setContactDuration(duration);
              }
          }
        
    }
    
      private void updateUniqueContactMetricsFor(DTNHost host) {
          int counter = 0;
          double sum = 0.0;
          double averageContactDuration = 0.0;
          for(ContactMetadata contact : contactMetrics) {
              if(contact.getHostName()==host.toString()) {
                  counter++;
                  sum+= contact.getContactDuration();
              }
              if(counter!=0) {
                  averageContactDuration= sum/counter;
              }
              UniqueContactMetadata uniqueContact = new UniqueContactMetadata(averageContactDuration, counter);
              contactMetadata.put(getHost().toString() + " " + "meets" + " " + host.toString(), uniqueContact);  
          }
        
    }
    
    private void updateTransitiveMetrics(TestRouter9 router) {
        Map<DTNHost, StorageMetadata> othersStorageMetadata =
                router.getStorageMetrics();
        Map<String, UniqueContactMetadata> othersContactMetadata =
                router.getContactMetrics();

        for (Map.Entry<DTNHost, StorageMetadata> e : othersStorageMetadata.entrySet()) {
            DTNHost h = e.getKey();
            StorageMetadata sm = e.getValue();
            if (h == getHost()) {
                continue; // don't add yourself
            }
            storageMetrics.put(h,sm);

        }
        
        for (Map.Entry<String, UniqueContactMetadata> e : othersContactMetadata.entrySet()) {
            String s = e.getKey();
            UniqueContactMetadata cm = e.getValue();
            if (s.contains(getHost().toString())) {
                System.out.println("fionaaaa"); //check later
                continue; // don't add yourself
            }
                contactMetadata.put(s,cm);
            
           

        }
    }
    
   
    private Map<String, UniqueContactMetadata> getContactMetrics() {
        return this.contactMetadata;
    }
    
    private Map<DTNHost,StorageMetadata> getStorageMetrics() {
        return this.storageMetrics;
    }
    
    @Override
    public void update() {
        super.update();
        if (!canStartTransfer() ||isTransferring()) {
            return; // nothing to transfer or is currently transferring
        }
        
        tryMetadataMessageExchange();

        // try messages that could be delivered to final recipient
        if (exchangeDeliverableMessages() != null) {
            return;
        }

        tryOtherMessages();
    }
     
    private void tryMetadataMessageExchange() {
        for (Connection con : getConnections()) {
            DTNHost other = con.getOtherNode(getHost());
            TestRouter9 othRouter = (TestRouter9)other.getRouter();

            if (othRouter.isTransferring()) {
                continue; // skip hosts that are transferring
            }
            if (othRouter.hasMessage("metadata")) {
                continue; // do not send metadata message again
            }
        Message metadata = new Message(getHost(),other,"metadata", this.getContactMetrics().size() + this.getStorageMetrics().size());
        //System.out.println(this.getContactMetrics().size() + this.getStorageMetrics().size());
        getHost().createNewMessage(metadata);
        startTransfer(metadata,con);
        }

    }
    
    
    private Tuple<Message, Connection> tryOtherMessages() {
        List<Tuple<Message, Connection>> messages =
            new ArrayList<Tuple<Message, Connection>>();

        Collection<Message> msgCollection = getMessageCollection();

        for (Connection con : getConnections()) {
            DTNHost other = con.getOtherNode(getHost());
            TestRouter9 othRouter = (TestRouter9)other.getRouter();

            if (othRouter.isTransferring()) {
                continue; // skip hosts that are transferring
            }
            
            for (Message m : msgCollection) {
                if (othRouter.hasMessage(m.getId())) {
                    continue; // skip messages that the other one has
                }
                
                    messages.add(new Tuple<Message, Connection>(m,con));
                
            }
        }

        if (messages.size() == 0) {
            return null;
        }
        //System.out.println(messages.size());
        return tryMessagesForConnected(messages);   // try to send messages
    }
    

    private String showStorageInfoHost(DTNHost h) {
String info = h.toString() + ": " + this.storageMetrics.get(h) + " " ;
return info;
    }
    
    private String showContactInfo(String s) {
        String info = s + ": " + this.contactMetadata.get(s);
        return info;
            }

    @Override
    public TestRouter9 replicate() {
        TestRouter9 r = new TestRouter9(this);
        return r;
    }

    @Override
    public RoutingInfo getRoutingInfo() {
    RoutingInfo top = super.getRoutingInfo();
RoutingInfo ri = new RoutingInfo("Metadata Metrics");
System.out.println();
for (DTNHost key: getStorageMetrics().keySet()){
ri.addMoreInfo(new RoutingInfo(String.format("%s ", showStorageInfoHost(key))));
}
System.out.println();
System.out.println();
for (String key: getContactMetrics().keySet()){
ri.addMoreInfo(new RoutingInfo(String.format("%s ", showContactInfo(key))));
}
top.addMoreInfo(ri);

return top;
    }
}