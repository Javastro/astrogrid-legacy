package org.astrogrid.community.resolver.policy.manager ;

import junit.framework.TestCase ;
import org.astrogrid.store.Ivorn ;
import org.astrogrid.common.ivorn.MockIvorn ;
import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory ;
import org.astrogrid.community.client.policy.manager.PolicyManagerDelegate ;
import org.astrogrid.community.client.policy.manager.PolicyManagerMockDelegate ;
import org.astrogrid.community.client.policy.manager.PolicyManagerSoapDelegate ;
import org.astrogrid.community.resolver.MockRegistry;

/**
 * Test case for the PolicyManagerResolver.
 *
 */
public class PolicyManagerResolverTestCase extends TestCase {
    
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
        PolicyManagerResolver resolver = 
            new PolicyManagerResolver(new MockRegistry());
        
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
        Ivorn ivorn = CommunityAccountIvornFactory.createIvorn(
            "org.astrogrid.new-registry",
            "community",
            "test-account"
            ) ;
        if (DEBUG_FLAG) System.out.println("  Ivorn : " + ivorn) ;
        //
        // Create our resolver.
        PolicyManagerResolver resolver = 
            new PolicyManagerResolver(new MockRegistry());
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
        }
    }
