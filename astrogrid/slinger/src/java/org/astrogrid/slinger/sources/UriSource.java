/*
 * $Id: UriSource.java,v 1.3 2005/01/26 17:31:57 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger.sources;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;

/**
 * Used to indicate the target where the results are to be sent, when that target
 * is given by some identifier (eg URL, AGSL, IVORN, emial address etc).  Subclasses
 * should handle each specific type; this should not be directly constructed.
 *
 */

public abstract class UriSource implements SourceIdentifier {

   protected URI uri = null;
   
   protected UriSource(URI givenUri) {
      this.uri = givenUri;
   }

   protected UriSource(String givenUri) throws URISyntaxException {
      this.uri = new URI(givenUri);
   }
   
   /** Returns a URI representing the target */
   public URI toURI() { return uri; }

   /** Returns an OutputStreamWrapper around the resolved stream */
   public Reader resolveReader(Principal user) throws IOException {
      return new InputStreamReader(resolveInputStream(user));
   }

   
   public String toString() {
      return "UriSource ["+uri+"]";
   }
   
   /** All URI sources are forwardable, by passing the URI */
   public boolean isForwardable()   { return true; }
   
}
/*
 $Log: UriSource.java,v $
 Revision 1.3  2005/01/26 17:31:57  mch
 Split slinger out to scapi, swib, etc.

 Revision 1.1.2.1  2004/11/22 00:46:28  mch
 New Slinger Package


 */



