package org.astrogrid.security;

import junit.framework.TestCase;


/**
 * JUnit tests for {@link org.astrogrid.security.AccountName}.
 *
 * @author Guy Rixon
 */
public class AccountNameTest extends TestCase {

  public void testEquality() throws Exception {
    AccountName n1 = new AccountName("Fred");
    AccountName n2 = new AccountName("Fred");
    AccountName n3 = new AccountName("Bill");
    assertTrue(n1.equals(n2));
    assertFalse(n2.equals(n3));
    assertFalse(n1.equals("Fred")); // Can't match against String
  }

  public void testToString() throws Exception {
    AccountName n = new AccountName("Mary");
    System.out.println(n.toString());
    assertTrue("Mary".equals(n.toString()));
  }

}