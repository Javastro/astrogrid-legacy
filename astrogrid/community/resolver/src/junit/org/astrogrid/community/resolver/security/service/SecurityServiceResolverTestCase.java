/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/junit/org/astrogrid/community/resolver/security/service/SecurityServiceResolverTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/23 16:34:08 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityServiceResolverTestCase.java,v $
 *   Revision 1.2  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/23 14:51:26  dave
 *   Added SecurityManagerResolver and SecurityServiceResolver.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.resolver.security.service ;

import junit.framework.TestCase ;

import org.apache.axis.client.Call ;

import org.astrogrid.store.Ivorn ;
import org.astrogrid.common.ivorn.MockIvorn ;

import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory ;

import org.astrogrid.community.client.security.service.SecurityServiceDelegate ;
import org.astrogrid.community.client.security.service.SecurityServiceMockDelegate ;
import org.astrogrid.community.client.security.service.SecurityServiceSoapDelegate ;

/**
 * Test case for the SecurityServiceResolver.
 *
 */
public class SecurityServiceResolverTestCase
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
        if (DEBUG_FLAG) System.out.println("SecurityServiceResolverTestCase.testResolveMock()") ;
        //
        // Create our Ivorn.
        Ivorn ivorn = CommunityAccountIvornFactory.createMock(
        	"community",
        	"frog"
        	) ;
        if (DEBUG_FLAG) System.out.println("  Ivorn : " + ivorn) ;
        //
        // Create our resolver.
        SecurityServiceResolver resolver = new SecurityServiceResolver() ;
        //
        // Resolve our Ivorn into a delegate.
        SecurityServiceDelegate delegate = resolver.resolve(ivorn) ;
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
            SecurityServiceMockDelegate.class,
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
        if (DEBUG_FLAG) System.out.println("SecurityServiceResolverTestCase.testResolveLocal()") ;
        //
        // Create our Ivorn.
        Ivorn ivorn = CommunityAccountIvornFactory.createLocal(
        	"frog"
        	) ;
        if (DEBUG_FLAG) System.out.println("  Ivorn : " + ivorn) ;
        //
        // Create our resolver.
        SecurityServiceResolver resolver = new SecurityServiceResolver() ;
        //
        // Resolve our Ivorn into a delegate.
        SecurityServiceDelegate delegate = resolver.resolve(ivorn) ;
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
            SecurityServiceSoapDelegate.class,
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
