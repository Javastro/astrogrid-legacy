package org.astrogrid.security;

import junit.framework.TestCase;

/**
 * JUnit tests for {@link ClientSecurityGuard}.
 *
 * These test only the extensions to {@link SecurityGuard}.
 *
 * @author Guy Rixon
 */
public class ClientSecurityGuardTest extends TestCase {

  /**
   * Tests the single-sign-on feature.
   */
  public void testSignOn () throws Exception {
    ClientSecurityGuard sg = new ClientSecurityGuard();
    sg.setUsername("Fred");
    sg.setPassword("secret");
    System.out.println("Username before sign-on: " + sg.getUsername());
    System.out.println("Password before sign-on: " + sg.getPassword());
    sg.signOn();
    System.out.println("Username after sign-on: " + sg.getUsername());
    System.out.println("Password after sign-on: " + sg.getPassword());
    this.assertFalse("secret".equals(sg.getPassword()));
  }

}
