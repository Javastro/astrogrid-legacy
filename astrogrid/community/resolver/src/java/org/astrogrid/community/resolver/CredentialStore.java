package org.astrogrid.community.resolver;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import org.globus.gsi.gssapi.GlobusGSSCredentialImpl;

/**
 * A container for authentication credentials.
 * The community resolver can convert a user IVOID and password
 * (known to the UI) into a private key, certificate chain and
 * user alias for the same user. This class packs the outputs into
 * one object type.
 *
 * @author Guy Rixon
 */
public class CredentialStore {
  
  /**
   * A Java key-store, to hold the private key and certificate chain.
   */
  KeyStore keyStore;
  
  /**
   * An alias for the user. This alias identifies the user's
   * credentials in the key-store.
   */
  String alias;
  
  /** 
   * Constructs a new CredentialStore using the credentials
   * returned from MyProxy by cog-jglobus.
   */
  public CredentialStore(GlobusGSSCredentialImpl credentials, 
                         String                  alias,
                         String                  password)
      throws KeyStoreException, 
             IOException,
             NoSuchAlgorithmException,
             CertificateException {
    this.alias = alias;
    
    // Create a new, empty key-store. Since this is expected to work with 
    // Axis and WSS4J, a JKS key-store is best. PKCS#12 key-stores seem
    // not to work with WSS4J. Loading the keystore contents from a null
    // input-stream initializes an empty key-store.
    KeyStore ks = KeyStore.getInstance("JKS");
    ks.load(null, password.toCharArray());
    
    // Extract the credentials and put them into the key-store.
    ks.setKeyEntry(alias,
                   credentials.getPrivateKey(), 
                   password.toCharArray(),
                   credentials.getCertificateChain());
  }
  
  /**
   * Extract the user alias.
   * @return The alias.
   */
  public String getUserAlias() {
    return this.alias;
  }
  
  /**
   * Extract the credentials as a Java key-store of type JKS.
   *
   * @return The key-store.
   */
  public KeyStore getJksKeyStore() {
    return this.keyStore;
  }

}