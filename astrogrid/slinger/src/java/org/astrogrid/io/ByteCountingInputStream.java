/*
 * $Id: ByteCountingInputStream.java,v 1.1 2005/02/14 17:53:38 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Counts the number of bytes being read
 */

public class ByteCountingInputStream extends FilterInputStream {

   protected long bytesRead = 0;
   
   public ByteCountingInputStream(InputStream in) {
      super(in);
   }
   
   /**
    * increments bytesRead and reads byte from wrapped stream
    */
   public int read() throws IOException {
      bytesRead++;
      return in.read();
   }

   /** Get the number of bytes read so far */
   public long getBytesRead() {
      return bytesRead;
   }
}
