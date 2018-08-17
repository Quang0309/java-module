package com.facebook.profilo.sample.model.ttypes;

public class Edge {
    private String sourcePoint;
    private String targetPoint;
    protected com.facebook.profilo.sample.model.Properties properties;

    public Edge() {
    }

    public Edge(String sourcePoint, String targetPoint, com.facebook.profilo.sample.model.Properties properties) {
        this.sourcePoint = sourcePoint;
        this.targetPoint = targetPoint;
        this.properties = properties;
    }
}
