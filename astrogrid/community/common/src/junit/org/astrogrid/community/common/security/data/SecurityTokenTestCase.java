/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/junit/org/astrogrid/community/common/security/data/SecurityTokenTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityTokenTestCase.java,v $
 *   Revision 1.5  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.4.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.4  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.3.52.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.security.data ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.community.common.junit.JUnitTestBase ;

/**
 * JUnit test case for the SecurityToken.
 *
 */
public class SecurityTokenTestCase
    extends JUnitTestBase
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(SecurityTokenTestCase.class);

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
