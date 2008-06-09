package org.astrogrid.security;

import java.net.URI;
import java.net.URISyntaxException;
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
   * The IVORN as a parsed URI.
   */
  private URI account;
  
  /**
   * The IVORN for the community hosting the account.
   */
  private URI community;
  
  /**
   * Constructs a AccountIvorn.
   *
   * @param account The IVORN in string form.
   * @throws URISyntaxException If the given string is not a valid URI.
   * @throws URISyntaxException If the scheme of the URI is not ivo.
   * @throws URISyntaxException If the URI does not contain a user name.
   * @throws URISyntaxException If the community IVORN cannot be formed form the given URI.
   */
  public AccountIvorn(String account) throws URISyntaxException {
    this.account = new URI(account);
    this.verify();
    String s = "ivo://" +
               this.account.getHost() +
               this.account.getPath();
    this.community = new URI(s);
  }
  
  /**
   * Constructs a AccountIvorn.
   *
   * @param account The IVORN in string form.
   * @throws URISyntaxException If the scheme of the URI is not ivo.
   * @throws URISyntaxException If the URI does not contain a user name.
   * @throws URISyntaxException If the community IVORN cannot be formed form the given URI.
   */
  public AccountIvorn(URI account) throws URISyntaxException {
    this.account = account;
    this.verify();
    String s = "ivo://" +
               this.account.getHost() +
               this.account.getPath();
    this.community = new URI(s);
  }
  
  /**
   * Checks that the account URI is the right kind of URI. 
   * This is for use only in the constructors.
   */
  private void verify() throws URISyntaxException {
    if (this.account.getUserInfo() == null) {
      throw new URISyntaxException("The given URI is not a valid account-name: " +
                                   "can't find the user name.",
                                   this.account.toString());
    }
    if (!this.account.getScheme().equals("ivo")) {
      throw new URISyntaxException("The given URI is not a valid account-name: " + 
                                   "The scheme of the URI is not ivo.",
                                   this.account.toString());
    }
  }

  /**
   * Reveals the name of the principal.
   *
   * @return The name: i.e. the IVORN in string form.
   */
  public String getName() {
    return this.account.toString();
  }
  
  /**
   * Returns the string form of the IVORN.
   *
   * @return The IVORN in string form.
   */
  public String toString() {
    return this.account.toString();
  }
  
  /**
   * Reveals the user name. E.g., if the account is named ivo://FredHoyle@IoA
   * then the user name is FredHoyle.
   *
   * @return The user name.
   */
  public String getUserName() {
    return this.account.getUserInfo();
  }

  /**
   * Reveals the IVORN for the community hosting the account. E.g., if the
   * account is named ivo://FredHoyle@IoA/community, then the community IVORN
   * is ivo://IoA/community.
   *
   * return The URI for the community.
   */
  public URI getCommunityIvorn() {
    return this.community;
  }
  
  /**
   * Represents the IVORN as a java.net.URI.
   *
   * @return The URI for the account.
   */
  public URI toUri() {
    return this.account;
  }
}
