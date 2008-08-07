package org.astrogrid.community.server.policy.manager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.common.policy.data.AccountData;
import org.astrogrid.community.common.policy.manager.PolicyManager;
import org.astrogrid.community.server.database.configuration.DatabaseConfiguration;
import org.astrogrid.config.SimpleConfig;

/**
 * Test cases for our PolicyManager.
 *
 */
public class PolicyManagerTest
    extends CommunityServiceTest
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(PolicyManagerTest.class);

    /**
     * Our PolicyManager to test.
     *
     */
    private PolicyManager manager = null ;
    
    
    public void setUp() throws Exception {
      SimpleConfig.getSingleton().setProperty("org.astrogrid.community.default.vospace",
                                              "ivo://foo.bar/vospace");
      SimpleConfig.getSingleton().setProperty("org.astrogrid.community.dbconfigurl",
                                              this.getClass().getResource("/test-database-001.xml"));
    }

    /**
     * Check we can create a manager, using default database configuration.
     * Note, this won't do much because there isn't a default database in the test environment.
     *
     */
    public void testCreateDefaultManager()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PolicyManagerTest:testCreateDefaultManager()") ;
        //
        // Try creating a default manager.
        assertNotNull("Null policy manager", new PolicyManagerImpl());
        }

    /**
     * Check we can create a manager, using test database configuration.
     *
     */
    public void testCreateTestManager()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PolicyManagerTest:testCreateTestManager()") ;
        //
        // Try creating our manager.
        assertNotNull("Null manager", getSut());
        }

    /**
     * Check we can create an Account.
     *
     */
    public void testCreateAccount()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PolicyManagerTest:testCreateAccount()") ;
        //
        // Try creating our manager.
        PolicyManager manager = getSut();
        assertNotNull("Null manager", manager) ;
        //
        // Try creating an Account.
        assertNotNull("Null account",
            manager.addAccount("ivo://test-account@whatever")
            ) ;
        }

    /**
     * Check we can delete an Account.
     *
     */
    public void testDeleteAccount()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PolicyManagerTest:testDeleteAccount()") ;
        //
        // Try creating our manager.
        PolicyManager manager = getSut();
        assertNotNull("Null manager", manager) ;
        //
        // Try creating an Account.
        AccountData created = manager.addAccount("ivo://test-account@whatever") ;
        assertNotNull("Null account", created) ;
        //
        // Try deleting the Account.
        AccountData deleted = manager.delAccount("ivo://test-account@whatever") ;
        assertNotNull("Null account", deleted) ;
        //
        // Check that the two objects represent the same Account.
        assertEquals("Different account objects", created, deleted) ;
        }


    private PolicyManager getSut() throws Exception {
      DatabaseConfiguration c = new DatabaseConfiguration("test-database-001");
      c.resetDatabaseTables();
      PolicyManagerImpl m = new PolicyManagerImpl(c);
      m.useMockVoSpace();
      return m;
    }
}
