package routing;

public class ContactHistory
{
    
    private double averageContactDuration;
    private double lastEncTime;
    private int noOfSocialContacts;
    
    public ContactHistory(double averageContactDuration, double lastEncTime, int noOfSocialContacts)
    {
        super();
        this.averageContactDuration = averageContactDuration;
        this.lastEncTime = lastEncTime;
        this.noOfSocialContacts = noOfSocialContacts;
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

    @Override
    public String toString()
    {
        return "ContactHistory [averageContactDuration=" + averageContactDuration + ", lastEncTime=" + lastEncTime + ", noOfSocialContacts=" + noOfSocialContacts + "]";
    }
    
    

}
