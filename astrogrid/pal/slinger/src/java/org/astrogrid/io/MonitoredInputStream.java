/*
 * $Id: MonitoredInputStream.java,v 1.2 2006/09/26 15:34:42 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Takes a listener that gets told every n bytes have passed, or when its
 * closed.
 */

public class MonitoredInputStream extends ByteCountingInputStream {

   int interval;
   StreamProgressListener listener;
   
   /** Create stream wrapping given input, and with the given interval as
    * the number of bytes between notifications */
   public MonitoredInputStream(InputStream in, int updateInterval, StreamProgressListener aListener) {
      super(in);
      this.interval = updateInterval;
      this.listener = aListener;
   }
   
   /**
    * updates listener when required
    */
   public int read() throws IOException {
      //by doing this before we're out by one byte...  this might be bad...
      if ((bytesRead % interval) == 0) {
         listener.setStreamProgress(bytesRead);
      }
      return super.read();
   }

   /**
    * tells listener the stream is complete
    */
   public void close() throws IOException {
      super.close();
      listener.setStreamClosed();
   }

   
   
}
