/*
 * $Id: VOSpaceSourceTarget.java,v 1.3 2009/05/22 17:52:43 gtr Exp $ 
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger.sourcetargets;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.cert.X509Certificate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.security.SecurityGuard;
import org.astrogrid.vospace.v11.client.system.SystemDelegate;
import org.astrogrid.vospace.v11.client.system.SystemDelegateResolver;
import org.astrogrid.vospace.v11.client.system.SystemDelegateResolverImpl;

/**
 * A SourceTarget implementation to handle connections with VOSpace services.
 */

public class VOSpaceSourceTarget implements SourceTargetIdentifier {
  
   private static Log log = LogFactory.getLog(VOSpaceSourceTarget.class);

   private URI nameUri;
   private SystemDelegate delegate;

  /**
   * Constructs a VOSpaceSourceTarget with a given identity for the user.
   * The constructed object is permanently associated with a given
   * storage-location. When the storage location is in a secured VOSpace,
   * credentials for the given is the target, credentials must previously
   * have been delegated for the given identity.
   *
   * @param name The URI for remote storage-location.
   * @param guard The identity and credentials of the caller.
   * @throws URISyntaxException If parameter "name" is not a valid URI.
   */
  public VOSpaceSourceTarget(URI           name,
                             SecurityGuard guard) {

    this.nameUri = name;

		log.debug("X500Principal = " + guard.getX500Principal());
	  log.debug("Private key = " + guard.getPrivateKey());
		log.debug("Certificate chain = " + guard.getCertificateChain());
		X509Certificate[] chain = guard.getCertificateChain();
	  for (int i = 0; i < chain.length; i++) {
			X509Certificate c = chain[i];
			log.debug(c.getSubjectDN() + " issued by " + c.getIssuerDN());
		}
    
    SystemDelegateResolver resolver = new SystemDelegateResolverImpl();
		if (guard.isSignedOn()) {
      delegate = resolver.resolve(new CredentialResolver(guard));
		}
		else {
			delegate = resolver.resolve();
		}
  }

   public URI toURI() {
      return nameUri;
   }
   
   /** opens output stream to the URL */
   public OutputStream openOutputStream() throws IOException {
      try {
        return delegate.write(nameUri);
        }
      catch (Exception e) {
// JDK 1.6 only
//      throw new IOException("Failed to open VOSpace connection to destination "+nameUri.toString() + ": " + e.getMessage(), e);
        throw new IOException("Failed to open VOSpace connection to destination "+nameUri.toString() + ": " + e.getMessage());
      }
   }

   /** Used to set the mime type of the data about to be sent to the target. . */
   public void setMimeType(String mimeType) throws IOException {
		//throw new IOException("NOT IMPLEMENTED YET!");
      //getConnection().setRequestProperty ("Content-Type", mimeType);
   }

  @Override
  public String toString() {
    return "[VOS resource: '"+nameUri.toString()+"']";
  }

   /** Resolves writer as a wrapper around resolved outputstream */
   public Writer openWriter() throws IOException {
      return new OutputStreamWriter(openOutputStream());
   }
   
   public InputStream openInputStream() throws IOException {
      try {
        return delegate.read(nameUri);
        }
      catch (Exception e) {
// JDK 1.6 only
//      throw new IOException("Failed to open VOSpace connection to source "+nameUri.toString() + ": " + e.getMessage(), e);
        throw new IOException("Failed to open VOSpace connection to source "+nameUri.toString() + ": " + e.getMessage());
      }
   }

   public String getMimeType() throws IOException {
		throw new IOException("NOT IMPLEMENTED YET!");
      //return getConnection().getContentType();
   }
   
   /** Returns an OutputStreamWrapper around the resolved stream */
   public Reader openReader() throws IOException {
      return new InputStreamReader(openInputStream());
   }
}
