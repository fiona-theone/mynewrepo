package routing;

import core.DTNHost;

public class BufferOccupancy
{
    private String name;
    private double value;

    public BufferOccupancy(DTNHost h) {
        update(h);
    }

    public String getName()
    {
        return name;
    }

    public void setName()
    {
        this.name = "BufferOccupancy";
    }
    
    public void update(DTNHost h) {
        setValue(h.getBufferOccupancy());
    }

    public double getValue()
    {
        return value;
    }

    public void setValue(double value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return "BufferOccupancy [name=" + name + ", value=" + value + "]";
    }
    
    
}
