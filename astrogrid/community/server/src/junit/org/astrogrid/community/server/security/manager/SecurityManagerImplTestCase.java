package org.astrogrid.community.server.security.manager;

import junit.framework.TestCase;
import org.astrogrid.community.server.database.configuration.DatabaseConfiguration;
import org.astrogrid.community.server.database.configuration.TestDatabaseConfigurationFactory;
import org.astrogrid.community.server.database.manager.DatabaseManagerImpl;
import org.astrogrid.community.server.security.data.PasswordData;
import org.astrogrid.community.server.security.service.SecurityServiceImpl;
import org.astrogrid.config.SimpleConfig;
import org.exolab.castor.jdo.Database;

/**
 * JUnit tests for SecurityManagerImpl.
 *
 * There is no longer a SecurityManager web-service, but the local UI for 
 * community management depends on SecurityManagerImpl for changing passwords.
 * The tested class is not obsolete yet.
 *
 * @author Guy Rixon
 */
public class SecurityManagerImplTestCase extends TestCase {
  
  /**
   * The configuration of the community database and its JDO wrapper.
   */
  private DatabaseConfiguration config;
  
  /**
   * Creates a community database in which to test the passwords.
   * The default configuration is loaded.
   */
  public void setUp() throws Exception {
    TestDatabaseConfigurationFactory factory = 
        new TestDatabaseConfigurationFactory();
    this.config = factory.testDatabaseConfiguration() ;
    DatabaseManagerImpl dbm = new DatabaseManagerImpl(this.config);
    dbm.resetDatabaseTables();
  }
  
  /**
   * Tests the creation, updating and deletion of an entry in the
   * password table.
   */
  public void testRoundTrip() throws Exception {
    SimpleConfig.getSingleton().setProperty("org.astrogrid.community.ident", 
                                            "org.astrogrid.regtest/community");
    
    SecurityManagerImpl sut = new SecurityManagerImpl(config);
    SecurityServiceImpl ss = new SecurityServiceImpl(config);
    String password = "fubarbaz";
    
    sut.setPassword("frog", password);
    
    // This will throw an exception if the password doesn't match.
    //ss.checkPassword("ivo://frog@org.astrogrid.regtest/community", password);
    
    // Check that it's used the right primary key in the DB.
    // For compatibility with old DBs, the key is the old form of
    // the account IVORN and the manager is supposed to translate from
    // the new form when making entries.
    Database database = config.getDatabase();
    database.begin();
    database.load(PasswordData.class, "ivo://org.astrogrid.regtest/frog");
    
    // Check that it can retrieve it.
    assertEquals("fubarbaz", sut.getPassword("frog"));
  } 
  
}
