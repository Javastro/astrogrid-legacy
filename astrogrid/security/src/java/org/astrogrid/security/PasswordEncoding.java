package org.astrogrid.security;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Date;
import javax.xml.soap.SOAPException;

/**
 * A password with associated nonce and timestamp, as specified in
 * WS-Security. This class supports the password hashing and encoding
 * algorithms of WS-Security.
 *
 * @author Guy Rixon
 */
public class PasswordEncoding {

  /**
   * The password, before hashing.
   */
  private String plainPassword;

  /**
   * The password, after hashing and base-64 encoding.
   */
  private String encodedPassword;

  /**
   * The nonce, unencoded and unhashed.
   */
  protected byte[] nonce;

  /**
   * The timestamp as an ISO8601 string.
   */
  protected String timestamp;

  /**
   * A formatter for using ISO8601 timestamps.
   */
  private static Iso8601DateFormat iso8601 = new Iso8601DateFormat();


  /**
   * Constructs a Password. The associated nonce is generated
   * and stored internally. The timestamp is taken and stored
   * internally.
   *
   * @param password the plain-text password
   */
  public PasswordEncoding (String password) throws SOAPException {
    try {
      this.plainPassword = password;
      SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
      this.nonce = new byte[16];
      prng.nextBytes(nonce);
      this.timestamp = iso8601.format(new Date());
    }
    catch (Exception e) {
      throw new SOAPException("Failed to construct a Password", e);
    }
  }


  /**
   * Constructs a password. The given nonce, timestamp and
   * encoded password are stored internally; no new nonces
   * or timestamps are generated.
   */
  public PasswordEncoding (String password,
                           String nonce,
                           String timestamp) throws SOAPException {
    try {
      this.nonce = Base64.decode(nonce);
      this.timestamp = timestamp;
      this.encodedPassword = password;
    }
    catch (Exception e) {
      throw new SOAPException("Failed to construct a password", e);
    }
  }


  /**
   * Returns the nonce in a form ready to serialize as XML.
   *
   * @return the nonce, in base-64 encoding
   */
  public String getNonce () {
    return Base64.encodeBytes(this.nonce);
  }


  /**
   * Returns the timestamp, in a form ready to serialize as XML.
   *
   * @return the timestamp in ISO8601 format with no encoding.
   */
  public String getTimestamp () {
    return this.timestamp;
  }


  /**
   * Hash, encode and return the password. The algorithm combines the
   * nonce, timestamp and clear-text password as prescribed by
   * WS-Security.  See the class comments for details.
   *
   * @return the encoded password
   * @throws SOAPException if anything goes wrong
   */
  public String getEncodedPassword () throws SOAPException {
    try {
      return this.encodePassword(this.plainPassword,
                                 this.nonce,
                                 this.timestamp);
    }
    catch (Exception e) {
      throw new SOAPException("Failed to encoded a password", e);
    }
  }


  /**
   * Tests a plain-text password against the cached password.
   * The given password is hashed and encoded and then tested
   * against the encoded password set at construction.
   */
  public boolean match (String plainPassword) throws SOAPException {
    try {
      String candidate = this.encodePassword(plainPassword,
                                             this.nonce,
                                             this.timestamp);
      return (candidate.equals(this.encodedPassword));
    }
    catch (Exception e) {
      throw new SOAPException("Failed to encode a password", e);
    }
  }


  /**
   * Applies the hashing and encoding algorithms.
   */
  private String encodePassword(String plainPassword,
                                byte[] nonce,
                                String timestamp) throws Exception {

    // Concatenate nonce, timestamp and password.
    // This has to be done with byte arrays since the nonce
    // is not character data.
    byte[] timestampBytes = timestamp.getBytes("UTF8");
    byte[] passwordBytes  = plainPassword.getBytes("UTF8");
    int totalLength = nonce.length
                    + timestampBytes.length
                    + passwordBytes.length;
    byte[] ensemble = new byte[totalLength];
    int i = 0;
    for (int j = 0; j < nonce.length; j++) {
      ensemble[i] = this.nonce[j];
      i++;
    }
    for (int k = 0; k < timestampBytes.length; k++) {
      ensemble[i] = timestampBytes[k];
      i++;
    }
    for (int l = 0; l < passwordBytes.length; l++) {
      ensemble[i] = passwordBytes[l];
      i++;
    }
    System.out.println("timestampBytes: " + new String(timestampBytes));
    System.out.println("passwordBytes:  " + new String(passwordBytes));
    System.out.println("nonce:          " + new String(nonce));
    System.out.println("ensemble:       " + new String(ensemble));

    // Hash the assembled byte array.
    MessageDigest md = MessageDigest.getInstance("SHA");
    md.update(ensemble);
    byte[] digest = md.digest();
    System.out.println("digest:         " + new String(digest));

    // Encode the bytes and return them as a string.
    return Base64.encodeBytes(digest);
  }

}