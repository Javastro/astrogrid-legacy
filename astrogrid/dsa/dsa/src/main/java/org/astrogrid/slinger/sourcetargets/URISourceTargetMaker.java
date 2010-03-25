/*
 * $Id: URISourceTargetMaker.java,v 1.3 2010/03/25 10:25:51 gtr Exp $
 */
package org.astrogrid.slinger.sourcetargets;

/**
 * Makes the appropriate sourcetarget depending on the given string uri.  This
 * is only required where the URL plugin mechanism is not being used - eg in
 * tomcat
 */

import java.net.URISyntaxException;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.URI;
import org.astrogrid.security.SecurityGuard;

public class URISourceTargetMaker {
  
   /**
    * Creates a SourceTargetIdentifier for a given location and an
    * anonymous user. This method will not work with secured VOSpace.
    *
    * @param uri The URI for the location.
    * @return The target identifier.
    * @throws URISyntaxException If the location is no good.
    * @throws MalformedURLException If the location is no good.
    */
   public static SourceTargetIdentifier makeSourceTarget(String uri) throws URISyntaxException, 
                                                                            MalformedURLException {
     return makeSourceTarget(uri, new SecurityGuard());
   }

  /**
   * Creates a SourceTargetIdentifier for a given location and identity. If
   * an security-guard object empty of credentials and principals is passed,
   * then the request is annonymous.
   * <p>
   * This method will work with secured VOSpace if the identity managed by the
   * security-guard object is capable of signing messages to the space. This
   * implies that the user has previously delegated credentials to the service
   * calling this method.
   *
   * @param uri The URI for the location.
   * @param user The authenticated identity of the user.
   * @return The target identifier.
   * @throws URISyntaxException If the location is no good.
   * @throws MalformedURLException If the location is no good.
   */

  public static SourceTargetIdentifier makeSourceTarget(String        location,
                                                        SecurityGuard guard) throws URISyntaxException,
                                                                                    MalformedURLException {
    URI u = new URI(location);

    // IVOA VOSpace.
    if (u.getScheme().equals("vos")) {
      return new VOSpaceSourceTarget(u, guard);
    }
    
    // AstroGrid MySpace.
    else if (u.getScheme().equals("ivo")) {
      return new MyspaceSourceTarget(location);
    }

    // Somethign like HTTP or FTP.
    else {
      return new UrlSourceTarget(new URL(location));
    }
  }

}

