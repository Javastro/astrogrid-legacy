package org.astrogrid.community.webapp;

import org.astrogrid.community.common.ivorn.CommunityIvornParser;
import org.astrogrid.community.common.policy.data.AccountData;
import org.astrogrid.community.server.database.configuration.DatabaseConfiguration;
import org.astrogrid.community.server.security.data.PasswordData;
import org.astrogrid.community.server.service.CommunityServiceImpl;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.Query;

/**
 * A Java bean representing the account details of a community member.
 * 
 * The SECRETS table of the database contains the elements of PasswordData
 * objects keyed by account names in the form 
 * ivo://(community name)/(user log-in name).
 *
 * @author Guy Rixon
 */
public class UserAccountBean {
  
  /**
   * Constructs a UserAccountBean for the default database.
   * This is the constructor to be used in the web application.
   */
  public UserAccountBean() {
  }
  
  /**
   * Constructs a UserAccountBean with a given database configuration. 
   * This is for unit testing.
   */
  public UserAccountBean(DatabaseConfiguration dbConfig) {
    this.databaseConfiguration = dbConfig;
  }
  
  /**
   * The configuration for the community database.
   * Changing this selects a different database, e.g. for unit testing.
   * If this property is null, the default configuration is used and
   * this is correct for routine use in the web-application.
   */
  DatabaseConfiguration databaseConfiguration;
  
  /**
   * The user's log-in name.
   */
  protected String loginName;
  
  /**
   * Sets the log-in name.
   * Setting this cause the bean to retrieve the user's other details
   * from the database if they are held there.
   */
  public void setUserLoginName(String name) throws Exception {
    this.loginName = name;
    try {
      this.lookUpPassword();
      this.lookUpAccount();
    }
    catch (Exception e) {
      // Ignore it.
    }
  }
  
  /**
   * Reveals the log-in name.
   */
  public String getUserLoginName() {
    return this.loginName;
  }
  
  /**
   * The user's password, copied from community records.
   */
  protected String oldPassword;
  
  /**
   * Reveals the user's password.
   */
  public String getUserOldPassword() {
    return this.oldPassword;
  }
  
  /**
   * Sets the user's existing password.
   * This is used for validation when changing the password.
   * It sets the property of this bean but does not change the community DB.
   */
  public void setUserOldPassword(String password) {
    this.oldPassword = password;
  }
  
  /**
   * The user's password, to be set in the next password change.
   */
  protected String newPassword;
  
  /**
   * Sets the user's password in the bean.
   * The persistent password in the community records is not affected.
   */
  public void setUserNewPassword(String password) {
    this.newPassword = password;
  }
  
  /**
   * The user's common name.
   * This is the /CN part of the subject in their
   * certificate and matches the "display name" datum
   * in the DB.
   */
  protected String commonName;
  
  /**
   * Reveals the common name.
   */
  public String getUserCommonName() {
    return this.commonName;
  }
  
  /**
   * Changes the user's password in the community records.
   * The persistent password is changed to match the ephemeral
   * value in the bean.
   */
  public String getPasswordChangeResult() throws Exception {
    if (this.newPassword == null
    ||  this.newPassword.length() < 7) {
      return "Password change failed: the new password must be " +
             "at least seven characters long.";
    }
    try {
      this.changePassword();
      return "Password change was successful.";
    }
    catch (Exception e) {
      return "Password change failed; the DB reports: " + e.getMessage();
    }
  }
  
  /**
   * Updates the SECRETS table in the database.
   */
  protected void changePassword() throws Exception {
    
    // Form the key for the password table.
    String accountName = "ivo://" +
                         CommunityIvornParser.getLocalIdent() +
                         "/" +
                         this.loginName;
    
    // Get at the configured database. If there is no declared configuration,
    // then the default database is used: this is normal for production use.
    CommunityServiceImpl service;
    if (this.databaseConfiguration == null) {
      service = new CommunityServiceImpl();
    }
    else {
      service = new CommunityServiceImpl(this.databaseConfiguration);
    }
    Database db = service.getDatabase();
    
    // Write the password from the DB. 
    // The query is contained in a transaction: this is mandatory in JDO.
    db.begin();
    PasswordData pwd = (PasswordData)db.load(PasswordData.class, accountName);
    if (this.oldPassword != null
    &&  this.oldPassword.equals(pwd.getPassword())) {
      pwd.setPassword(this.newPassword);
      db.commit();
    }
    else {
      db.rollback();
      throw new Exception("The given, old password does not match that in the database.");
    }
  }
  
  /**
   * Looks up the user's password in the DB.
   */
  protected void lookUpPassword() throws Exception {
    
    // Form the key for the password table.
    String accountName = "ivo://" +
                         CommunityIvornParser.getLocalIdent() +
                         "/" +
                         this.loginName;
    
    // Get at the configured database. If there is no declared configuration,
    // then the default database is used: this is normal for production use.
    CommunityServiceImpl service;
    if (this.databaseConfiguration == null) {
      service = new CommunityServiceImpl();
    }
    else {
      service = new CommunityServiceImpl(this.databaseConfiguration);
    }
    Database db = service.getDatabase();
    
    // Read the password from the DB. 
    // The query is contained in a transaction: this is mandatory in JDO.
    db.begin();
    PasswordData passwordData = 
        (PasswordData)db.load(PasswordData.class, accountName);
    db.commit();
    this.oldPassword = passwordData.getPassword();
  }
  
  /**
   * Looks up the user's password in the DB.
   */
  protected void lookUpAccount() throws Exception {
    
    // Form the key for the password table.
    String accountName = "ivo://" +
                         CommunityIvornParser.getLocalIdent() +
                         "/" +
                         this.loginName;
    
    // Get at the configured database. If there is no declared configuration,
    // then the default database is used: this is normal for production use.
    CommunityServiceImpl service;
    if (this.databaseConfiguration == null) {
      service = new CommunityServiceImpl();
    }
    else {
      service = new CommunityServiceImpl(this.databaseConfiguration);
    }
    Database db = service.getDatabase();
    
    // Read the password from the DB. 
    // The query is contained in a transaction: this is mandatory in JDO.
    db.begin();
    AccountData accountData = 
        (AccountData)db.load(AccountData.class, accountName);
    db.commit();
    this.commonName = accountData.getDisplayName();
  }
  
}