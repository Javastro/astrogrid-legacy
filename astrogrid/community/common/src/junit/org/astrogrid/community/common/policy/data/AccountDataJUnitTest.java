/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/junit/org/astrogrid/community/common/policy/data/Attic/AccountDataJUnitTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/02/20 21:11:05 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: AccountDataJUnitTest.java,v $
 *   Revision 1.3  2004/02/20 21:11:05  dave
 *   Merged development branch, dave-dev-200402120832, into HEAD
 *
 *   Revision 1.2.2.1  2004/02/16 15:20:54  dave
 *   Changed tabs to spaces
 *
 *   Revision 1.2  2004/02/12 08:12:13  dave
 *   Merged development branch, dave-dev-200401131047, into HEAD
 *
 *   Revision 1.1.2.2  2004/01/30 03:21:23  dave
 *   Added initial code for SecurityManager and SecurityService
 *
 *   Revision 1.1.2.1  2004/01/27 05:43:10  dave
 *   Moved the JUnit tests around a bit
 *
 *   Revision 1.1.2.1  2004/01/13 14:29:41  dave
 *   Added initial JUnit tests
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.data ;

import org.astrogrid.community.common.junit.JUnitTestBase ;

/**
 * Base class for our data object JUnit tests.
 *
 */
public class AccountDataJUnitTest
    extends JUnitTestBase
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Create an empty AccountData.
     *
     */
    public void testCreateEmpty()
        {
        //
        // Create our AccountData.
        AccountData alpha = new AccountData() ;
        assertNotNull("Account is null", alpha) ;
        //
        // Check the account ident is null.
        assertNull("Account ident not null", alpha.getIdent()) ;
        }

    /**
     * Create an AccountData with a valid ident.
     *
     */
    public void testCreateValid()
        {
        //
        // Create our AccountData.
        AccountData alpha = new AccountData("frog") ;
        assertNotNull("Account is null", alpha) ;
        //
        // Check the account ident.
        assertEquals("Account ident not equal", "frog", alpha.getIdent()) ;
        }

    /**
     * Compare two Accounts with the same ident.
     * TODO This needs to refactored to check for local community in the ident.
     *
     */
    public void testSameIdent()
        {
        //
        // Create our Accounts.
        AccountData alpha = new AccountData("frog") ;
        AccountData beta  = new AccountData("frog") ;
        assertNotNull("Account is null", alpha) ;
        assertNotNull("Account is null", beta)  ;
        //
        // Check the account idents.
        checkEqual("Account ident not equal", "frog", alpha.getIdent()) ;
        checkEqual("Account ident not equal", "frog", beta.getIdent()) ;
        //
        // Check that the two Accounts are the same.
        checkEqual("Same ident accounts not equal", alpha, beta) ;
        }

    /**
     * Compare two Accounts with different idents.
     * TODO This needs to refactored to check for local community in the ident.
     *
     */
    public void testDifferentIdent()
        {
        //
        // Create our Accounts.
        AccountData alpha = new AccountData("frog") ;
        AccountData beta = new AccountData("toad") ;
        assertNotNull("Account is null", alpha) ;
        assertNotNull("Account is null", beta) ;
        //
        // Check the account idents.
        checkEqual("Account ident not equal", "frog", alpha.getIdent()) ;
        checkEqual("Account ident not equal", "toad", beta.getIdent()) ;
        //
        // Check that the two Accounts are not the same.
        checkNotEqual("Different ident accounts equal", alpha, beta) ;
        }

    }
