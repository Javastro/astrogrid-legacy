/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/junit/org/astrogrid/community/common/security/service/SecurityServiceTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.7 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityServiceTestCase.java,v $
 *   Revision 1.7  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.6.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
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

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

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
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(SecurityServiceTestCase.class);

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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("SecurityServiceTestCase.testMockPassword()") ;
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
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            log.debug("Class     : " + ouch.getClass()) ;
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
