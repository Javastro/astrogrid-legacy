/*
 * $Id: UrlTarget.java,v 1.3 2005/01/26 17:31:57 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger.targets;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.security.Principal;
import org.astrogrid.slinger.SRL;

/**
 * Used to indicate the target where the results are to be sent when it's been
 * given as a URL.
 *
 */

public class UrlTarget implements SRL, TargetIdentifier {
   
   URLConnection connection;

   URL url; //as well as superclasses URI, saves making new URL each getUrl()
   
   public UrlTarget(URL targetUrl) {
      url = targetUrl;
   }

   public URL getUrl() {
     return url;
   }

   public String toURI() {
      return url.toString();
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
      return "[UrlTarget '"+url+"']";
   }

   /** Resolves writer as a wrapper around resolved outputstream */
   public Writer resolveWriter(Principal user) throws IOException {
      return new OutputStreamWriter(resolveOutputStream(user));
   }
   
   
}
/*
 $Log: UrlTarget.java,v $
 Revision 1.3  2005/01/26 17:31:57  mch
 Split slinger out to scapi, swib, etc.

 Revision 1.1.2.3  2005/01/26 14:35:29  mch
 Separating slinger and scapi

 Revision 1.1.2.2  2004/12/08 18:37:11  mch
 Introduced SPI and SPL

 Revision 1.1.2.1  2004/11/22 00:46:28  mch
 New Slinger Package

 Revision 1.1  2004/11/09 17:42:22  mch
 Fixes to tests after fixes for demos, incl adding closable to targetIndicators


 */



