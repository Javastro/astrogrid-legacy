package org.astrogrid.security;


import java.net.URI;
import java.security.GeneralSecurityException;
import java.util.Set;
import javax.security.auth.Subject;
import javax.security.auth.x500.X500Principal;
import junit.framework.TestCase;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.security.authorization.AuthenticatedAccessPolicy;
import org.astrogrid.security.authorization.OpenAccessPolicy;

/**
 * JUnit tests for the {@link SecurityGuard class}.
 *
 * @author Guy Rixon
 */
public class SecurityGuardTest extends TestCase {
  
  /**
   * To allow unit testing, some of the objects used by the security guard
   * must make mockeries of themselves.
   */
  public void setUp() {
    SimpleConfig.getSingleton().setProperty(
      "org.astrogrid.security.community.RegistryClient.mock",
      "true"
    );
    SimpleConfig.getSingleton().setProperty(
      "org.astrogrid.security.community.SsoClient.mock",
      "true"
    );
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
    SimpleConfig.getSingleton().setProperty("org.astrogrid.security.mock.community", "true");
    SecurityGuard sut = new SecurityGuard();
    sut.signOn("ivo://frog@pond/community", "croakcroak", 36000);
    assertEquals("ivo://frog@pond/community", sut.getAccountIvorn().toString());
  }
  
  /**
   * Tests the sign-on function. This mocks the community.
   */
  public void testSignOn4ArgumentCommunity() throws Exception {
    SimpleConfig.getSingleton().setProperty("org.astrogrid.security.mock.community", "true");
    SecurityGuard sut = new SecurityGuard();
    URI community = new URI("ivo://pond/community");
    sut.signOn("frog", "croakcroak", 36000, community);
    assertEquals("ivo://frog@pond/community", sut.getAccountIvorn().toString());
  }
  
  /**
   * Tests the sign-on function. This mocks the community.
   */
  public void testFailedSignOn4ArgumentCommunity() throws Exception {
    SimpleConfig.getSingleton().setProperty("org.astrogrid.security.mock.community", "true");
    SecurityGuard sut = new SecurityGuard();
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
    URI keyStore = this.getClass().getResource("/tester.jks").toURI();
    sut.signOn("tester", "testing", 36000, keyStore);
    assertNull(sut.getAccountIvorn());
    assertNotNull(sut.getCertificateChain());
    assertNotNull(sut.getPrivateKey());
    assertNotNull(sut.getX500Principal());
  }
  
}