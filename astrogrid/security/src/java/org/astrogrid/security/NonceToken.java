package org.astrogrid.security;

import org.astrogrid.community.common.security.data.SecurityToken;

/**
 * Trivial extension of
 * org.astrogrid.community.common.security.data.SecurityToken.
 * This class just changes the name, so as to be more specific
 * about what kind of token is represented.
 *
 * @author Guy Rixon
 */
public class NonceToken extends SecurityToken {

  public NonceToken (String nonce) {
    super(nonce);
  }

  public NonceToken (String account, String nonce) {
    super(account, nonce);
  }


  public NonceToken (SecurityToken t) {
    super();
    this.setAccount(t.getAccount());
    this.setToken(t.getToken());
    this.setStatus(t.getStatus());
  }

}