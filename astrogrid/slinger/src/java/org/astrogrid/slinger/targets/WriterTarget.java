/*
 * $Id: WriterTarget.java,v 1.3 2005/01/26 17:31:57 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger.targets;


import java.io.OutputStream;
import java.io.Writer;
import java.security.Principal;
import org.astrogrid.slinger.io.NoCloseWriter;
import org.astrogrid.slinger.io.WriterStream;

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
   public Writer resolveWriter(Principal user)  {
      if (doClose) {
         return out;
      }
      else {
         return new NoCloseWriter(out);
      }
   }

   /** All targets must be able to resolve to a stream.  The user is required
    * for permissioning. */
   public OutputStream resolveOutputStream(Principal user) {
      return new WriterStream(resolveWriter(user));
   }
   
   public String toString() {
      return out.getClass()+" TargetIndicator";
   }
   
   /** Used to set the mime type of the data about to be sent to the target. Does nothing. */
   public void setMimeType(String mimeType, Principal user) {
   }
   
}
/*
 $Log: WriterTarget.java,v $
 Revision 1.3  2005/01/26 17:31:57  mch
 Split slinger out to scapi, swib, etc.

 Revision 1.1.2.3  2005/01/26 14:35:29  mch
 Separating slinger and scapi

 Revision 1.1.2.2  2004/12/13 15:53:39  mch
 Moved stuff to IO package and new progress monitoring streams

 Revision 1.1.2.1  2004/11/22 00:46:28  mch
 New Slinger Package


 */



