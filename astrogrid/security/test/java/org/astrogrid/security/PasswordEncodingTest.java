package org.astrogrid.security;

import junit.framework.TestCase;

/**
 * JUnit tests for {@link PasswordEncoding}.
 *
 * @author Guy Rixon
 */
public class PasswordEncodingTest extends TestCase {

  public void testRoundTripWithMatch () throws Exception {
    String clearTextPassword = "secret";
    PasswordEncoding pe1 = new PasswordEncoding(clearTextPassword);
    String nonce = pe1.getNonce();
    String timestamp = pe1.getTimestamp();
    String encodedPassword = pe1.getEncodedPassword();
    System.out.println("Nonce:     " + nonce);
    System.out.println("Timestamp: " + timestamp);
    System.out.println("Password:  " + clearTextPassword);
    System.out.println("Hash:      " + encodedPassword);

    PasswordEncoding pe2 = new PasswordEncoding(encodedPassword,
                                                nonce,
                                                timestamp);
    assertTrue(pe2.nonce.length == pe1.nonce.length);
    System.out.println(new String(pe2.nonce));
    System.out.println(new String(pe1.nonce));
    for (int i = 0; i < pe2.nonce.length; i++) {
      assertEquals(pe2.nonce[i], pe1.nonce[i]);
    }
    boolean matched = pe2.match(clearTextPassword);
    System.out.println("Matched:   " + matched);

    this.assertTrue(matched);
  }


}