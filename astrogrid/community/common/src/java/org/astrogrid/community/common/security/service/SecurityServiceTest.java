/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/security/service/SecurityServiceTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.8 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityServiceTest.java,v $
 *   Revision 1.8  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.7.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.7  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.6.32.2  2004/06/17 14:50:03  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.6.32.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.security.service ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.rmi.RemoteException ;

import org.astrogrid.community.common.policy.data.AccountData ;
import org.astrogrid.community.common.policy.manager.AccountManager ;

import org.astrogrid.community.common.security.data.SecurityToken ;
import org.astrogrid.community.common.security.manager.SecurityManager ;

import org.astrogrid.community.common.service.CommunityServiceTest ;

import org.astrogrid.community.common.exception.CommunitySecurityException ;

/**
 * A JUnit test case for our SecurityService interface.
 * This is designed to be extended by each set of tests, mock, client and server.
 * @todo Chech the Exception type wrapped in the RemoteException.
 *
 */
public class SecurityServiceTest
    extends CommunityServiceTest
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(SecurityServiceTest.class);

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
    public SecurityServiceTest()
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
        log.debug("SecurityServiceTest.setSecurityManager()") ;
        log.debug("  Manager : " + manager.getClass()) ;
        this.securityManager = manager ;
        }

    /**
     * Our target SecurityService.
     *
     */
    private SecurityService securityService ;

    /**
     * Get our target SecurityService.
     *
     */
    public SecurityService getSecurityService()
        {
        return this.securityService ;
        }

    /**
     * Set our target SecurityService.
     *
     */
    public void setSecurityService(SecurityService service)
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("SecurityServiceTest.setSecurityService()") ;
        log.debug("  Service : " + service.getClass()) ;
        //
        // Set our SecurityService reference.
        this.securityService = service ;
        //
        // Set our CommunityService reference.
        this.setCommunityService(securityService) ;
        }

    /**
     * Check an Account password.
     *
     */
    public void testCheckPassword()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("SecurityServiceTest.testCheckPassword()") ;
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
        // Setup our test password.
        assertTrue(
            "setPassword returned false",
            securityManager.setPassword(
                account.getIdent(),
                TEST_PASSWORD
                )
            ) ;
        //
        // Check we can validate our password.
        SecurityToken token = securityService.checkPassword(
            account.getIdent(),
            TEST_PASSWORD
            ) ;
        //
        // Check that we got a token.
        assertNotNull(
            "checkPassword returned NULL",
            token
            ) ;
        //
        // Check that the token has the right account.
        assertEquals(
            "Token has wrong account",
            account.getIdent(),
            token.getAccount()
            ) ;
        //
        // Check that the token is valid.
        assertTrue(
            "Token is not valid",
            token.isValid()
            ) ;
        }

    /**
     * Check that we can validate a SecurityToken.
     *
     */
    public void testCheckToken()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("SecurityServiceTest.testCheckToken()") ;
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
        // Setup our test password.
        assertTrue(
            "setPassword returned false",
            securityManager.setPassword(
                account.getIdent(),
                TEST_PASSWORD
                )
            ) ;
        //
        // Check we can validate our password.
        SecurityToken original = securityService.checkPassword(
            account.getIdent(),
            TEST_PASSWORD
            ) ;
        //
        // Check that we got a token.
        assertNotNull(
            "NULL original token",
            original
            ) ;
        //
        // Check that the token has the right account.
        assertEquals(
            "Token has wrong account",
            account.getIdent(),
            original.getAccount()
            ) ;
        //
        // Check that the token is valid.
        assertTrue(
            "Token is not valid",
            original.isValid()
            ) ;
        //
        // Check that we can validate our token
        SecurityToken response = securityService.checkToken(original) ;
        //
        // Check that we got a token.
        assertNotNull(
            "NULL response token",
            response
            ) ;
        //
        // Check that the token has the right account.
        assertEquals(
            "Token has wrong account",
            account.getIdent(),
            response.getAccount()
            ) ;
        //
        // Check that the token is valid.
        assertTrue(
            "Token is not valid",
            response.isValid()
            ) ;
        //
        // Check that the two tokens have different values.
        checkNotEqual(
            "Token has same value",
            original.getToken(),
            response.getToken()
            ) ;
        //
        // Check that the two tokens are not equal.
        checkNotEqual(
            "Token are equal",
            original,
            response
            ) ;
        //
        // Check that the original is no longer valid.
        try {
            securityService.checkToken(original) ;
            fail("Expected CommunitySecurityException") ;
            }
        catch (CommunitySecurityException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            log.debug("Class     : " + ouch.getClass()) ;
            }
        catch (RemoteException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            log.debug("Class     : " + ouch.getClass()) ;
            }
        //
        // Check that the original is no longer valid.
//
// This won't work on a remote service unless you use the delegate.
//
        assertFalse(
            "Original token still valid",
            original.isValid()
            ) ;
        }

    /**
     * The default number of splits to test.
     *
     */
    private static int SPLIT_COUNT = 3 ;

    /**
     * Check that we can split a SecurityToken.
     *
     */
    public void testSplitToken()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("SecurityServiceTest.testSplitToken()") ;
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
        // Setup our test password.
        assertTrue(
            "setPassword returned false",
            securityManager.setPassword(
                account.getIdent(),
                TEST_PASSWORD
                )
            ) ;
        //
        // Check we can validate our password.
        SecurityToken original = securityService.checkPassword(account.getIdent(), TEST_PASSWORD) ;
        //
        // Check that we got a token.
        assertNotNull(
            "NULL original token",
            original
            ) ;
        //
        // Check that the token has the right account.
        assertEquals(
            "Token has wrong account",
            account.getIdent(),
            original.getAccount()
            ) ;
        //
        // Check that the token is valid.
        assertTrue(
            "Token is not valid",
            original.isValid()
            ) ;
        //
        // Check that we can validate our token
        Object[] array = securityService.splitToken(original, SPLIT_COUNT) ;
        //
        // Check that we got an array.
        assertNotNull(
            "NULL token array",
            array
            ) ;
        //
        // Check that we got the right number of tokens.
        assertTrue(
            "Wrong number of tokens",
            (array.length == SPLIT_COUNT)
            ) ;
        //
        // Check each of the new tokens.
        for (int i = 0 ; i < array.length ; i++)
            {
            SecurityToken token = (SecurityToken) array[i] ;
            //
            // Check that the token has the right account.
            assertEquals(
                "Token has wrong account",
                account.getIdent(),
                token.getAccount()
                ) ;
            //
            // Check that the token is valid.
            assertTrue(
                "Token is not valid",
                token.isValid()
                ) ;
            //
            // Check that the token has a different value.
            checkNotEqual(
                "Token has same value",
                original.getToken(),
                token.getToken()
                ) ;
            //
            // Check that the token is not equal to our original.
            checkNotEqual(
                "Token are equal",
                original,
                token
                ) ;
            }
        //
        // Check that the original is no longer valid.
        try {
            securityService.checkToken(original) ;
            fail("Expected CommunitySecurityException") ;
            }
        catch (CommunitySecurityException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            }
        catch (RemoteException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            log.debug("Class     : " + ouch.getClass()) ;
            }
        //
        // Check that the original is no longer valid.
//
// This won't work on a remote service unless you use the delegate.
//
        assertFalse(
            "Original token still valid",
            original.isValid()
            ) ;
        }
    }

