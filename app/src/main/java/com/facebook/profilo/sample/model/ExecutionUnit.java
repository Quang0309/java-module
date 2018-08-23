package com.facebook.profilo.sample.model;

import java.util.ArrayList;
import java.util.Stack;

public class ExecutionUnit extends com.facebook.profilo.sample.model.ttypes.ExecutionUnit {
    private Trace trace;
    private IntervalTree tree;
    private Stack<Block> stack;
    public ExecutionUnit(Trace trace)
    {
        super(GenID.new_id(),new ArrayList<String>(),new Properties(null,null,null,null,null));
        this.trace = trace;
        this.tree = null;
        this.stack = new Stack<>();
    }
    public Block add_block(Long beginTimestamp, Long endTimestamp)
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
    public Block push_block(long timestamp)
    {
        Block block = this.add_block(timestamp,null);
        this.stack.push(block);
        return block;
    }
    public Block pop_block(long timestamp)
    {
        Block block;
        if (stack.size()==0 || stack.peek().getEnd()!=null)
        {
            block = this.add_block(null,timestamp);
            stack.push(block);
        }
        else
        {
            block = stack.pop();
            block.create_end_point(timestamp);
        }
        return block;
    }
    public Point add_point(long timestamp)
    {
        if(tree==null)
            throw new RuntimeException("Call normalize_blocks first");
        Intervals interval = this.tree.find_interval(timestamp);
        if (interval == null || interval.data == null)
        {
            Block block = this.push_block(timestamp);
            this.pop_block(timestamp);
            return block.add_point(timestamp);
        }
        else
        {
            return interval.data.add_point(timestamp);
        }
    }
    public Block[] all_blocks()
    {
        ArrayList<Block> allBlocks = new ArrayList<>();
        for(String key:this.blocks)
        {
            Block value = this.trace.blocks.get(key);
            allBlocks.add(value);
        }
        return allBlocks.toArray(new Block[allBlocks.size()]);
    }
    public void normalize_blocks()
    {
        Block[] allBlocks = all_blocks();
        for(Block block:allBlocks)
        {
            if(block.getBegin()==null)
                block.create_begin_point(trace.getBegin());
            if(block.getEnd()==null)
                block.create_end_point(trace.getEnd());
        }
        tree = new IntervalTree();
        for(Block block:allBlocks)
        {
            tree.add_interval(block.getBeginPoint().getTimestamp(),block.getEndPoint().getTimestamp(),block);
        }
        __assign_parent_child_blocks(tree.getRoot());
    }

    public void __assign_parent_child_blocks(Intervals node) {
        if (node == null)
            return;
        for(Intervals child:node.children)
        {
            if(node.data!=null)
                node.data.add_child_block(child.data);
            __assign_parent_child_blocks(child);
        }
    }
    public String getID()
    {
        return this.id;
    }
    public Properties getProperties()
    {
        return this.properties;
    }
}
