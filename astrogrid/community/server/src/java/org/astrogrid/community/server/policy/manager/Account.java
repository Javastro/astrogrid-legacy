package org.astrogrid.community.server.policy.manager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.astrogrid.community.common.policy.data.AccountData;

/**
 * Details of an user account.
 *
 * @author Guy Rixon
 */
public class Account extends AccountData {
  
  /**
   * The regular expression for extracting the user-name from the account IVORN.
   */
  private static Pattern pattern = Pattern.compile("ivo://(\\w+)@[^/]+/\\w+");
  
  /**
   * Constructs an Account, taking the data from an AccountData.
   */
  public Account(AccountData o) {
    this.setIdent(o.getIdent());
    this.setDisplayName(o.getDisplayName());
    this.setEmailAddress(o.getEmailAddress());
    this.setHomeSpace(o.getHomeSpace());
  }
  
  /**
   * Reveals the user-name of the account. This is unqualified: i.e
   * FredHoyle rather than ivo://FredHoyle@ivoa/communty
   */
  public String getUserName() {
    String ivorn = this.getIdent();
    Matcher matcher = pattern.matcher(ivorn);
    return (matcher.find())? matcher.group(1) : null;
  }
  
}
