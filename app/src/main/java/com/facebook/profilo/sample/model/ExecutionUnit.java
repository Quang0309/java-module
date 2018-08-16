package com.facebook.profilo.sample.model;

import java.util.ArrayList;
import java.util.Stack;

public class ExecutionUnit extends com.facebook.profilo.sample.model.ttypes.ExecutionUnit {
    private Trace trace;
    private IntervalTree tree;
    private Stack<Block> stack;
    public ExecutionUnit(Trace trace)
    {
        super(GenID.new_id(),new ArrayList(),new Properties());
        this.trace = trace;
        this.tree = null;
        this.stack = new Stack<>();
    }
    public Block add_block(Integer beginTimestamp, Integer endTimestamp)
    {
        Block block = new Block(this.trace,this,null,null);
        this.trace.blocks.put(block.getID(),block);
        this.blocks.add(block.getID());
        if(beginTimestamp!=null)
            block.create_begin_point(beginTimestamp);
        if(endTimestamp!=null)
            block.create_end_point(endTimestamp);
        return block;
    }
    public Block push_block(int timestamp)
    {
        Block block = this.add_block(timestamp,null);
        this.stack.push(block);
        return block;
    }
}
