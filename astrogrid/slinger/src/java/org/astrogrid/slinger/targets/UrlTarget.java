/*
 * $Id: UrlTarget.java,v 1.2 2004/12/07 01:33:36 jdt Exp $
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
import java.security.Principal;

/**
 * Used to indicate the target where the results are to be sent when it's been
 * given as a URL.
 *
 */

public class UrlTarget extends UriTarget {
   
   URLConnection connection;

   URL url; //as well as superclasses URI, saves making new URL each getUrl()
   
   public UrlTarget(URL targetUrl) throws URISyntaxException {
      super(targetUrl.toString());
      url = targetUrl;
   }

   public URL getUrl() {
     return url;
   }

   /** opens output stream to the URL */
   public OutputStream resolveOutputStream(Principal user) throws IOException {
      return getConnection().getOutputStream();
   }

   /** Used to set the mime type of the data about to be sent to the target. . */
   public void setMimeType(String mimeType, Principal user) throws IOException {
      getConnection().setRequestProperty ("Content-Type", mimeType);
   }

   /** lazy connection creation, so it's only created when we start writing to it */
   public synchronized URLConnection getConnection() throws IOException {
      if (connection == null) {
         connection = getUrl().openConnection();
      }
      return connection;
   }
   
   public String toString() {
      return "[UrlTarget '"+uri+"']";
   }
   
}
/*
 $Log: UrlTarget.java,v $
 Revision 1.2  2004/12/07 01:33:36  jdt
 Merge from PAL_Itn07

 Revision 1.1.2.1  2004/11/22 00:46:28  mch
 New Slinger Package

 Revision 1.1.6.1  2004/11/17 11:15:46  mch
 Changes for serving images

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



