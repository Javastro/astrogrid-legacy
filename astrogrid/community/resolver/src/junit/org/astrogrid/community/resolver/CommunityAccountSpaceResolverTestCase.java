/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/junit/org/astrogrid/community/resolver/CommunityAccountSpaceResolverTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.7 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityAccountSpaceResolverTestCase.java,v $
 *   Revision 1.7  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.6.32.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.resolver ;

import junit.framework.TestCase ;

import org.astrogrid.store.Ivorn ;
import org.astrogrid.common.ivorn.MockIvorn ;

import org.astrogrid.community.common.ivorn.CommunityIvornParser ;
import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory ;

import org.astrogrid.community.common.policy.data.AccountData ;
import org.astrogrid.community.common.policy.manager.PolicyManager ;

import org.astrogrid.community.client.policy.manager.PolicyManagerDelegate ;
import org.astrogrid.community.client.policy.manager.PolicyManagerMockDelegate ;

/**
 * Test case for the CommunityAccountSpaceResolver.
 *
 */
public class CommunityAccountSpaceResolverTestCase
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
        if (DEBUG_FLAG) System.out.println("CommunityAccountSpaceResolver.testResolveMock()") ;
        //
        // Create our account Ivorn.
        Ivorn ident = CommunityAccountIvornFactory.createMock(
            "community",
            "frog"
            ) ;
        if (DEBUG_FLAG) System.out.println("  Ident  : " + ident) ;
        //
        // Create our myspace Ivorn.
        Ivorn home = new MockIvorn("myspace", "toad") ;
        if (DEBUG_FLAG) System.out.println("  Home   : " + home) ;
        //
        // Initialise our mock service.
        PolicyManager manager = new PolicyManagerMockDelegate() ;
        //
        // Add the account.
        AccountData created = new AccountData(
            new CommunityIvornParser(ident).getAccountIdent()
            ) ;
        created.setHomeSpace(home.toString()) ;
        manager.addAccount(created) ;
        //
        // Create our target Ivorn.
        Ivorn target = CommunityAccountIvornFactory.createMock(
            "community",
            "frog/public#qwertyuiop.xml"
            ) ;
        if (DEBUG_FLAG) System.out.println("  Target : " + target) ;
        //
        // Create our resolver.
        CommunityAccountSpaceResolver resolver = new CommunityAccountSpaceResolver() ;
        //
        // Ask our resolver for the home address.
        Ivorn found = resolver.resolve(target) ;
        assertNotNull(
            "Failed to resolve home space",
            found
            ) ;
        if (DEBUG_FLAG) System.out.println("  Found : " + found) ;
        //
        // Check that the home space is right.
        assertEquals(
            "Wrong ivorn",
            "ivo://org.astrogrid.mock.myspace/toad/public#qwertyuiop.xml",
            found.toString()
            ) ;
        }
    }
