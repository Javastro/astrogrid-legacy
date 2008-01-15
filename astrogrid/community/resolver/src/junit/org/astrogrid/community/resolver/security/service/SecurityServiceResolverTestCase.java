package org.astrogrid.community.resolver.security.service ;

import junit.framework.TestCase;
import org.astrogrid.community.resolver.MockRegistry;
import org.astrogrid.store.Ivorn;
import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory;
import org.astrogrid.community.client.security.service.SecurityServiceDelegate;
import org.astrogrid.community.client.security.service.SecurityServiceMockDelegate;
import org.astrogrid.community.client.security.service.SecurityServiceSoapDelegate;

/**
 * JUnit tests for the SecurityServiceResolver.
 *
 */
public class SecurityServiceResolverTestCase extends TestCase {
    
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;



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
        SecurityServiceResolver resolver = 
            new SecurityServiceResolver(new MockRegistry());
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
        Ivorn ivorn = CommunityAccountIvornFactory.createIvorn(
            "org.astrogrid.new-registry",
            "community",
            "frog"
            ) ;
        if (DEBUG_FLAG) System.out.println("  Ivorn : " + ivorn) ;
        //
        // Create our resolver.
        SecurityServiceResolver resolver = 
            new SecurityServiceResolver(new MockRegistry());
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
        }
    }
