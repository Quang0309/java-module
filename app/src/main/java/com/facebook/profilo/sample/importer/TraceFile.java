package com.facebook.profilo.sample.importer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class TraceFile {
    public HashMap<String,String> headers;
    public ArrayList<TraceEntry> entries;

    public TraceFile(HashMap<String,String> headers, ArrayList<TraceEntry> entries) {
        this.headers = headers;
        this.entries = entries;
    }
    static long do64bitAddition(long a,long b)
    {
        return a + b;
    }
    static int do32bitAddition(int a,int b)
    {
        return a+b;
    }
    static ArrayList<TraceEntry> delta_decode_entries(HashMap<String,String> headers, ArrayList<TraceEntry> delta_encoded)
    {
        int precision = 0;
        if(headers.containsKey("prec"))
            precision = Integer.parseInt(headers.get("prec"));
        int timestamp_multiplier = (int) Math.pow(10,9-precision);
        ArrayList<TraceEntry> entries = new ArrayList<>();
        StandardEntry last_entry = null;

        for(TraceEntry entry:delta_encoded)
        {
            if(!(entry instanceof StandardEntry))
            {
                entries.add(entry);
                continue;
            }
            if(last_entry == null)
            {
                StandardEntry adjusted_entry = new StandardEntry(((StandardEntry) entry).getId(),
                        ((StandardEntry) entry).getType(),
                        ((StandardEntry) entry).getTimestamp()*timestamp_multiplier,
                        ((StandardEntry) entry).getTid(),
                        ((StandardEntry) entry).getArg1(),
                        ((StandardEntry) entry).getArg2(),
                        ((StandardEntry) entry).getArg3());
                entries.add(adjusted_entry);
                last_entry = adjusted_entry;
                continue;
            }
            StandardEntry delta_entry = new StandardEntry(do32bitAddition(last_entry.getId(),((StandardEntry) entry).getId()),
                    ((StandardEntry) entry).getType(),
                    do64bitAddition(last_entry.getTimestamp(),((StandardEntry) entry).getTimestamp() * timestamp_multiplier),
                    do32bitAddition(last_entry.getTid(),((StandardEntry) entry).getTid()),
                    do32bitAddition(last_entry.getArg1(),((StandardEntry) entry).getArg1()),
                    do32bitAddition(last_entry.getArg2(),((StandardEntry) entry).getArg2()),
                    do64bitAddition(last_entry.getArg3(),((StandardEntry) entry).getArg3()));
            entries.add(delta_entry);
            last_entry = delta_entry;
        }
        return entries;
    }
    static TraceFile from_string(String data)
    {
        String []datas = data.split("\n\n",2);
        String []headers = datas[0].split("\n");
        HashMap<String,String> headersMap = new HashMap<>();
        for(String temp:headers)
        {
            String []pair = temp.split("|");
            if(pair.length >= 2)
                headersMap.put(pair[0],pair[1]);
        }
        String bodies[] = datas[1].split("\n");
        ArrayList<TraceEntry> gen_entries = new ArrayList<>();
        for(String line:bodies)
        {
            if(line.trim().length()>0)
                gen_entries.add(TraceEntry.construct(line));
        }
        ArrayList<TraceEntry> entries = delta_decode_entries(headersMap,gen_entries);
        return new TraceFile(headersMap,entries);
    }
    public static TraceFile from_file(InputStream inputStream) throws IOException
    {
        String data = convertBytestoString(inputStream);
        return TraceFile.from_string(data);
    }
    private static String convertBytestoString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        int read;
        char[] buffer = new char[1024];
        StringBuilder output = new StringBuilder();
        while ((read = bufferedReader.read(buffer)) > 0) {
            output.append(buffer, 0, read);
        }
        bufferedReader.close();

        // Waits for the command to finish.


        return output.toString();
    }
}
