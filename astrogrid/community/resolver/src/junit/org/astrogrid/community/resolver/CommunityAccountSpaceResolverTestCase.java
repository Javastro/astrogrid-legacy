/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/junit/org/astrogrid/community/resolver/CommunityAccountSpaceResolverTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/15 07:49:30 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityAccountSpaceResolverTestCase.java,v $
 *   Revision 1.2  2004/03/15 07:49:30  dave
 *   Merged development branch, dave-dev-200403121536, into HEAD
 *
 *   Revision 1.1.2.3  2004/03/15 07:07:20  dave
 *   Fixed missing import.
 *
 *   Revision 1.1.2.2  2004/03/15 07:01:49  dave
 *   Changed tests to use ivorn ident in account.
 *
 *   Revision 1.1.2.1  2004/03/15 06:52:08  dave
 *   Refactored PolicyManagerMockDelegate to use ivorn identifiers.
 *   Refactored CommunityAccountResolver to just handle AccountData.
 *   Added CommunityAccountSpaceResolver to resolve home space ivorn.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.resolver ;

import junit.framework.TestCase ;

import org.astrogrid.store.Ivorn ;
import org.astrogrid.common.ivorn.MockIvorn ;

import org.astrogrid.community.common.ivorn.CommunityIvornParser ;

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
		Ivorn ident = new MockIvorn("community", "frog") ;
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
		// Set the Account home address.
		created.setHomeSpace(home.toString()) ;
		manager.setAccount(created) ;

		//
		// Create our target Ivorn.
		Ivorn target = new MockIvorn("community", "frog/public#qwertyuiop.xml") ;
		if (DEBUG_FLAG) System.out.println("  Target : " + target) ;
		//
		// Create our resolver.
		CommunityAccountSpaceResolver resolver = new CommunityAccountSpaceResolver() ;
		//
		// Ask our resolver for the home address.
		Ivorn found = resolver.resolve(target) ;
		if (DEBUG_FLAG) System.out.println("  Found : " + found) ;
        }
    }
