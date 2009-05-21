package org.astrogrid.security.delegation;

import java.io.IOException;
import java.io.Writer;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.HashMap;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMWriter;

/**
 * A collection of delegated credentials. For each key there is a private key,
 * a certificate-signing request (CSR) and, optionally, a certificate.
 *
 * @author Guy Rixon
 */
public class Delegations {
  
  static private Delegations instance = new Delegations();
  
  private Map identities;
  private KeyPairGenerator keyPairGenerator;
  
  /**
   * Constructs a Delegations object.
   */
  private Delegations() {
    
    // Add the Bouncy Castle JCE provider. This allows the CSR
    // classes to work. The BC implementation of PKCS#10 depends on
    // the ciphers in the BC provider.
    if (Security.getProvider("BC") == null) {
      Security.addProvider(new BouncyCastleProvider());
    }
    
    this.identities = new HashMap();
    try {
      this.keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    } catch (NoSuchAlgorithmException ex) {
      ex.printStackTrace();
      throw new RuntimeException("The JCE doesn't do RSA! Game over.");
    }
    this.keyPairGenerator.initialize(1024);
  }
  
  static public Delegations getInstance() {
    return Delegations.instance;
  }

  /**
   * Determines the hash-key corresponding to a principal.
   *
   * @param principal The identity to be hashed.
   * @return The hash.
   */
  public String hash(X500Principal principal) {
    return Integer.toString(principal.hashCode());
  }


  /**
   * Initializes a group of credentials for one identity.
   * This sets the private key and CSR properties for that identity, 
   * overwriting any credentials that were previously there. The certificate
   * property for the identity is made null.
   *
   * @return The hash of the distinguished name.
   */
  public String initializeIdentity(String identity)
      throws InvalidKeyException, 
             SignatureException, NoSuchAlgorithmException, NoSuchProviderException {
    X500Principal p = new X500Principal(identity);
    return initializeIdentity(p);
  }
  
  /**
   * Initializes a group of credentials for one identity.
   * This sets the private key and CSR properties for that identity, 
   * overwriting any credentials that were previously there. The certificate
   * property for the identity is made null.
   *
   * @param principal The distinguished name on which to base the identity.
   * @return The hash key corresponding to the distinguished name.
   */
  public String initializeIdentity(X500Principal principal) throws InvalidKeyException, 
                                                                   SignatureException, 
                                                                   NoSuchAlgorithmException, 
                                                                   NoSuchProviderException {
    DelegatedIdentity id = new DelegatedIdentity();
    id.dn = principal.getName(X500Principal.CANONICAL);
    id.keys = this.keyPairGenerator.generateKeyPair();
    id.certificate = null;
    id.csr = new CertificateSigningRequest(id.dn, id.keys);
    
    String hashKey = this.hash(principal);
    this.identities.put(hashKey, id);
    
    return hashKey;
  }
  
  public CertificateSigningRequest getCsr(String hashKey) 
      throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
    DelegatedIdentity id = this.getIdentity(hashKey);
    return (id == null)? null : id.csr;
  }
  
  public PrivateKey getPrivateKey(String hashKey) { 
    DelegatedIdentity id = this.getIdentity(hashKey);
    return (id == null)? null: (PrivateKey) id.keys.getPrivate();
  }
  
  public X509Certificate getCertificate(String hashKey) {
    DelegatedIdentity id = this.getIdentity(hashKey);
    return (id == null)? null : id.certificate;
  }
  
  public void remove(String hashKey) {
    this.identities.remove(hashKey);
  }
  
  /**
   * Reveals whether an identity is known from the delegation records.
   */
  public boolean isKnown(String hashKey) {
    return this.identities.containsKey(hashKey);
  }
  
  /**
   * Stores a certificate for the given identity.
   * Any previous certificate is overwritten.
   */
  public void setCertificate(String hashKey, X509Certificate certificate) {
    DelegatedIdentity id = this.getIdentity(hashKey);
    id.certificate = certificate;
  }
  
  public Object[] getPrincipals() {
    return identities.keySet().toArray();
  }
  
  public String getName(String hashKey) {
    DelegatedIdentity id = this.getIdentity(hashKey);
    return (id == null)? null : id.dn;
  }
  
  /**
   * Writes a user's certificate to a given stream, in PEM encoding.
   *
   * @param hashKey The hash key identifying the user.
   * @param out The destination for the certificate.
   */
  public void writeCertificate(String hashKey, Writer out) throws IOException {
    PEMWriter pem = new PEMWriter(out);
    pem.writeObject(getCertificate(hashKey));
    pem.flush();
    pem.close();
  }
  
  /**
   * Reveals whether a certificate is held for this identity.
   *
   * @param hashKey The hash key identifying the user.
   */
  public boolean hasCertificate(String hashKey) {
    return (this.getCertificate(hashKey) != null);
  }
  
  private DelegatedIdentity getIdentity(String hashKey) {
    return (DelegatedIdentity) (this.identities.get(hashKey));
  }
  
  protected class DelegatedIdentity {
    protected String                    dn;
    protected KeyPair                   keys;
    protected X509Certificate           certificate;
    protected CertificateSigningRequest csr;
  }
  
}
