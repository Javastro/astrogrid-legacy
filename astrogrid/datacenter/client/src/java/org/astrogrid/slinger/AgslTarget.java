/*
 * $Id: AgslTarget.java,v 1.3 2004/11/03 00:17:56 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import org.astrogrid.community.Account;
import org.astrogrid.store.Agsl;

/**
 * Used to indicate the target where the results are to be sent when it's been
 * given as an AGSL.
 *
 */

public class AgslTarget extends UriTarget {

   public AgslTarget(Agsl targetAgsl)  {
      super(targetAgsl.toUri());
   }

   public Agsl getAgsl() {
      try {
         return new Agsl(uri.toString());
      }
      catch (MalformedURLException e) {
         //since this class only allows AGSLs to be set, this shouldn't happen...
         throw new RuntimeException("Application error: "+toString()+" is not an AGSL");
      }
   }
   
   public OutputStream resolveStream(Account user) throws IOException {
     return getAgsl().openOutputStream(user.toUser());
   }

   
   public String toString() {
      return "Agsl TargetIndicator "+uri;
   }
   
}
/*
 $Log: AgslTarget.java,v $
 Revision 1.3  2004/11/03 00:17:56  mch
 PAL_MCH Candidate 2 merge

 Revision 1.2.8.1  2004/11/01 20:47:23  mch
 Added a little bit of doc and introduced MsrlTarget/UrlTargets

 Revision 1.2  2004/10/12 17:41:41  mch
 added isForwardable

 Revision 1.1  2004/10/06 21:12:17  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger


 */



