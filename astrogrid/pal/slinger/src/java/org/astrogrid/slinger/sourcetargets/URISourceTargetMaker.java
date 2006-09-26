/*
 * $Id: URISourceTargetMaker.java,v 1.2 2006/09/26 15:34:42 clq2 Exp $
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
      if (HomespaceName.isHomespaceName(uri)) {
         return new HomespaceSourceTarget(uri);
      }
      else {
         return new UrlSourceTarget(new URL(uri));
      }
   }
}

