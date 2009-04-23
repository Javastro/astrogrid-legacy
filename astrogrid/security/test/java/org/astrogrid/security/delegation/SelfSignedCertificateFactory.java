package org.astrogrid.security.delegation;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Date;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.x509.X509V3CertificateGenerator;

/**
 *
 * @author Guy Rixon
 */
public class SelfSignedCertificateFactory {
  
  /**
   * A generator for RSA keys.
   */
  private KeyPairGenerator keyGenerator;
  
  /**
   * A generator for certificates.
   * This is a bean whose properties must be set before generating
   * certificates. Some properties, such as the signing algorithm, are set
   * at construction and used for all certificates. Others are reset each
   * time a certificate is created.
   */
  private X509V3CertificateGenerator certificateGenerator;
  
  /**
   * The serial number of the next certificate to be generated.
   */
  long serialNumber;
  
  /**
   * Constructs a SelfSignedCertificateFactory.
   *
   * @throws NoSuchAlgorithmException If the JCE providers cannot make RSA keys.
   * @throws NoSuchAlgorithmException If the JCE providers cannot do MD5/RSA signatures.
   */
  public SelfSignedCertificateFactory() throws NoSuchAlgorithmException {
    this.keyGenerator = KeyPairGenerator.getInstance("RSA");
    this.certificateGenerator = new X509V3CertificateGenerator();
    this.certificateGenerator.setSignatureAlgorithm("MD5WITHRSA");
    this.serialNumber = 1;
  }
  
  /**
   * Generates an RSA key-pair.
   * The key-pair is guaranteed to be compatible with the certificate generator.
   *
   * @return The generated keys.
   */
  public KeyPair generateKeyPair() {
    return this.keyGenerator.genKeyPair();
  }
  
  /**
   * Generates the certificate for a given key-pair.
   *
   * @param keys A RSA key-pair.
   * @param identity The subject of the certificate, parseable as an X.500 Distinguished Name.
   * @param lifetime The duration of validaity of the certificate in milliseconds.
   */
  public X509Certificate generateCertificate(KeyPair keys,
                                             String  identity,
                                             long    lifetime)  
      throws SignatureException, 
             NoSuchAlgorithmException, 
             CertificateEncodingException, 
             InvalidKeyException {
    X500Principal p = new X500Principal(identity);
    this.certificateGenerator.setIssuerDN(p);
    this.certificateGenerator.setSubjectDN(p);
    this.certificateGenerator.setPublicKey(keys.getPublic());
    this.certificateGenerator.setSerialNumber(BigInteger.valueOf(this.serialNumber));
    this.certificateGenerator.setNotBefore(new Date());
    this.certificateGenerator.setNotAfter(new Date(System.currentTimeMillis()+lifetime));
    X509Certificate c =  this.certificateGenerator.generate(keys.getPrivate());
    this.serialNumber++;
    return c;
    }
}
