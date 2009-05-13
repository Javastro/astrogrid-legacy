/*
 * $Id: ReaderSource.java,v 1.1 2009/05/13 13:20:41 gtr Exp $
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

   public Reader openReader()  {
      return in;
   }

   
   /** All targets must be able to resolve to a stream.  The user is required
    * for permissioning. */
   public InputStream openInputStream() {
      throw new UnsupportedOperationException("todo");
   }
   
   public String toString() {
      return in.getClass()+" Source";
   }
   
   /** Cannot be forwarded to remote services */
   public boolean isForwardable() { return false; }
   
   /** Used to set the mime type of the data about to be sent to the target. Does nothing. */
   public String getMimeType() {
      return null;
   }
   
}
/*
 $Log: ReaderSource.java,v $
 Revision 1.1  2009/05/13 13:20:41  gtr
 *** empty log message ***

 Revision 1.2  2006/09/26 15:34:42  clq2
 SLI_KEA_1794 for slinger and PAL_KEA_1974 for pal and xml, deleted slinger jar from repo, merged with pal

 Revision 1.1.2.1  2006/09/11 11:40:46  kea
 Moving slinger functionality back into DSA (but preserving separate
 org.astrogrid.slinger namespace for now, for easier replacement of
 slinger functionality later).

 Revision 1.3  2005/05/27 16:21:02  clq2
 mchv_1

 Revision 1.2.10.1  2005/04/21 17:09:03  mch
 incorporated homespace etc into URLs

 Revision 1.2  2005/03/23 15:29:42  mch
 added command-line Slinger, rationalised copy, send, get etc

 Revision 1.1  2005/02/14 20:47:38  mch
 Split into API and webapp

 Revision 1.3  2005/01/26 17:31:57  mch
 Split slinger out to scapi, swib, etc.

 Revision 1.1.2.3  2005/01/26 14:35:25  mch
 Separating slinger and scapi


 Revision 1.1.2.1  2004/11/22 00:46:28  mch
 New Slinger Package


 */



