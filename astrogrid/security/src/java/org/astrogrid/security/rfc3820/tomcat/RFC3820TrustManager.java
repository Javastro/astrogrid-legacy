package org.astrogrid.security.rfc3820.tomcat;

import java.security.GeneralSecurityException;
import java.security.Principal;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.security.rfc3820.CertificateChainValidator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * A JSSE trust-manager that supports PKIX with RFC3820.
 * That means that it validates chains of X.509 certificates 
 * (the PKIX part) which may contain proxy certficates (the RFC3820 part).
 * This trust manager does not support CRLs.
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
  private CertificateChainValidator validator;
  
  /**
   * The trust anchors aginst which to check certificate chains.
   */ 
  private X509Certificate[] anchors;
  
  /**
   * Constructs a RFC3820TrustManager.
   */
  public RFC3820TrustManager(X509Certificate[] anchors) {
    try {
      if (Security.getProvider("BC") == null) {
        Security.addProvider(new BouncyCastleProvider());
      }
      this.validator = new CertificateChainValidator(anchors);
    } catch (GeneralSecurityException ex) {
      throw new RuntimeException(ex);
    }
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
      this.validator.validate(chain);
      log.info(getIdentity(chain) + " is authenticated by TLS.");
    } 
    catch (Exception ex) {
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
  
  /**
   * Determines the identity in a certificate chain that may contain proxies.
   */
  private String getIdentity(X509Certificate[] chain) {
    final String ietfProxyCertInfoOid = "1.3.6.1.5.5.7.1.14";
    final String globusProxyCertInfoOid = "1.3.6.1.4.1.3536.1.222";
    for (int i = 0; i < chain.length; i++) {
      if (chain[i].getExtensionValue(ietfProxyCertInfoOid) == null &&
          chain[i].getExtensionValue(globusProxyCertInfoOid) == null){
        return chain[i].getSubjectX500Principal().getName();
      }
    }
    return null;
  }
  
}
