/*
 * $Id: WriterStream.java,v 1.2 2004/12/07 01:33:36 jdt Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger.targets.out;


import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;


/** Used to present an OutputStream for writing to the Writer.
 * I gather this is fraught with difficulties, but we need a way of writing binary
 * data to writers... Actually I'm not sure we do.  This started because I was
 * passing around a WriterTarget for http responses, but I've added an HttpResponseTarget
 * now...
 */
public class WriterStream extends OutputStream {
   
   private Writer out;
   
   /**
    * Constructor, wrapping the given writer */
   public WriterStream(Writer writer) {
      this.out = writer;
   }
   
   /**
    * Writes the specified byte to this output stream. The general
    * contract for <code>write</code> is that one byte is written
    * to the output stream. The byte to be written is the eight
    * low-order bits of the argument <code>b</code>. The 24
    * high-order bits of <code>b</code> are ignored.
    *
    * This implementation is probably not right... Might need to find out more
    * about Writers and how they handle binary stuff...
    */
   public void write(int b) throws IOException {
      out.write(b);
   }
   
}

/*
 $Log: WriterStream.java,v $
 Revision 1.2  2004/12/07 01:33:36  jdt
 Merge from PAL_Itn07

 Revision 1.1.2.1  2004/11/22 00:46:28  mch
 New Slinger Package


 */



