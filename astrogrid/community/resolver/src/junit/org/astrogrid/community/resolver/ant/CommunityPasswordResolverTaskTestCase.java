/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/junit/org/astrogrid/community/resolver/ant/Attic/CommunityPasswordResolverTaskTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/30 04:44:01 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityPasswordResolverTaskTestCase.java,v $
 *   Revision 1.2  2004/03/30 04:44:01  dave
 *   Merged development branch, dave-dev-200403300258, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/30 03:24:57  dave
 *   Fixes to resolver tasks.
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
 * Test case for the CommunityPasswordResolverTask.
 *
 */
public class CommunityPasswordResolverTaskTestCase
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
        if (DEBUG_FLAG) System.out.println("CommunityPasswordResolverTaskTestCase.testResolveMock()") ;
        //
        // Create our Account Ivorn.
        Ivorn ident = CommunityAccountIvornFactory.createMock(
            "community",
            "frog"
            ) ;
        if (DEBUG_FLAG) System.out.println("  Ident  : " + ident) ;
        //
        // Create our resolver.
        CommunityPasswordResolverTask task = new CommunityPasswordResolverTask() ;
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
