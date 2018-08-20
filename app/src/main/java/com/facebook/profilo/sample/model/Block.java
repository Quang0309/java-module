package com.facebook.profilo.sample.model;

import java.util.ArrayList;

public class Block extends com.facebook.profilo.sample.model.ttypes.Block {
    private Trace trace;
    private ExecutionUnit unit;
    private Point begin_point;
    private Point end_point;
    private Block parent;
    public Block(Trace trace,ExecutionUnit unit,String begin, String end)
    {
        super(GenID.new_id(),begin,end,new ArrayList<String>(),new Properties());
        this.trace = trace;
        this.unit = unit;

    }
    public Point create_begin_point(long timestamp)
    {
        if(this.begin != null)
            System.exit(1);
        Point point = new Point(this.trace,this.unit,this,timestamp,0);

        trace.points.put(point.getID(),point);
        this.begin = point.getID();
        this.begin_point = point;
        return point;
    }
    public Point create_end_point(long timestamp) {
        if(this.end != null)
            System.exit(1);
        Point point = new Point(this.trace,this.unit,this,timestamp,0);

        trace.points.put(point.getID(),point);
        this.end = point.getID();
        this.end_point = point;
        return point;
    }
    public Point add_point(long timestamp)
    {
        Point point = new Point(this.trace,this.unit,this,timestamp,0);
        trace.points.put(point.getID(),point);
        this.otherPoints.add(point.getID());
        return point;
    }
    public void add_child_block(Block child)
    {
        if(child.parent!=null)
            System.exit(1);
        long call_time = child.begin_point.getTimestamp();
        long return_time = child.end_point.getTimestamp();
        if(!(this.begin_point.getTimestamp()<=call_time && call_time<=return_time
                 && return_time<this.end_point.getTimestamp()))
            System.exit(1);
        Point call_from = this.add_point(call_time);
        Point call_to = child.add_point(call_time);
        Point return_from = child.add_point(return_time);
        Point return_to = this.add_point(return_time);

        Edge call_edge = trace.add_edge(call_from,call_to);
        call_edge.getProperties().coreProps.put("edge_event_source","call_to_block");
        call_edge.getProperties().coreProps.put("type","nested_call");

        Edge return_edge = trace.add_edge(return_from,return_to);
        return_edge.getProperties().coreProps.put("edge_event_source", "wait_for_block");
        return_edge.getProperties().coreProps.put("type", "nested_return");

        child.parent = this;
    }
    public String getID()
    {
        return this.id;
    }
    public String getEnd()
    {
        return this.end;
    }
    public String getBegin()
    {
        return this.begin;
    }
    public Point getBeginPoint()
    {
        return this.begin_point;
    }
    public Point getEndPoint()
    {
        return this.end_point;
    }
    public Properties getProperties()
    {
        return this.properties;
    }
}
