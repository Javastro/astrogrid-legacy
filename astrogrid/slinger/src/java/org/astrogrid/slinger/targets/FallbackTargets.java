/*
 * $Id: FallbackTargets.java,v 1.3 2005/01/26 17:31:57 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger.targets;



import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.security.Principal;

/**
 * A set of targets that are tried one after the other in the case of failure
 *
 */

public class FallbackTargets implements TargetIdentifier  {

   TargetIdentifier[] targets = null;

   /** This one marks the currently used target */
   TargetIdentifier validTarget = null;
   
   public FallbackTargets(TargetIdentifier[] someTargets) {
      targets =someTargets;
   }

   /** Returns an OutputStreamWrapper around the resolved stream */
   public Writer resolveWriter(Principal user) throws IOException {
      return new OutputStreamWriter(resolveOutputStream(user));
   }

   public OutputStream resolveOutputStream(Principal user) throws IOException {
      if (validTarget != null) {
         try {
            return validTarget.resolveOutputStream(user);
         } catch (IOException ioe) {
            //didn't work - try again from the beginning
            validTarget = null;
            return resolveOutputStream(user);
         }
      }
      else {
         //find which one is OK to connect to
         int t = 0;
         OutputStream out = null;
         String errors = "";
         while ((t<targets.length) && (out == null)) {
            try {
               out = targets[t].resolveOutputStream(user);
            }
            catch (IOException ioe) {
               errors = errors + ioe.getMessage()+" connecting to "+targets[t]+", ";
            }
         }
         if (out != null) {
            return null;
         }
         throw new IOException(errors);
      }
   }
   
   public String toString() {
      String s = "[FallbackTargets";
      for (int i = 0; i < targets.length; i++) {
         s = s + " "+targets[i].toString();
      }
      return s+"]";
   }
   
   /** Cannot be forwarded to remote services (at the moment) */
   public boolean isForwardable() { return false; }
   
   /** Used to set the mime type of the data about to be sent to the target. Does nothing. */
   public void setMimeType(String mimeType, Principal user) throws IOException {
      if (validTarget != null) {
         validTarget.setMimeType(mimeType, user);
      }
   }
}
/*
 $Log: FallbackTargets.java,v $
 Revision 1.3  2005/01/26 17:31:57  mch
 Split slinger out to scapi, swib, etc.

 Revision 1.1.2.2  2005/01/26 14:35:27  mch
 Separating slinger and scapi

 Revision 1.1.2.1  2004/11/22 00:46:28  mch
 New Slinger Package


 */



