/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/security/manager/SecurityManagerTest.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/08/12 16:08:47 $</cvs:date>
 * <cvs:version>$Revision: 1.8 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityManagerTest.java,v $
 *   Revision 1.8  2005/08/12 16:08:47  clq2
 *   com-jl-1315
 *
 *   Revision 1.7.110.1  2005/07/26 11:30:19  jl99
 *   Tightening up of unit tests for the server subproject
 *
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
 *   Revision 1.5.32.2  2004/06/17 15:53:22  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.5.32.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.security.manager ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.community.common.policy.data.AccountData ;
import org.astrogrid.community.common.policy.manager.AccountManager ;

import org.astrogrid.community.common.service.CommunityServiceTest ;

/**
 * A JUnit test case for our Securitymanager interface.
 * This is designed to be extended by each set of tests, mock, client and server.
 *
 */
public class SecurityManagerTest
    extends CommunityServiceTest
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(SecurityManagerTest.class);

    /**
     * Our test Account ident.
     *
     */
    public static String TEST_ACCOUNT = "test-account" ;

    /**
     * Our test password.
     *
     */
    public static String TEST_PASSWORD = "test-password" ;

    /**
     * Public constructor.
     *
     */
    public SecurityManagerTest()
        {
        }

    /**
     * Our target AccountManager.
     *
     */
    private AccountManager accountManager ;

    /**
     * Get our target AccountManager.
     *
     */
    public AccountManager getAccountManager()
        {
        return this.accountManager ;
        }

    /**
     * Set our target AccountManager.
     *
     */
    public void setAccountManager(AccountManager manager)
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("SecurityServiceTest.setAccountManager()") ;
        log.debug("  Manager : " + manager.getClass()) ;
        this.accountManager = manager ;
        }

    /**
     * Our target SecurityManager.
     *
     */
    private SecurityManager securityManager ;

    /**
     * Get our target SecurityManager.
     *
     */
    public SecurityManager getSecurityManager()
        {
        return this.securityManager ;
        }

    /**
     * Set our target SecurityManager.
     *
     */
    public void setSecurityManager(SecurityManager manager)
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("SecurityManagerTest.setSecurityManager()") ;
        log.debug("  Manager : " + manager.getClass()) ;
        //
        // Set our SecurityManager reference.
        this.securityManager = manager ;
        //
        // Set our CommunityService reference.
        this.setCommunityService(securityManager) ;
        }

    /**
     * Check we can set an account password.
     *
     */
    public void testSetPassword()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("SecurityManagerTest.testSetPassword()") ;
        //
        // Setup our test account.
        AccountData account = accountManager.addAccount(
            createLocal(TEST_ACCOUNT).toString()
            ) ;
        assertNotNull(
            "addAccount returned null",
            account
            ) ;
        //
        // Check that we can set a password.
        assertTrue(
            "setPassword returned false",
            securityManager.setPassword(
                account.getIdent(),
                TEST_PASSWORD
                )
            ) ;
        }
    
    
    /**
     * Check we cannot set a password for non-existent account.
     *
     */
    public void testSetPasswordForFalseAccount()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("SecurityManagerTest.testSetPasswordForFalseAccount()") ;
        //
        // Setup our test account.
        AccountData account = accountManager.addAccount(
            createLocal(TEST_ACCOUNT).toString()
            ) ;
        assertNotNull(
            "addAccount returned null",
            account
            ) ;
        //
        // Check that we can set a password.
        assertTrue(
            "setPassword returned false",
            securityManager.setPassword(
                account.getIdent(),
                TEST_PASSWORD
                )
            ) ;
        //
        // Everything ok so far.
        // Can we upset the applecart?...
        try {
            securityManager.setPassword( "false-account", TEST_PASSWORD ) ;
            fail( "Expected some exception here. Not sure what.") ;
        }
        catch( Exception ouch ) {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            log.debug("Class     : " + ouch.getClass()) ;
        }

        
        }
    
    
    

    /**
     * Check we can change an account password.
     *
     */
    public void testChangePassword()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("SecurityManagerTest.testChangePassword()") ;
        //
        // Setup our test account.
        AccountData account = accountManager.addAccount(
            createLocal(TEST_ACCOUNT).toString()
            ) ;
        assertNotNull(
            "addAccount returned null",
            account
            ) ;
        //
        // Check that we can set a password.
        assertTrue(
            "setPassword returned false",
            securityManager.setPassword(
                account.getIdent(),
                TEST_PASSWORD
                )
            ) ;
        //
        // Check that we can set the same password.
        assertTrue(
            "setPassword returned false",
            securityManager.setPassword(
                account.getIdent(),
                TEST_PASSWORD
                )
            ) ;
        }
    }

