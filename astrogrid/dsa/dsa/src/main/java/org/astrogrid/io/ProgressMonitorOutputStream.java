/*
 * $Id: ProgressMonitorOutputStream.java,v 1.1 2009/05/13 13:20:35 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Takes a listener that gets told every n bytes have passed, or when its
 * closed.
 */

public class ProgressMonitorOutputStream extends ByteCountingOutputStream {

   int interval;
   StreamProgressListener listener;
   
   /** Create stream wrapping given output, and with the given interval as
    * the number of bytes between notifications */
   public ProgressMonitorOutputStream(OutputStream out, int updateInterval, StreamProgressListener aListener) {
      super(out);
      this.interval = updateInterval;
      this.listener = aListener;
   }
   
   /**
    * increments bytesWritten and writes byte to wrapped out
    */
   public void write(int b) throws IOException {
      super.write(b);
      if ((bytesWritten % interval) == 0) {
         listener.setStreamProgress(bytesWritten);
      }
   }

   /**
    * tells listener the stream is complete
    */
   public void close() throws IOException {
      super.close();
      listener.setStreamClosed();
   }

   
   
}
