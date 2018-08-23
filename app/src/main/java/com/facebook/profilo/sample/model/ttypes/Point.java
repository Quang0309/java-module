package com.facebook.profilo.sample.model.ttypes;

import com.facebook.profilo.sample.model.Properties;

public class Point {
    protected String id;
    protected long timestamp;
    protected long unalignedTimestamp;
    public com.facebook.profilo.sample.model.Properties properties;
    private int sequenceNumber;

    public Point() {
    }

    public Point(String id, long timestamp, long unalignedTimestamp, Properties properties, int sequenceNumber) {
        this.id = id;
        this.timestamp = timestamp;
        this.unalignedTimestamp = unalignedTimestamp;
        this.properties = properties;
        this.sequenceNumber = sequenceNumber;
    }
}
