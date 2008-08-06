package org.astrogrid.security;


import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.util.Set;
import javax.security.auth.Subject;
import javax.security.auth.x500.X500Principal;
import junit.framework.TestCase;
import org.astrogrid.community.server.sso.AccountServlet;
import org.astrogrid.security.community.MockRegistry;
import org.astrogrid.community.server.sso.PondLifeDb;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.security.authorization.AuthenticatedAccessPolicy;
import org.astrogrid.security.authorization.OpenAccessPolicy;
import org.astrogrid.security.community.MockRegistryClient;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

/**
 * JUnit tests for the {@link SecurityGuard class}.
 * <p>
 * Some of the tests involve calls to the accounts web-service in a
 * community installation. For these, the web-service is run locally, in an
 * embedded copy of Jetty, using a local database of accounts. In order that
 * the software under test find this service, a MockRegistryClient is
 * injected.
 *
 * @author Guy Rixon
 */
public class SecurityGuardTest extends TestCase {
  
  private Server jetty;
  
  public void setUp() throws Exception {
    
    // Create a DB with test accounts.
    PondLifeDb pond = new PondLifeDb();
    pond.initialize();
    
    // Run the community service in an embedded copy of Jetty, using the
    // DB initialized above.
    jetty = new Server(MockRegistry.ACCOUNTS_PORT);
    Context c = new Context(jetty,"/community",Context.SESSIONS);
    c.addServlet(new ServletHolder(new AccountServlet()), "/accounts/*");
    jetty.start();
  }
  
  protected void tearDown() throws Exception {
    jetty.stop();
  }

  /**
   * Tests the ssoUsername property.
   */
  public void testSsoUsername () throws Exception {
    SecurityGuard g = new SecurityGuard();
    assertNull("Null SSO user-name after construction", g.getSsoUsername());
    g.setSsoUsername("fred");
    assertEquals("fred", g.getSsoUsername());
  }

  /**
   * Tests the ssoPassword property.
   */

  public void testSsoPassword () throws Exception {
    SecurityGuard g = new SecurityGuard();
    assertNull("Null SSO password after construction", g.getSsoPassword());
    g.setSsoPassword("secret");
    assertEquals("Password matches", "secret", g.getSsoPassword());
  }
  
  /**
   * Tests the copy constructor.
   */
  public void testCopy() throws Exception {
    X500Principal p1 = new X500Principal("CN=foo");
    SecurityGuard g1 = new SecurityGuard();
    g1.setX500Principal(p1);
    g1.setAccessPolicy(new AuthenticatedAccessPolicy());
    
    SecurityGuard g2 = new SecurityGuard(g1);
    X500Principal p2 = g2.getX500Principal();
    assertEquals("Principals match", p1, p2);
    assertTrue("Access policy was copied correctly",
               g2.accessPolicy instanceof AuthenticatedAccessPolicy);
  }

  /**
   * Tests the sign-on function. This mocks the community.
   */
  public void testSignOn3Argument() throws Exception {
    SecurityGuard sut = new SecurityGuard();
    sut.setRegistryClient(new MockRegistryClient());
    sut.signOn("ivo://frog@pond/community", "croakcroak", 36000);
    assertEquals("ivo://frog@pond/community", sut.getAccountIvorn().toString());
  }
  
  /**
   * Tests the sign-on function. This mocks the community.
   */
  public void testSignOn4ArgumentCommunity() throws Exception {
    SecurityGuard sut = new SecurityGuard();
    sut.setRegistryClient(new MockRegistryClient());
    URI community = new URI("ivo://pond/community");
    sut.signOn("frog", "croakcroak", 36000, community);
    assertEquals("ivo://frog@pond/community", sut.getAccountIvorn().toString());
  }
  
  /**
   * Tests the sign-on function. This mocks the community.
   */
  public void testFailedSignOn4ArgumentCommunity() throws Exception {
    SecurityGuard sut = new SecurityGuard();
    sut.setRegistryClient(new MockRegistryClient());
    URI community = new URI("ivo://pond/community");
    try {
      sut.signOn("frog", "wrong", 36000, community);
      fail("Should throw exception for wrong password.");
    }
    catch (GeneralSecurityException e) {
      // Expected.
    }
    assertNull(sut.getAccountIvorn());
  }
  
  /**
   * Tests the sign-on function. This uses local credentials.
   */
  public void testFailedSignOn4Keystore() throws Exception {
    SecurityGuard sut = new SecurityGuard();
    sut.setRegistryClient(new MockRegistryClient());
    URI keyStore = this.getClass().getResource("/tester.jks").toURI();
    sut.signOn("tester", "testing", 36000, keyStore);
    assertNull(sut.getAccountIvorn());
    assertNotNull(sut.getCertificateChain());
    assertNotNull(sut.getPrivateKey());
    assertNotNull(sut.getX500Principal());
  }
  
  public void testPasswordChangeInLocalKeyStore() throws Exception {
    File store = new File("tester.jks");
    TestUtilities.copyResourceToFile(this.getClass(), "/tester.jks", store);
    SecurityGuard sut = new SecurityGuard();
    sut.changePassword("tester", "testing", "tested", store.toURI());
    sut.signOn("tester", "tested", 3600, store.toURI());
  }
  
  public void testPasswordChangeInRemoteKeyStore() throws Exception {
    URI store = new URI("http://some.where/over/the/rainbow");
    SecurityGuard sut = new SecurityGuard();
    sut.setRegistryClient(new MockRegistryClient());
    try {
      sut.changePassword("tester", "testing", "tested", store);
      fail("Should have rejected this password change as impossible.");
    }
    catch (IOException e) {
      // Expected.
    }
  }
  
  public void testPasswordChangeInCommunity() throws Exception {
    URI community = new URI("ivo://pond/community");
    SecurityGuard sut = new SecurityGuard();
    sut.setRegistryClient(new MockRegistryClient());
    sut.changePassword("frog", "croakcroak", "ribbitribbit", community);
    sut.signOn("frog", "ribbitribbit", 36000, community);
  }
  
}