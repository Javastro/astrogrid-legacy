/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/junit/org/astrogrid/community/server/policy/manager/PolicyManagerTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyManagerTest.java,v $
 *   Revision 1.6  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.5.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.5  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.4.54.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.policy.manager ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.community.server.service.CommunityServiceTest ;

import org.astrogrid.community.common.policy.data.AccountData ;

import org.astrogrid.community.common.policy.manager.PolicyManager ;

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
        assertNotNull("Null policy manager",
            new PolicyManagerImpl()
            ) ;
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
        assertNotNull("Null manager",
            new PolicyManagerImpl(
                this.getDatabaseConfiguration()
                )
            ) ;
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
        PolicyManager manager = new PolicyManagerImpl(
            this.getDatabaseConfiguration()
            ) ;
        assertNotNull("Null manager", manager) ;
        //
        // Try creating an Account.
        assertNotNull("Null account",
            manager.addAccount("test-account")
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
        PolicyManager manager = new PolicyManagerImpl(
            this.getDatabaseConfiguration()
            ) ;
        assertNotNull("Null manager", manager) ;
        //
        // Try creating an Account.
        AccountData created = manager.addAccount("test-account") ;
        assertNotNull("Null account", created) ;
        //
        // Try deleting the Account.
        AccountData deleted = manager.delAccount("test-account") ;
        assertNotNull("Null account", deleted) ;
        //
        // Check that the two objects represent the same Account.
        assertEquals("Different account objects", created, deleted) ;
        }




    }
