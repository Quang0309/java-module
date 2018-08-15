package com.facebook.profilo.sample.model.ttypes;

import java.util.HashMap;

public class CounterUnit {
    public final static short BYTES = 0;
    public final static short SECONDS = 1;
    public final static short ITEMS = 2;
    public final static short RATIO = 3;
    public final static HashMap<Integer,String> _VALUES_TO_NAMES;
    static {
        _VALUES_TO_NAMES = new HashMap<>();
        _VALUES_TO_NAMES.put(0,"BYTES");
        _VALUES_TO_NAMES.put(1,"SECONDS");
        _VALUES_TO_NAMES.put(2,"ITEMS");
        _VALUES_TO_NAMES.put(3,"RATIO");
    }
    public static final HashMap<String,Integer> _NAMES_TO_VALUES;
    static {
        _NAMES_TO_VALUES = new HashMap<>();
        _NAMES_TO_VALUES.put("BYTES",0);
        _NAMES_TO_VALUES.put("SECONDS",1);
        _NAMES_TO_VALUES.put("ITEMS",2);
        _NAMES_TO_VALUES.put("RATIO",3);
    }
}
