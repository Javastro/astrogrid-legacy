/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/junit/org/astrogrid/community/resolver/CommunityAccountResolverTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/23 16:34:08 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityAccountResolverTestCase.java,v $
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
        // Create our Account Ivorn.
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
		PolicyManager manager = PolicyManagerMockDelegate.addManager(ident) ;
		//
		// Create an Ivorn parser.
		CommunityIvornParser parser = new CommunityIvornParser(ident) ;
		//
		// Add the account.
		AccountData created = manager.addAccount(parser.getAccountIdent()) ;
		//
		// Create our target Ivorn.
        Ivorn target = CommunityAccountIvornFactory.createMock(
        	"community",
        	"frog/public#qwertyuiop.xml"
        	) ;
		if (DEBUG_FLAG) System.out.println("  Target : " + target) ;
		//
		// Create our resolver.
		CommunityAccountResolver resolver = new CommunityAccountResolver() ;
		//
		// Ask our resolver for the account data.
		AccountData found = resolver.resolve(target) ;
		if (DEBUG_FLAG) System.out.println("  Found : " + found) ;
        }
    }
