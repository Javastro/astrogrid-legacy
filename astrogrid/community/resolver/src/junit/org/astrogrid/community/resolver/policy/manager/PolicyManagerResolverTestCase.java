/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/junit/org/astrogrid/community/resolver/policy/manager/PolicyManagerResolverTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/12 15:22:17 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyManagerResolverTestCase.java,v $
 *   Revision 1.2  2004/03/12 15:22:17  dave
 *   Merged development branch, dave-dev-200403101018, into HEAD
 *
 *   Revision 1.1.2.2  2004/03/12 15:08:14  dave
 *   Fixed reference to old class name
 *
 *   Revision 1.1.2.1  2004/03/12 00:46:25  dave
 *   Added CommunityIvornFactory and CommunityIvornParser.
 *   Added MockIvorn (to be moved to common project).
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.resolver.policy.manager ;

import junit.framework.TestCase ;

import org.astrogrid.store.Ivorn ;
import org.astrogrid.common.ivorn.MockIvorn ;

import org.astrogrid.community.client.policy.manager.PolicyManagerDelegate ;
import org.astrogrid.community.client.policy.manager.PolicyManagerMockDelegate ;
import org.astrogrid.community.client.policy.manager.PolicyManagerSoapDelegate ;

import org.astrogrid.community.resolver.CommunityResolver ;

/**
 * Test case for the PolicyManagerResolver.
 *
 */
public class PolicyManagerResolverTestCase
	extends TestCase
	{
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

	/**
	 * Test that we can resolve a mock delegate.
	 *
	 */
	public void testResolveMockDelegate()
		throws Exception
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("PolicyManagerResolverTestCase.testResolveMockDelegate()") ;
		//
		// Create our Ivorn.
		Ivorn ivorn = MockIvorn.createIvorn("policy", "frog") ;
		if (DEBUG_FLAG) System.out.println("  Ivorn : " + ivorn) ;
		//
		// Create our resolver.
		PolicyManagerResolver resolver = new PolicyManagerResolverImpl() ;
		//
		// Resolve our Ivorn into a delegate.
		PolicyManagerDelegate delegate = resolver.resolve(ivorn) ;
		//
		// Check we got something.
		assertNotNull(
			"Null delegate",
			delegate
			) ;
		//
		// Check it is the right type.
		assertEquals(
			"Wrong delegate type.",
			PolicyManagerMockDelegate.class,
			delegate.getClass()
			) ;
		}
	}
