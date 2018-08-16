package com.facebook.profilo.sample.model;




public class Edge extends com.facebook.profilo.sample.model.ttypes.Edge{

    private Point sourcePoint;
    private Point targetPoint;
    public Edge(Trace trace,Point sourcePoint,Point targetPoint)
    {
        super(sourcePoint,targetPoint,new Properties());
    }
}
