/*
 * $Id: StreamTarget.java,v 1.3 2005/01/26 17:31:57 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger.targets;



import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.security.Principal;

/**
 * Used to indicate the target where the results are to be sent when that target
 * is a given stream.
 *
 */

public class StreamTarget implements TargetIdentifier  {

   protected OutputStream out = null;

   public StreamTarget(OutputStream targetOut) {
      this.out = targetOut;
   }

   /** Returns an OutputStreamWrapper around the resolved stream */
   public Writer resolveWriter(Principal user)  {
      return new OutputStreamWriter(resolveOutputStream(user));
   }

   public OutputStream resolveOutputStream(Principal user) {
      return out;
   }
   
   public String toString() {
       return out.getClass()+" TargetIndicator ";
   }
   
   
   /** Used to set the mime type of the data about to be sent to the target. Does nothing. */
   public void setMimeType(String mimeType, Principal user) {
   }
}
/*
 $Log: StreamTarget.java,v $
 Revision 1.3  2005/01/26 17:31:57  mch
 Split slinger out to scapi, swib, etc.

 Revision 1.1.2.2  2005/01/26 14:35:28  mch
 Separating slinger and scapi

 Revision 1.1.2.1  2004/11/22 00:46:28  mch
 New Slinger Package

 Revision 1.1  2004/11/09 17:42:22  mch
 Fixes to tests after fixes for demos, incl adding closable to targetIndicators

 Revision 1.2.8.2  2004/11/02 19:41:26  mch
 Split TargetIndicator to indicator and maker

 Revision 1.2.8.1  2004/11/01 20:47:23  mch
 Added a little bit of doc and introduced MsrlTarget/UrlTargets

 Revision 1.2  2004/10/12 17:41:41  mch
 added isForwardable

 Revision 1.1  2004/03/14 04:13:04  mch
 Wrapped output target in TargetIndicator

 */



