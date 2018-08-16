package com.facebook.profilo.sample.model.ttypes;

import java.util.ArrayList;

public class Block {
    protected String id;
    protected String begin;
    protected String end;
    protected ArrayList<String> otherPoints;
    private Properties properties;

    public Block() {
    }

    public Block(String id, String begin, String end, ArrayList<String> otherPoints, Properties properties) {
        this.id = id;
        this.begin = begin;
        this.end = end;
        this.otherPoints = otherPoints;
        this.properties = properties;
    }
}
