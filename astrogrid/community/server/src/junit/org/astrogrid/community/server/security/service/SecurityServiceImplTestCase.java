package org.astrogrid.community.server.security.service ;

import junit.framework.TestCase;
import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;
import org.astrogrid.community.server.database.manager.DatabaseManagerImpl ;
import org.astrogrid.community.server.database.configuration.DatabaseConfiguration ;
import org.astrogrid.community.server.database.configuration.DatabaseConfigurationFactory ;
import org.astrogrid.community.server.database.configuration.TestDatabaseConfigurationFactory ;
import org.astrogrid.community.server.security.manager.SecurityManagerImpl;
import org.astrogrid.config.SimpleConfig;

/**
 * A JUnit test case for our SecurityService implementation.
 *
 */
public class SecurityServiceImplTestCase extends TestCase {

  private static Log log = LogFactory.getLog(SecurityServiceImplTestCase.class);

  private DatabaseConfiguration config;
    
  public void setUp() throws Exception {
    TestDatabaseConfigurationFactory factory = 
        new TestDatabaseConfigurationFactory();
    this.config = factory.testDatabaseConfiguration();
    SimpleConfig.getSingleton().setProperty("org.astrogrid.community.default.vospace",
                                            "ivo://foo.bar/vospace");
    SimpleConfig.getSingleton().setProperty("org.astrogrid.community.ident",
                                            "org.astrogrid.regtest/community");
    DatabaseManagerImpl dbm = new DatabaseManagerImpl(config);
    dbm.resetDatabaseTables();
    SecurityManagerImpl sm = new SecurityManagerImpl(config);
    sm.setPassword("frog", "croakcroak");
  }

  public void testCheckPassword() throws Exception {
    SecurityServiceImpl sut = new SecurityServiceImpl(config);
    
    sut.checkPassword("ivo://frog@org.astrogrid.regtest/community", "croakcroak");
  }
  
  public void testAuthenticate() throws Exception {
    SecurityServiceImpl sut = new SecurityServiceImpl(config);
    
    sut.authenticate("frog", "croakcroak");
  }
}
