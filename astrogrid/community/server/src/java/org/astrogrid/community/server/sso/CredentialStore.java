package org.astrogrid.community.server.sso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.AccessControlException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertPath;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.server.CommunityConfiguration;
import org.astrogrid.community.server.subprocess.Subprocess;
import org.astrogrid.community.server.database.configuration.DatabaseConfiguration;
import org.astrogrid.community.server.security.data.PasswordData;
import org.astrogrid.community.server.service.CommunityServiceImpl;
import org.astrogrid.security.SecurityGuard;
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
  
  private File storeLocation;
  
  private ProxyCertificateFactory proxyFactory;
  private CertificateFactory certificateFactory;

  private String publishingAuthority;
  
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

    CommunityConfiguration c = new CommunityConfiguration();
    
    // The location of the credential store is set in the general configuration.
    this.storeLocation = c.getCredentialDirectory();

    // The pblishing authority, used to construct database keys, is set
    // in the general configuration.
    this.publishingAuthority = c.getPublishingAuthority();
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
    File f1 = new File(this.storeLocation, userName);
    File f2 = new File(f1, "key.pem");
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(f2);
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
    File f1 = new File(this.storeLocation, userName);
    File f2 = new File(f1, "certificate.pem");
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(f2);
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
    File f1 = new File(this.storeLocation, userName);
    File f2 = new File(f1, "key.pem");
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(f2);
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
      FileWriter fos = new FileWriter(f2);
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
    return "ivo://" + this.publishingAuthority + "/" + userName;
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
    
    // Find where the key ought to go on disc.
    File f1 = new File(this.storeLocation, userName);
    f1.mkdir();
    File f2 = new File(f1, "key.pem");

    // Write the key to the file, protecting it with encryption based on
    // the given password.
    try {
      FileWriter fos = new FileWriter(f2);
      PEMWriter pw = new PEMWriter(fos);
      pw.writeObject(key, "DESede", password.toCharArray(), new SecureRandom());
      pw.close();
    }
    catch (Exception e) {
      throw new AccessControlException("Failed to store the private key: " + e);
    }

    // Find where the certificates ought to go on disc.
    File f3 = new File(f1, "certificate.pem");

     // Write the certificates to the file.
    try {
      FileWriter fos = new FileWriter(f3);
      PEMWriter pw = new PEMWriter(fos);
      for (int i = 0; i < chain.length; i++) {
        pw.writeObject(chain[i]);
      }
      pw.close();
    }
    catch (Exception e) {
      throw new AccessControlException("Failed to store the certificates: " + e);
    }
  }

  /**
   * Loads credentials from a PKCS#12 store.
   */
  public void loadPkcs12(InputStream storeStream,
                         String      accountName,
                         String      accountPassword,
                         String      storeAlias,
                         String      storePassword) throws IOException,
                                                      KeyStoreException,
                                                      NoSuchAlgorithmException,
                                                      CertificateException,
                                                      UnrecoverableKeyException,
                                                      UnrecoverableEntryException {
    System.out.println("Store alias: " + storeAlias);
    System.out.println("Store password: " + storePassword);
    System.out.println("Account name: " + accountName);
    System.out.println("Account password: " + accountPassword);
    KeyStore store = KeyStore.getInstance("PKCS12");
    store.load(storeStream, storePassword.toCharArray());
    Enumeration<String> aliases = store.aliases();
    while (aliases.hasMoreElements()) {
      String alias = aliases.nextElement();
      System.out.println("alias: " + alias);
      System.out.println("Is private key entry? " + store.entryInstanceOf(alias, PrivateKeyEntry.class));
    }

    PrivateKey key =
        (PrivateKey) store.getKey(storeAlias, storePassword.toCharArray());
    if (key == null) {
      throw new IOException("No private key");
    }
    Certificate[] chain1 = store.getCertificateChain(accountName);
    if (chain1 == null) {
      throw new IOException("No certificate chain");
    }
    X509Certificate[] chain2 = new X509Certificate[chain1.length];
    for (int i = 0; i < chain2.length; i++) {
      chain2[i] = (X509Certificate) chain1[i];
    }
    
    store(accountName, accountPassword, key, chain2);
  }

  public void loadPkcs12(String storeFileName,
                         String storeAlias,
                         String storePassword,
                         String accountName,
                         String accountPassword) {

    try {

      // Work out where the credential files should be kept.
      File f1 = new File(this.storeLocation, accountName);
      f1.mkdir();
      File f2 = new File(f1, "key.pem");
      File f3 = new File(f1, "certificate.pem");
      File f4 = new File(f1, "temp.pem");
      String keyFileName = f2.getAbsolutePath();
      String certFileName = f3.getAbsolutePath();
      String tempFileName = f4.getAbsolutePath();

      // Copy the certificate out of the store into a temporary
      // file. Openssl writes junk before the certificate data
      // which Java can't read.
      String[] command1 = {
        "openssl",     // Invoke openssl
        "pkcs12",      // Use the PKCS#12 command
        "-in",         // Read the key-store...
        storeFileName, // ...from here.
        "-nokeys",      // Copy only certificates on this pass.
        "-clcerts",    // Copy only the client certificate.
        "-passin",     // Read the store password...
        "stdin",        // From standard input
        "-out",        // Write the certificate PEM-file...
        tempFileName   // ...here.
      };
      Subprocess p1 = new Subprocess(command1);
      p1.sendToStdin(storePassword);
      p1.waitForEnd();

      // Copy the private key out of the store into a file.
      String[] command2 = {
        "openssl",     // Invoke openssl
        "pkcs12",      // Use the PKCS#12 command
        "-in",         // Read the key-store...
        storeFileName, // ...from here.
        "-passin",     // Read the store password...
        "stdin",        // From standard input.
        "-nocerts",    // Copy only the private key on this pass
        "-out",        // Write the key PEM-file...
        keyFileName,   // ...here.
        "-passout",    // Read the password for the key file...
        "stdin"        // ...from standard input.
      };
      Subprocess p2 = new Subprocess(command2);
      p2.sendToStdin(storePassword + "\n" + accountPassword);
      p2.waitForEnd();

      // Copy the certificate from the temporary file to the proper file.
      // This command in openssl strips out the junk so Java can read the result.
      String[] command3 = {
        "openssl",     // Invoke openssl
        "x509",        // Use the X.509-parsing command
        "-in",         // Read the certificate...
        tempFileName,  // ...from here.
        "-out",        // Write the certificate PEM-file...
        certFileName   // ...here.
      };
      Subprocess p3 = new Subprocess(command3);
      p3.waitForEnd();
    }
    catch (Exception e) {
      throw new RuntimeException("Failed to read a PKCS#12 store", e);
    }
  }

  /**
   * Supplies the named user's credentials in one object.
   *
   */
  public SecurityGuard getCredentials(String userName, String password) {
    SecurityGuard sg = new SecurityGuard();

    sg.setPrivateKey(getPrivateKey(userName, password));

    List<X509Certificate> l = getCertificateChain(userName, password);
    try {
      CertPath p = certificateFactory.generateCertPath(l);
      sg.setCertificateChain(p);
    }
    catch (Exception e) {
      throw new AccessControlException(userName + " has no valid certificate-chain");
    }

    sg.setX500PrincipalFromCertificateChain();

    return sg;
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
