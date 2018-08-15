package com.facebook.profilo.sample.model.ttypes;

import java.util.ArrayList;
import java.util.HashMap;

public class Properties {
    private HashMap coreProps;
    private HashMap customProps;
    private HashMap counterProps;
    private ArrayList errors;
    private HashMap sets;

    public Properties() {
    }

    public Properties(HashMap coreProps, HashMap customProps, HashMap counterProps, ArrayList errors, HashMap sets) {
        this.coreProps = coreProps;
        this.customProps = customProps;
        this.counterProps = counterProps;
        this.errors = errors;
        this.sets = sets;
    }
}
