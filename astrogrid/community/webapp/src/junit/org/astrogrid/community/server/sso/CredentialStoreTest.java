package org.astrogrid.community.server.sso;

import java.io.OutputStream;
import java.security.Provider;
import junit.framework.*;
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.SimpleConfig;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * JUnit tests for CredentialStore.
 *
 * @author Guy Rixon
 */
public class CredentialStoreTest extends TestCase {
  
  private File testUserCredentialDir;
  
  /**
   * Emplaces test credentials for a hypothetical user called frog.
   */
  public void setUp() throws Exception {
    
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
  }
  
  public CredentialStoreTest(String testName) {
    super(testName);
  }

  /**
   * Test of getPrivateKey method, of class org.astrogrid.community.server.sso.CredentialStore.
   */
  public void testGetPrivateKey() throws Exception {
    CredentialStore sut = new CredentialStore();
    PrivateKey key = sut.getPrivateKey("frog", "croakcroak");
    assertNotNull(key);
  }

  /**
   * Test of getCertificateChain method, of class org.astrogrid.community.server.sso.CredentialStore.
   */
  public void testGetCertificateChain() throws Exception {
    
    CredentialStore sut = new CredentialStore();
    List chain = sut.getCertificateChain("frog", "croakcroak");
    assertNotNull(chain);
    assertTrue(chain.size() == 1);
  }

  /**
   * Test of changeKeyPassword method, of class org.astrogrid.community.server.sso.CredentialStore.
   */
  public void testChangeKeyPassword() throws Exception {
    CredentialStore sut = new CredentialStore();
    sut.changeKeyPassword("frog", "croakcroak", "ribbitribbit");
    PrivateKey key = sut.getPrivateKey("frog", "ribbitribbit");
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
  
}
