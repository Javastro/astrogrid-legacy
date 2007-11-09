package org.astrogrid.tools.inflater;

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
import java.util.zip.ZipInputStream;
import java.io.FilterInputStream;
import java.io.FileInputStream;
import java.io.File;
import java.util.Properties;


/**
 * Class: Infater
 * Description: Small servlet that by looking at a parameter or two will inflate a 
 * zip/archive file so the client/user gets the raw data.  Found usefull in the cases of
 * zipped up fits files and user would prefer to have the real Fits file streamed back.
 *
 *
 */
public class Inflater extends HttpServlet {

    private static final int BLOCK_SIZE = 2048;

    /**
     * This prints out the standard "Hello world" message with a date stamp.
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

    private void processResult(HttpServletRequest request,
            HttpServletResponse response) throws IOException {

      String type = request.getParameter("type");
      URL url = new URL(request.getParameter("url"));
      InputStream input = null;
      if(type.equals("gz")) {
    	  input = new GZIPInputStream(url.openStream());
      }else if(type.equals("zip")) {
    	  input = new ZipInputStream(url.openStream());
      }
      
      //response.setHeader("Content-Location",url.toString());
      response.setContentType("application/octet-stream");
      if(input != null) {
    	  bufferedPipe(input,response.getOutputStream());
      }
     
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