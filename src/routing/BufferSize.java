package routing;

import core.DTNHost;

public class BufferSize
{
    private String name;
    private double value;

    public BufferSize(DTNHost h) {
        update(h);
    }

    public String getName()
    {
        return name;
    }

    public void setName()
    {
        this.name = "BufferSize";
    }
    
    public void update(DTNHost h) {
        setValue(h.getRouter().getBufferSize());
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
        return "BufferSize [name=" + name + ", value=" + value + "]";
    }
    
    
    
    
}
