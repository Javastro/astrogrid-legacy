/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/junit/org/astrogrid/community/resolver/ant/Attic/CommunityLoginTaskTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/30 01:40:03 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityLoginTaskTestCase.java,v $
 *   Revision 1.2  2004/03/30 01:40:03  dave
 *   Merged development branch, dave-dev-200403242058, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/30 01:38:14  dave
 *   Refactored resolver and install toolkits.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.resolver.ant ;

import junit.framework.TestCase ;

import org.astrogrid.store.Ivorn ;
import org.astrogrid.common.ivorn.MockIvorn ;

import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory ;

/**
 * Test case for the CommunityLoginTask.
 *
 */
public class CommunityLoginTaskTestCase
    extends TestCase
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Test that we can resolve a mock address.
     *
     */
    public void testResolveMock()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityLoginTaskTestCase.testResolveMock()") ;
        //
        // Create our Account Ivorn.
        Ivorn ident = CommunityAccountIvornFactory.createMock(
            "community",
            "frog"
            ) ;
        if (DEBUG_FLAG) System.out.println("  Ident  : " + ident) ;
        //
        // Create our resolver.
        CommunityLoginTask task = new CommunityLoginTask() ;
        //
        // Set the account.
        task.setAccount(ident.toString()) ;
        //
        // Set the password.
        task.setPassword("test-password") ;
        //
        // Ask our resolver to check the password
        task.execute() ;
        //
        // Check the token.
        assertNotNull(
            "Null token",
            task.getToken()
            ) ;
        }
    }
