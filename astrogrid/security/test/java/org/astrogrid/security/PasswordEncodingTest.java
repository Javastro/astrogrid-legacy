package org.astrogrid.security;

import junit.framework.TestCase;

/**
 * JUnit tests for {@link PasswordEncoding}.
 *
 * @author Guy Rixon
 */
public class PasswordEncodingTest extends TestCase {

  public void testRoundTripPasswordOnly () throws Exception {
    String clearTextPassword = "secret";
    String nonce = null;
    String timestamp = null;
    String encodedPassword = PasswordEncoding.encode(clearTextPassword,
                                                     nonce,
                                                     timestamp);
    boolean matched = PasswordEncoding.match(encodedPassword,
                                             clearTextPassword,
                                             nonce,
                                             timestamp);
    this.assertTrue(matched);
  }


  public void testRoundTripPasswordNonceTimestamp () throws Exception {
    String clearTextPassword = "secret";
    String nonce = "wibble";
    String timestamp = "2004-03-03T17:18.00.000";
    String encodedPassword = PasswordEncoding.encode(clearTextPassword,
                                                     nonce,
                                                     timestamp);
    boolean matched = PasswordEncoding.match(encodedPassword,
                                             clearTextPassword,
                                             nonce,
                                             timestamp);
    this.assertTrue(matched);

    matched = PasswordEncoding.match(encodedPassword,
                                     clearTextPassword,
                                     null,
                                     null);
    this.assertFalse(matched);
  }


}