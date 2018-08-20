package com.facebook.profilo.sample.importer;




public class StandardEntry extends TraceEntry  {
    private int id;
    private String type;
    private long timestamp;
    private int tid;
    private int arg1;
    private int arg2;
    private long arg3;

    public StandardEntry(int id, String type, long timestamp, int tid, int arg1, int arg2, long arg3) {
        this.id = id;
        this.type = type;
        this.timestamp = timestamp;
        this.tid = tid;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    @Override
    String getData() {
        return null;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public int getArg1() {
        return arg1;
    }

    public void setArg1(int arg1) {
        this.arg1 = arg1;
    }

    public int getArg2() {
        return arg2;
    }

    public void setArg2(int arg2) {
        this.arg2 = arg2;
    }

    public long getArg3() {
        return arg3;
    }

    public void setArg3(long arg3) {
        this.arg3 = arg3;
    }



}
