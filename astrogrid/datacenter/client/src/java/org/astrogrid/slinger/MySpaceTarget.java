/*
 * $Id: MySpaceTarget.java,v 1.2 2004/11/03 00:17:56 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import org.astrogrid.community.Account;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.Msrl;
import java.net.URISyntaxException;

/**
 * Used to indicate the target where the results are to be sent when it's been
 * given as a MySpace Resource Locator (see MSRL)
 *
 */

public class MySpaceTarget extends UriTarget {

   public MySpaceTarget(Msrl targetMsrl) throws URISyntaxException  {
      super(targetMsrl.toString());
   }

   public Msrl getMsrl() {
      try {
         return new Msrl(uri.toString());
      }
      catch (MalformedURLException e) {
         //since this class only allows AGSLs to be set, this shouldn't happen...
         throw new RuntimeException("Application error: "+toString()+" is not an MySpace Resource Location");
      }
   }

   /** Returns AGSL form */
   public Agsl getAgsl() {
      return new Agsl(getMsrl());
   }
   
   public OutputStream resolveStream(Account user) throws IOException {
     return getMsrl().openOutputStream();
   }

   
   public String toString() {
      return "Msrl TargetIndicator "+uri;
   }
   
}
/*
 $Log: MySpaceTarget.java,v $
 Revision 1.2  2004/11/03 00:17:56  mch
 PAL_MCH Candidate 2 merge

 Revision 1.1.2.2  2004/11/02 21:51:54  mch
 Replaced AgslTarget with UrlTarget and MySpaceTarget

 Revision 1.1.2.1  2004/11/01 20:47:23  mch
 Added a little bit of doc and introduced MsrlTarget/UrlTargets



 */



