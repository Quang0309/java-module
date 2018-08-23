package com.facebook.profilo.sample.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Trace extends com.facebook.profilo.sample.model.ttypes.Trace {
    private String id;
    private long begin;
    private long end;
    public Trace(long begin,long end,String id)
    {
        super(id,new HashMap<String, ExecutionUnit>(),new HashMap<String, Block>(),new HashMap<String, Point>(),0,new ArrayList<Edge>(),new Properties(null,null,null,null,null));
        this.id = id!=null ? id : GenID.new_id();
        this.begin = begin;
        this.end = end;
    }
    public ExecutionUnit add_unit()
    {
        ExecutionUnit unit = new ExecutionUnit(this);
        executionUnits.put(unit.getID(),unit);
        return unit;
    }
    public Edge add_edge(Point source, Point target)
    {
        Edge edge = new Edge(this,source.getID(),target.getID());
        this.edges.add(edge);
        return edge;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getBegin() {
        return begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }
    public Properties getProperties()
    {
        return this.properties;
    }
}

