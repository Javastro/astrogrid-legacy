/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/junit/org/astrogrid/community/common/security/data/SecurityTokenTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/08 13:42:33 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityTokenTestCase.java,v $
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
 *   Revision 1.1.2.1  2004/03/04 16:33:50  dave
 *   Added String constructor to SecurityToken
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.security.data ;

import org.astrogrid.community.common.junit.JUnitTestBase ;

/**
 * JUnit test case for the SecurityToken.
 *
 */
public class SecurityTokenTestCase
    extends JUnitTestBase
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Convert a SecurityToken to a String and back.
     *
     */
    public void testConvertString()
        {
        //
        // Create SecurityToken.
        SecurityToken original = new SecurityToken("alpha", "beta") ;
        //
        // Convert it to a String.
        String string = original.toString() ;
        //
        // Create a new SecurityToken from the String.
        SecurityToken recovered = new SecurityToken(string) ;
        //
        // Check that the two SecurityTokens are equal.
        assertEquals(
            "Tokens not equal",
            original,
            recovered
            ) ;
        }
    }
