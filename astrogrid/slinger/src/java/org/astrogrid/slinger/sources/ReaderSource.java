/*
 * $Id: ReaderSource.java,v 1.3 2005/01/26 17:31:57 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger.sources;


import java.io.InputStream;
import java.io.Reader;
import java.security.Principal;

/**
 * Where the source is a Reader (eg an http request in)
 *
 */

public class ReaderSource implements SourceIdentifier {
   
   protected Reader in = null;
   
   public ReaderSource(Reader sourceIn) {
      this.in = sourceIn;
   }

   public Reader getReader() {
      return in;
   }

   public Reader resolveReader(Principal user)  {
      return in;
   }

   
   /** All targets must be able to resolve to a stream.  The user is required
    * for permissioning. */
   public InputStream resolveInputStream(Principal user) {
      throw new UnsupportedOperationException("todo");
   }
   
   public String toString() {
      return in.getClass()+" TargetIndicator";
   }
   
   /** Cannot be forwarded to remote services */
   public boolean isForwardable() { return false; }
   
   /** Used to set the mime type of the data about to be sent to the target. Does nothing. */
   public String getMimeType(Principal user) {
      return null;
   }
   
}
/*
 $Log: ReaderSource.java,v $
 Revision 1.3  2005/01/26 17:31:57  mch
 Split slinger out to scapi, swib, etc.

 Revision 1.1.2.3  2005/01/26 14:35:25  mch
 Separating slinger and scapi


 Revision 1.1.2.1  2004/11/22 00:46:28  mch
 New Slinger Package


 */



