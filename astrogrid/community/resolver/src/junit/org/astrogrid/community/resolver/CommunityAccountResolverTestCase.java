/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/junit/org/astrogrid/community/resolver/CommunityAccountResolverTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/30 01:40:03 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityAccountResolverTestCase.java,v $
 *   Revision 1.6  2004/03/30 01:40:03  dave
 *   Merged development branch, dave-dev-200403242058, into HEAD
 *
 *   Revision 1.5.2.1  2004/03/28 09:11:43  dave
 *   Convert tabs to spaces
 *
 *   Revision 1.5  2004/03/24 16:56:25  dave
 *   Merged development branch, dave-dev-200403231641, into HEAD
 *
 *   Revision 1.4.2.1  2004/03/24 15:19:20  dave
 *   Added check for Throwable on registry call.
 *   Added more JUnit tests.
 *
 *   Revision 1.4  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.3.2.1  2004/03/22 15:31:10  dave
 *   Added CommunitySecurityException.
 *   Updated SecurityManager and SecurityService to use Exceptions.
 *
 *   Revision 1.3  2004/03/19 14:43:15  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.2.2.1  2004/03/16 11:55:15  dave
 *   Split CommunityIvornFactory into CommunityAccountIvornFactory and CommunityServiceIvornFactory.
 *
 *   Revision 1.2  2004/03/15 07:49:30  dave
 *   Merged development branch, dave-dev-200403121536, into HEAD
 *
 *   Revision 1.1.2.4  2004/03/15 07:01:49  dave
 *   Changed tests to use ivorn ident in account.
 *
 *   Revision 1.1.2.3  2004/03/15 06:52:08  dave
 *   Refactored PolicyManagerMockDelegate to use ivorn identifiers.
 *   Refactored CommunityAccountResolver to just handle AccountData.
 *   Added CommunityAccountSpaceResolver to resolve home space ivorn.
 *
 *   Revision 1.1.2.2  2004/03/13 17:57:20  dave
 *   Remove RemoteException(s) from delegate interfaces.
 *   Protected internal API methods.
 *
 *   Revision 1.1.2.1  2004/03/13 16:08:08  dave
 *   Added CommunityAccountResolver and CommunityEndpointResolver.
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
 * Test case for the CommunityAccountResolver.
 *
 */
public class CommunityAccountResolverTestCase
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
        if (DEBUG_FLAG) System.out.println("CommunityAccountResolverTestCase.testResolveMock()") ;
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
    }
