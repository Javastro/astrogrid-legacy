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
public class Password {

  /**
   * The password, before hashing.
   */
  private String plainPassword;

  /**
   * The nonce, unencoded and unhashed.
   */
  protected byte[] nonce;

  /**
   * The timestamp as an ISO8601 string.
   */
  protected String timestamp;

  /**
   * True if the password should be encoded for transmission.
   */
  private boolean encodable;

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
   * @param encodable true if the password should be encoded
   * for transmission
   */
  public Password (String  password,
                   boolean encodable) throws SOAPException {
    this.plainPassword = password;
    this.nonce         = Password.generateNonce();
    this.timestamp     = Password.generateTimestamp();
    this.encodable     = encodable;
  }


  /**
   * Constructs a password. The given nonce, timestamp and
   * encoded password are stored internally; no new nonces
   * or timestamps are generated.
   *
   * @param password  the plain-text password
   * @param nonce     the nonce, in base-64 encoding
   * @param timestamp the timestamp, in plain text
   * @boolean encodable true if the password should be
   * encoded for transmission
   */
  public Password (String  password,
                   String  nonce,
                   String  timestamp,
                   boolean encodable) throws SOAPException {
    try {
      this.nonce         = Base64.decode(nonce);
      this.timestamp     = timestamp;
      this.plainPassword = password;
      this.encodable     = encodable;
    }
    catch (Exception e) {
      throw new SOAPException("Failed to construct a password", e);
    }
  }


  /**
   * Renew the nonce and timestamp fields of the object.
   */
  public void renewNonceAndTimestamp () throws SOAPException {
    this.nonce     = Password.generateNonce();
    this.timestamp = Password.generateTimestamp();
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
   * Tests a plain-text password against the cached password.
   * The given password is hashed and encoded and then tested
   * against the encoded password set at construction.
   */
  public boolean match (String candidate) throws SOAPException {
    return (candidate.equals(this.getEncodedPassword()));
  }

  /**
   * Reveals the plain-text password.
   *
   * @return the password
   */
  public String getPlainPassword () {
    return this.plainPassword;
  }


  /**
   * Applies the hashing and encoding algorithms.
   *
   * @return the encoded password
   */
  public String getEncodedPassword() throws SOAPException {
    try {

      // Concatenate nonce, timestamp and password.
      // This has to be done with byte arrays since the nonce
      // is not character data.
      byte[] timestampBytes = this.timestamp.getBytes("UTF8");
      byte[] passwordBytes  = this.plainPassword.getBytes("UTF8");
      int totalLength = this.nonce.length
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
    catch (Exception e) {
      throw new SOAPException("Failed to encode a password");
    }
  }


  /**
   * Indicates whether the password should be encoded for transmission.
   */
  public boolean isEncodable () {
    return this.encodable;
  }


  /**
   * Regenerates a nonce.
   *
   * @return the nonce, with no encoding
   */
  private static byte[] generateNonce () throws SOAPException {
    try {
      SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
      byte[] nonce = new byte[16];
      prng.nextBytes(nonce);
      return nonce;
    }
    catch (Exception e) {
      throw new SOAPException("Failed to construct a nonce", e);
    }
  }

  /**
   * Regenerates a timestamp.
   *
   * @return the stampstamp in ISO8601 format
   */
  private static String generateTimestamp () throws SOAPException {
    try {
      return Password.iso8601.format(new Date());
    }
    catch (Exception e) {
      throw new SOAPException("Failed to construct a timestamp", e);
    }
  }

}