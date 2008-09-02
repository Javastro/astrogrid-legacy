package org.astrogrid.community.server.sso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.AccessControlException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.server.database.configuration.DatabaseConfiguration;
import org.astrogrid.community.server.security.data.PasswordData;
import org.astrogrid.community.server.service.CommunityServiceImpl;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.security.rfc3820.ProxyCertificateFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.openssl.PasswordFinder;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.ObjectNotFoundException;

/**
 * The collection of credentials stored by the community for all its accounts.
 *
 * @author Guy Rixon
 */
public class CredentialStore extends CommunityServiceImpl {
  static private Log log = LogFactory.getLog(CredentialStore.class);
  
  private String storeLocation;
  
  private ProxyCertificateFactory proxyFactory;
  private CertificateFactory certificateFactory;
  
  /**
   * Constructs a CredentialStore with the dfault, configured user-database and
   * credentials directory. This constructor should be used in production.
   */
  public CredentialStore() throws GeneralSecurityException {
    super();
    initialize();
  }
  
  /**
   * Constructs a CredentialStore with a given user-database and
   * the default credentials-directory. This constructor is for unit tests.
   */
  public CredentialStore(DatabaseConfiguration config) throws GeneralSecurityException {
    super(config);
    initialize();
  }
  
  /**
   * Initializes the object during construction. This method is called by
   * more than one constructor.
   */
  private void initialize() throws GeneralSecurityException {
        
    // Make sure that the Bouncy Castle provider for the JCE is loaded.
    // Only this provider is able to read the encrypted key files
    // written by OpenSSL. Further, the constructors for PEMReader and
    // PEMWriter assume the BC provider unless directed otherwise.
    if (Security.getProvider("BC") == null) {
      Security.addProvider(new BouncyCastleProvider());
    }
    
    this.proxyFactory = new ProxyCertificateFactory();
    this.certificateFactory = CertificateFactory.getInstance("X509");
    //this.securityService = new SecurityServiceImpl();
    
    // The location of the credential store is set in the general configuration.
    this.storeLocation =
        SimpleConfig.getSingleton().getString("org.astrogrid.community.myproxy");
  }
  
  /**
   * Checks a password against the password database.
   * The given user-name is assumed to be part of this community;
   * e.g. "fred" rather than "ivo://fred@foo.bar/community".
   *
   * @throws AccessControlException If the password does not match the user name.
   * @throws AccessControlException If the user name does not match a local account.
   */
  public void authenticate(String userName, String password)
      throws AccessControlException, FileNotFoundException {
    assert userName != null;
    assert password != null;
    
    Database database = null;
    PasswordData match = null;
    try {
      database = this.getDatabase();
      database.begin();
      String primaryKey = primaryKey(userName);
      match = (PasswordData) database.load(PasswordData.class, primaryKey);
      database.commit();
    }
    catch (Exception e) {
      rollbackTransaction(database);
      log.info("Password check failed for " + userName + ": " + e);
      throw new FileNotFoundException("Password check failed for " + userName);
    }
    finally {
      closeConnection(database);
    }
    
    assert match != null;
    
    if (password.equals(match.getPassword())) {
      log.info("Password check succeeded for " + userName);
    }
    else {
      throw new AccessControlException("Password is invalid");
    }
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
      log.info(userName + " has no certificate chain stored in this community");
      throw new AccessControlException(userName + " has no stored credentials.");
    }
    
    // Read the certificate file. 
    try {
      Certificate c = 
          this.certificateFactory.generateCertificate(fis);
      List path = new ArrayList(1);
      path.add(c);
      return path;
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new AccessControlException("Can't read the certificates for " + userName);
    }
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
  public List getCertificateChain(String    userName,
                                  String    password,
                                  PublicKey proxyKey,
                                  int       lifetime) throws GeneralSecurityException {
    
    // Get the user's stored certificate-chain. This is typically just one
    // EEC but could also be an EEC and a proxy.
    List certificates = getCertificateChain(userName, password);
    if (certificates.size() == 0) {
      throw new GeneralSecurityException(userName +
                                         " has an empty certificate-chain.");
    }
    
    // Generate a proxy certificate signed by the certificate on the
    // front of the stored chain. Add this to the front of the chain.
    log.debug("Got " + certificates.size() + " certificates.");
    this.proxyFactory.extendCertificateChain(certificates,
                                             getPrivateKey(userName, password),
                                             proxyKey,
                                             lifetime,
                                             false); // GSI-3 proxy, not RFC-3820.
    return certificates;
  }
  
  /**
   * Changes a user's password. This change is registered both in the 
   * user database and in the user's private-key file. The user is authenticated
   * by the old password before any changes are made.
   *
   * @param userName The unqualified user-name.
   * @param oldPassword The current password, use for authentication.
   * @param newPassword The value to which the password should be set.
   */
  public void changePassword(String userName,
                             String oldPassword,
                             String newPassword) throws AccessControlException {
    assert(userName    != null);
    assert(oldPassword != null);
    assert(newPassword != null);
    
    String primaryKey = primaryKey(userName);
    log.debug("Setting the password for user " + userName + ", primary key " + primaryKey);

    // Update the password in the database.
    // This makes a new connection and runs a transaction. The error handling
    // ensures that the transaction is either committed or rolled back and that
    // the connection is always closed before the end of the method.
    // In the transaction, an attempt is made to read and update an existing
    // record. If this fails, a new record is created.
    Database database = null ;
    try {
      database = this.getDatabase();
      database.begin();
      PasswordData data = null;
      try {
        data = (PasswordData) database.load(PasswordData.class, primaryKey);
      }
      catch (ObjectNotFoundException ouch) {
        // This is OK: it means we're setting the password for the first time.
      }
      if (null != data) {
        if (!oldPassword.equals(data.getPassword())) {
          throw new AccessControlException("Authentication failure: wrong password.");
        }
        log.debug("This user already has a password, which shall be changed.");
        data.setPassword(newPassword);
        data.setEncryption(PasswordData.NO_ENCRYPTION);
      }
      else {
        log.debug("This user doesn't have a password yet.");
        data = new PasswordData(primaryKey, newPassword);
        database.create(data);
      }
      
      // Now that the change in the DB is known to be possible, make the
      // change in the credentials files. if the latter fails, the DB changes
      // can be rolled back.
      changeKeyPassword(userName, oldPassword, newPassword);
      
      database.commit();
    }
    catch (Exception ouch) {
      log.error("Password change failed: " + ouch);
      rollbackTransaction(database);
      throw new AccessControlException("Password change failed for " + userName);
    }
    finally {
      closeConnection(database) ;
    }
  }
  
  /**
   * Changes a user's password. This change is registered only in the 
   * user database and not in the user's private-key file; therefore, the
   * key file should be recreated (using the community CA) after this method 
   * completes. The user is not authenticated before restting the password:
   * this method is needed when the user's password is lost.
   *
   * @param userName The unqualified user-name.
   * @param newPassword The value to which the password should be set.
   */
  public void resetDbPassword(String userName,
                              String password) throws GeneralSecurityException {
    assert(userName != null);
    assert(password != null);
    String primaryKey = primaryKey(userName);
    log.debug("Setting the password for user " + userName + ", primary key " + primaryKey);

    // Update the password in the database.
    // This makes a new connection and runs a transaction. The error handling
    // ensures that the transaction is either committed or rolled back and that
    // the connection is always closed before the end of the method.
    // In the transaction, an attempt is made to read and update an existing
    // record. If this fails, a new record is created.
    Database database = null ;
    try {
      database = this.getDatabase();
      database.begin();
      PasswordData data = null;
      try {
        data = (PasswordData) database.load(PasswordData.class, primaryKey);
      }
      catch (ObjectNotFoundException ouch) {
        // This is OK: it means we're setting the password for the first time.
      }
      if (null != data) {
        log.debug("This user already has a password, which shall be changed.");
        data.setPassword(password);
        data.setEncryption(PasswordData.NO_ENCRYPTION);
      }
      else {
        log.debug("This user doesn't have a password yet.");
        data = new PasswordData(primaryKey, password);
        database.create(data);
      }
      database.commit();
    }
    catch (Exception ouch) {
      log.error("Password change failed: " + ouch);
      rollbackTransaction(database);
      throw new GeneralSecurityException("Password change failed", ouch);
    }
    finally {
      closeConnection(database) ;
    }
  }
  
  
  /**
   * Changes the password protecting a private key.
   * The file is assumed to be in a PEM file.
   * If the file does not exist, no exception is thrown.
   *
   * @throws AccessControlException If the key file exists and cannot be read. 
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
   * Derives the primary key for the DB tables from the user name and
   * configured community name.
   */
  protected String primaryKey(String userName) {
    String community = 
        SimpleConfig.getSingleton().getString("org.astrogrid.community.ident");
    int slash = community.indexOf("/");
    String authority = (slash == -1)? community : community.substring(0, slash);
    return "ivo://" + authority + "/" + userName;
  }
  
  /**
   * Exposes the password for a user account.
   * <p>
   * This method supports the certificate-authority servlet which
   * uses the password of an existing account to protect that account's
   * credentials. The method should definitely not be exposed as a web service!
   *
   * @param userName The name of the account, not in IVORN form, e.g. fred rather than ivo://fred@foo/
   */
  public String getPassword(String userName) throws GeneralSecurityException {
    
    // Get the password record from the community database.
    PasswordData data;
    Database database = null ;
    try {
      database = this.getDatabase();
      database.begin();
      String primaryKey = primaryKey(userName);
      log.debug("Loading PasswordData with JDO identity " + primaryKey);
      data = (PasswordData) (database.load(PasswordData.class, primaryKey));
      database.commit();
    }
    catch (Exception e) {
      rollbackTransaction(database);
      throw new GeneralSecurityException("Failed to read the password for " + userName, e);
    }
    finally {
      closeConnection(database);
    }
    
    return data.getPassword();
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
