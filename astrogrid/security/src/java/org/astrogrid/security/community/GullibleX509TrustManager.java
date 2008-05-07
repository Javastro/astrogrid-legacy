package org.astrogrid.security.community;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

/**
 * A trust manager that trusts all peers identified by X509 certificates.
 * This manager is appropriate when the client needs to encrypt a connection
 * but does not need to authenticate the peer.
 *
 * @author Guy Rixon
 */
public class GullibleX509TrustManager implements X509TrustManager {
  
  /**
   * Determines whether a client is trusted.
   * All such peers are trusted.
   */
  public void checkClientTrusted(X509Certificate[] x509Certificate, 
                                String string) throws CertificateException {
  }

  /**
   * Determines whether a server is trusted.
   * All such peers are trusted.
   */
  public void checkServerTrusted(X509Certificate[] x509Certificate, 
                                 String string) throws CertificateException {
  }

  /**
   * Lists the trust anchors.
   * Since this manager doesn't use trust anchors, returns null.
   */
  public X509Certificate[] getAcceptedIssuers() {
    return null;
  }
  
}
