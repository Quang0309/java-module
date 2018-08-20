package com.facebook.profilo.sample.model.ttypes;

import com.facebook.profilo.sample.model.Properties;

import java.util.ArrayList;

public class ExecutionUnit {
    protected String id;
    protected ArrayList<String> blocks;
    protected com.facebook.profilo.sample.model.Properties properties;

    public ExecutionUnit() {
    }

    public ExecutionUnit(String id, ArrayList<String> blocks, Properties properties) {
        this.id = id;
        this.blocks = blocks;
        this.properties = properties;
    }
}
