package com.facebook.profilo.sample;

import com.facebook.profilo.sample.device.Device;
import com.facebook.profilo.sample.importer.TraceFile;
import com.facebook.profilo.sample.importer.TraceFileInterpreter;
import com.facebook.profilo.sample.model.Block;
import com.facebook.profilo.sample.model.ExecutionUnit;
import com.facebook.profilo.sample.model.Point;
import com.facebook.profilo.sample.model.Trace;
import com.facebook.profilo.sample.model.ttypes.CounterUnit;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class WorkFlow {
    public static class Stat
    {
        long timestamp;
        long count;
        Stat(long timestamp,long count)
        {
            this.timestamp = timestamp;
            this.count = count;
        }
    }
    public static void main(String[] args) throws IOException
    {
       String trace_name = pullTrace(args[0]);
       if(trace_name==null)
       {
           return;
       }
       InputStream inputStream = new FileInputStream(trace_name);
       GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
       TraceFile traceFile = TraceFile.from_file(gzipInputStream);
       syscounters(traceFile);
    }
    static String pullTrace(String pack)
    {
        Device device = new Device();
        return device.pull_last_trace(pack);
    }

    static void syscounters(TraceFile traceFile)
    {
        TraceFileInterpreter interpreter = new TraceFileInterpreter(traceFile);
        Trace trace = interpreter.interpret();
        HashMap<String,ArrayList<Stat>> counters = new HashMap<>();
        for(ExecutionUnit unit:trace.executionUnits.values())
        {
            for(String block_id:unit.blocks)
            {
                Block block = trace.blocks.get(block_id);
                Point[] points = block.points();
                for(Point point:points)
                {
                    HashMap<String,Long> counter = point.getProperties().counterProps.get(CounterUnit.ITEMS);
                    if(counter == null)
                        continue;
                    for(Map.Entry<String,Long> pair:counter.entrySet())
                    {
                        if(counters.containsKey(pair.getKey()))
                            counters.get(pair.getKey()).add(new Stat(point.getTimestamp(),pair.getValue()));
                        else
                        {
                            ArrayList<Stat> temp = new ArrayList<>();
                            temp.add(new Stat(point.getTimestamp(),pair.getValue()));
                            counters.put(pair.getKey(),temp);
                        }
                    }
                }
            }
        }
        String[] test_counters = new String[]{"PROC_CPU_TIME","JAVA_ALLOC_BYTES"};
        for(String tc:test_counters)
        {
           ArrayList<Stat> data = counters.get(tc);
            Collections.sort(data, new Comparator<Stat>() {
                @Override
                public int compare(Stat o1, Stat o2) {
                    if(o1.timestamp==o2.timestamp)
                        return 0;
                    return  (o1.timestamp-o2.timestamp>=0?1:-1);

                }
            });
            System.out.println(tc + "->");
            long avr = 0;
            for(Stat temp:data)
            {
                //System.out.print("(");
                //System.out.print(temp.timestamp);
                //System.out.print(",");
                //System.out.print(temp.count);
                //System.out.print(")");
                avr+=temp.count/data.size();
            }
            System.out.println(avr);
            System.out.println();
        }

    }

}
