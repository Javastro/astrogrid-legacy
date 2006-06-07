package org.astrogrid.security.rfc3820;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.security.auth.x500.X500Principal;
import org.globus.gsi.TrustedCertificates;
import org.globus.gsi.proxy.ProxyPathValidator;
import org.globus.gsi.proxy.ProxyPathValidatorException;

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
public class CertificateChainValidator extends ProxyPathValidator {

  /**
   * Constructs a new instance of CertificateChainValidator.
   */
  public CertificateChainValidator() {
    super();
  }

  /**
   * Validates a certificate chain conforming to RFC3820. Such a chain
   * may include proxy certificates. The equivalent method in the
   * Globus superclass is called to check the subject-issuer relationships
   * and the use of the ProxyCert and BasicConstraints extensions. The
   * signatures on the certificates are then checked. This method assumes that
   * the given chain is complete and includes the trust anchor in the last
   * element of the array.
   *
   * @param chain The chain to be checked, with the trust anchor in the last place.
   * @throws ProxyPathException - if the use of proxy certificates is invalid.
   * @throws NoSuchAlgorithmException - on unsupported signature algorithms.
   * @throws InvalidKeyException - on incorrect key.
   * @throws NoSuchProviderException - if there's no default provider for the JCE.
   * @throws SignatureException - on signature errors.
   * @throws CertificateException - on encoding errors.
   */
  public void validateChain(X509Certificate[] chain)
      throws CertificateException,
             InvalidKeyException,
             NoSuchAlgorithmException,
             NoSuchProviderException,
             ProxyPathValidatorException,
             SignatureException {

    // Validate the subject-issuer relationships and the contraints
    // but not the signatures. (The authors of Globus seem to treat
    // signatures separately.)
    super.validate(chain);

    // Validate the signatures; i.e. for certificate i putatively issued by
    // certificate i+1, check that i was signed by the private key matching
    // the public key in i+1. For the last certificate in the chain, check the
    // signature against the key in the trust anchor.
    for (int i = 0; i < chain.length - 1; i++) {
      PublicKey key = chain[i+1].getPublicKey();
      chain[i].verify(key);
    }
  }

  /**
   * Validates a certificate chain conforming to RFC3820. Such as chain
   * may include proxy certificates. The equivalent method in the
   * Globus superclass is called to check the subject-issuer relationships
   * and the use of the ProxyCert and BasicConstraints extensions. The
   * signatures on the certificates are then checked. This method assumes that
   * the given chain lacks a trust anchor and that a set of possible
   * trust-anchors is passed separately.
   *
   * @param chain The chain to be checked.
   * @param trustAnchors The set of trusted certificates.
   * @throws ProxyPathException - if the use of proxy certificates is invalid.
   * @throws NoSuchAlgorithmException - on unsupported signature algorithms.
   * @throws InvalidKeyException - on incorrect key.
   * @throws NoSuchProviderException - if there's no default provider for the JCE.
   * @throws SignatureException - on signature errors.
   * @throws CertificateException - on encoding errors.
   */
  public void validateChain(X509Certificate[] chain,
                            TrustedCertificates trustAnchors)
      throws CertificateException,
             InvalidKeyException,
             NoSuchAlgorithmException,
             NoSuchProviderException,
             ProxyPathValidatorException,
             SignatureException {

    // Validate the subject-issuer relationships and the contraints
    // but not the signatures. (The authors of Globus seem to treat
    // signatures separately.)
    super.validate(chain, trustAnchors);

    // Validate the signatures; i.e. for certificate i putatively issued by
    // certificate i+1, check that i was signed by the private key matching
    // the public key in i+1.
    for (int i = 0; i < chain.length - 1; i++) {
      X509Certificate signatory = chain[i+1];
      chain[i].verify(signatory.getPublicKey());
    }

    // Validate the signature on the last certificate in the chain against the
    // appropriate trust-anchor.
    X509Certificate lastInGivenChain = chain[chain.length-1];
    X500Principal issuer = lastInGivenChain.getIssuerX500Principal();
    X509Certificate[] anchors = trustAnchors.getCertificates();
    for (int j = 0; j < anchors.length; j++) {
      X509Certificate anchor = anchors[j];
      //System.out.println("\nHave: " + anchor.getIssuerX500Principal().getName(X500Principal.CANONICAL));
      //System.out.println("Have: " + anchor.getIssuerX500Principal().getName(X500Principal.RFC1779));
      //System.out.println("Have: " + anchor.getIssuerX500Principal().getName(X500Principal.RFC2253));
      //System.out.println("Need: " + issuer.getName(X500Principal.CANONICAL));
      if(issuer.equals(anchor.getSubjectX500Principal())) {
        lastInGivenChain.verify(anchor.getPublicKey());
        return;
      }
    }
    throw new ProxyPathValidatorException(ProxyPathValidatorException.UNKNOWN_CA,
                                          lastInGivenChain,
                                          "No trusted certificate with subject " +
                                          issuer.getName() +
                                          " is available.");
  }

}
