package com.facebook.profilo.sample.model;

import java.util.ArrayList;
import java.util.Collections;

public class Intervals {
    private long begin;
    private long end;
    public Block data;
    public ArrayList<Intervals> children;
    private ArrayList<Long> __children_begins;
    private static ArrayList<Intervals> slots;

    public long getBegin() {
        return begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public Object getData() {
        return data;
    }

    public void setData(Block data) {
        this.data = data;
    }

    public Intervals(long begin, long end, Block data) {
        this.begin = begin;
        this.end = end;
        this.data = data;
        children = new ArrayList<>();
        __children_begins = new ArrayList<>();
    }
    long length()
    {
        return end - begin;
    }
    boolean qualify(Object object)
    {

        if (object instanceof Intervals) {
            Intervals temp = (Intervals) object;
            if(this.begin<temp.begin && temp.begin<this.end && this.end<temp.end )
                throw new IllegalArgumentException();
            return this.begin<=temp.begin && temp.begin<=temp.end && temp.end<=this.end ;
        }
        else if(object instanceof Integer)
        {
            int temp = (int) object;
            return this.begin<= temp && temp <=this.end;
        }
        else
            throw new IllegalArgumentException();
    }
    void add_child(Intervals intervals)
    {
        if(!qualify(intervals))
            System.exit(0);
        int insert_index = Collections.binarySearch(__children_begins,intervals.begin);
        if(insert_index>=0)
            insert_index++;
        else
            insert_index = insert_index*-1 -1;
        __children_begins.add(insert_index,intervals.begin);
        children.add(insert_index,intervals);
    }
    Intervals find_interval(Object intervals)
    {
        if(!qualify(intervals))
            return null;
        long begin;
        if (intervals instanceof Intervals)
            begin = ((Intervals) intervals).begin;
        else
            begin = (int) intervals;
        int insert_index = Collections.binarySearch(__children_begins,begin);
        if(insert_index>=0)
            insert_index++;
        else
            insert_index = insert_index*-1 -1;
        if(0<insert_index && insert_index<=children.size())
        {
            Intervals child = this.children.get(insert_index-1);
            Intervals result = child.find_interval(intervals);
            if(result!=null)
                return result;
        }
        return this;
    }
}
