/*
 * $Id: UrlSource.java,v 1.3 2005/01/26 17:31:57 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger.sources;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.security.Principal;

/**
 * A SourceIdentifier wrapper to URLs.  Identifies a source with a URL
 *
 */

public class UrlSource extends UriSource {
   
   URLConnection connection;
   
   public UrlSource(URL sourceUrl) throws URISyntaxException {
      super(sourceUrl.toString());
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

   public InputStream resolveInputStream(Principal user) throws IOException {
      return getConnection().getInputStream();
   }

   public synchronized URLConnection getConnection() throws IOException {
      if (connection == null) {
         connection = getUrl().openConnection();
      }
      return connection;
   }
   
   public String toString() {
      return "URL sourceIndicator "+uri;
   }
   
   public String getMimeType(Principal user) throws IOException {
      return getConnection().getContentType();
   }
   
}
/*
 $Log: UrlSource.java,v $
 Revision 1.3  2005/01/26 17:31:57  mch
 Split slinger out to scapi, swib, etc.

 Revision 1.1.2.2  2004/12/03 11:50:19  mch
 renamed Msrl etc to separate from storeclient ones.  Prepared for SRB

 Revision 1.1.2.1  2004/11/22 00:46:28  mch
 New Slinger Package


 */



