package com.facebook.profilo.sample.device;






import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.OutputStream;


import java.util.ArrayList;

import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;


import java.util.zip.ZipInputStream;

public class Device {
    private Context context;
    public Device(Context context)
    {
        this.context = context;
    }
    class FileMovedException extends Exception
    {
        public FileMovedException(String message) {
            super(message);
        }
    }
    class DeviceTrace
    {
        String file_name;
        String full_path;
        long modified_time;
        long size;

        public DeviceTrace(String file_name, String full_path, long modified_time, long size) {
            this.file_name = file_name;
            this.full_path = full_path;
            this.modified_time = modified_time;
            this.size = size;
        }
    }
    //private static String _PROFILO_DIR = "cache/";


    private static String _PROFILO_HEADER_START = "dt\n";

    //private static String[] _CHECK_PROFILO_DIR_EXISTS_CMD = {"ls", _PROFILO_DIR};
    //private static String[] _ADB_EXEC_OUT_CMD_BASE = {"adb", "exec-out", "run-as","","cat",""};
    //private static String _CAT_CMD = "cat";
    private static String _NO_SUCH_FILE = "No such file or directory";
   /* String _get_data_dir_full_path(String pack)
    {
        return "/data/data/" + pack;
    }*/
    private boolean _validate_trace(File file) throws FileMovedException {
        //String full_path = "/data/data/" + pack + "/" + file_path;
        //System.out.println(full_path + file_path);
        //System.out.println(full_path);
        //System.out.println("haha");
        try {

            if(file==null)
                throw new FileMovedException(_NO_SUCH_FILE);
            InputStream o_out = new FileInputStream(file);
            //System.out.println(full_path);
            //_ADB_EXEC_OUT_CMD_BASE[3] = pack;
            //_ADB_EXEC_OUT_CMD_BASE[5] = full_path;
            //System.out.println(full_path);
            //String[] command = _ADB_EXEC_OUT_CMD_BASE + new String[]{pack} +_CAT_CMD + new String[]{full_path};
            //Process process =  Runtime.getRuntime().exec(_ADB_EXEC_OUT_CMD_BASE);
            //InputStream o_err = process.getErrorStream();
            //InputStream o_out = process.getInputStream();
            o_out.mark(Integer.MAX_VALUE);//mark the start of input stream
            //System.out.println("->");
            //for(String temp: _ADB_EXEC_OUT_CMD_BASE)
            //    System.out.println(temp);
            //BufferedReader out_bufferedReader = new BufferedReader(new InputStreamReader(o_out));
            //BufferedReader err_bufferedReader = new BufferedReader(new InputStreamReader(o_err));
            /*if((o_err!=null&& convertBytestoStringV2(err_bufferedReader).contains(_NO_SUCH_FILE))
                    || (o_out!=null&& convertBytestoStringV2(out_bufferedReader).contains(_NO_SUCH_FILE)))
            {
                System.out.println("throw at validate");
                err_bufferedReader.close();
                out_bufferedReader.close();
                throw new FileMovedException(_NO_SUCH_FILE + full_path);
            }*/
            o_out.reset(); // set the cusor back to the start
            ZipInputStream zipInputStream = new ZipInputStream(o_out);
            ZipEntry ze =  zipInputStream.getNextEntry();

            boolean isZip = ze != null; // check file is zip file
            if(isZip)
            {
                //do sth if file is zip
                do {
                    if(ze.getName().startsWith("extra/"))
                        continue;
                    GZIPInputStream gzipInputStream = new GZIPInputStream(zipInputStream);
                    if(!convertBytestoString(gzipInputStream).startsWith(_PROFILO_HEADER_START))
                        return false;

                }while((ze = zipInputStream.getNextEntry())!=null);
            }
            else
            {
                o_out.reset();
                // check Gzip
                GZIPInputStream gzipInputStream = new GZIPInputStream(o_out);
                if(!convertBytestoString(gzipInputStream).startsWith(_PROFILO_HEADER_START))
                    return false;
            }
            //out_bufferedReader.close();
            //err_bufferedReader.close();
            return true;

        }

        catch (IOException e) {
            e.printStackTrace();
            return false;
        }


    }
    private String convertBytestoString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));

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
  /*  private String convertBytestoStringV2(BufferedReader bufferedReader) throws IOException
    {
        //bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        int read;
        char[] buffer = new char[1024];
        StringBuilder output = new StringBuilder();
        while ((read = bufferedReader.read(buffer)) > 0) {
            output.append(buffer, 0, read);
        }
        //bufferedReader.close();

        // Waits for the command to finish.

        //System.out.println(output.toString());
        return output.toString();
    }*/
    private DeviceTrace _create_trace(File file) throws IOException, FileMovedException {
       // String adjust_file_path = file_path.trim(); //remove leading space
       // adjust_file_path = adjust_file_path.substring(1); // remove leading "./"
       // String full_path = data_dir_path.concat(adjust_file_path);
       // String file_name = full_path.substring(data_dir_path.length()+adjust_file_path.lastIndexOf("/")+1); //get name of file .log
       // String[] command = {"adb","shell","run-as",pack,"ls","-ln",full_path};
       // Process process = Runtime.getRuntime().exec(command);
       // InputStream o_err = process.getErrorStream();
       // InputStream o_out = process.getInputStream();
       /* if(o_err!=null&& convertBytestoString(o_err).contains(_NO_SUCH_FILE)) {
            //o_err.close();
            o_out.close();
            System.out.println("throw at create trace");
            throw new FileMovedException(_NO_SUCH_FILE + full_path);
        }
*/
        //String parse = convertBytestoString(o_out);
       // parse = parse.trim().replaceAll("\\s{2,}"," "); // delete additional spaces
        //String[] file_info = parse.split(" ");
        //String modified_time = file_info[4] + " " +file_info[5];
        //int size = Integer.parseInt(file_info[3]);
       //o_out.close();

        return new DeviceTrace(file.getName(),file.getAbsolutePath(),file.lastModified(),file.length()/1024);
        //return new DeviceTrace(file_name,full_path,modified_time,size);
    }
    /*private boolean _pull_trace(String pack,DeviceTrace trace) throws IOException {
        String []command = {"adb","exec-out","run-as",pack,_CAT_CMD,trace.full_path};
        Process process = Runtime.getRuntime().exec(command);
        InputStream o_out = process.getInputStream();
        OutputStream os = new FileOutputStream(trace.file_name);

        byte[] buffer = new byte[1024];
        int bytesRead;
        //read from is to buffer
        while((bytesRead = o_out.read(buffer)) !=-1){
            os.write(buffer, 0, bytesRead);
        }
        o_out.close();
        //flush OutputStream to write any buffered data to file
        os.flush();
        os.close();
        try {
            return process.waitFor()==0;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }*/
    /*private boolean _check_profilo_dir_exists(String pack) throws IOException {
        String command[] = {"adb","shell","run-as",pack,"ls",_PROFILO_DIR};
        Process process = Runtime.getRuntime().exec(command);
        InputStream o_out = process.getInputStream();
        int isEmpty = o_out.read();
        o_out.close();
        return isEmpty!=-1;
    }*/
    /*private DeviceTrace[] list_traces(String pack) throws IOException, FileMovedException {
        if(!_check_profilo_dir_exists(pack))
        {
            System.out.println("Profilo directory doesn't exist");
            return null;
        }
        String data_dir_path = _get_data_dir_full_path(pack);
        String command[] = {"adb","shell","run-as",pack,"ls", "-R", "|", "grep", "-B", "1", ".log"};
        Process process = Runtime.getRuntime().exec(command);
        InputStream o_out = process.getInputStream();

        String output = convertBytestoString(o_out)*//*.trim()*//*;


        String []data = output.split("\r\n");

        String directory = data[0].replace(":","/"); // remove trailing ":" with "/"
        ArrayList<String> traces = new ArrayList<>();
       // String output_v2;

        for(int i=1;i<data.length;i++)
        {
            //System.out.println(directory);
          //  System.out.println(data[i]+"hazzz");

           // String temp = data[i].substring(0,data[i].length());
           // System.out.println((int)data[i].charAt(data[i].length()-1));
           // System.out.println(temp +"wtf");
            //char[] v1 = directory.toCharArray();

            output = directory + data[i];
            //output_v2 = output_v2.concat("mua");
            //System.out.println(buffer.toString()+"z"+"noob");
            //System.out.println(output_v2);

            if(_validate_trace(pack,data_dir_path,output))
                traces.add(output);
        }

        DeviceTrace[] deviceTraces = new DeviceTrace[traces.size()];
        for(int i=0;i<deviceTraces.length;i++)
        {
            deviceTraces[i] = _create_trace(pack,data_dir_path,traces.get(i));
        }
        return deviceTraces;
    }*/
   /* private DeviceTrace get_latest_trace(DeviceTrace[] traces)
    {
        DeviceTrace latest = traces[0];
        for(DeviceTrace temp:traces)
        {
            if(latest.modified_time.compareTo(temp.modified_time)<0)
                latest = temp;
        }
        return latest;
    }*/
    public File pull_last_trace()
    {
        DeviceTrace trace = null;
        File file = null;
        try
            {
                file = getLatestFile();
                if(file!=null)
                    trace = _create_trace(file);
                else
                {
                    System.out.println("No such file.");
                    System.exit(1);
                }
            }
            catch (FileMovedException e)
            {
                e.printStackTrace();
                System.out.println("Retrying, file moved");
                System.exit(1);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                System.out.println("Fail, existing...");
                System.exit(1);
            }
            System.out.println(trace.file_name);
            return file;


    }
    private File getLatestFile() throws FileMovedException
    {
        File file = new File(context.getCacheDir(),"upload");
        //File file = new File(getCacheDir(),);
        File max = null;
        for(File temp:file.listFiles()) {
            //Log.e("Name",temp.getName());
            if (max == null)
                max = temp;
            if (max.lastModified() < temp.lastModified())
                max = temp;
        }
        if(_validate_trace(file))
        {
            return file;
        }
        return null;

    }

}
