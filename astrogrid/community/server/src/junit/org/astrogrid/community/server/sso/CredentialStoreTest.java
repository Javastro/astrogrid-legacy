package org.astrogrid.community.server.sso;

import java.io.OutputStream;
import java.net.URL;
import java.security.KeyPairGenerator;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.List;
import junit.framework.TestCase;
import org.astrogrid.community.server.CommunityConfiguration;
import org.astrogrid.community.server.database.configuration.DatabaseConfiguration;

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
  @Override
  public void setUp() throws Exception {
    CommunityConfiguration config = new CommunityConfiguration();

    config.setPublishingAuthority("pond/community");
    
    File testCredentialStore = new File("target");
    testCredentialStore.mkdirs();
    config.setCredentialDirectory(testCredentialStore);
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

  public void testLoadPkcs12() throws Exception {
    CredentialStore sut = new CredentialStore(this.dbConfiguration);
    InputStream is = this.getClass().getResourceAsStream("/tester.p12");
    assertNotNull(is);
    File f = File.createTempFile("tester", "p12");
    f.deleteOnExit();
    OutputStream os = new FileOutputStream(f);
    while (true) {
      int c = is.read();
      if (c == -1) {
        break;
      }
      else {
        os.write(c);
      }
    }
    sut.loadPkcs12(f.getAbsolutePath(), "tester", "testing", "tester", "testing");
    List l = sut.getCertificateChain("tester", "testing");
    assertTrue(l.size() == 1);
    assertNotNull(sut.getPrivateKey("tester", "testing"));
  }
  
}
