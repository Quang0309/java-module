package com.facebook.profilo.sample.model;

public class Point extends com.facebook.profilo.sample.model.ttypes.Point {
    private Trace trace;
    private ExecutionUnit unit;
    private Block block;
    public Point(Trace trace, ExecutionUnit unit, Block block, int timestamp, int sequenceNumber)
    {
        super(GenID.new_id(),timestamp,timestamp,new Properties(),sequenceNumber);
        this.trace = trace;
        this.unit = unit;
        this.block = block;
    }
    public String getID()
    {
        return this.id;
    }
    public int getTimestamp()
    {
        return this.timestamp;
    }
}
