/*
 * $Id: MultiTargets.java,v 1.4 2005/02/14 17:53:38 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger.targets;


import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.security.Principal;
import org.astrogrid.io.MultiCastOutputStream;

/**
 * A set of targets that are simultaneously written to
 *
 */

public class MultiTargets implements TargetIdentifier  {

   TargetIdentifier[] targets = null;

   public MultiTargets(TargetIdentifier[] someTargets) {
      targets =someTargets;
   }

   /** Returns an OutputStreamWrapper around the resolved stream */
   public Writer resolveWriter(Principal user) throws IOException {
      return new OutputStreamWriter(resolveOutputStream(user));
   }

   public OutputStream resolveOutputStream(Principal user) throws IOException {

      OutputStream[] outs = new OutputStream[targets.length];
      for (int i = 0; i < targets.length; i++) {
         outs[i] = targets[i].resolveOutputStream(user);
      }
      return new MultiCastOutputStream(outs);
   }
   
   
   public String toString() {
      String s = "[MultiTargets";
      for (int i = 0; i < targets.length; i++) {
         s = s + " "+targets[i].toString();
      }
      return s+"]";
   }
   
   /** Used to set the mime type of the data about to be sent to the target. Does nothing. */
   public void setMimeType(String mimeType, Principal user) throws IOException {
      for (int i = 0; i < targets.length; i++) {
         targets[i].setMimeType(mimeType, user);
      }
   }
}
/*
 $Log: MultiTargets.java,v $
 Revision 1.4  2005/02/14 17:53:38  mch
 Split between webnode (webapp) and library, prepare to split between API and special implementations

 Revision 1.3  2005/01/26 17:31:57  mch
 Split slinger out to scapi, swib, etc.

 Revision 1.1.2.3  2005/01/26 14:35:27  mch
 Separating slinger and scapi


 Revision 1.1.2.1  2004/11/22 00:46:28  mch
 New Slinger Package


 */



