/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/junit/org/astrogrid/community/common/security/service/SecurityServiceTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityServiceTestCase.java,v $
 *   Revision 1.6  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.5.32.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.security.service ;

import org.astrogrid.community.common.database.manager.DatabaseManager ;
import org.astrogrid.community.common.database.manager.DatabaseManagerMock ;

import org.astrogrid.community.common.policy.manager.AccountManager ;
import org.astrogrid.community.common.policy.manager.AccountManagerMock ;

import org.astrogrid.community.common.security.manager.SecurityManager ;
import org.astrogrid.community.common.security.manager.SecurityManagerMock ;

import org.astrogrid.community.common.exception.CommunitySecurityException ;

/**
 * A JUnit test case for our mock SecurityService.
 *
 */
public class SecurityServiceTestCase
    extends SecurityServiceTest
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Setup our test.
     * Creates a mock SecurityManager and SecurityService to test.
     *
     */
    public void setUp()
        throws Exception
        {
        //
        // Set out test targets.
        this.setDatabaseManager(
            new DatabaseManagerMock()
            ) ;
        this.setAccountManager(
            new AccountManagerMock()
            ) ;
        this.setSecurityManager(
            new SecurityManagerMock()
            ) ;
        this.setSecurityService(
            new SecurityServiceMock()
            ) ;
        //
        // Reset our database tables.
        this.resetDatabase() ;
        }

    /**
     * Test the mock password.
     *
     */
    public void testMockPassword()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("SecurityServiceTestCase.testMockPassword()") ;
        //
        // Create our mock service.
        SecurityServiceMock mock = new SecurityServiceMock() ;
        //
        // Clear the password.
        mock.setPassword(null) ;
        //
        // Check that the wrong password works.
        assertNotNull(
            "Check password returned null",
            mock.checkPassword("frog", "unknown")
            ) ;
        //
        // Set the password.
        mock.setPassword("ribbet") ;
        //
        // Check that the right password works.
        assertNotNull(
            "Check password returned null",
            mock.checkPassword("frog", "ribbet")
            ) ;
        //
        // Check that the wrong passord fails.
        try {
            mock.checkPassword("frog", "unknown") ;
            fail("Expected CommunitySecurityException") ;
            }
        catch (CommunitySecurityException ouch)
            {
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("Class     : " + ouch.getClass()) ;
            }
        //
        // Clear the password.
        mock.setPassword(null) ;
        //
        // Check that the wrong password works.
        assertNotNull(
            "Check password returned null",
            mock.checkPassword("frog", "unknown")
            ) ;
        }
    }
