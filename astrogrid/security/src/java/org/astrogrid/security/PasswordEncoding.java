package org.astrogrid.security;

import java.security.MessageDigest;
import java.security.SecureRandom;
import javax.xml.soap.SOAPException;

/**
 * Encoder and checker for passwords.
 *
 * This class provides static methods for encoding passwords with
 * Secure Hash Agorithm 1 (SHA-1) hashing and base-64 encoding.
 *
 * The SHA-1 implemention comes with the JDK and the base-64
 * implementation is public-domain code from Sourceforge. This
 * class is just a wrapper.
 *
 * @author Guy Rixon
 */
public class PasswordEncoding {

  /**
   * Takes a clear-text password and returns the encoded version.
   * An optional nonce and optional timestamp may be used.
   *
   * @param password the clear-text password
   * @nonce the optional nonce (set to null if no nonce is required)
   * @timestamp the optional timestamp (set to null if no timestamp is required)
   *
   * @return the endcoded password
   *
   * @throws SOAP exception if the encoding fails
   */
  public static String encode (String password,
                               String nonce,
                               String timestamp) throws SOAPException {
    try {
      String p2 = (timestamp == null)? password : timestamp + password;
      String p3 = (nonce == null)? p2 : nonce + p2;
      System.out.println(p3);
      MessageDigest md = MessageDigest.getInstance("SHA");
      md.update(p3.getBytes());
      byte[] digest = md.digest();
      return Base64.encodeBytes(digest);
    }
    catch (Exception e) {
      throw new SOAPException("Failed to encoded a password", e);
    }
  }

  /**
   * Takes a clear-text password and an encoded password and
   * checks to see if they match. An optional nonce and optional
   * timestamp may be used. NB: if the nonce was used to encode the
   * password then the match will fail unless it is supplied;
   * similarly, the timestamp must be given if it was used to encode
   * the password.
   *
   * @encodedDigest the encoded password
   * @param password the clear-text password
   * @nonce the optional nonce (set to null if no nonce is required)
   * @timestamp the optional timestamp (set to null if no timestamp is required)
   *
   * @return the endcoded password
   *
   * @throws SOAP exception if the encoding fails
   */
  public static boolean match (String encodedDigest,
                               String password,
                               String nonce,
                               String timestamp) throws SOAPException {

    String inferredEncodedDigest
        = PasswordEncoding.encode(password, nonce, timestamp);
    return (encodedDigest.equals(inferredEncodedDigest));
  }

  /**
   * Generates a base-64-encoded nonce.
   */
  public static String generateEncodedNonce () throws SOAPException {
    try {
      SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
      byte[] b = new byte[20];
      prng.nextBytes(b);
      return Base64.encodeBytes(b);
    }
    catch (Exception e) {
      throw new SOAPException("Failed to generate a nonce", e);
    }
  }

}