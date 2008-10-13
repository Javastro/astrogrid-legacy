/*
 * $Id: URISourceTargetMaker.java,v 1.4 2008/10/13 10:51:35 clq2 Exp $
 */
package org.astrogrid.slinger.sourcetargets;

/**
 * Makes the appropriate sourcetarget depending on the given string uri.  This
 * is only required where the URL plugin mechanism is not being used - eg in
 * tomcat
 */

import java.security.Principal;
import org.astrogrid.io.account.LoginAccount;
import org.astrogrid.slinger.homespace.HomespaceName;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.MalformedURLException;

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
     return makeSourceTarget(uri, LoginAccount.ANONYMOUS);
   }
   
   /**
    * Creates a SourceTargetIdentifier for a given location and an
    * identified user. 
    * <p>
    * This method will work with secured VOSpace under two
    * conditions: the user has previously delegated credentials to the service
    * calling this method; the given identity is an X500Principal and matches
    * the identity under which the credentials were delegated. It is a bad
    * breach of security to pass an identity that has not been authenticated.
    *
    * @param uri The URI for the location.
    * @param user The authenticated identity of the user.
    * @return The target identifier.
    * @throws URISyntaxException If the location is no good.
    * @throws MalformedURLException If the location is no good.
    */
   public static SourceTargetIdentifier makeSourceTarget(String uri,
                                                         Principal user) throws URISyntaxException, 
                                                                                MalformedURLException {
   
      if (uri.startsWith("vos://")) {
         // Assume this is a VOSpace URI
         return new VOSpaceSourceTarget(uri, user);
      }
		
      // The homespace stuff below should become redundant when VOSpace
      // fully replaces AstroGrid MySpace (FileManager/FileStore)
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

