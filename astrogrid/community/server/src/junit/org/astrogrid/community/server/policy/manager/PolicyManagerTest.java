/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/junit/org/astrogrid/community/server/policy/manager/PolicyManagerTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/02/20 21:11:05 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyManagerTest.java,v $
 *   Revision 1.3  2004/02/20 21:11:05  dave
 *   Merged development branch, dave-dev-200402120832, into HEAD
 *
 *   Revision 1.2.2.2  2004/02/19 21:09:27  dave
 *   Refactored ServiceStatusData into a common package.
 *   Refactored CommunityServiceImpl constructor to take a parent service.
 *   Refactored default database for CommunityServiceImpl
 *
 *   Revision 1.2.2.1  2004/02/16 15:20:54  dave
 *   Changed tabs to spaces
 *
 *   Revision 1.2  2004/02/12 08:12:13  dave
 *   Merged development branch, dave-dev-200401131047, into HEAD
 *
 *   Revision 1.1.2.6  2004/02/06 14:32:30  dave
 *   Added PolicyServiceTest.
 *   Refactored some test data.
 *
 *   Revision 1.1.2.5  2004/02/06 13:49:09  dave
 *   Moved CommunityManagerBase into server.common.CommunityServer.
 *   Moved getServiceStatus into server.common.CommunityServer.
 *   Moved JUnit tests to match.
 *
 *   Revision 1.1.2.4  2004/01/30 03:21:23  dave
 *   Added initial code for SecurityManager and SecurityService
 *
 *   Revision 1.1.2.3  2004/01/29 03:59:47  dave
 *   Tesing WinCvs ....
 *
 *   Revision 1.1.2.2  2004/01/27 18:55:09  dave
 *   Removed unused imports listed in PMD report
 *
 *   Revision 1.1.2.1  2004/01/27 17:10:00  dave
 *   Refactored database handling in JUnit tests
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.policy.manager ;

import org.astrogrid.community.server.common.CommunityServiceTest ;

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
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyManagerTest:testCreateDefaultManager()") ;
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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyManagerTest:testCreateTestManager()") ;
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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyManagerTest:testCreateAccount()") ;
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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyManagerTest:testDeleteAccount()") ;
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
