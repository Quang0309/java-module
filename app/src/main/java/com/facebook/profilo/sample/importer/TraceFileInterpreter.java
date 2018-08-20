package com.facebook.profilo.sample.importer;

import com.facebook.profilo.sample.model.Block;
import com.facebook.profilo.sample.model.ExecutionUnit;
import com.facebook.profilo.sample.model.Trace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class TraceFileInterpreter {
    private TraceFile trace_file;
    private HashMap<String,ExecutionUnit> units;
    private HashMap<Block,BlockEntries> block_entries;
    private Trace trace;
    HashMap<TraceEntry,TraceEntry> parents;
    HashMap<TraceEntry,ArrayList<TraceEntry>> children;
    static long entry_compare(StandardEntry x,StandardEntry y)
    {
        if(x==null||y ==null)
            System.exit(1);
        if(x.getTimestamp()==y.getTimestamp())
            return x.getId()-y.getId();
        else
            return x.getTimestamp() - y.getTimestamp();
    }
    public class BlockEntries
    {
        StandardEntry begin;
        StandardEntry end;

        public BlockEntries(StandardEntry begin, StandardEntry end) {
            this.begin = begin;
            this.end = end;
        }
    }
    public class FamilyEntries
    {
        HashMap<TraceEntry,TraceEntry> parents;
        HashMap<TraceEntry,ArrayList<TraceEntry>> children;
        long min;
        long max;
        public FamilyEntries(HashMap<TraceEntry, TraceEntry> parents, HashMap<TraceEntry, ArrayList<TraceEntry>> children,long max,long min) {
            this.parents = parents;
            this.children = children;
            this.max = max;
            this.min = min;
        }
    }
    public TraceFileInterpreter(TraceFile trace_file) {
        this.trace_file = trace_file;
        this.units = new HashMap<>();
        this.block_entries = new HashMap<>();
    }
    FamilyEntries __calculate_parents_children()
    {
        HashMap<Integer,TraceEntry> entries = new HashMap<>();
        parents = new HashMap<>();
        children = new HashMap<>();
        int parent_id;
        long min_ts = -1;
        long max_ts = -1;
        for(TraceEntry entry:trace_file.entries)
        {
            entries.put(entry.getId(),entry);
            if(entry instanceof StandardEntry)
            {
                StandardEntry temp = (StandardEntry) entry;
                if(min_ts==-1||max_ts==-1) {
                    min_ts = temp.getTimestamp();
                    max_ts = temp.getTimestamp();
                }
                else
                {
                    if(min_ts>temp.getTimestamp())
                        min_ts = temp.getTimestamp();
                    if(max_ts<temp.getTimestamp())
                        max_ts = temp.getTimestamp();
                }
                if(temp.getType().equals("CPU_COUNTER"))
                    continue;
                parent_id = (temp.getArg2());
            }
            else if(entry instanceof BytesEntry)
                parent_id = ((BytesEntry) entry).getArg1();
            else
                parent_id = 0;
            if(parent_id!=0)
            {
                TraceEntry parent = entries.get(parent_id);
                if(parent!=null)
                {
                    parents.put(entry,parent);
                    if(children.containsKey(parent))
                        children.get(parent).add(entry);
                    else
                    {
                        ArrayList<TraceEntry> temp = new ArrayList<>();
                        temp.add(entry);
                        children.put(parent,temp);
                    }
                }
            }
        }
        return new FamilyEntries(parents,children,max_ts,min_ts);
    }
    ExecutionUnit ensure_unit(int tid_int)
    {
        String tid = Integer.toString(tid_int);
        String name;
        if (tid.equals(this.trace_file.headers.get("pid")))
             name = "Main Thread_" + tid;
        else
            name = "Thread_" + tid;
        ExecutionUnit unit = units.get(name);
        if(unit==null)
        {
            unit = this.trace.add_unit();
            unit.getProperties().coreProps.put("name",name);
            unit.getProperties().customProps.put("tid",tid);
            unit.getProperties().coreProps.put("priority","0");
            units.put(name,unit);
        }
        return unit;
    }
    Trace interpret()
    {
        FamilyEntries familyEntries = __calculate_parents_children();
        HashMap<TraceEntry,TraceEntry> parents = familyEntries.parents;
        HashMap<TraceEntry,ArrayList<TraceEntry>> children = familyEntries.children;
        trace = new Trace(familyEntries.min,familyEntries.max,trace_file.headers.get("id"));
        HashMap<Integer,ArrayList<StandardEntry>> thread_items = new HashMap<>();
        for(TraceEntry entry:trace_file.entries)
        {
            if(entry instanceof StandardEntry)
            {
                StandardEntry temp = (StandardEntry) entry;
                if(thread_items.containsKey(temp.getTid()))
                    thread_items.get(temp.getTid()).add(temp);
                else
                {
                    ArrayList<StandardEntry> temp_arr = new ArrayList<>();
                    temp_arr.add(temp);
                    thread_items.put(temp.getTid(),temp_arr);
                }

            }
        }
        for(int key:thread_items.keySet())
        {
            ArrayList<StandardEntry> entries = thread_items.get(key);
            Collections.sort(entries, new Comparator<StandardEntry>() {
                @Override
                public int compare(StandardEntry o1, StandardEntry o2) {

                    if(o1==null||o2 ==null)
                        System.exit(1);
                    if(o1.getTimestamp()==o2.getTimestamp())
                        return o1.getId()-o2.getId();
                    else
                        return (int) (o1.getTimestamp() - o2.getTimestamp());
                }
            });
            ExecutionUnit unit = ensure_unit(key);
            HashMap<Long,Long> stack = new HashMap<>();
            for(StandardEntry entry:entries)
            {
                Block block=null;
                if(entry.getType().equals("MARK_PUSH")||entry.getType().equals("IO_START"))
                {
                    block = unit.push_block(entry.getTimestamp());
                    if(block_entries.containsKey(block))
                        block_entries.get(block).begin = entry;
                    else
                    {
                        BlockEntries temp = new BlockEntries(entry,null);
                        block_entries.put(block,temp);
                    }
                }
                else if(entry.getType().equals("MARK_POP")||entry.getType().equals("IO_END"))
                {
                    block = unit.pop_block(entry.getTimestamp());
                    if(block_entries.containsKey(block))
                        block_entries.get(block).end = entry;
                    else
                    {
                        BlockEntries temp = new BlockEntries(null,entry);
                        block_entries.put(block,temp);
                    }
                }
                else if(entry.getType().equals("TRACE_THREAD_NAME")||entry.getType().equals("TRACE_THREAD_PRI"))
                {
                    process_thread_metadata(entry);
                }
                if (block!=null)
                {
                    BlockEntries block_entries_temp = this.block_entries.get(block);
                    StandardEntry[] arr = new StandardEntry[]{block_entries_temp.begin,block_entries_temp.end};
                    assign_name(block,arr);
                }
            }
        }

    }

    private void assign_name(Block block, StandardEntry[] entries) {

        String name = null;
        for(StandardEntry entry:entries)
        {
            if(entry==null)
                continue;
            ArrayList<TraceEntry> keys = children.get(entry);
            if(keys==null)
                keys = new ArrayList<>();
            for(TraceEntry temp:keys)
            {
                if(!(temp.getType().equals("STRING_KEY")&&temp.getData().equals("__name")))
                    keys.remove(temp);
            }
            if(keys.size()>1)
                System.exit(1);
            if(keys.size()==0)
                continue;
            TraceEntry key = keys.get(0);
            if(!children.containsKey(key))
                return;
            if(children.get(key).size()!=1)
                System.exit(1);
            TraceEntry value = children.get(key).get(0);
            if(!value.getType().equals("STRING_VALUE"))
              System.exit(1);
            name = value.getData();
            break;
        }
        if(name == null)
        {
            if(entries[0]!=null && entries[1]==null)
                name = entries[0].getType();
            else if(entries[0]==null && entries[1]!=null)
                name = entries[1].getType();
            else if(entries[0]!=null && entries[1]!=null)
                name = entries[0].getType()+" to "+ entries[1];
        }

        if(entries.length==2)
        {
            if(entries[0]!=null && entries[1]==null)
                block.getProperties().coreProps.put("name",name + " to Missing");

            else if(entries[0]==null && entries[1]!=null)
                block.getProperties().coreProps.put("name","Missing to "+name);

        }
        block.getProperties().coreProps.put("name",name);
    }

    private void process_thread_metadata(StandardEntry entry) {
        if(entry.getType().equals("TRACE_THREAD_PRI"))
        {
            ExecutionUnit unit = ensure_unit(entry.getTid());
            unit.getProperties().coreProps.put("priority",Long.toString(entry.getArg3()));
        }
        else if(entry.getType().equals("TRACE_THREAD_NAME"))
        {
            ArrayList<TraceEntry> key_child_list = children.get(entry);
            if(key_child_list == null)
                key_child_list = new ArrayList<>();
            if(key_child_list.size()!=1)
                System.exit(1);
            TraceEntry key_child = key_child_list.get(0);
            if(!key_child.getType().equals("STRING_KEY"))
            {
                System.exit(1);
            }
            String tid = key_child.getData();
            ArrayList<TraceEntry> value_child_list = children.get(key_child);
            if(value_child_list==null)
                value_child_list = new ArrayList<>();
            if(value_child_list.size()!=1)
                System.exit(1);
            TraceEntry value_child = value_child_list.get(0);
            if(!value_child.getType().equals("STRING_VALUE"))
                System.exit(1);
            String tname = value_child.getData();
            ExecutionUnit unit = ensure_unit(Integer.parseInt(tid));
            String currname = unit.getProperties().coreProps.get("name");
            if(currname.contains("Main"))
                unit.getProperties().coreProps.put("name","(Main) "+tname);
            else
                unit.getProperties().coreProps.put("name",tname);
        }
    }
}
