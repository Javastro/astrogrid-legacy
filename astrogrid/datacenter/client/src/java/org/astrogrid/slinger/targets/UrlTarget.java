/*
 * $Id: UrlTarget.java,v 1.1 2004/11/09 17:42:22 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger.targets;


import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import org.astrogrid.community.Account;
import org.astrogrid.store.Agsl;

/**
 * Used to indicate the target where the results are to be sent when it's been
 * given as a URL.
 *
 */

public class UrlTarget extends UriTarget {

   public UrlTarget(URL targetUrl) throws URISyntaxException {
      super(targetUrl.toString());
   }

   public URL getUrl() {
      try {
         return new URL(uri.toString());
      }
      catch (MalformedURLException e) {
         //since this class only allows AGSLs to be set, this shouldn't happen...
         throw new RuntimeException("Application error: "+uri.toString()+" is not a URL");
      }
   }

   /** Returns AGSL form */
   public Agsl getAgsl() {
      return new Agsl(getUrl());
   }
   
   public OutputStream resolveStream(Account user) throws IOException {
      URLConnection connection = getUrl().openConnection();
      return connection.getOutputStream();
   }

   
   public String toString() {
      return "URL TargetIndicator "+uri;
   }
   
}
/*
 $Log: UrlTarget.java,v $
 Revision 1.1  2004/11/09 17:42:22  mch
 Fixes to tests after fixes for demos, incl adding closable to targetIndicators

 Revision 1.2  2004/11/03 00:17:56  mch
 PAL_MCH Candidate 2 merge

 Revision 1.1.2.2  2004/11/02 21:51:54  mch
 Replaced AgslTarget with UrlTarget and MySpaceTarget

 Revision 1.1.2.1  2004/11/01 20:47:23  mch
 Added a little bit of doc and introduced MsrlTarget/UrlTargets

 Revision 1.2  2004/10/12 17:41:41  mch
 added isForwardable

 Revision 1.1  2004/10/06 21:12:17  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger


 */



