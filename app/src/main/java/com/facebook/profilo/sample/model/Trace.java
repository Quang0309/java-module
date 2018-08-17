package com.facebook.profilo.sample.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Trace extends com.facebook.profilo.sample.model.ttypes.Trace {
    private String id;
    private int begin;
    private int end;
    public Trace(int begin,int end,String id)
    {
        super(id,new HashMap<>(),new HashMap<>(),new HashMap<>(),0,new ArrayList<>(),new Properties());
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

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}

