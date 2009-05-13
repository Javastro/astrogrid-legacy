/*
 * $Id: WriterTarget.java,v 1.1.1.1 2009/05/13 13:20:42 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger.targets;


import java.io.OutputStream;
import java.io.Writer;
import java.security.Principal;
import org.astrogrid.io.NoCloseWriter;
import org.astrogrid.io.WriterStream;

/**
 * Where the target is a Writer (eg an http response.out)
 *
 */

public class WriterTarget implements TargetIdentifier {
   
   
   protected Writer out = null;
   
   /** @see closeIt() */
   boolean doClose = true;
   

   public WriterTarget(Writer targetOut) {
      this.out = targetOut;
   }

   /** Creates targetIndicator to wrap given writer.  Set closeIt to true to
    * tell users of the stream to close it when they've finished with it, false
    * if not
    */
   public WriterTarget(Writer targetOut, boolean closeIt) {
      this.out = targetOut;
      this.doClose = closeIt;
   }

   /** direct handle to wrapped writer */
   public Writer getWriter() {
      return out;
   }

   /** Normal writer resolver; returns a NoCloseWriter wrapper if doClose is false */
   public Writer openWriter()  {
      if (doClose) {
         return out;
      }
      else {
         return new NoCloseWriter(out);
      }
   }

   /** All targets must be able to resolve to a stream.  The user is required
    * for permissioning. */
   public OutputStream openOutputStream() {
      return new WriterStream(openWriter());
   }
   
   public String toString() {
      return out.getClass()+" TargetIndicator";
   }
   
   /** Used to set the mime type of the data about to be sent to the target. Does nothing. */
   public void setMimeType(String mimeType) {
   }
   
}
/*
 $Log: WriterTarget.java,v $
 Revision 1.1.1.1  2009/05/13 13:20:42  gtr


 Revision 1.2  2006/09/26 15:34:42  clq2
 SLI_KEA_1794 for slinger and PAL_KEA_1974 for pal and xml, deleted slinger jar from repo, merged with pal

 Revision 1.1.2.1  2006/09/11 11:40:47  kea
 Moving slinger functionality back into DSA (but preserving separate
 org.astrogrid.slinger namespace for now, for easier replacement of
 slinger functionality later).

 Revision 1.2  2005/05/27 16:21:01  clq2
 mchv_1

 Revision 1.1.20.1  2005/04/21 17:09:03  mch
 incorporated homespace etc into URLs

 Revision 1.1  2005/02/14 20:47:38  mch
 Split into API and webapp

 Revision 1.4  2005/02/14 17:53:38  mch
 Split between webnode (webapp) and library, prepare to split between API and special implementations

 Revision 1.3  2005/01/26 17:31:57  mch
 Split slinger out to scapi, swib, etc.

 Revision 1.1.2.3  2005/01/26 14:35:29  mch
 Separating slinger and scapi

 Revision 1.1.2.2  2004/12/13 15:53:39  mch
 Moved stuff to IO package and new progress monitoring streams

 Revision 1.1.2.1  2004/11/22 00:46:28  mch
 New Slinger Package


 */



