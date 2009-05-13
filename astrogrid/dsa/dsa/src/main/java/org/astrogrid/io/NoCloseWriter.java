/*
 * $Id: NoCloseWriter.java,v 1.1.1.1 2009/05/13 13:20:35 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.io;

import java.io.IOException;
import java.io.Writer;

/**
 * A writer wrapper that only flushes the output rather than closing it.
 * Useful for, eg, browser output where you may want to add more afterwards
 */

public class NoCloseWriter extends Writer {

   Writer out = null;
   
   public NoCloseWriter(Writer writerToNotClose) {
      this.out = writerToNotClose;
   }
   
   /**
    * Flush the stream.
    */
   public void flush() throws IOException {
      out.flush();
   }
   
   /**
    * Normally Closes the stream.  Just flushes it here
    */
   public void close() throws IOException {
      out.flush();
   }
   
   /**
    * Really close the wrapped writer
    */
   public void reallyClose() throws IOException {
      out.close();
   }
   /**
    * Write a portion of an array of characters.
    */
   public void write(char[] cbuf, int off, int len) throws IOException {
      out.write(cbuf, off, len);
   }
   
}
