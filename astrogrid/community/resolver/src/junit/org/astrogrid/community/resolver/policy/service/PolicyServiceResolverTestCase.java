/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/junit/org/astrogrid/community/resolver/policy/service/Attic/PolicyServiceResolverTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/30 01:40:03 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyServiceResolverTestCase.java,v $
 *   Revision 1.3  2004/03/30 01:40:03  dave
 *   Merged development branch, dave-dev-200403242058, into HEAD
 *
 *   Revision 1.2.4.1  2004/03/28 09:11:43  dave
 *   Convert tabs to spaces
 *
 *   Revision 1.2  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/23 14:51:26  dave
 *   Added SecurityManagerResolver and SecurityServiceResolver.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.resolver.policy.service ;

import junit.framework.TestCase ;

import org.apache.axis.client.Call ;

import org.astrogrid.store.Ivorn ;
import org.astrogrid.common.ivorn.MockIvorn ;

import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory ;

import org.astrogrid.community.client.policy.service.PolicyServiceDelegate ;
import org.astrogrid.community.client.policy.service.PolicyServiceMockDelegate ;
import org.astrogrid.community.client.policy.service.PolicyServiceSoapDelegate ;

/**
 * Test case for the PolicyServiceResolver.
 *
 */
public class PolicyServiceResolverTestCase
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
        if (DEBUG_FLAG) System.out.println("PolicyServiceResolverTestCase.testResolveMock()") ;
        //
        // Create our Ivorn.
        Ivorn ivorn = CommunityAccountIvornFactory.createMock(
            "community",
            "frog"
            ) ;
        if (DEBUG_FLAG) System.out.println("  Ivorn : " + ivorn) ;
        //
        // Create our resolver.
        PolicyServiceResolver resolver = new PolicyServiceResolver() ;
        //
        // Resolve our Ivorn into a delegate.
        PolicyServiceDelegate delegate = resolver.resolve(ivorn) ;
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
            PolicyServiceMockDelegate.class,
            delegate.getClass()
            ) ;
        //
        // Check that our service works.
        assertNotNull(
            "Null service status",
            delegate.getServiceStatus()
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
        if (DEBUG_FLAG) System.out.println("PolicyServiceResolverTestCase.testResolveLocal()") ;
        //
        // Create our Ivorn.
        Ivorn ivorn = CommunityAccountIvornFactory.createLocal(
            "frog"
            ) ;
        if (DEBUG_FLAG) System.out.println("  Ivorn : " + ivorn) ;
        //
        // Create our resolver.
        PolicyServiceResolver resolver = new PolicyServiceResolver() ;
        //
        // Resolve our Ivorn into a delegate.
        PolicyServiceDelegate delegate = resolver.resolve(ivorn) ;
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
            PolicyServiceSoapDelegate.class,
            delegate.getClass()
            ) ;
        //
        // Check that our service works.
        assertNotNull(
            "Null service status",
            delegate.getServiceStatus()
            ) ;
        }
    }
