/*
 * $Id: ByteCountingInputStream.java,v 1.2 2006/09/26 15:34:42 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Counts the number of bytes being read.  Implements InputStream rather than
 * FilterInputStream as its simpler... FilterInputStream does not route all
 * reads through the read() method.
 */

public class ByteCountingInputStream extends InputStream {

   protected long bytesRead = 0;

   InputStream wrappedIn = null;
   
   public ByteCountingInputStream(InputStream in) {
      super();
      wrappedIn= in;
   }
   
   /**
    * increments bytesRead and reads byte from wrapped stream
    */
   public int read() throws IOException {
      bytesRead++;
      return wrappedIn.read();
   }

   /** Get the number of bytes read so far */
   public long getBytesRead() {
      return bytesRead;
   }
}
