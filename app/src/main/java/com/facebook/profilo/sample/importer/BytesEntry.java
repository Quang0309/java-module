package com.facebook.profilo.sample.importer;


public class BytesEntry extends TraceEntry {
    private int id;
    private String type;
    private int arg1;
    private String data;

    public BytesEntry(int id, String type, int arg1, String data) {
        this.id = id;
        this.type = type;
        this.arg1 = arg1;
        this.data = data;
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

    public void setType(String type) {
        this.type = type;
    }

    public int getArg1() {
        return arg1;
    }

    public void setArg1(int arg1) {
        this.arg1 = arg1;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
