package routing;

import core.DTNHost;

public class ContactDuration
{
    private String name;
    private double value;

    public ContactDuration(DTNHost thisHost, DTNHost otherHost, double startTime, double endTime) {
        setName(thisHost, otherHost);
        update(otherHost, startTime, endTime);
    }

    public String getName()
    {
        return name;
    }

    public void setName(DTNHost thisHost, DTNHost otherHost)
    {
        this.name = "Contact Duration between" + thisHost.toString() + " and " + otherHost.toString() + " is :";
    }
    
    public void update(DTNHost otherHost, double startTime, double endTime) {
        double contactDuration;
        //contactDuration = time- getLastEncounterTimeFor(otherHost);
        contactDuration = endTime - startTime;
        setValue(contactDuration);
    }
    
    public void setValue(double value)
    {
        this.value = value;
    }

    public double getValue()
    {
        return value;
    }

    

  
    
    
    
    
}
