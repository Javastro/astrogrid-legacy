package org.astrogrid.security;


import java.util.Set;
import javax.security.auth.Subject;
import javax.security.auth.x500.X500Principal;
import junit.framework.TestCase;
import org.astrogrid.security.authorization.AuthenticatedAccessPolicy;
import org.astrogrid.security.authorization.OpenAccessPolicy;





/**
 * Unit tests for the {@link SecurityGuard class}.
 * JUnit is used.
 *
 * The SUT is a Java bean with properties but no
 * business logic. Each test case tests the setter
 * and getter for one property, assuming that there
 * are no internal linkagaes between properties.
 *
 * @author Guy Rixon

 */

public class SecurityGuardTest extends TestCase {

  /**
   * Constructs the test suite.
   */
  public SecurityGuardTest () {}



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

}