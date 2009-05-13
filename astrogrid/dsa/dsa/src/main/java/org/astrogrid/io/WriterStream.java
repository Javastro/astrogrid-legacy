/*
 * $Id: WriterStream.java,v 1.1.1.1 2009/05/13 13:20:35 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.io;


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
 Revision 1.1.1.1  2009/05/13 13:20:35  gtr


 Revision 1.2  2006/09/26 15:34:42  clq2
 SLI_KEA_1794 for slinger and PAL_KEA_1974 for pal and xml, deleted slinger jar from repo, merged with pal

 Revision 1.1.2.1  2006/09/11 11:40:46  kea
 Moving slinger functionality back into DSA (but preserving separate
 org.astrogrid.slinger namespace for now, for easier replacement of
 slinger functionality later).

 Revision 1.1  2005/02/14 20:47:38  mch
 Split into API and webapp

 Revision 1.1  2005/02/14 17:53:38  mch
 Split between webnode (webapp) and library, prepare to split between API and special implementations

 Revision 1.2  2005/01/26 17:31:56  mch
 Split slinger out to scapi, swib, etc.

 Revision 1.1.2.1  2004/12/13 15:53:39  mch
 Moved stuff to IO package and new progress monitoring streams

 Revision 1.1.2.1  2004/11/22 00:46:28  mch
 New Slinger Package


 */



