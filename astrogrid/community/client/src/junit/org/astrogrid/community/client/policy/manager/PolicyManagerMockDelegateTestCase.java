/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/junit/org/astrogrid/community/client/policy/manager/Attic/PolicyManagerMockDelegateTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/23 16:34:08 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyManagerMockDelegateTestCase.java,v $
 *   Revision 1.4  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.3.2.1  2004/03/22 02:25:35  dave
 *   Updated delegate interfaces to include Exception handling.
 *
 *   Revision 1.3  2004/03/19 14:43:14  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.2.2.2  2004/03/19 03:31:21  dave
 *   Changed AccountManagerMock to recognise DatabaseManager reset()
 *
 *   Revision 1.2.2.1  2004/03/19 00:18:09  dave
 *   Refactored delegate Exception handling
 *
 *   Revision 1.2  2004/03/15 07:49:30  dave
 *   Merged development branch, dave-dev-200403121536, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/15 06:52:08  dave
 *   Refactored PolicyManagerMockDelegate to use ivorn identifiers.
 *   Refactored CommunityAccountResolver to just handle AccountData.
 *   Added CommunityAccountSpaceResolver to resolve home space ivorn.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.policy.manager ;

import org.astrogrid.community.client.policy.manager.PolicyManagerMockDelegate ;
import org.astrogrid.community.client.database.manager.DatabaseManagerMockDelegate ;

import org.astrogrid.community.common.policy.manager.AccountManagerTest ;

/**
 * A JUnit test case for our mock PolicyManager.
 * @todo create the top level PolicyManagerTestSuite.
 *
 */
public class PolicyManagerMockDelegateTestCase
    extends AccountManagerTest
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Setup our test.
     * Creates the Mock delegates to test.
     *
     */
    public void setUp()
        throws Exception
        {
        //
        // Create our test targets.
        this.setDatabaseManager(
            new DatabaseManagerMockDelegate()
            ) ;
        this.setAccountManager(
            new PolicyManagerMockDelegate()
            ) ;
        //
        // Reset our database tables.
        this.resetDatabase() ;
        }
    }
