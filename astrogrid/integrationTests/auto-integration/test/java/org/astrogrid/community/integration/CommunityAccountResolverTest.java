/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/integrationTests/auto-integration/test/java/org/astrogrid/community/integration/CommunityAccountResolverTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/04/21 03:42:57 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityAccountResolverTest.java,v $
 *   Revision 1.1  2004/04/21 03:42:57  dave
 *   Added initial Community test
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.integration ;

import junit.framework.TestCase ;

import org.astrogrid.store.Ivorn ;
import org.astrogrid.common.ivorn.MockIvorn ;

import org.astrogrid.community.common.ivorn.CommunityIvornParser ;
import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory ;

import org.astrogrid.community.common.policy.data.AccountData ;
import org.astrogrid.community.common.policy.manager.PolicyManager ;

import org.astrogrid.community.client.policy.manager.PolicyManagerDelegate ;
import org.astrogrid.community.client.policy.manager.PolicyManagerMockDelegate ;

import org.astrogrid.community.resolver.CommunityAccountResolver ;

/**
 * Test case for the CommunityAccountResolver.
 *
 */
public class CommunityAccountResolverTest
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
        if (DEBUG_FLAG) System.out.println("CommunityAccountResolverTest.testResolveMock()") ;
        //
        // Create our target Ivorn.
        Ivorn ivorn = CommunityAccountIvornFactory.createMock(
            "community",
            "frog/public#qwertyuiop.xml"
            ) ;
        if (DEBUG_FLAG) System.out.println("  Ivorn : " + ivorn) ;
        //
        // Initialise our mock service.
        PolicyManager manager = new PolicyManagerMockDelegate() ;
        //
        // Add the account.
        AccountData created = manager.addAccount(
            new CommunityIvornParser(ivorn).getAccountIdent()
            ) ;
        //
        // Create our resolver.
        CommunityAccountResolver resolver = new CommunityAccountResolver() ;
        //
        // Ask our resolver for the account data.
        AccountData found = resolver.resolve(ivorn) ;
        if (DEBUG_FLAG) System.out.println("  Found : " + ivorn) ;
        assertNotNull(
            "Failed to find account",
            found) ;
        //
        // Check that the two objects represent the same Account.
        assertEquals("Different identifiers", created, found) ;
        }

    /**
     * Test that we can resolve a real address.
     *
     */
    public void testResolveFrog()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityAccountResolverTest.testResolveFrog()") ;
        //
        // Create our target Ivorn.
        Ivorn ivorn = CommunityAccountIvornFactory.createLocal(
            "frog/public#qwertyuiop.xml"
            ) ;
        if (DEBUG_FLAG) System.out.println("  Ivorn : " + ivorn) ;
        //
        // Create our resolver.
        CommunityAccountResolver resolver = new CommunityAccountResolver() ;
        //
        // Ask our resolver for the account data.
        AccountData found = resolver.resolve(ivorn) ;
        if (DEBUG_FLAG) System.out.println("  Found : " + ivorn) ;
        assertNotNull(
            "Failed to find account",
            found) ;
        //
        // Check that the two objects represent the same Account.
        assertNotNull("Null account", found) ;
		//
		// Print our the account details.
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----") ;
        if (DEBUG_FLAG) System.out.println("  Account ident : " + found.getIdent()) ;
        if (DEBUG_FLAG) System.out.println("  Display name  : " + found.getDisplayName()) ;
        if (DEBUG_FLAG) System.out.println("  Description   : " + found.getDescription()) ;
        if (DEBUG_FLAG) System.out.println("  Home space    : " + found.getHomeSpace()) ;
        if (DEBUG_FLAG) System.out.println("  Email address : " + found.getEmailAddress()) ;
        if (DEBUG_FLAG) System.out.println("----") ;
        }
    }
