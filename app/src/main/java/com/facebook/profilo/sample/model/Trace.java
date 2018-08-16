package com.facebook.profilo.sample.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Trace extends com.facebook.profilo.sample.model.ttypes.Trace {
    private String id;
    private int begin;
    private int end;
    public Trace(int begin,int end,String id)
    {
        super(id,new HashMap(),new HashMap(),new HashMap(),0,new ArrayList(),new Properties());
        this.id = id!=null ? id : GenID.new_id();
        this.begin = begin;
        this.end = end;
    }
    public ExecutionUnit add_unit()
    {

    }
}

