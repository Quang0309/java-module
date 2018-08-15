package com.facebook.profilo.sample.model.ttypes;

import java.util.ArrayList;

public class Block {
    private String id;
    private int begin;
    private int end;
    private ArrayList otherPoints;
    private Properties properties;

    public Block() {
    }

    public Block(String id, int begin, int end, ArrayList otherPoints, Properties properties) {
        this.id = id;
        this.begin = begin;
        this.end = end;
        this.otherPoints = otherPoints;
        this.properties = properties;
    }
}
