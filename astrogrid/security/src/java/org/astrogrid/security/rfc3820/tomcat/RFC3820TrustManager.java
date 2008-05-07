package org.astrogrid.security.rfc3820.tomcat;

import java.security.Principal;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.common.CoGProperties;
import org.globus.gsi.proxy.ProxyPathValidator;
import org.globus.gsi.proxy.ProxyPathValidatorException;

/**
 * A JSSE trust-manager that supports PKIX with RFC3820.
 * That means that it validates chains of X.509 certificates 
 * (the PKIX part) which may contain proxy certficates (the RFC3820 part).
 * This trust manager does not support CRLs.
 *
 * This implementation wraps a Globus artifact that
 * does what is necessary but doesn't fit into JSSE.
 *
 * The trust manager is given its trust anchors at construction. It doesn't
 * know anything about how the trust anchors are configured or loaded.
 *
 * @author Guy Rixon
 */
public class RFC3820TrustManager implements X509TrustManager {
  
  static Log log = LogFactory.getLog(RFC3820Implementation.class);
  
  /**
   * The validator for the certificate path.
   * This is the Globus packaging of the validation function.
   */
  private ProxyPathValidator validator;
  
  /**
   * The trust anchors aginst which to check certificate chains.
   */ 
  private X509Certificate[] anchors;
  
  /**
   * Constructs a RFC3820TrustManager.
   */
  public RFC3820TrustManager(X509Certificate[] anchors) {
    
    // Make Globus shut up about missing CA files and CRLs.
    // We don't need it to load them for us, so give it an
    // empty list of places to look for them.
    CoGProperties p = new CoGProperties();
    p.setCaCertLocations("");
    CoGProperties.setDefault(p);
    
    // This Globus artifact will do the actual validation.
    // We feed it its trust anchors for each chain validated,
    // so remember the set of anchors.
    this.validator = new ProxyPathValidator();
    this.anchors = anchors;
  }
  
  /**
   * Determines whether a client's certificate-chain is trusted.
   *
   * @param chain The chain of certificates to be checked.
   * @param authType Ignored.
   * @throws CertificateException If the client is not trusted.
   */
  public void checkClientTrusted(X509Certificate[] chain, String authType) 
      throws CertificateException {
    
    // Validate the chain with the Globus artifact which knows PKIX and RFC3820.
    // The validate(X509Certificate[], X509Certificate[]) method is the
    // variant that does all the validation; c.f. the other variants of 
    // validate in this class. The Globus artifact throws a
    // Globus exception: we have to change this to a JSSE exception to
    // satify our interface. The reset() call is essential. If this is missed
    // out then getIdentityCertificate() returns the first identity certificate
    // to be validated after construction and doesn't change thereafter.
    try {
      this.validator.reset();
      this.validator.validate(chain, anchors);
      Principal identity = this.validator.getIdentityCertificate().getSubjectDN();
      log.info(identity + " is authenticated by TLS.");
    } 
    catch (ProxyPathValidatorException ex) {
      String message = "This party's certificate-chain is not trusted: " + ex;
      log.info(message);
      throw new CertificateException(message);
    }
    
  }
  
  /**
   * Determines whether a server's certificate-chain is trusted.
   * No servers are trusted as this trust-manager is not for use in clients.
   *
   * @param chain The chain of certificates to be checked.
   * @param authType Ignored.
   * @throws CertificateException If the server is not trusted.
   */
  public void checkServerTrusted(X509Certificate[] chain, String authType) 
      throws CertificateException {
    throw new CertificateException("This trust manager cannot authenticate services.");
  }
  
  /**
   * Lists the certificate authorities recognized by this trust manager.
   * These are determined by the trust anchors passed to the constructor.
   */
  public X509Certificate[] getAcceptedIssuers() {
    return this.anchors;
  }
  
}
