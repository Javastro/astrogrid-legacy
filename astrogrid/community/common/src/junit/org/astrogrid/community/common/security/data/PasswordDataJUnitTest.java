/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/junit/org/astrogrid/community/common/security/data/Attic/PasswordDataJUnitTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/02/20 21:11:05 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PasswordDataJUnitTest.java,v $
 *   Revision 1.3  2004/02/20 21:11:05  dave
 *   Merged development branch, dave-dev-200402120832, into HEAD
 *
 *   Revision 1.2.2.1  2004/02/16 15:20:54  dave
 *   Changed tabs to spaces
 *
 *   Revision 1.2  2004/02/12 08:12:13  dave
 *   Merged development branch, dave-dev-200401131047, into HEAD
 *
 *   Revision 1.1.2.1  2004/01/30 14:55:46  dave
 *   Added PasswordData object
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.security.data ;

import org.astrogrid.community.common.junit.JUnitTestBase ;

/**
 * Test cases for our PasswordData data object.
 *
 */
public class PasswordDataJUnitTest
    extends JUnitTestBase
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Create an empty PasswordData.
     *
     */
    public void testCreateEmpty()
        {
        //
        // Create our PasswordData.
        PasswordData data = new PasswordData() ;
        //
        // Check the account and password are null.
        assertNull("Account identifier not null", data.getAccount())   ;
        assertNull("Account password not null",   data.getPassword()) ;
        }

    /**
     * Create a PasswordData with a valid account and password.
     *
     */
    public void testCreateValid()
        {
        //
        // Create our AccountData.
        PasswordData data = new PasswordData("frog", "qwerty") ;
        //
        // Check the account and password.
        assertEquals("Account identifier not equal", "frog",   data.getAccount())  ;
        assertEquals("Account password not equal",   "qwerty", data.getPassword()) ;
        }

    /**
     * Compare two PasswordData with the same values.
     * TODO This needs to refactored to check for local community in the ident.
     *
     */
    public void testSame()
        {
        //
        // Create our PasswordData.
        PasswordData alpha = new PasswordData("frog", "qwerty") ;
        PasswordData beta  = new PasswordData("frog", "qwerty") ;
        //
        // Check the names are equal.
        checkEqual("Account identifiers not equal", alpha.getAccount(), beta.getAccount()) ;
        //
        // Check the passwords are equal.
        checkEqual("Account passwords not equal", alpha.getPassword(), beta.getPassword()) ;
        //
        // Check that the two objects are the same.
        checkEqual("PasswordData objects not equal", alpha, beta) ;
        }

    }
