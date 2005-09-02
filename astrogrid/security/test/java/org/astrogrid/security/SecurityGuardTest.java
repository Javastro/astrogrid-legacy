package org.astrogrid.security;

import java.util.Set;
import javax.security.auth.Subject;
import junit.framework.TestCase;


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
   * Tests the gridSubject property. This property
   * encapsulates credentials from the security guard
   * in a JAAS subject.
   */
  public void testSsoSubject () throws Exception {
    SecurityGuard g = new SecurityGuard();
    assertNotNull("Has JAAS Subject", g.getSsoSubject());

    // Test that no default credentials are added by the constructor.
    assertTrue(g.getSsoSubject().getPrivateCredentials().isEmpty());

    // Test adding the token to the subject.  This test is
    // passed if no exceptions are thrown.
    Object c1 = new Object();
    g.getSsoSubject().getPrivateCredentials().add(c1);

    // Test retreiving the token.
    Set credentials = g.getSsoSubject().getPrivateCredentials(Object.class);
    assertEquals("Exactly one credential", 1, credentials.size());
    Object c2 = credentials.iterator().next();
    assertEquals("Credentials match", c1, c2);
  }


  /**
   * Tests the gridSubject property. This property
   * encapsulates credentials from the security guard
   * in a JAAS subject.
   */
  public void testGridSubject () throws Exception {
    SecurityGuard g = new SecurityGuard();
    assertNotNull("Has JAAS Subject", g.getGridSubject());

    // Test that no default credentials are added by the constructor.
    assertTrue(g.getGridSubject().getPrivateCredentials().isEmpty());

    // Test adding the token to the subject.  This test is
    // passed if no exceptions are thrown.
    Object c1 = new Object();
    g.getGridSubject().getPrivateCredentials().add(c1);

    // Test retreiving the token.
    Set credentials = g.getGridSubject().getPrivateCredentials(Object.class);
    assertEquals("Exactly one credential", 1, credentials.size());
    Object c2 = credentials.iterator().next();
    assertEquals("Credentials match", c1, c2);
  }

}