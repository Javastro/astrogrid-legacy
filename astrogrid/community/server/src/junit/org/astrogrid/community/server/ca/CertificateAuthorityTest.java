package org.astrogrid.community.server.ca;

import junit.framework.*;
import java.io.File;

/**
 * JUnit tests for org.astrogrid.community.server.ca.CertificateAuthority.
 *
 * @author Guy Rixon
 */
public class CertificateAuthorityTest extends TestCase {
  
  /**
   * Test of generateKeyPair method, of class ca.CertificateAuthority.
   */
  public void testGenerateKeyPair() throws Exception {
    System.out.println("generateKeyPair");
    
    String rootDn = "/O=foo/OU=bar";
    String password = "fubar";
    File keyFile = this.createFreshFile("ca.key");
    File certificateFile = this.createFreshFile("ca.crt");
    File serialFile = this.createFreshFile("ca.srl");
    File myProxyDirectory = new File("/opt/globus4.0.2/var/myproxy");
    CertificateAuthority instance = 
        new CertificateAuthority(rootDn,
                                 password,
                                 keyFile,
                                 certificateFile,
                                 serialFile,
                                 myProxyDirectory);
    
    instance.generateKeyPair(password, keyFile);
    
    assertTrue("Key file was created", keyFile.exists());
  }
  
  /**
   * Tests the creation of a new CA.
   */
  public void testGenerateCa() throws Exception {
    String rootDn = "/O=foo/OU=bar";
    String password = "fubar";
    
    File keyFile = this.createFreshFile("ca.key");
    assertNotNull("Key file-name was generated", keyFile);
    
    File certificateFile = this.createFreshFile("ca.crt");
    assertNotNull("Certificate file-name was generated", certificateFile);
    System.out.println(keyFile.getAbsolutePath());
    
    File serialFile = this.createFreshFile("ca.srl");
    assertNotNull("Serial file-name was generated", serialFile);
    System.out.println(certificateFile.getAbsolutePath());
    
    File myProxyDirectory = new File("/opt/globus4.0.2/var/myproxy");
    assertNotNull(myProxyDirectory);
    CertificateAuthority instance = 
        new CertificateAuthority(rootDn,
                                 password,
                                 keyFile,
                                 certificateFile,
                                 serialFile,
                                 myProxyDirectory);
    assertTrue("Key file should exist", keyFile.exists());
    assertTrue("Certificate file should exist", certificateFile.exists());
    assertTrue("Serial file should exist", serialFile.exists());
  }
  
  /**
   * Tests the generation of a CA object from existing credentials.
   */
  public void testReuseCa() throws Exception {
    
    // Create a CA with new cerdentials.
    String rootDn = "/O=foo/OU=bar";
    String password = "fubar";
    File keyFile = this.createFreshFile("ca.key");
    File certificateFile = this.createFreshFile("ca.crt");
    File serialFile = this.createFreshFile("ca.srl");
    File myProxyDirectory = new File("/opt/globus4.0.2/var/myproxy");
    CertificateAuthority instance = 
        new CertificateAuthority(rootDn,
                                 password,
                                 keyFile,
                                 certificateFile,
                                 serialFile,
                                 myProxyDirectory);
    
    // Create another CA using the same credentials from file.
    CertificateAuthority sut = 
        new CertificateAuthority(password,
                                 keyFile,
                                 certificateFile,
                                 serialFile,
                                 myProxyDirectory);
    assertNotNull("CA was recovered from files", sut);
  }
  
  /**
   * Tests the generation of a CA object from existing credentials,
   * but with an incorrect password.
   */
  public void testReuseCaBadPassword() throws Exception {
    
    // Create a CA with new cerdentials.
    String rootDn = "/O=foo/OU=bar";
    String password = "fubar";
    File keyFile = this.createFreshFile("ca.key");
    File certificateFile = this.createFreshFile("ca.crt");
    File serialFile = this.createFreshFile("ca.srl");
    File myProxyDirectory = new File("/opt/globus4.0.2/var/myproxy");
    CertificateAuthority instance = 
        new CertificateAuthority(rootDn,
                                 password,
                                 keyFile,
                                 certificateFile,
                                 serialFile,
                                 myProxyDirectory);
    
    // Create another CA using the same credentials from file.
    try {
      CertificateAuthority sut = 
          new CertificateAuthority("wrong",
                                   keyFile,
                                   certificateFile,
                                   serialFile,
                                   myProxyDirectory);
      fail("CA object should not be obtained with a bad password.");
    }
    catch (Exception e) {
      // Expected.
    }
  }
  
  /**
   * Creates a File object for a file that does not yet exist.
   * Deletes any file with that name.
   */
  private File createFreshFile(String fileName) throws Exception {
    File f = new File(fileName);
    if (f.exists()) {
      f.delete();
    }
   if (f.exists()) {
      throw new Exception("Failed to delete " + fileName);
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
                                 new File("/tmp"));
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
