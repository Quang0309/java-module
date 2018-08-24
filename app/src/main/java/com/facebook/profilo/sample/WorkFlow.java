package com.facebook.profilo.sample;

import android.content.Context;

import com.facebook.profilo.sample.device.Device;
import com.facebook.profilo.sample.importer.TraceFile;
import com.facebook.profilo.sample.importer.TraceFileInterpreter;
import com.facebook.profilo.sample.model.Block;
import com.facebook.profilo.sample.model.ExecutionUnit;
import com.facebook.profilo.sample.model.Point;
import com.facebook.profilo.sample.model.Trace;
import com.facebook.profilo.sample.model.ttypes.CounterUnit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class WorkFlow extends Thread  {

    private zamCallBack callBack;
    private Context context;
    public WorkFlow(zamCallBack callBack,Context context) {

        this.callBack = callBack;
        this.context = context;
    }

    public class Stat
    {
        long timestamp;
        long count;
        Stat(long timestamp,long count)
        {
            this.timestamp = timestamp;
            this.count = count;
        }
    }


    @Override
    public void run() {

        try {
            File trace_name = pullTrace();
            if (trace_name == null) {
                callBack.onDataDumpedFail();
                return;
            }
            InputStream inputStream = new FileInputStream(trace_name);
            GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
            TraceFile traceFile = TraceFile.from_file(gzipInputStream);
            syscounters(traceFile,callBack);
            //callBack.onDataDumpedSucess();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            callBack.onDataDumpedFail();
        }

    }
    private File pullTrace()
    {
        Device device = new Device(context);
        return device.pull_last_trace();
    }

    private void syscounters(TraceFile traceFile,zamCallBack callBack)
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
        double AVG_JAVA_HEAP = 0;
        double PROCESS_CPU_TIME = 0;
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
            double avr = 0;
            for(Stat temp:data)
            {
                //System.out.print("(");
                //System.out.print(temp.timestamp);
                //System.out.print(",");
                //System.out.print(temp.count);
                //System.out.print(", ");
                //System.out.print(")");
                //System.out.println((double)temp.count/data.size());
                avr+=(double)temp.count/data.size();
            }
            System.out.println(avr);
            if(tc.equals("PROC_CPU_TIME"))
                PROCESS_CPU_TIME = avr;
            else
                AVG_JAVA_HEAP = avr;
            System.out.println();
        }
        callBack.onDataDumpedSucess(PROCESS_CPU_TIME,AVG_JAVA_HEAP);

    }

}
