package com.facebook.profilo.sample.model.ttypes;

public class Edge {
    private Point sourcePoint;
    private Point targetPoint;
    private Properties properties;

    public Edge() {
    }

    public Edge(Point sourcePoint, Point targetPoint, Properties properties) {
        this.sourcePoint = sourcePoint;
        this.targetPoint = targetPoint;
        this.properties = properties;
    }
}
