/*
 * $Id: URISourceTargetMaker.java,v 1.3 2008/02/07 17:27:44 clq2 Exp $
 */
package org.astrogrid.slinger.sourcetargets;

/**
 * Makes the appropriate sourcetarget depending on the given string uri.  This
 * is only required where the URL plugin mechanism is not being used - eg in
 * tomcat
 */

import org.astrogrid.slinger.homespace.HomespaceName;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.MalformedURLException;

public class URISourceTargetMaker {
   
   public static SourceTargetIdentifier makeSourceTarget(String uri) throws URISyntaxException, MalformedURLException {
      if (HomespaceName.isMyspaceIvorn(uri)) {
         return new MyspaceSourceTarget(uri);
      }
      if (HomespaceName.isHomespaceName(uri)) {
         return new HomespaceSourceTarget(uri);
      }
      else {
         return new UrlSourceTarget(new URL(uri));
      }
   }
}

