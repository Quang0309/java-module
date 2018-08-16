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
    public Point create_begin_point(int timestamp)
    {
        if(this.begin != null)
            System.exit(1);
        Point point = new Point(this.trace,this.unit,this,timestamp,0);
        //miss 1 line
        this.begin = point.getID();
        this.begin_point = point;
        return point;
    }
    public Point create_end_point(int timestamp) {
        if(this.end != null)
            System.exit(1);
        Point point = new Point(this.trace,this.unit,this,timestamp,0);
        //miss 1 line
        this.end = point.getID();
        this.end_point = point;
        return point;
    }
    public Point add_point(int timestamp)
    {
        Point point = new Point(this.trace,this.unit,this,timestamp,0);
        //miss 1 line
        this.otherPoints.add(point.getID());
        return point;
    }
    public void add_child_block(Block child)
    {
        if(child.parent!=null)
            System.exit(1);
        int call_time = child.begin_point.getTimestamp();
        int return_time = child.end_point.getTimestamp();
        if(!(this.begin_point.getTimestamp()<=call_time && call_time<=return_time
                 && return_time<this.end_point.getTimestamp()))
            System.exit(1);
        Point call_from = this.add_point(call_time);
        Point call_to = child.add_point(call_time);
        Point return_from = child.add_point(return_time);
        Point return_to = this.add_point(return_time);
    }
    public String getID()
    {
        return this.id;
    }
}
