package org.astrogrid.community.server.policy.manager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.common.policy.data.AccountData;
import org.astrogrid.community.common.policy.manager.PolicyManager;
import org.astrogrid.community.server.CommunityConfiguration;
import org.astrogrid.community.server.database.configuration.DatabaseConfiguration;

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
    
    @Override
    public void setUp() throws Exception {
      CommunityConfiguration c = new CommunityConfiguration();
      c.setVoSpaceIvorn("ivo://foo.bar/vospace");
      c.setDatabaseConfigurationUrl(this.getClass().getResource("/test-database-004.xml"));
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
      VOSpace v = new VOSpace(new MockNodeDelegate());
      PolicyManagerImpl m = new PolicyManagerImpl(c, v);
      return m;
    }
}
