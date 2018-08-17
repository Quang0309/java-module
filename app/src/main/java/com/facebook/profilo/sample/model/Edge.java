package com.facebook.profilo.sample.model;




public class Edge extends com.facebook.profilo.sample.model.ttypes.Edge{


    public Edge(Trace trace,String sourcePoint,String targetPoint)
    {
        super(sourcePoint,targetPoint,new Properties());
    }
    public Properties getProperties()
    {
        return this.properties;
    }
}
