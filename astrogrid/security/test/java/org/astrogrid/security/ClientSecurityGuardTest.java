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
    String accountName = "ivo://org.astrogrid.mock/Fred";
    ClientSecurityGuard sg = ClientSecurityGuard.getInstance();
    sg.setSsoUsername(accountName);
    sg.setSsoPassword("secret");
    System.out.println("Username before sign-on: " + sg.getSsoUsername());
    System.out.println("Password before sign-on: " + sg.getSsoPassword());
    sg.signOn();
    NonceToken t = sg.getNonceToken();
    assertNotNull("NonceToken", t);
    System.out.println("Value of token: " + t.toString());
    System.out.println("Account after sign-on: " + t.getAccount());
    System.out.println("Password after sign-on: " + sg.getSsoPassword());
    this.assertEquals("Names match", accountName, t.getAccount());
  }

}
