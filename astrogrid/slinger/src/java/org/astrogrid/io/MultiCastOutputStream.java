/*
 * $Id: MultiCastOutputStream.java,v 1.1 2005/02/14 17:53:38 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * Writes to several streams at once
 */

public class MultiCastOutputStream extends OutputStream {
   
   OutputStream[] outs = null;

   public MultiCastOutputStream(OutputStream[] multiOuts) {
      this.outs = multiOuts;
   }
   
   /**
    * Writes the specified byte to the output streams.
    */
   public void write(int b) throws IOException {
      for (int i = 0; i < outs.length; i++) {
         outs[i].write(b);
      }
   }
   
}
