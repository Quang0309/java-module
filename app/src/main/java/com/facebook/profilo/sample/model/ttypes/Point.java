package com.facebook.profilo.sample.model.ttypes;

public class Point {
    protected String id;
    protected int timestamp;
    protected int unalignedTimestamp;
    private Properties properties;
    private int sequenceNumber;

    public Point() {
    }

    public Point(String id, int timestamp, int unalignedTimestamp, Properties properties, int sequenceNumber) {
        this.id = id;
        this.timestamp = timestamp;
        this.unalignedTimestamp = unalignedTimestamp;
        this.properties = properties;
        this.sequenceNumber = sequenceNumber;
    }
}
