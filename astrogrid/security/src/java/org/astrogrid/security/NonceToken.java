package org.astrogrid.security;

import org.astrogrid.community.common.security.data.SecurityToken;

/**
 * Trivial extension of
 * org.astrogrid.community.common.security.data.SecurityToken.
 * This class just changes the name, so as to be more specific
 * about what kind of token is represented.
 *
 * A NonceToken composes an account name, which must be expressed
 * as an IVORN and a nonce.
 *
 * @author Guy Rixon
 */
public class NonceToken extends SecurityToken {

  /**
   * Constructs a NonceToken from a given string.
   * The string must follow the conventions of the
   * AstroGrid community component.
   *
   * @param token the nonce, including both nonce and account name
   */
  public NonceToken (String token) {
    super(token);
  }

  /**
   * Constructs a NonceToken from a pair of strings.
   * The strings must follow the conventions of the
   * AstroGrid community component.
   *
   * @param account the account name
   * @param nonce   the nonce
   */
  public NonceToken (String account, String nonce) {
    super(account, nonce);
  }


  /**
   * Constructs a NonceToken by cloning a token
   * obtained from the community service.
   *
   * @param t the community's original token
   */
  public NonceToken (SecurityToken t) {
    super();
    this.setAccount(t.getAccount());
    this.setToken(t.getToken());
    this.setStatus(t.getStatus());
  }

}