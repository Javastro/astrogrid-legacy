package org.astrogrid.security.delegation;

import java.io.IOException;
import java.io.Writer;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.openssl.PEMWriter;

/**
 * A CSR that can write itself out in PEM'd PKCS#10 format.
 *
 * @author Guy Rixon
 */
public class CertificateSigningRequest extends PKCS10CertificationRequest {
   
  /**
   * Constructs a CertificateSigningRequest.
   * The subject is taken in as a plain string. The signing
   * algorithm is fixed as MD5 digest, RSA encryption. This means that the
   * key-pair passed in must be RSA keys.
   *
   * @throws NoSuchAlgorithmException If the JCE-provider doesn't know MD5WITHRSA.
   * @throws NoSuchProviderException If the JCE-provider "BC" isn't registered.
   * @throws InvalidKeyException If the given keys are not RSA keys.
   */
  public CertificateSigningRequest(String identity, KeyPair keys) 
      throws InvalidKeyException, 
             SignatureException, NoSuchAlgorithmException, NoSuchProviderException {
    super("MD5WITHRSA",
            new X509Name(identity),
            keys.getPublic(),
            null,
            keys.getPrivate(),
            "BC");
  }
  
  /**
   * Writes out the CSR as a PEM-encoded PKCS#10 artifact.
   */
  public void writePem(Writer out) throws IOException {
    PEMWriter pem = new PEMWriter(out);
    pem.writeObject(this);
    pem.flush();
  }
  
}
