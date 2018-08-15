package com.facebook.profilo.sample.model;

import java.util.ArrayList;

public class IntervalTree {
    private Intervals root;

    public IntervalTree() {
        root = null;
    }
    Intervals find_interval(Object number)
    {
        if (root!=null)
            return root.find_interval(number);
        else
            return null;
    }
    Intervals add_interval(int begin, int end, Object data)
    {
        Intervals intervals = new Intervals(begin, end, data);
        if(root == null)
        {
            root = intervals;
            return intervals;
        }
        Intervals containing = root.find_interval(intervals);
        if(containing == null) {
            if (intervals.qualify(root)) {
                intervals.add_child(root);
                root = containing;
            } else if (root.getData() != null) {
                Intervals new_root = new Intervals(
                        intervals.getBegin() < root.getBegin() ? intervals.getBegin() : root.getBegin(),
                        intervals.getEnd() > root.getEnd() ? intervals.getEnd() : root.getEnd(),
                        null);
                new_root.children.add(intervals);
                new_root.children.add(root);
                root = new_root;
            } else {
                root.setBegin(intervals.getBegin() < root.getBegin() ? intervals.getBegin() : root.getBegin());
                root.setEnd(intervals.getEnd() > root.getEnd() ? intervals.getEnd() : root.getEnd());
                root.add_child(intervals);
            }
        }
        else{
            containing.add_child(intervals);
        }
        return intervals;
    }
}
