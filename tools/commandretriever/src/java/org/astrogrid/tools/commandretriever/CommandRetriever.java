package org.astrogrid.tools.commandretriever;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.net.*;
import javax.servlet.ServletOutputStream;
import java.io.DataInputStream;
import java.io.PrintWriter;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.InflaterInputStream;
import java.util.zip.GZIPInputStream;
import java.io.FilterInputStream;
import java.io.FileInputStream;
import java.io.File;
import java.util.Properties;


/**
 * Small Servlet that will run a command on a machine and stream some kind
 * of output file back to the user.
 *
 *
 */
public class CommandRetriever extends HttpServlet {

    private static final int BLOCK_SIZE = 2048;

    /**
     * 
     *
     * @param request
     *                   the HTTP request object
     * @param response
     *                   the HTTP response object
     * @throws IOException
     *                    thrown when there is a problem getting the writer
     */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        processResult(request,response);
    }

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
      processResult(request,response);
    }

    private InputStream loadProperty() {
       //new FileInputStream("tape.properties")
       ClassLoader loader = this.getClass().getClassLoader();
       return loader.getResourceAsStream("command.properties");
    }

    private void processResult(HttpServletRequest request,
            HttpServletResponse response) throws IOException {

      Properties prop = new Properties();
      prop.load(loadProperty());
      String command = prop.getProperty("execute.command");
      String output = prop.getProperty("result.output");
      String temp;
      //System.out.println("here is the command = " + command);
      int index = 0;
      while((index = command.indexOf("<")) != -1) {
         //System.out.println("here is the index = " + index);
         temp = command.substring(index+1,command.indexOf(">",index));
         System.out.println("Req param = " + temp);
         command = command.replaceFirst("<" + temp + ">",request.getParameter(temp));
         //System.out.println("command not filled out = " + command);
      }

      System.out.println("this is the command to be ran = " + command);

      while(output.indexOf("<") != -1) {
         temp = output.substring(output.indexOf("<")+1,output.indexOf(">",command.indexOf("<")));
         //System.out.println("Req param = " + temp);
         output = output.replaceFirst("<" + temp + ">",request.getParameter(temp));
      }
      System.out.println("here is where the output will be = " + output);

      String s = request.getHeader("user-agent");
      if (s == null) {
         System.out.println("the user-agent = null");
         return;
      }
      System.out.println("the user-agent = " + s);
      String remoteHost = request.getRemoteHost();
      System.out.println("The remote host = " + remoteHost);

      boolean zipFile = false;
      if(output.endsWith("gz")) {
        zipFile = true;
      }
      command = command.trim();

      File fi = new File(output);
      if(!fi.exists()) {
         Runtime rt = Runtime.getRuntime();
         Process proc = rt.exec(command);
         try {
            System.out.println("Command being ran wait till it finishes");
            proc.waitFor();
         }catch(InterruptedException ie) {
            System.out.println("Ouch interrupted exception here is the stack trace");
            ie.printStackTrace();
         }
         System.out.println("Command has finished with a value of = " + proc.exitValue()  + " typically if it is not 0 then something went wrong");
      }else {
         System.out.println("Output file already exists; not running command just process/stream out file.");
      }

      response.setContentType("application/octet-stream");
      FileInputStream fis = new FileInputStream(output);
      if(zipFile) {
         System.out.println("It was a zip file -- inflate and buffer out a zip file");
         GZIPInputStream gzi = new GZIPInputStream(fis);
         bufferedPipe(gzi,response.getOutputStream());
      }else {
         bufferedPipe(fis,response.getOutputStream());
      }

      //response.getOutputStream().write(new String("Done").getBytes());

    }

   public static void pipe(InputStream in, OutputStream out) throws IOException
   {
      byte[] block = new byte[BLOCK_SIZE];
      int read = in.read(block);
      while (read > -1)
      {
         out.write(block,0, read);
         read = in.read(block);
      }
   }

   /** Convenience routine to wrap given streams in Buffered*putStreams and
    * pipe
    */
   public static void bufferedPipe(InputStream in, OutputStream out) throws IOException
   {
      out = new BufferedOutputStream(out);
      pipe(new BufferedInputStream(in), out);
      out.flush();
      out.close();
   }

}