package org.astrogrid.security.community;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.AccessControlException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PasswordFinder;

/**
 * A container for information on a user account.
 *
 * @author Guy Rixon
 */
public class Account {

  /**
   * The name of the account.
   */
  private String userName;

  /**
   * The current password.
   */
  private String password;

  /**
   * The private key matching the head of the certificate chain.
   */
  private PrivateKey key;
  
  /**
   * The certificate chain matching the private key.
   */
  private List<X509Certificate> chain;

  /**
   * The factory for EECs.
   */
  private CertificateFactory certificateFactory;


  /**
   * Constructs an account given the name and password.
   * The credentials are read from storage and unlocked using the password.
   */
  public Account(String             userName,
                 String             password,
                 CertificateFactory eecFactory) {
    this.userName = userName;
    this.password = password;
    this.key = loadPrivateKey();
    this.certificateFactory = eecFactory;
    this.chain = loadCertificateChain();
  }

  /**
   * Supplies the current password.
   *
   * @return The password.
   */
  public String getPassword() {
    return password;
  }

  /**
   * Changes the password.
   *
   * @param newPassword The password.
   */
  public void setPassword(String password) {
    this.password = password;
  }


  /**
   * Supplies the user's private key.
   *
   * @return The key (null if no key was found for this account).
   */
  public PrivateKey getPrivateKey() {
    return key;
  }

  /**
   * Produces the user's certificate chain extended with a newly-created
   * proxy certificate.
   *
   * @param userName The user name.
   * @param password The password, in plain text.
   * @param proxyKey The key to be written into the new proxy-certficate.
   * @param lifetime The duration of validaity, in seconds, for the new proxy.
   * @return A certificate chain starting with the new proxy.
   */
  public List<X509Certificate> getCertificateChain() {
    return chain;
  }

  /**
   * Retrives the certificate chain from the user's stored credentials.
   */
  private List<X509Certificate> loadCertificateChain() throws AccessControlException {

    String resourceName = "/" + userName + "-certificate.pem";
    InputStream is = this.getClass().getResourceAsStream(resourceName);
    if (is == null) {
      throw new AccessControlException(userName + " has no stored certificates.");
    }

    // Read the certificate file.
    try {
      X509Certificate c = (X509Certificate) certificateFactory.generateCertificate(is);
      List<X509Certificate> path = new ArrayList<X509Certificate>(1);
      path.add(c);
      return path;
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new AccessControlException("Can't read the certificates for " + userName);
    }
  }


  /**
   * Retrieves the private key from the user's stored credentials.
   */
  private PrivateKey loadPrivateKey() throws AccessControlException {

    String resourceName = "/" + userName + "-key.pem";
    InputStream is = this.getClass().getResourceAsStream(resourceName);
    if (is == null) {
      throw new AccessControlException(userName + " has no stored private key.");
    }

    // Read the key file.
    PEMReader pr = getPemReader(new InputStreamReader(is), password);
    KeyPair keys = null;
    try {
      keys = (KeyPair) (pr.readObject());
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new AccessControlException("Can't read the private key for " + userName);
    }

    return keys.getPrivate();
  }

  /**
   * Produces a PEMReader.
   * The password for reading private keys is initialized.
   *
   * @param reader The reader for the input stream to be read.
   * @param password The unencrypted, undigested password for the private key.
   * @return The reader.
   */
  private PEMReader getPemReader(Reader reader, String password) {
    PasswordFinder f = new Password(password);
    return new PEMReader(reader, f);
  }

  public class Password implements PasswordFinder {

    private String password;

    public Password(String password) {
      this.password = password;
    }

    public char[] getPassword() {
      return this.password.toCharArray();
    }
  }
}
