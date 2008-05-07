package org.astrogrid.security;

import java.security.Principal;

/**
 * A principal that identifies an account within an IVO community. The community
 * is identified by the IVORN of its community service, e.g. 
 * ivo://uk.ac.cam.ast/community and the account by the user name, e.g.
 * FredHoyle. The combination can be written ivo://FredHoyle@uk.ac.cam.ast/community.
 * Posession of this kind of principal implies that the the user name has been
 * authenticated, usually with a password, at the community service associated
 * with the community IVORN.
 *
 * @author Guy Rixon
 */
public class AccountIvorn implements Principal {
  
  /**
   * The IVORN in string form.
   */
  String serialized;
  
  /**
   * Constructs a AccountIvorn.
   */
  public AccountIvorn(String account) throws Exception {
    this.serialized = account;
  }

  public String getName() {
    return this.serialized;
  }
  
}
