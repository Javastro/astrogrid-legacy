package org.astrogrid.datacenter.io;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Copies the data coming through the stream to a trace window, for debug
 * purposes.
 *
 * @author:  M Hill
 */

public class TraceInputStream extends java.io.FilterInputStream
{
   private TraceWindow traceWindow = null;
   private boolean on = false;
   private DataOutputStream copyStream = null;

   /**
    * Constructor - pass in the stream to monitor
    */
   public TraceInputStream(java.io.InputStream in) {
      super(in);
   }

   /**
    * Returns the trace window. Creates it if it does not yet exist.
    */
   private TraceWindow getTraceWindow() {

      if (traceWindow == null) {
         traceWindow = new TraceWindow();
         traceWindow.setTitle(toString());
         traceWindow.show();
      }

      return traceWindow;
   }

    /**
    * Reads (and returns) a byte, and displays if the trace is 'on'
    * @return int
    */
   public int read() throws java.io.IOException {

      if (!on) {
         return super.read();
      } else {
         int i = super.read();

         if (getTraceWindow().isShowing())
            getTraceWindow().write(i);
         else {
            setState(false);
            traceWindow = null;
         }

         if (copyStream != null)
         {
            copyStream.write(i);
//          String intString = "00" + Integer.toHexString(i);
//          copyStream.writeChars(intString.toUpperCase().substring(intString.length()-2) + " ");
         }

         return i;
      }
   }

    /**
    * Overrides normal read() to display data
    * @return int
    */
   public int read(byte[] b, int off, int len) throws java.io.IOException
   {
      if (!on) {
         return super.read(b, off, len);
      } else {
         int i = super.read(b, off, len);

         if (getTraceWindow().isShowing())
            for (int j=0;j<len;j++) getTraceWindow().write(b[j+off]);
         else {
            setState(false);
            traceWindow = null;
         }

         if (copyStream != null)
         {
            copyStream.write(i);
//          String intString = "00" + Integer.toHexString(i);
//          copyStream.writeChars(intString.toUpperCase().substring(intString.length()-2) + " ");
         }

         return i;
      }
   }

   /**
    * Switches the trace on/off
    */
   public void setState(boolean newOn)
   {
      on = newOn;
   }

   /**
    * Copies throughput to given file
    */
   public void copy2File(File givenFile) throws FileNotFoundException, IOException
   {
      copyStream = new DataOutputStream(new FileOutputStream(givenFile, true));
      copyStream.writeChars("\n");
      copyStream.writeChars("\n");
   }
}

/*
$Log: TraceInputStream.java,v $
Revision 1.2  2003/10/02 12:53:49  mch
It03-Close

 */
