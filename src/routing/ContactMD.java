package routing;


import java.util.List;
import java.util.Map;


import core.Connection;
import core.DTNHost;


public class ContactMD extends Metadata
{
    /* public static List<ContactMetrics> cm;
    static {
        cm = new ArrayList<ContactMetrics>(); 
        }
    public static Map<String, ContactHistory> contactMetadata;
    static {
        contactMetadata = new HashMap<String, ContactHistory>(); 
        }*/

    public ContactMetrics contactMetrics;
    private double last_conn_start;

    public ContactMD(DTNHost h)
    {
        super(h);
        last_conn_start = 0.0;
    }

    @Override
    public void connUp(Connection con, DTNHost thisHost, double time)
    {
        last_conn_start = time;
        DTNHost otherHost = con.getOtherNode(thisHost);
        updateContactMetricsFor(otherHost, thisHost, time);
        //updateTransitiveContactMetrics(otherHost, thisHost, time);  

    }

    public void updateContactMetricsFor(DTNHost otherHost, DTNHost thisHost, double time)
    {
        MessageRouter thisRouter = thisHost.getRouter();
        List<ContactMetrics> myContacts = ((Router10)thisRouter).getContactMetrics();
        contactMetrics = new ContactMetrics(otherHost, time);
        myContacts.add(contactMetrics);
    }

    @Override
    public void connDown(Connection con, DTNHost thisHost, double time)
    {
        DTNHost otherHost = con.getOtherNode(thisHost);
        MessageRouter thisRouter = thisHost.getRouter();
        List<ContactMetrics> myContacts = ((Router10)thisRouter).getContactMetrics();
        ContactMetrics lastContactedHost = myContacts.get(myContacts.size() - 1);
        lastContactedHost.setContactDuration(thisHost, otherHost, time);
        updateAverageContactDuration(thisHost, otherHost, lastContactedHost.getStartEncTime());
        updateTransitiveContactMetrics(otherHost, thisHost, time);

    }

    public void updateAverageContactDuration(DTNHost thisHost, DTNHost otherHost, double lastEncTime)
    {
        MessageRouter thisRouter = thisHost.getRouter();
        List<ContactMetrics> myContacts = ((Router10)thisRouter).getContactMetrics();
        Map<String, ContactHistory> myContactMetadata = ((Router10)thisRouter).getContactMetadata();
        int totalNoOfEncounters = myContacts.size();
        int counter = 0;
        double sum = 0.0;
        double averageContactDuration = 0.0;
        for(ContactMetrics contactMetrics : myContacts)
        {
            if(contactMetrics.getHostName() == otherHost.toString())
            {
                counter++;
                sum += contactMetrics.getContactDuration();
            }
        }
            if(counter != 0)
            {
                averageContactDuration = sum / counter;
            }
            
            ContactHistory contactHistory = new ContactHistory(averageContactDuration, lastEncTime, counter);
            myContactMetadata.put(thisHost.toString() + " had contact with" + otherHost.toString() + ". ", contactHistory);
 
    }

    public void updateTransitiveContactMetrics(DTNHost otherHost, DTNHost thisHost, double time) {
        MessageRouter thisRouter = thisHost.getRouter();
        Map<String, ContactHistory> myContactMetadata =
                ((Router10)thisRouter).getContactMetadata();
        MessageRouter otherRouter = otherHost.getRouter();
        Map<String, ContactHistory> otherHostContactMetadata =
                ((Router10)otherRouter).getContactMetadata();
        
        for (Map.Entry<String, ContactHistory> e : otherHostContactMetadata.entrySet()) {
            String contactDescription = e.getKey();
            ContactHistory contactDetails1 = e.getValue();
            if (contactDescription.contains(thisHost.toString())) {
                continue; // don't add yourself
            }
//            ContactHistory contactDetails2 = myContactMetadata.get(contactDescription); 
//            if(contactDetails2 != null) {
//                if(contactDetails1.getLastEncTime()>contactDetails2.getLastEncTime()) {
//                    myContactMetadata.put(contactDescription, contactDetails1);
//                }
//            }else {
//                myContactMetadata.put(contactDescription, contactDetails1); // there is no most recent info here, you want all the contact history apart yourself
//            }
            myContactMetadata.put(contactDescription, contactDetails1); 
        }   
        
    }
    
    public double get_last_start() {
        return this.last_conn_start;
    }
}
