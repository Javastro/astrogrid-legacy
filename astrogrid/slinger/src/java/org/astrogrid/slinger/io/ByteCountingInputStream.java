/*
 * $Id: ByteCountingInputStream.java,v 1.2 2005/01/26 17:31:56 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Counts the number of bytes being written In
 */

public class ByteCountingInputStream extends FilterInputStream {

   protected long bytesRead = 0;
   
   public ByteCountingInputStream(InputStream in) {
      super(in);
   }
   
   /**
    * increments bytesRead and writes byte to wrapped In
    */
   public int read() throws IOException {
      bytesRead++;
      return in.read();
   }

   /** Get the number of bytes written so far */
   public long getbytesRead() {
      return bytesRead;
   }
}
