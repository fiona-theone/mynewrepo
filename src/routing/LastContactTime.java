package routing;

import core.DTNHost;

public class LastContactTime
{
    private String name;
    private double value;

    public LastContactTime(double time) {
        update(time);
    }

    public String getName()
    {
        return name;
    }

    public void setName()
    {
        this.name = "LastContactTime";
    }
    
    public void update(double time) {
        setValue(time);
    }

    public double getValue()
    {
        return value;
    }

    public void setValue(double value)
    {
        this.value = value;
    }
    
    
}
