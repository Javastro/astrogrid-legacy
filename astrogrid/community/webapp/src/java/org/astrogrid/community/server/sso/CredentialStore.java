package org.astrogrid.community.server.sso;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.AccessControlException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertPath;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.SimpleConfig;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.openssl.PasswordFinder;

/**
 * The collection of credentials stored by the community for all its accounts.
 *
 * @author Guy Rixon
 */
public class CredentialStore {
  static private Log log = LogFactory.getLog(CredentialStore.class);
  
  private String storeLocation;
  
  private CertificateFactory factory;

  private String provider;
  
  /**
   * Constructs a CredentialStore.
   */
  public CredentialStore() throws GeneralSecurityException {
    
    // Make sure that the Bouncy Castle provider for the JCE is loaded.
    // Only this provider is able to read the encrypted key files
    // written by OpenSSL. Further, the constructors for PEMReader and
    // PEMWriter assume the BC provider unless directed otherwise.
    if (Security.getProvider("BC") == null) {
      Security.addProvider(new BouncyCastleProvider());
      this.provider = "BC";
    }
    
    this.factory = CertificateFactory.getInstance("X509");
    
    // The location of the credential store is set in the general configuration.
    this.storeLocation =
        SimpleConfig.getSingleton().getString("org.astrogrid.community.myproxy");
  }
  
  /**
   * Retrieves the private key from the user's stored credentials.
   */
  public PrivateKey getPrivateKey(String userName, String password) 
      throws AccessControlException {
    
    // Find the credentials on disc.
    File file = new File(this.storeLocation + "/" + userName + "/key.pem");
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(file);
    }
    catch (FileNotFoundException e) {
      log.info(e);
      throw new AccessControlException(userName + " has no stored credentials.");
    }
    
    // Read the key file.
    PEMReader pr = getPemReader(new InputStreamReader(fis), password);
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
   * Retrives the certificate chain from the user's stored credentials.
   */
  public List getCertificateChain(String userName, String password)
      throws AccessControlException {
    
    // Find the credentials on disc.
    File file = new File(this.storeLocation + "/" + userName + "/certificate.pem");
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(file);
    }
    catch (FileNotFoundException e) {
      log.info(e);
      throw new AccessControlException(userName + " has no stored credentials.");
    }
    
    // Read the certificate file. 
    try {
      Certificate c = 
          this.factory.generateCertificate(fis);
      List path = new ArrayList(1);
      path.add(c);
      return path;
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new AccessControlException("Can't read the certificates for " + userName);
    }
  }
  
  /**
   * Changes the password protecting a private key.
   */
  public void changeKeyPassword(String userName,
                                String oldPassword,
                                String newPassword) throws AccessControlException {
    
    // Find the key on disc.
    String keyFileName = this.storeLocation + "/" + userName + "/key.pem";
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(keyFileName);
    }
    catch (FileNotFoundException e) {
      log.info(e);
      return;
    }
    
    // Read the key file.
    
    PEMReader pr = getPemReader(new InputStreamReader(fis), oldPassword);
    KeyPair keys = null;
    try {
      keys = (KeyPair) (pr.readObject());
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new AccessControlException("Can't read the private key for " + userName);
    }
    
    // Write out the key, protecting it with the new password.
    try {
      FileWriter fos = new FileWriter(keyFileName);
      PEMWriter pw = new PEMWriter(fos);
      pw.writeObject(keys.getPrivate(), "DESede", newPassword.toCharArray(), new SecureRandom());
      pw.close();
    }
    catch (Exception e) {
      throw new AccessControlException("Failed to change the password on the private key: " + e);
    }
  }
  
  /**
   * Stores a user's credentials.
   */
  public void store(String            userName,
                    String            password,
                    PrivateKey        key,
                    X509Certificate[] chain) {
    // Not implemented yet.
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
