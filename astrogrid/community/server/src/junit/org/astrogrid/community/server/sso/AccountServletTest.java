/*
 * AccountServletTest.java
 * JUnit based test
 *
 * Created on July 31, 2008, 6:56 PM
 */

package org.astrogrid.community.server.sso;

import java.io.File;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import junit.framework.TestCase;
import org.astrogrid.community.server.CommunityConfiguration;
import org.bouncycastle.openssl.PEMWriter;
import org.mortbay.jetty.testing.HttpTester;
import org.mortbay.jetty.testing.ServletTester;

/**
 * JUnit tests for {@link AccountServlet}.
 * The tests use an embedded Jetty server.
 *
 * @author Guy Rixon
 */
public class AccountServletTest extends TestCase {
  
  private ServletTester tester;

  @Override
  protected void setUp() throws Exception {
    File here = new File("target");
    new CommunityConfiguration().setCredentialDirectory(here);

    PondLifeDb pond = new PondLifeDb();
    pond.initialize();
    
    // Start the embedded Jetty server with the SUT in its context.
    // The servlet is bound to the path /community/accounts/*.
    tester = new ServletTester();
    tester.setAttribute(AccountServlet.CREDENTIAL_DIR_ATTR, here.toString());
    tester.setContextPath("/community");
    tester.addServlet(AccountServlet.class, "/accounts/*");
    tester.start();
  }
  
  public void testGoodPostToProxy() throws Exception {
    
    // Generate the content of the HTTP request.
    KeyPair keys = generateKeyPair();
    StringWriter key = new StringWriter();
    PEMWriter pw = new PEMWriter(key);
    pw.writeObject(keys.getPublic());
    pw.close();
    StringWriter content = new StringWriter();
    content.write("password=croakcroak");
    content.write("&lifetime=36000");
    content.write("&key=");
    content.write(URLEncoder.encode(key.toString(), "UTF-8"));
    
    System.out.println(content.toString());
    
    HttpTester request = new HttpTester();
    HttpTester response = new HttpTester();
    request.setMethod("POST");
    request.setHeader("Host", "tester");
    request.setVersion("HTTP/1.0");
    request.setURI("/community/accounts/frog/proxy");
    request.addHeader("Content-Type", "application/x-www-form-urlencoded");
    request.setContent(content.toString());
    response.parse(tester.getResponses(request.generate()));
    assertEquals(200, response.getStatus());
  }
  
  public void testGoodPostToAccount() throws Exception {
    assertEquals(200, changePassword("frog", 
                                     "croakcroak",
                                     "ribbitribbit"));
  }
  
  public void testBadPostToAccountWrongAccount() throws Exception {
    assertEquals(404, changePassword("gnu", // Not a pond dweller :) 
                                     "croakcroak",
                                     "ribbitribbit"));
  }
  
  public void testBadPostToAccountNoNewPassword() throws Exception {
    assertEquals(400, changePassword("frog", 
                                     "croakcroak",
                                     null));
  }
  
  public void testBadPostToAccountNoOldPassword() throws Exception {
    assertEquals(400, changePassword("frog", 
                                     null,
                                     "ribbitribbit"));
  }
  
  public void testBadPostToAccountWrongOldPassword() throws Exception {
    assertEquals(403, changePassword("frog", 
                                     "wrong",
                                     "ribbitribbit"));
  }
  
  public void testBadPostToAccountPasswordTooShort() throws Exception {
    assertEquals(400, changePassword("frog", 
                                     "croakcroak",
                                     "<7"));
  }

  /**
   * Generates a key-pair to use in the proxy certificate. 
   * The keys are RSA keys.
   *
   * @return The key-pair.
   */
  private KeyPair generateKeyPair() {
    try {
      return KeyPairGenerator.getInstance("RSA").generateKeyPair();
    } catch (Exception e) {
      // This cannot happen in service unless there is a bug.
      throw new RuntimeException("Failed to generate a key pair", e);
    }
  }
  
  private int changePassword(String user,
                             String oldPassword, 
                             String newPassword) throws Exception {
    // Generate the content of the HTTP request.
    StringWriter content = new StringWriter();
    if (oldPassword != null) {
      content.write("oldPassword=");
      content.write(URLEncoder.encode(oldPassword, "UTF-8"));
    }
    if (newPassword != null) {
      if (oldPassword != null) {
        content.write('&');
      }
      content.write("newPassword=");
      content.write(URLEncoder.encode(newPassword, "UTF-8"));
    }
    
    System.out.println(content.toString());
    
    HttpTester request = new HttpTester();
    HttpTester response = new HttpTester();
    request.setMethod("POST");
    request.setHeader("Host", "tester");
    request.setVersion("HTTP/1.0");
    request.setURI("/community/accounts/" + user);
    request.addHeader("Content-Type", "application/x-www-form-urlencoded");
    request.setContent(content.toString());
    response.parse(tester.getResponses(request.generate()));
    return response.getStatus();
  }
  
}
