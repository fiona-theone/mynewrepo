package routing;

import java.io.Serializable;

public class ContactHistory implements Serializable
{
    
    private double averageContactDuration;
    private double lastEncTime;
    private int noOfSocialContacts;
    private double lastUpdatedContactFrequnecy;
    
    public ContactHistory(double averageContactDuration, double lastEncTime, int noOfSocialContacts)
    {
        super();
        this.averageContactDuration = averageContactDuration;
        this.lastEncTime = lastEncTime;
        this.noOfSocialContacts = noOfSocialContacts;
        //this.lastUpdatedContactFrequnecy = lastUpdatedContactFrequnecy;
    }

    public double getAverageContactDuration()
    {
        return averageContactDuration;
    }

    public void setAverageContactDuration(double averageContactDuration)
    {
        this.averageContactDuration = averageContactDuration;
    }

    public double getLastEncTime()
    {
        return lastEncTime;
    }

    public void setLastEncTime(double lastEncTime)
    {
        this.lastEncTime = lastEncTime;
    }

    public int getNoOfSocialContacts()
    {
        return noOfSocialContacts;
    }

    public void setNoOfSocialContacts(int noOfSocialContacts)
    {
        this.noOfSocialContacts = noOfSocialContacts;
    }
    
    

    public double getLastUpdatedContactFrequnecy()
    {
        return lastUpdatedContactFrequnecy;
    }

    public void setLastUpdatedContactFrequnecy(double lastUpdatedContactFrequnecy)
    {
        this.lastUpdatedContactFrequnecy = lastUpdatedContactFrequnecy;
    }

    @Override
    public String toString()
    {
        return "ContactHistory [averageContactDuration=" + averageContactDuration + ", lastEncTime=" + lastEncTime + ", noOfSocialContacts=" + noOfSocialContacts + "]";
    }

    

    
    
    

}
