/*
 * $Id: WriterTarget.java,v 1.2 2004/12/07 01:33:36 jdt Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger.targets;


import java.io.OutputStream;
import java.io.Writer;
import java.security.Principal;
import org.astrogrid.slinger.targets.out.NoCloseWriter;
import org.astrogrid.slinger.targets.out.WriterStream;

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
 Revision 1.2  2004/12/07 01:33:36  jdt
 Merge from PAL_Itn07

 Revision 1.1.2.1  2004/11/22 00:46:28  mch
 New Slinger Package

 Revision 1.2.6.1  2004/11/17 11:15:46  mch
 Changes for serving images

 Revision 1.2  2004/11/10 22:01:50  mch
 skynode starts and some fixes

 Revision 1.1  2004/11/09 17:42:22  mch
 Fixes to tests after fixes for demos, incl adding closable to targetIndicators

 Revision 1.3  2004/11/03 00:17:56  mch
 PAL_MCH Candidate 2 merge

 Revision 1.2.8.2  2004/11/02 19:41:26  mch
 Split TargetIndicator to indicator and maker

 Revision 1.2.8.1  2004/11/01 20:47:23  mch
 Added a little bit of doc and introduced MsrlTarget/UrlTargets

 Revision 1.2  2004/10/12 17:41:41  mch
 added isForwardable

 Revision 1.1  2004/10/06 21:12:17  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

 Revision 1.5  2004/10/05 14:55:00  mch
 Added factory methods

 Revision 1.4  2004/09/07 01:39:27  mch
 Moved email keys from TargetIndicator to Slinger

 Revision 1.3  2004/09/07 01:01:29  mch
 Moved testConnection to server slinger

 Revision 1.2  2004/09/07 00:54:20  mch
 Tidied up Querier/Plugin/Results, and removed deprecated SPI-visitor-SQL-translator

 Revision 1.1  2004/08/25 23:38:33  mch
 (Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

 Revision 1.3  2004/08/19 08:35:54  mch
 Fix to email constructor

 Revision 1.2  2004/08/18 22:27:57  mch
 Better error checking

 Revision 1.1  2004/08/17 20:19:36  mch
 Moved TargetIndicator to client

 Revision 1.5  2004/07/15 17:07:23  mch
 Added factory method to make from a string

 Revision 1.4  2004/03/15 19:16:12  mch
 Lots of fixes to status updates

 Revision 1.3  2004/03/15 17:08:11  mch
 Added compression adn format placeholders

 Revision 1.2  2004/03/14 16:55:48  mch
 Added XSLT ADQL->SQL support

 Revision 1.1  2004/03/14 04:13:04  mch
 Wrapped output target in TargetIndicator

 */



