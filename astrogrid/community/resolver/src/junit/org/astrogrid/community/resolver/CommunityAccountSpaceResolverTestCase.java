/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/junit/org/astrogrid/community/resolver/CommunityAccountSpaceResolverTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: gtr $</cvs:author>
 * <cvs:date>$Date: 2008/01/24 16:56:43 $</cvs:date>
 * <cvs:version>$Revision: 1.11 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityAccountSpaceResolverTestCase.java,v $
 *   Revision 1.11  2008/01/24 16:56:43  gtr
 *   branch community-gtr-2521 is merged
 *
 *   Revision 1.10.4.1  2008/01/24 15:51:42  gtr
 *   Fixed.
 *
 *   Revision 1.10  2008/01/18 16:36:08  gtr
 *   Branch community-gtr-2502 is merged.
 *
 *   Revision 1.9.2.1  2008/01/18 09:44:43  gtr
 *   Altered to work with community-common 2008.0a3.
 *
 *   Revision 1.9  2008/01/15 22:57:49  gtr
 *   community-gtr-2491 is merged
 *
 *   Revision 1.8.138.1  2008/01/15 14:35:06  gtr
 *   I fixed it to be independent of configuration files.
 *
 *   Revision 1.8  2004/11/22 13:03:04  jdt
 *   Merges from Comm_KMB_585
 *
 *   Revision 1.7.104.1  2004/11/02 11:51:09  KevinBenson
 *   added the check on getting  a homespace account when there is just the account name given
 *
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
            "frog#qwertyuiop.xml"
            ) ;
        if (DEBUG_FLAG) System.out.println("  Target : " + target) ;
        //
        // Create our resolver.
        CommunityAccountSpaceResolver resolver = 
            new CommunityAccountSpaceResolver(new MockRegistry()) ;
        //
        // Ask our resolver for the home address.
        Ivorn found = resolver.resolve(target) ;
        assertNotNull(
            "Failed to resolve home space",
            found
            ) ;
        if (DEBUG_FLAG) System.out.println("  Found : " + found) ;
        
        if (DEBUG_FLAG) System.out.println("  Found toString : " + found.toString()) ;
        //
        // Check that the home space is right.
        assertEquals(
            "Wrong ivorn",
            "ivo://org.astrogrid.mock.myspace/toad#qwertyuiop.xml",
            found.toString()
            ) ;

        }
    
    }
