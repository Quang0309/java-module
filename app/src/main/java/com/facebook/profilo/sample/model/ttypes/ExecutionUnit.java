package com.facebook.profilo.sample.model.ttypes;

import java.util.ArrayList;

public class ExecutionUnit {
    private String id;
    private ArrayList blocks;
    private Properties properties;

    public ExecutionUnit() {
    }

    public ExecutionUnit(String id, ArrayList blocks, Properties properties) {
        this.id = id;
        this.blocks = blocks;
        this.properties = properties;
    }
}
