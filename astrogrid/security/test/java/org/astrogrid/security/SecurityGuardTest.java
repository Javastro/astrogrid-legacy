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
   * Tests the username property.
   */
  public void testUsername () throws Exception {
    SecurityGuard g = new SecurityGuard();
    g.setUsername("fred");
    assertEquals("fred", g.getUsername());
  }

  /**
   * Tests the password property.
   */
  public void testPassword () throws Exception {
    SecurityGuard g = new SecurityGuard();
    g.setPassword("secret");
    assertEquals("Password matches", "secret", g.getPassword());
  }

  /**
   * Tests the ssoUsername property.
   */
  public void testSsoUsername () throws Exception {
    SecurityGuard g = new SecurityGuard();
    g.setSsoUsername("fred");
    assertEquals("fred", g.getSsoUsername());
  }

  /**
   * Tests the ssoPassword property.
   */
  public void testSsoPassword () throws Exception {
    SecurityGuard g = new SecurityGuard();
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

    // Test adding the token to the subject.  This test is
    // passed if no exceptions are thrown.
    NonceToken t1 = new NonceToken("secret");  // arbitrary text
    g.getSsoSubject().getPrivateCredentials().add(t1);

    // Test retreiving the token.
    Set tokens = g.getSsoSubject().getPrivateCredentials(NonceToken.class);
    assertEquals("Exactly one token", 1, tokens.size());
    NonceToken t2 = (NonceToken)tokens.iterator().next();
    assertEquals("Tokens match", t1, t2);
  }


  /**
   * Tests the gridSubject property. This property
   * encapsulates credentials from the security guard
   * in a JAAS subject.
   */
  public void testGridSubject () throws Exception {
    SecurityGuard g = new SecurityGuard();
    assertNotNull("Has JAAS Subject", g.getGridSubject());

    // Test adding the token to the subject.  This test is
    // passed if no exceptions are thrown.
    NonceToken t1 = new NonceToken("secret");  // arbitrary text
    g.getGridSubject().getPrivateCredentials().add(t1);

    // Test retreiving the token.
    Set tokens = g.getGridSubject().getPrivateCredentials(NonceToken.class);
    assertEquals("Exactly one token", 1, tokens.size());
    NonceToken t2 = (NonceToken)tokens.iterator().next();
    assertEquals("Tokens match", t1, t2);
  }


  /**
   * Tests the use of an AstroGrid security token.
   */
  public void testNonceToken () throws Exception {
    NonceToken t1 = new NonceToken("Fred", "secret");
    SecurityGuard g = new SecurityGuard();
    g.setNonceToken(t1);

    Subject s = g.getGridSubject();
    Set tokens = s.getPrivateCredentials(NonceToken.class);
    assertEquals("Exactly one token", 1, tokens.size());
    Set names = s.getPrincipals();
    assertEquals("Exactly one Principal", 1, names.size());

    NonceToken t2 = g.getNonceToken();
    assertEquals("Tokens match", t1, t2);
  }

}