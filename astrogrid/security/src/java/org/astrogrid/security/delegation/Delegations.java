package org.astrogrid.security.delegation;

import java.io.IOException;
import java.io.Writer;
import java.security.GeneralSecurityException;
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
import java.util.concurrent.ConcurrentHashMap;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMWriter;

/**
 * A collection of delegated credentials. For each key there is a private key,
 * a certificate-signing request (CSR) and, optionally, a certificate.
 * <p>
 * This class is thread safe. Initializing an identity is idempotent. The
 * name, keys and CSR for each identity are immutable, and access to the
 * certificate is synchronized. Further, the class will reject an attempt
 * to set a certificate whose public key does not match that set for the
 * identity at initialization; therefore, if two threads delegate to the
 * same identity concurrently, the credentials held are not disrupted.
 *
 * @author Guy Rixon
 */
public class Delegations {
  
  static private Delegations instance = new Delegations();

  /**
   * All the delegations, partial or complete, known to this
   * object. The key-pairs for delegated credentials live here.
   * The keys are hashes of the delegated principals: see
   * {@link #hash} for details.
   */
  private Map<String, DelegatedIdentity> identities;

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
    
    erase();

    try {
      keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    } catch (NoSuchAlgorithmException ex) {
      ex.printStackTrace();
      throw new RuntimeException("The JCE doesn't do RSA! Game over.");
    }
    keyPairGenerator.initialize(1024);
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
   * Erases all delegations.
   */
  public void erase() {
    identities = new ConcurrentHashMap<String, DelegatedIdentity>();
  }


  /**
   * Initializes a group of credentials for one identity.
   * If there were already credentials for that identity, nothing is changed.
   * If not, a key pair and a CSR are generated and stored; the certificate
   * property is set to null.
   *
   * @return The hash of the distinguished name.
   */
  public String initializeIdentity(String identity) throws GeneralSecurityException {
    X500Principal p = new X500Principal(identity);
    return initializeIdentity(p);
  }
  
  /**
   * Initializes a group of credentials for one identity.
   * If there were already credentials for that identity, nothing is changed.
   * If not, a key pair and a CSR are generated and stored; the certificate
   * property is set to null.
   *
   * @param principal The distinguished name on which to base the identity.
   * @return The hash key corresponding to the distinguished name.
   */
  public String initializeIdentity(X500Principal principal) throws GeneralSecurityException {
    String hashKey = hash(principal);
    if (!identities.containsKey(hashKey)) {
      DelegatedIdentity id = 
          new DelegatedIdentity(principal.getName(X500Principal.CANONICAL),
                                this.keyPairGenerator.generateKeyPair());
      identities.put(hashKey, id);
    }
    return hashKey;
  }
  
  public CertificateSigningRequest getCsr(String hashKey) {
    DelegatedIdentity id = identities.get(hashKey);
    return (id == null)? null : id.csr;
  }
  
  public PrivateKey getPrivateKey(String hashKey) { 
    DelegatedIdentity id = identities.get(hashKey);
    return (id == null)? null: (PrivateKey) id.keys.getPrivate();
  }
  
  public X509Certificate getCertificate(String hashKey) {
    DelegatedIdentity id = identities.get(hashKey);
    if (id == null) {
      return null;
    }
    else {
      synchronized(id) {
        return id.certificate;
      }
    }
  }
  
  public void remove(String hashKey) {
    identities.remove(hashKey);
  }
  
  /**
   * Reveals whether an identity is known from the delegation records.
   */
  public boolean isKnown(String hashKey) {
    return identities.containsKey(hashKey);
  }
  
  /**
   * Stores a certificate for the given identity.
   * Any previous certificate is overwritten.
   * This operation is thread-safe against concurrent reading of the certificate.
   */
  public void setCertificate(String          hashKey,
                             X509Certificate certificate) throws InvalidKeyException {
    DelegatedIdentity id = identities.get(hashKey);
    if (id == null) {
      throw new InvalidKeyException("No identity matches the hash key " + hashKey);
    } else {
      id.setCertificate(certificate);
    }
  }
  
  public Object[] getPrincipals() {
    return identities.keySet().toArray();
  }
  
  public String getName(String hashKey) {
    DelegatedIdentity id = identities.get(hashKey);
    return (id == null)? null : id.dn;
  }

  /**
   * Reveals the keys held for an identity.
   *
   * @param hashKey The hash of the identity.
   * @return The keys (null if identity not known).
   */
  public KeyPair getKeys(String hashKey) {
    DelegatedIdentity id = identities.get(hashKey);
    return (id == null)? null : id.getKeys();
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

  
  protected class DelegatedIdentity {
    protected final String                    dn;
    protected final KeyPair                   keys;
    protected final CertificateSigningRequest csr;
    protected X509Certificate                 certificate;

    protected DelegatedIdentity(String  dn,
                                KeyPair keys) throws GeneralSecurityException {
      this.dn          = dn;
      this.keys        = keys;
      this.csr         = new CertificateSigningRequest(dn, keys);
      this.certificate = null;
    }

    protected synchronized X509Certificate getCertificate() {
      return certificate;
    }

    protected synchronized void setCertificate(X509Certificate c) throws InvalidKeyException {
      if (c.getPublicKey().equals(keys.getPublic())) {
        certificate = c;
      }
      else {
        throw new InvalidKeyException("This certificate does not match the cached private-key.");
      }
    }

    protected KeyPair getKeys() {
      return keys;
    }

  }
  
}
