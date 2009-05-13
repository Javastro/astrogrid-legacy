/*
 * $Id: TraceInputStream.java,v 1.1 2009/05/13 13:20:36 gtr Exp $
 */

package org.astrogrid.io.trace;


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
Revision 1.1  2009/05/13 13:20:36  gtr
*** empty log message ***

Revision 1.2  2006/09/26 15:34:42  clq2
SLI_KEA_1794 for slinger and PAL_KEA_1974 for pal and xml, deleted slinger jar from repo, merged with pal

Revision 1.1.2.1  2006/09/11 11:40:46  kea
Moving slinger functionality back into DSA (but preserving separate
org.astrogrid.slinger namespace for now, for easier replacement of
slinger functionality later).

Revision 1.1  2005/02/15 18:33:20  mch
multiproject-ed

Revision 1.1  2004/07/01 23:03:36  mch
Moved trace classes to trace package

Revision 1.1  2004/03/03 10:08:01  mch
Moved UI and some IO stuff into client

Revision 1.2  2003/12/02 19:49:44  mch
Moved snippet and socket-processing stuff into their own packages

Revision 1.1  2003/11/14 00:36:40  mch
Code restructure

Revision 1.2  2003/10/02 12:53:49  mch
It03-Close

 */
