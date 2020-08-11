package routing;


import java.util.List;
import java.util.Map;

import com.sun.xml.internal.ws.util.StringUtils;

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

    /* Update contact metadata for host we just met.
       Add all hosts I meet in my list first
    */
    
    public void updateContactMetricsFor(DTNHost otherHost, DTNHost thisHost, double time)
    {
        MessageRouter thisRouter = thisHost.getRouter();
        List<ContactMetrics> myContacts = ((Router10)thisRouter).getContactMetrics();
        contactMetrics = new ContactMetrics(otherHost, time);
        myContacts.add(contactMetrics);
    }

    /* When connection is dawn 2 things should happen
     * -set contact duration for the last element I added in the list
     * -I update average contact duration with host I met
     * Update transitive contact metadata
      
    */ 
    
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
    
    /* 
      Iterate through my list and update average contact duration of host I just met
      Put result in map
    */ 

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
    
    /* 
       Update contact metadata transitively
       If I already have this info in my map check the last encounter time of 
       info I have in my map and compare 
       Check also if I have same contact description but with different order
       of hosts. For example: if in my map I have s1 <-> s2, I want to get
       from other host last updated contact of s1 <-> s2 OR s2 <-> s1
       
    */ 

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
            String firstHost = contactDescription.substring(0, contactDescription.indexOf(" had contact with"));
            String secondHost = contactDescription.substring(contactDescription.indexOf(" had contact with")+ " had contact with".length(), contactDescription.indexOf(". "));
            String reversedContactDescription = firstHost + " had contact with" + secondHost + ". ";
            System.out.println(reversedContactDescription);
            if (contactDescription.contains(thisHost.toString())) {
                continue; // don't add yourself
            }
            ContactHistory contactDetails2 = myContactMetadata.get(contactDescription); 
            ContactHistory contactDetails3 = myContactMetadata.get(reversedContactDescription);
            if(contactDetails2 != null && contactDetails3== null) {
                if(contactDetails1.getLastEncTime()>contactDetails2.getLastEncTime()) {
                    myContactMetadata.put(contactDescription, contactDetails1);
                }
            }else if(contactDetails2 == null && contactDetails3!= null) {
                if(contactDetails1.getLastEncTime()>contactDetails3.getLastEncTime()) {
                    myContactMetadata.put(contactDescription, contactDetails1);
                }
            }else if(contactDetails2 != null && contactDetails3!= null) {
               double lastEncTime1 = contactDetails1.getLastEncTime();
               double lastEncTime2 = contactDetails2.getLastEncTime();
               double lastEncTime3 = contactDetails3.getLastEncTime();
               double max = Math.max(Math.max(lastEncTime1,lastEncTime2),lastEncTime3);
               if (max == lastEncTime1) {
                   myContactMetadata.put(contactDescription, contactDetails1);
                   myContactMetadata.remove(reversedContactDescription);
               }else if(max == lastEncTime2) {
                   myContactMetadata.put(contactDescription, contactDetails2);
                   myContactMetadata.remove(reversedContactDescription);
               }else {
                   myContactMetadata.put(reversedContactDescription, contactDetails3);
                   myContactMetadata.remove(contactDescription);
               }
            }
            else {
                myContactMetadata.put(contactDescription, contactDetails1);
            }
         
        }   
        
    }
    
    public double get_last_start() {
        return this.last_conn_start;
    }
}
