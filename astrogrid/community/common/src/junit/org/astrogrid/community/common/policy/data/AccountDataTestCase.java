/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/junit/org/astrogrid/community/common/policy/data/AccountDataTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/19 14:43:14 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: AccountDataTestCase.java,v $
 *   Revision 1.2  2004/03/19 14:43:14  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.1.2.2  2004/03/18 13:41:19  dave
 *   Added Exception handling to AccountManager
 *
 *   Revision 1.1.2.1  2004/03/17 01:08:48  dave
 *   Added AccountNotFoundException
 *   Added DuplicateAccountException
 *   Added InvalidIdentifierException
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.data ;

import org.astrogrid.community.common.junit.JUnitTestBase ;

/**
 * JUnit test for AccountData objects.
 *
 */
public class AccountDataTestCase
    extends JUnitTestBase
    {
    /**
     * Switch for our debug statements.
     * @TODO Refactor to use the common logging.
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
        AccountData frog = new AccountData() ;
        assertNotNull("Account is null", frog) ;
        //
        // Check the account ident is null.
        assertNull("Account ident not null", frog.getIdent()) ;
        }

    /**
     * Create an AccountData with a valid ident.
     * @TODO Refactor to use local community ident.
     *
     */
    public void testCreateValid()
        {
        //
        // Create our AccountData.
        AccountData frog = new AccountData("frog") ;
        assertNotNull("Account is null", frog) ;
        //
        // Check the account ident.
        assertEquals("Account ident not equal", "frog", frog.getIdent()) ;
        }

    /**
     * Create an AccountData and try to change the ident
     * @TODO Refactor to use local community ident.
     *
     */
    public void testChangeValid()
        {
        //
        // Create our AccountData.
        AccountData frog = new AccountData("frog") ;
        assertNotNull("Account is null", frog) ;
        //
        // Check the account ident.
        assertEquals("Account ident not equal", "frog", frog.getIdent()) ;
        //
        // Try to change the Account ident.
        frog.setIdent("toad") ;
        //
        // Check the account ident has NOT changed.
        assertEquals("Account ident not equal", "frog", frog.getIdent()) ;
        }

    /**
     * Compare two Accounts with the same ident.
     * @TODO Refactor to use local community ident.
     *
     */
    public void testSameIdent()
        {
        //
        // Create our Accounts.
        AccountData frog = new AccountData("frog") ;
        AccountData toad  = new AccountData("frog") ;
        assertNotNull("Account is null", frog) ;
        assertNotNull("Account is null", toad)  ;
        //
        // Check the account idents.
        checkEqual("Account ident not equal", "frog", frog.getIdent()) ;
        checkEqual("Account ident not equal", "frog", toad.getIdent()) ;
        //
        // Check that the two Accounts are the same.
        checkEqual("Same ident accounts not equal", frog, toad) ;
        }

    /**
     * Compare two Accounts with different idents.
     * @TODO Refactor to use local community ident.
     *
     */
    public void testDifferentIdent()
        {
        //
        // Create our Accounts.
        AccountData frog = new AccountData("frog") ;
        AccountData toad = new AccountData("toad") ;
        assertNotNull("Account is null", frog) ;
        assertNotNull("Account is null", toad) ;
        //
        // Check the account idents.
        checkEqual("Account ident not equal", "frog", frog.getIdent()) ;
        checkEqual("Account ident not equal", "toad", toad.getIdent()) ;
        //
        // Check that the two Accounts are not the same.
        checkNotEqual("Different ident accounts equal", frog, toad) ;
        }
    }
