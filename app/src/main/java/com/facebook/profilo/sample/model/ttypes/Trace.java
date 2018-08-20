package com.facebook.profilo.sample.model.ttypes;


import com.facebook.profilo.sample.model.Block;
import com.facebook.profilo.sample.model.Edge;
import com.facebook.profilo.sample.model.Properties;

import java.util.ArrayList;
import java.util.HashMap;

public class Trace {
    private String id;
    public HashMap<String,ExecutionUnit> executionUnits;
    public HashMap<String, Block> blocks;
    public HashMap<String,Point> points;
    private int version;
    public ArrayList<Edge> edges;
    protected com.facebook.profilo.sample.model.Properties properties;

    public Trace() {
    }

    public Trace(String id, HashMap executionUnits, HashMap blocks, HashMap points, int version, ArrayList edges, Properties properties) {
        this.id = id;
        this.executionUnits = executionUnits;
        this.blocks = blocks;
        this.points = points;
        this.version = version;
        this.edges = edges;
        this.properties = properties;
    }
}
