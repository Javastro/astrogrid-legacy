package org.astrogrid.community.server.ca;

import junit.framework.*;
import java.io.File;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * JUnit tests for org.astrogrid.community.server.ca.CertificateAuthority.
 *
 * @author Guy Rixon
 */
public class CertificateAuthorityTest extends TestCase {
  
  public CertificateAuthorityTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(CertificateAuthorityTest.class);
    
    return suite;
  }

  public void testCreateCa() throws Exception {
    System.out.println("constructor that creates CA files");
    
    File caKeyFile = this.createFreshFile("ca.key");
    File caCertificateFile = this.createFreshFile("ca.crt");
    File caSerialFile = this.createFreshFile("ca.srl");
    File myProxyDirectory = new File(".");
    
    assertFalse(caKeyFile.exists());
    assertFalse(caCertificateFile.exists());
    assertFalse(caSerialFile.exists());
    CertificateAuthority instance = 
        new CertificateAuthority("/O=foo/OU=bar baz",
                                 "fubar",
                                 caKeyFile,
                                 caCertificateFile,
                                 caSerialFile,
                                 myProxyDirectory);
    assertTrue(caKeyFile.exists());
    assertTrue(caCertificateFile.exists());
    assertTrue(caSerialFile.exists());
  }
  
  
  /**
   * Test of generateKeyPair method, of class ca.CertificateAuthority.
   */
  public void testGenerateKeyPair() throws Exception {
    System.out.println("generateKeyPair");
    
    String password = "fubar";
    File keyFile = this.createFreshFile("ca.key");
    File certificateFile = this.createFreshFile("ca.crt");
    File serialFile = this.createFreshFile("ca.srl");
    File myProxyDirectory = new File("/opt/globus4.0.2/var/myproxy");
    CertificateAuthority instance = 
        new CertificateAuthority("/O=foo/OU=bar",
                                 password,
                                 keyFile,
                                 certificateFile,
                                 serialFile,
                                 myProxyDirectory);
    
    instance.generateKeyPair(password, keyFile);
    
    assertTrue("Key file was created", keyFile.exists());
  }
  
  private File createFreshFile(String fileName) throws Exception {
    File f = new File(fileName);
    if (f.exists()) {
      f.delete();
    }
    return f;
  }

  /**
   * Test of generateUserCredentials method, of class ca.CertificateAuthority.
   */
  public void testGenerateUserCredentials() throws Exception {
    System.out.println("generateUserCredentials");
    
    CertificateAuthority instance = 
        new CertificateAuthority("/O=foo/OU=bar baz", 
                                 "fubar",
                                 new File("ca.key"),
                                 new File("ca.crt"),
                                 new File("ca.srl"),
                                 new File("."));
    instance.generateCa();
    
    this.createUser(instance, "fred");
    this.createUser(instance, "bill");
    
  }
  
  public void testChangePassword() throws Exception {
    CertificateAuthority instance = 
        new CertificateAuthority("/O=foo/OU=bar baz", 
                                 "fubar",
                                 new File("ca.key"),
                                 new File("ca.crt"),
                                 new File("ca.srl"),
                                 new File("/opt/globus4.0.2/var/myproxy"));
    instance.generateCa();
    
    this.createUser(instance, "fred");
    instance.changePasswordInMyProxy("fred", "fredfred", "frederick");
  }

  private void createUser(CertificateAuthority instance,
                          String commonName) throws Exception {
    
    String loginName = commonName;
    String password = commonName + commonName;
    UserFiles userFiles = new UserFiles(new File("."), loginName);
    userFiles.deleteUserFiles();
    
    instance.setUserCredentialsInMyProxy(loginName, 
                                         commonName, 
                                         password, 
                                         userFiles);
    
    assertTrue(userFiles.getKeyFile().exists());
    assertTrue(userFiles.getCertificateFile().exists());
  }

  
}
