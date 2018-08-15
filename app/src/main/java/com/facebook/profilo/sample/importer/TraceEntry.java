package com.facebook.profilo.sample.importer;

abstract class TraceEntry {
    static TraceEntry construct(String line)
    {
        String []lines = line.split("|");
        if(lines[1].equals("STRING_KEY")||lines[1].equals("STRING_VALUE"))
            return new BytesEntry(
                    Integer.parseInt(lines[0]),
                    lines[1],
                    Integer.parseInt(lines[2]),
                    lines[3]);
        else
            return new StandardEntry(
                    Integer.parseInt(lines[0]),
                    lines[1],
                    Long.parseLong(lines[2]),
                    Integer.parseInt(lines[3]),
                    Integer.parseInt(lines[4]),
                    Integer.parseInt(lines[5]),
                    Long.parseLong(lines[6]));
    }
}
