package org.astrogrid.security.keystore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import org.astrogrid.security.SecurityGuard;
import org.astrogrid.security.SignOnClient;

/**
 * An adaptor for using a key-store file to sign on to the IVO.
 * The underlying store must be of the JKS type. The store may be in a
 * local file or on a remote server (e.g. on a web server). In the latter case,
 * it will not be possible to change the password protecting the store.
 * <p>
 * This class assumes that the store contains credentials for just one user
 * whose alias is passed as the user-name argument in the {@link #authenticate}
 * and {@link #changePassword} methods. The same password is used for the store
 * and for the user's private-key; this would be unworkable if the store had
 * credentials for more than one user.
 * <p>
 * The {@link home} method does nothing because a key store cannot record a
 * user's home-space. This method returns silently.
 * 
 * @author Guy Rixon
 */
public class KeyStoreClient implements SignOnClient {

  private URI source;

  /**
   * Constructs a client for a key-store at a given URL.
   *
   * @param u The location of hte key-store.
   */
  public KeyStoreClient(URI u) {
    source = u;
  }

  /**
   * Obtain credentials for a user from the store.
   * The credentials are added to the given security guard.
   *
   * @param userName The user alias in the store.
   * @param password The password protecting the store and private key.
   * @param lifetime Not used.
   * @param guard The container for the credentials.
   * @throws java.io.IOException If the store cannot be read.
   * @throws java.security.GeneralSecurityException If the password is incorrect.
   */
  public void authenticate(String        userName,
                           String        password,
                           int           lifetime,
                           SecurityGuard guard) throws IOException,
                                                       GeneralSecurityException {
    KeyStore store = load(password);

    Certificate[] chain1 = store.getCertificateChain(userName);
    X509Certificate[] chain2 = new X509Certificate[chain1.length];
    for (int i = 0; i < chain1.length; i++) {
      chain2[i] = (X509Certificate) chain1[i];
    }
    guard.setCertificateChain(chain2);

    PrivateKey key = (PrivateKey)store.getKey(userName, password.toCharArray());
    guard.setPrivateKey(key);

    guard.setX500PrincipalFromCertificateChain();
    }


  /**
   * Reveals the location of the user's home space in VOSpace.
   * This method does not, and returns silently, because a key-store does
   * not know about the home space.
   */
  public void home(String        userName,
                   SecurityGuard guard) {
    // Nothing to do here. A key-store doesn't have this information.
  }

    /**
     * Change the password in the key-store.
     * This only works if the store is in a local file; other wise the call is
     * rejected. The same password is set for the store and for the private key.
     * 
     * @param userName The user alias in the store.
     * @param oldPassword The current password.
     * @param newPassword The desired password.
     * @param guard Not used.
     * @throws java.security.GeneralSecurityException
     * @throws java.io.IOException If the store is inaccessible.
     * @throws GeneralSecurityException If the old password is incorrect.
     */
    public void changePassword(String        userName,
                               String        oldPassword,
                               String        newPassword,
                               SecurityGuard guard) throws GeneralSecurityException,
                                                           IOException {
      if (source.getScheme().equals("file")) {
         KeyStore store = KeyStore.getInstance("JKS");
         store.load(source.toURL().openStream(), oldPassword.toCharArray());
         PrivateKey key = (PrivateKey)store.getKey(userName, oldPassword.toCharArray());
         Certificate[] chain = store.getCertificateChain(userName);
         store.setKeyEntry(userName,
                           key,
                           newPassword.toCharArray(),
                           chain);
         store.store(new FileOutputStream(new File(source)),
                                          newPassword.toCharArray());
      }
      else {
        throw new IOException("Password was not changed; can't write to " + source);
      }
    }

  /**
   * Loads the store from its URL.
   * PKCS#12 and JKS stores are supported. If the configured location ends in
   * .jks or .p12 that is taken to indicate the type of the store. Otherwise,
   * the store is first read as PKCS#12 format and, if that fails, as JKS format.
   * 
   * @param password The password protecting the store.
   * @return The store.
   * @throws KeyStoreException If the requested type is not supported.
   * @throws IOException If the configured location of the store cannot be read.
   * @throws IOException If the store at the configured location is not the requested type.
   * @throws NoSuchAlgorithmException If the encryption protecting the store is not supported.
   * @throws CertificateException If the contents of the store are an unsupported type.
   */
  protected KeyStore load(String password) throws KeyStoreException,
                                                  IOException,
                                                  NoSuchAlgorithmException,
                                                  CertificateException {
    if (source.toString().endsWith(".p12")) {
      return load("PKCS12", password);
    }
    else if (source.toString().endsWith(".jks")) {
      return load("JKS", password);
    }
    else {
      try {
        return load("PKCS12", password);
      }
      catch (IOException e) {
        return load("JKS", password);
      }
    }
  }

  /**
   * Loads the store from its URL.
   * PKCS#12 and JKS stores are supported.
   *
   * @param type The type of the store: JKS or PKCS12.
   * @param password The password protecting the store.
   * @return The store.
   * @throws KeyStoreException If the requested type is not supported.
   * @throws IOException If the configured location of the store cannot be read.
   * @throws IOException If the store at the configured location is not the requested type.
   * @throws NoSuchAlgorithmException If the encryption protecting the store is not supported.
   * @throws CertificateException If the contents of the store are an unsupported type.
   */
  protected KeyStore load(String type, String password) throws KeyStoreException,
                                                               IOException,
                                                               NoSuchAlgorithmException,
                                                               CertificateException {
    KeyStore store = KeyStore.getInstance(type);
    store.load(source.toURL().openStream(), password.toCharArray());
    return store;
  }

}