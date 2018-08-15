package com.facebook.profilo.sample.model;

import com.facebook.profilo.sample.model.ttypes.CounterUnit;

import java.util.ArrayList;
import java.util.HashMap;

public class Properties extends com.facebook.profilo.sample.model.ttypes.Properties {
    private HashMap coreProps;
    private HashMap customProps;
    private HashMap counterProps;
    private ArrayList errors;
    private HashMap sets;

    public Properties(HashMap coreProps, HashMap customProps, HashMap counterProps, ArrayList errors, HashMap sets) {
        super(coreProps, customProps, counterProps, errors, sets);
        this.coreProps = coreProps != null ? coreProps : new HashMap();
        this.customProps = customProps != null ? customProps : new HashMap();
        this.counterProps = counterProps != null ? customProps : new HashMap();
        this.errors = errors != null ? errors : new ArrayList();
        this.sets = sets != null ? sets : new HashMap();
    }
    public void add_counter(String name,int value,Short counter_unit)
    {
        if(counter_unit == null)
            counter_unit = CounterUnit.ITEMS;
        if(counterProps.containsKey(counter_unit))
            ((HashMap<String,Integer>)(counterProps.get(counter_unit))).put(name,value);
        else
            counterProps.put(counter_unit,new HashMap<String,Integer>().put(name,value));
    }
}
