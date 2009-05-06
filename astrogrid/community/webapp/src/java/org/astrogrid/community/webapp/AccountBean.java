package org.astrogrid.community.webapp;

import java.util.List;
import java.security.cert.X509Certificate;
import javax.security.auth.x500.X500Principal;
import org.astrogrid.community.common.policy.data.AccountData;
import org.astrogrid.community.server.CommunityConfiguration;
import org.astrogrid.community.server.policy.manager.AccountManagerImpl;
import org.astrogrid.community.server.sso.CredentialStore;

/**
 * A Java bean to give acccess to account details.
 *
 * @author Guy Rixon
 */
public class AccountBean {
  
  private String userName;
  private String commonName;
  private X500Principal distinguishedName;
  private String description;
  private String email;
  private String homeSpace;
  private boolean stored;

  private String community;
  
  /**
   * Constructs a AccountBean.
   */
  public AccountBean() {
    this.userName          = null;
    this.commonName        = null;
    this.distinguishedName = null;
    this.description       = null;
    this.email             = null;
    this.homeSpace         = null;
    this.stored            = false;
    
    // Get the authority under which the community is published.
    // This determines the account names in the database.
    this.community = new CommunityConfiguration().getPublishingAuthority();
  }
  
  /**
   * Reveals the user name.
   * This is the not qualified with a community name, e.g. fred rather than 
   * fred@foo/community. The user name is what the user types into the
   * log-in dialogue.
   *
   * @return The user name (null if the bean is not initialized).
   */
  public String getUserName() {
    return this.userName;
  }
  
  /**
   * Reveals the common name.
   * This is the user's name in society, e.g. Fred Hoyle. It is used
   * to form the common-name field of the user's credentials.
   *
   * @return The common name (may be null).
   */
  public String getCommonName() {
    return this.commonName;
  }
  
  /**
   * Reveals the distinguished name.
   * This is the subject from the user's credentials, e.g.
   * C=UK,O=Cambridge,OU=IoA,CN=Fred Hoyle.
   *
   * @return The distinguished name (may be null).
   */
  public X500Principal getDistinguishedName() {
    return this.distinguishedName;
  }

  /**
   * Reveals the description of the user.
   *
   * @return The description (may be null).
   */
  public String getDescription() {
    return this.description;
  }
  
  /**
   * Reveals the user's email address.
   *
   * @return The email address (may be null).
   */
  public String getEmail() {
    return this.email;
  }
  
  /**
   * Reveals the user's home space.
   * This is an IVORN for the user's top-level node in VOSpace.
   *
   * @return The home-space IVORN (may be null).
   */
  public String getHomeSpace() {
    return (this.homeSpace == null)? "" : this.homeSpace;
  }
  
  /**
   * Reveals whether the information in this bean is present in
   * community storage. False is returned only when a new account
   * is being created. In this case, the information in the bean has not yet
   * been committed to storage.
   */
  public boolean isStored() {
    return this.stored;
  }
  
  /**
   * Specifies the user name and loads accounts details from storage.
   */
  public void setUserName(String userName) {
    this.userName = userName;
    
    // Changing the user name invalidates the other properties.
    this.commonName        = null;
    this.distinguishedName = null;
    this.description       = null;
    this.email             = null;
    this.homeSpace         = null;
    this.stored            = false;
    
    // Get the information from the community database...
    AccountData ad = getBasicAccount(userName);
    this.commonName  = ad.getDisplayName();
    this.description = ad.getDescription();
    this.email       = ad.getEmailAddress();
    this.homeSpace   = ad.getHomeSpace();
    this.stored = true;
      
    // ... and the DN from the certificate, independently of the database.
    try {
      CredentialStore cs = new CredentialStore();
      List l = cs.getCertificateChain(userName, "");
      X509Certificate c = (X509Certificate) (l.get(0));
      this.distinguishedName = (c == null)? null : c.getSubjectX500Principal();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  /**
   * Updates the common name in the database.
   */
  public void setCommonName(String commonName) {
    this.commonName = commonName;
    if (this.stored) {
      AccountData ad = getBasicAccount(this.userName);
      ad.setDisplayName(commonName);
      setBasicAccount(ad);
    }
  }
  
  /**
   * Updates the description in the database.
   */
  public void setDescription(String description) {
    this.description = description;
    if (this.stored) {
      AccountData ad = getBasicAccount(this.userName);
      ad.setDescription(description);
      setBasicAccount(ad);
    }
  }
  
  /**
   * Updates the email address in the database.
   */
  public void setEmail(String email) {
    this.email = email;
    if (this.stored) {
      AccountData ad = getBasicAccount(this.userName);
      ad.setEmailAddress(email);
      setBasicAccount(ad);
    }
  }
  
  /**
   * Updates the home space in the database. If the given homespace
   * is null or an empty string, creates a new space in the community's
   * configured VOSpace.
   */
  public void setHomeSpace(String homeSpace) {
    this.homeSpace = homeSpace;
    System.out.println("Setting homespace " + homeSpace);
    if (this.stored) {
      AccountManagerImpl ami = AccountManagerImpl.getDefault();
      if ("new".equals(homeSpace)) {
        try {
          AccountData ad = getBasicAccount(this.userName);
          CredentialStore cs = new CredentialStore();
          homeSpace =
              ami.allocateHomespace(ad.getIdent(), userName, cs.getPassword(userName));
          ad.setHomeSpace(homeSpace);
          ami.setAccount(ad);
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
      else {
        try {
          AccountData ad = getBasicAccount(this.userName);
          ad.setHomeSpace(homeSpace);
          ami.setAccount(ad);
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }
  
  
  /**
   * Raises the basic account data from the community database.
   * If this fails, most of the fields of the result will be null.
   */
  protected AccountData getBasicAccount(String userName) {
    String accountIvorn = "ivo://" + this.community + "/" + userName;
    AccountManagerImpl ami = AccountManagerImpl.getDefault();
    try {
      return ami.getAccount(accountIvorn);
    }
    catch (Exception e) {
      return new AccountData(accountIvorn);
    }
  }
  
  /**
   * Updates the basic account data in the community database.
   */
  protected void setBasicAccount(AccountData ad) {
    AccountManagerImpl ami = AccountManagerImpl.getDefault();
    try {
      ami.setAccount(ad);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  
}
