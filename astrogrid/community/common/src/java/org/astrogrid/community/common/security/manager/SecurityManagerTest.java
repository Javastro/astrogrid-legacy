/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/security/manager/SecurityManagerTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/23 16:34:08 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityManagerTest.java,v $
 *   Revision 1.4  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.3.16.1  2004/03/22 15:31:10  dave
 *   Added CommunitySecurityException.
 *   Updated SecurityManager and SecurityService to use Exceptions.
 *
 *   Revision 1.3  2004/03/08 13:42:33  dave
 *   Updated Maven goals.
 *   Replaced tabs with Spaces.
 *
 *   Revision 1.2.2.1  2004/03/08 12:53:17  dave
 *   Changed tabs to spaces
 *
 *   Revision 1.2  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.1.2.2  2004/03/05 14:03:23  dave
 *   Added first client side SOAP test - SecurityServiceSoapDelegateTestCase
 *
 *   Revision 1.1.2.1  2004/03/02 15:29:35  dave
 *   Working round Castor problem with PasswordData - objects remain in database cache
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.security.manager ;

import java.rmi.RemoteException ;

import org.astrogrid.community.common.policy.data.AccountData ;
import org.astrogrid.community.common.policy.manager.AccountManager ;

import org.astrogrid.community.common.security.data.SecurityToken ;
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
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("SecurityServiceTest.setAccountManager()") ;
        if (DEBUG_FLAG) System.out.println("  Manager : " + manager.getClass()) ;
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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("SecurityManagerTest.setSecurityManager()") ;
        if (DEBUG_FLAG) System.out.println("  Manager : " + manager.getClass()) ;
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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("SecurityManagerTest.testSetPassword()") ;
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
     * Check we can change an account password.
     *
     */
    public void testChangePassword()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("SecurityManagerTest.testChangePassword()") ;
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

