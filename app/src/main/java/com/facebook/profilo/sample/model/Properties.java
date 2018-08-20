package com.facebook.profilo.sample.model;

import com.facebook.profilo.sample.model.ttypes.CounterUnit;

import java.util.ArrayList;
import java.util.HashMap;

public class Properties extends com.facebook.profilo.sample.model.ttypes.Properties {
    public HashMap<String,String> coreProps;
    public HashMap<String,String> customProps;
    public HashMap<Short,HashMap<String,Integer>> counterProps;
    private ArrayList errors;
    private HashMap sets;
    public Properties()
    {
        super();
    }
    public Properties(HashMap<String,String> coreProps, HashMap<String,String> customProps, HashMap<Short,HashMap<String,Integer>> counterProps, ArrayList errors, HashMap sets) {
        super(coreProps, customProps, counterProps, errors, sets);
        this.coreProps = coreProps != null ? coreProps : new HashMap<String, String>();
        this.customProps = customProps != null ? customProps : new HashMap<String, String>();
        this.counterProps = counterProps != null ? counterProps : new HashMap<Short, HashMap<String, Integer>>();
        this.errors = errors != null ? errors : new ArrayList<>();
        this.sets = sets != null ? sets : new HashMap<>();
    }
    public void add_counter(String name,int value,Short counter_unit)
    {
        if(counter_unit == null)
            counter_unit = CounterUnit.ITEMS;
        if(counterProps.containsKey(counter_unit))
            counterProps.get(counter_unit).put(name,value);
        else {
            HashMap<String,Integer> temp = new HashMap<>();
            temp.put(name,value);
            counterProps.put(counter_unit, temp);
        }

    }
}
