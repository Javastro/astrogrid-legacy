/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/junit/org/astrogrid/community/resolver/policy/manager/PolicyManagerResolverTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/23 16:34:08 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyManagerResolverTestCase.java,v $
 *   Revision 1.5  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.4.2.2  2004/03/23 14:51:26  dave
 *   Added SecurityManagerResolver and SecurityServiceResolver.
 *
 *   Revision 1.4.2.1  2004/03/23 10:48:32  dave
 *   Registry configuration files and PolicyManagerResolver tests.
 *
 *   Revision 1.4  2004/03/19 14:43:15  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.3.2.1  2004/03/16 11:55:15  dave
 *   Split CommunityIvornFactory into CommunityAccountIvornFactory and CommunityServiceIvornFactory.
 *
 *   Revision 1.3  2004/03/15 07:49:30  dave
 *   Merged development branch, dave-dev-200403121536, into HEAD
 *
 *   Revision 1.2.2.3  2004/03/15 06:52:08  dave
 *   Refactored PolicyManagerMockDelegate to use ivorn identifiers.
 *   Refactored CommunityAccountResolver to just handle AccountData.
 *   Added CommunityAccountSpaceResolver to resolve home space ivorn.
 *
 *   Revision 1.2.2.2  2004/03/13 16:08:08  dave
 *   Added CommunityAccountResolver and CommunityEndpointResolver.
 *
 *   Revision 1.2.2.1  2004/03/12 17:42:09  dave
 *   Replaced tabs with spaces
 *
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

import org.apache.axis.client.Call ;

import org.astrogrid.store.Ivorn ;
import org.astrogrid.common.ivorn.MockIvorn ;

import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory ;

import org.astrogrid.community.client.policy.manager.PolicyManagerDelegate ;
import org.astrogrid.community.client.policy.manager.PolicyManagerMockDelegate ;
import org.astrogrid.community.client.policy.manager.PolicyManagerSoapDelegate ;

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
     * Setup our test.
     * Initialise the Axis 'local:' URL protocol.
     *
     */
    public void setUp()
        throws Exception
        {
        Call.initialize() ;
        }

    /**
     * Test that we can resolve a mock delegate.
     *
     */
    public void testResolveMock()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyManagerResolverTestCase.testResolveMock()") ;
        //
        // Create our Ivorn.
        Ivorn ivorn = CommunityAccountIvornFactory.createMock(
        	"community",
        	"frog"
        	) ;
        if (DEBUG_FLAG) System.out.println("  Ivorn : " + ivorn) ;
        //
        // Create our resolver.
        PolicyManagerResolver resolver = new PolicyManagerResolver() ;
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
		//
		// Check that our service works.
		assertNotNull(
            "Null service status",
			delegate.getServiceStatus()
			) ;
		//
		// Try adding an account
		assertNotNull(
            "Null account data",
			delegate.addAccount(
				ivorn.toString()
				)
			) ;
        }

    /**
     * Test that we can resolve a local service.
     *
     */
    public void testResolveLocal()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyManagerResolverTestCase.testResolveLocal()") ;
        //
        // Create our Ivorn.
        Ivorn ivorn = CommunityAccountIvornFactory.createLocal(
        	"frog"
        	) ;
        if (DEBUG_FLAG) System.out.println("  Ivorn : " + ivorn) ;
        //
        // Create our resolver.
        PolicyManagerResolver resolver = new PolicyManagerResolver() ;
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
            PolicyManagerSoapDelegate.class,
            delegate.getClass()
            ) ;
		//
		// Check that our service works.
		assertNotNull(
            "Null service status",
			delegate.getServiceStatus()
			) ;
		//
		// Try adding an account
		assertNotNull(
            "Null account data",
			delegate.addAccount(
				ivorn.toString()
				)
			) ;
        }
    }
