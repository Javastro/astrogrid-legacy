/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/junit/org/astrogrid/community/common/policy/data/Attic/AccountDataJUnitTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/15 07:49:30 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: AccountDataJUnitTest.java,v $
 *   Revision 1.5  2004/03/15 07:49:30  dave
 *   Merged development branch, dave-dev-200403121536, into HEAD
 *
 *   Revision 1.4.2.1  2004/03/12 17:42:09  dave
 *   Replaced tabs with spaces
 *
 *   Revision 1.4  2004/03/12 15:22:17  dave
 *   Merged development branch, dave-dev-200403101018, into HEAD
 *
 *   Revision 1.3.18.1  2004/03/10 13:32:01  dave
 *   Added home space to AccountData.
 *   Improved null param checking in AccountManager.
 *   Improved null param checking in AccountManager tests.
 *
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
        // Try to change toe Account ident.
        frog.setIdent("toad") ;
        //
        // Check the account ident.
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
