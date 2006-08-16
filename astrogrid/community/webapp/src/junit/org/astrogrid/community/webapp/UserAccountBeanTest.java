package org.astrogrid.community.webapp;

import junit.framework.TestCase;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.community.common.policy.data.AccountData;
import org.astrogrid.community.server.database.configuration.DatabaseConfiguration;
import org.astrogrid.community.server.security.data.PasswordData;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.DuplicateIdentityException;

/**
 * JUnit tests for org.astrogrid.community.webapp.UserAccountBean.
 *
 * @author Guy Rixon
 */
public class UserAccountBeanTest extends TestCase {
  
  /**
   * The test database. This is not the default database.
   */
  DatabaseConfiguration databaseConfiguration;
  
  public void setUp() throws Exception {
    //super.setUp();
    
    SimpleConfig.getSingleton().setProperty("org.astrogrid.community.ident", 
                                            "test-community");
    
    // Cache the configuration for the test database.
    this.databaseConfiguration 
        = new DatabaseConfiguration("org.astrogrid.community.database",
                                    "astrogrid-community-database.xml",
                                    "astrigrid-community-database.sql");
    
    // Add a test user to the test database.
    Database db = this.databaseConfiguration.getDatabase();
    
    PasswordData pwd = new PasswordData();
    pwd.setAccount("ivo://test-community/fred");
    pwd.setPassword("fred");
    pwd.setEncryption("none");
    try {
      db.begin();
      db.create(pwd);
      db.commit();
    }
    catch (DuplicateIdentityException e) {
      // Ignore it; the DB is OK for the test.
      db.rollback();
    }
    AccountData acd = new AccountData();
    acd.setIdent("ivo://test-community/fred");
    acd.setDisplayName("Fred");
    try {
      db.begin();
      db.create(acd);
      db.commit();
    }
    catch (DuplicateIdentityException e) {
      // Ignore it; the DB is OK for the test.
      db.rollback();
    }
  }
  
  public void testLoginName() throws Exception {
    
    // Create the bean with the atypical constructor. The no-argument
    // constructor associates the bean with the default database and isn't
    // appropriate for unit testing.
    UserAccountBean account = new UserAccountBean(this.databaseConfiguration);
    account.setUserLoginName("fred");
  }
  
  public void testChangePassword() throws Exception {
    UserAccountBean account = new UserAccountBean(this.databaseConfiguration);
    account.setUserLoginName("fred");
    account.setUserNewPassword("woofwoof");
    String msg = account.getPasswordChangeResult();
    System.out.println(msg);
    assertTrue("Password change was successful.".equals(msg));
  }
  
}