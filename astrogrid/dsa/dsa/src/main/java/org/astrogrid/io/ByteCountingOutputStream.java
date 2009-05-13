/*
 * $Id: ByteCountingOutputStream.java,v 1.1 2009/05/13 13:20:35 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Counts the number of bytes being written out
 */

public class ByteCountingOutputStream extends FilterOutputStream {

   protected long bytesWritten = 0;
   
   public ByteCountingOutputStream(OutputStream out) {
      super(out);
   }
   
   /**
    * increments bytesWritten and writes byte to wrapped out
    */
   public void write(int b) throws IOException {
      bytesWritten++;
      out.write(b);
   }

   /** Get the number of bytes written so far */
   public long getBytesWritten() {
      return bytesWritten;
   }
}
