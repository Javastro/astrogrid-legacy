package org.astrogrid.security;

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
    assertEquals("secret", g.getPassword());
  }

}