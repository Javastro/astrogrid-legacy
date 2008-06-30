package org.astrogrid.security.rfc3820;

import java.security.AccessControlException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.CertPath;
import java.security.cert.CertificateFactory;
import java.security.cert.PKIXParameters;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import javax.security.auth.x500.X500Principal;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.i18n.ErrorBundle;
import org.bouncycastle.x509.CertPathReviewerException;
import org.globus.gsi.TrustedCertificates;

/**
 * A validator for certificate chains formed according to RFC3820.
 * These chains are the kind that can include proxy certificates.
 *
 * Most of the checking is done in the superclass,
 * org.globus.gsi.proxy.ProxyPathValidator; see that class for details.
 * This current class adds checks on the signatures of the certificates.
 *
 * @author Guy Rixon
 */
public class CertificateChainValidator {
  
  static private Log log = LogFactory.getLog(CertificateChainValidator.class);
  
  /**
   * The parameters for PKIX validation of a path.
   * These define the trust anchors used in a validation.
   */
  private PKIXParameters pkixParameters;

  /**
   * Constructs a new CertificateChainValidator with no trust anchors.
   */
  public CertificateChainValidator() {
    this.pkixParameters = null;
  }
  
  /**
   * Constructs a new CertificateChainValidator with give trust anchors.
   *
   * @param anchors The trust anchors to use for subsequent validations.
   */
  public CertificateChainValidator(List<X509Certificate> anchors) 
      throws GeneralSecurityException {
    loadTrustAnchors(anchors);
  }
  
  /**
   * Constructs a new CertificateChainValidator with give trust anchors.
   *
   * @param anchors The trust anchors to use for subsequent validations.
   */
  public CertificateChainValidator(X509Certificate[] anchors) 
      throws GeneralSecurityException {
    loadTrustAnchors(anchors);
  }
  
  /**
   * Constructs a new CertificateChainValidator with give trust anchors.
   *
   * @param anchors The store holding trust anchors to use for subsequent validations.
   */
  public CertificateChainValidator(KeyStore anchors) 
      throws GeneralSecurityException {
    loadTrustAnchors(anchors);
  }

  /**
   * Validates a certificate chain conforming to RFC3820. 
   *
   * @param chain The chain to be checked.
   * @throws AccessControlException If the chain is invalid.
   * @throws GeneralSecurityException If the validation environment is broken.
   */
  public void validate(X509Certificate[] chain) 
     throws GeneralSecurityException {
    validate(Arrays.asList(chain));
  }
  
  /**
   * Validates a certificate chain conforming to RFC3820. 
   *
   * @param chain The chain to be checked.
   * @throws AccessControlException If the chain is invalid.
   * @throws GeneralSecurityException If the validation environment is broken.
   * @deprecated Use validate(X509Certificate[]) instead.
   */
  public void validateChain(X509Certificate[] chain) 
     throws GeneralSecurityException {
    validate(chain);
  }

  /**
   * Validates a certificate chain conforming to RFC3820. 
   *
   * @param chain The chain to be checked.
   * @param trustAnchors The set of trusted certificates.
   * @throws AccessControlException If the chain is invalid.
   * @throws GeneralSecurityException If the validation environment is broken.
   * @deprecated Use one of the one-argument validate methods instead.
   */
  public void validateChain(X509Certificate[] chain,
                            TrustedCertificates trustAnchors)
      throws GeneralSecurityException {
    loadTrustAnchors(trustAnchors.getCertificates());
    validate(Arrays.asList(chain));
  }
  
  /**
   * Validates a certficate chain.
   *
   * @param chain The chain to be checked.
   * @param trustAnchors The set of trusted certificates.
   * @throws AccesControlException If the chain is invalid.
   * @throws GeneralSecurityException If the validation environment is broken.
   * @deprecated Use one of the one-argument validate methods instead.
   */
  public void validate(List<X509Certificate> chain,
                       X509Certificate[]     trustAnchors)
      throws GeneralSecurityException {
    loadTrustAnchors(trustAnchors);
    validate(chain);
  }
  
  /**
   * Validates a certificate chain presented as a CertPath.
   * The previously-loaded trust anchors are used.
   *
   * @param chain The chain to be checked.
   * @throws AccessControlException If the chain is invalid.
   * @throws GeneralSecurityException If the validation environment is broken.
   */
  public void validate(CertPath chain) throws GeneralSecurityException {
    try {
      ProxyCertPathReviewer r = 
          new ProxyCertPathReviewer(chain, this.pkixParameters);
      if (!r.isValidCertPath()) {
        log.info("A certificate chain was rejected.");
        for (int i = 0; i < chain.getCertificates().size(); i++) {
          log.info("Errors in certificate " + i + ":");
          for (Object o : r.getErrors(i)) {
            log.info(o);
          }
        }
        throw new AccessControlException("The certificate chain is invalid.");
      }
    } catch (CertPathReviewerException ex) {
      throw new GeneralSecurityException(
          "Failed to set up validation for certificates", ex
      );
    }

  }
  
  /**
   * Validates a certificate chain presented as a list of certificates.
   * The previously-loaded trust anchors are used.
   *
   * @param chain The chain to be checked.
   * @throws AccessControlException If the chain is invalid.
   * @throws GeneralSecurityException If the validation environment is broken.
   */
  public void validate(List<X509Certificate> givenChain) throws GeneralSecurityException {
    CertPath chain = 
        CertificateFactory.getInstance("X509").generateCertPath(givenChain);
    try {
      ProxyCertPathReviewer r = 
          new ProxyCertPathReviewer(chain, this.pkixParameters);
      if (!r.isValidCertPath()) {
        log.info("A certificate chain was rejected.");
        for (int i = 0; i < chain.getCertificates().size(); i++) {
          log.info("Errors in certificate " + i + ":");
          for (Object o : r.getErrors(i)) {
            ErrorBundle e = (ErrorBundle) o;
            log.info(e.getDetail(Locale.getDefault()));
          }
        }
        throw new AccessControlException("The certificate chain is invalid.");
      }
    } catch (CertPathReviewerException ex) {
      throw new GeneralSecurityException(
          "Failed to set up validation for certificates", ex
      );
    }
  }
  
  /**
   * Loads the trust anchors for subsequent validations.
   *
   * @param keystore The key-store holding the trusted certificates.
   * @throws GeneralSecurityException If the key-store cannot be read.
   */
  private void loadTrustAnchors(KeyStore keystore) throws GeneralSecurityException {
    this.pkixParameters = new PKIXParameters(keystore);
    this.pkixParameters.setRevocationEnabled(false);
  }
  
  /**
   * Loads the trust anchors for subsequent validations.
   *
   * @param anchors The list of trusted certificates.
   * @throws GeneralSecurityException If the anchors cannot be loaded.
   */
  private void loadTrustAnchors(List<X509Certificate> anchors) 
      throws GeneralSecurityException {
    KeyStore keyStore = KeyStore.getInstance("JKS");
    try {
      keyStore.load(null);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
    for (X509Certificate x : anchors) {
      keyStore.setCertificateEntry(x.getSubjectX500Principal().getName(), x);
    }
    loadTrustAnchors(keyStore);
  }
  
  /**
   * Loads the trust anchors for subsequent validations.
   *
   * @param anchors The list of trusted certificates.
   * @throws GeneralSecurityException If the anchors cannot be loaded.
   */
  private void loadTrustAnchors(X509Certificate[] anchors) 
      throws GeneralSecurityException {
    loadTrustAnchors(Arrays.asList(anchors));
  }

}