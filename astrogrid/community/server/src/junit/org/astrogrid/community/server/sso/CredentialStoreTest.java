package org.astrogrid.community.server.sso;

import java.io.OutputStream;
import java.net.URL;
import java.security.KeyPairGenerator;
import java.security.Provider;
import java.security.spec.KeySpec;
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
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.server.database.configuration.DatabaseConfiguration;
import org.astrogrid.config.SimpleConfig;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * JUnit tests for CredentialStore.
 *
 * @author Guy Rixon
 */
public class CredentialStoreTest extends TestCase {
  
  private File testUserCredentialDir;
  
  private DatabaseConfiguration dbConfiguration;
  
  /**
   * Emplaces test credentials for a hypothetical user called frog.
   */
  public void setUp() throws Exception {
    SimpleConfig.getSingleton().setProperty("org.astrogrid.community.ident",
                                            "pond/community");
    
    File testCredentialStore = new File(".");
    SimpleConfig.getSingleton().setProperty(
        "org.astrogrid.community.myproxy",
        testCredentialStore.getAbsolutePath()
    );
    this.testUserCredentialDir = new File(testCredentialStore, "frog");
    this.testUserCredentialDir.mkdir();
    assertTrue(this.testUserCredentialDir.isDirectory());
    File keyFile = new File(this.testUserCredentialDir, "key.pem");
    InputStream in = this.getClass().getResourceAsStream("/frog-key.pem");
    assertNotNull(in);
    OutputStream out = new FileOutputStream(keyFile);
    while (true) {
      int c = in.read();
      if (c == -1) {
        break;
      }
      else {
        out.write(c);
      }
    }
    File certFile = new File(this.testUserCredentialDir, "certificate.pem");
    in = this.getClass().getResourceAsStream("/frog-certificate.pem");
    assertNotNull(in);
    out = new FileOutputStream(certFile);
    while (true) {
      int c = in.read();
      if (c == -1) {
        break;
      }
      else {
        out.write(c);
      }
    }
    
    // Load the DB configuration as a class resource and then create the DB schema.
    URL jdoConfig = this.getClass().getResource("/credential-store-test-db.xml");
    this.dbConfiguration = 
        new DatabaseConfiguration("credential-store-test-db", jdoConfig);
    URL script = this.getClass().getResource("/credential-store-test-db.sql");
    this.dbConfiguration.resetDatabaseTables(script);
    
    // Enter a test user in the DB.
    CredentialStore cs = new CredentialStore(this.dbConfiguration);
    cs.changePassword("frog", "croakcroak", "croakcroak");
  }
  
  public CredentialStoreTest(String testName) {
    super(testName);
  }

  /**
   * Test of getPrivateKey method, of class org.astrogrid.community.server.sso.CredentialStore.
   */
  public void testGetPrivateKey() throws Exception {
    CredentialStore sut = new CredentialStore(this.dbConfiguration);
    PrivateKey key = sut.getPrivateKey("frog", "croakcroak");
    assertNotNull(key);
  }

  /**
   * Test of getCertificateChain method, of class org.astrogrid.community.server.sso.CredentialStore.
   */
  public void testGetCertificateChain() throws Exception {
    
    CredentialStore sut = new CredentialStore(this.dbConfiguration);
    List chain = sut.getCertificateChain("frog", "croakcroak");
    assertNotNull(chain);
    assertTrue(chain.size() == 1);
  }
  
  public void testGetCertificateChainWithProxy() throws Exception {
    CredentialStore sut = new CredentialStore(this.dbConfiguration);
    KeyPair keys = KeyPairGenerator.getInstance("RSA").generateKeyPair();
    List chain = sut.getCertificateChain("frog", 
                                         "croakcroak", 
                                         keys.getPublic(),
                                         36000);
    assertNotNull(chain);
    assertTrue(chain.size() == 2);
  }

  /**
   * Test of changeKeyPassword method, of class org.astrogrid.community.server.sso.CredentialStore.
   */
  public void testChangeKeyPassword() throws Exception {
    CredentialStore sut = new CredentialStore(this.dbConfiguration);
    sut.changeKeyPassword("frog", "croakcroak", "ribbitribbit");
    PrivateKey key = sut.getPrivateKey("frog", "ribbitribbit");
  }
  
  public void testChangePassword() throws Exception {
    CredentialStore sut = new CredentialStore(this.dbConfiguration);
    sut.authenticate("frog", "croakcroak");
    sut.changePassword("frog", "croakcroak", "ribbitribbit");
    sut.authenticate("frog", "ribbitribbit");
  }

  /**
   * Test of store method, of class org.astrogrid.community.server.sso.CredentialStore.
   */
  public void testStore() {
    // TODO add your test code.
  }

  /**
   * Test of getPemReader method, of class org.astrogrid.community.server.sso.CredentialStore.
   */
  public void testGetPemReader() {
    // TODO add your test code.
  }
  
  public void testResetDbPassword() throws Exception {
    CredentialStore sut = new CredentialStore(this.dbConfiguration);
    sut.resetDbPassword("frog", "woofwoofwoof");
    sut.authenticate("frog", "woofwoofwoof");
  }
  
}
