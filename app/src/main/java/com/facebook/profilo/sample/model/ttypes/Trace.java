package com.facebook.profilo.sample.model.ttypes;


import java.util.ArrayList;
import java.util.HashMap;

public class Trace {
    private String id;
    private HashMap executionUnits;
    private HashMap blocks;
    private HashMap points;
    private int version;
    private ArrayList edges;
    private Properties properties;

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