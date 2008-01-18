package org.astrogrid.community.resolver ;

import java.net.URI;
import java.net.URL;
import junit.framework.TestCase;
import org.astrogrid.store.Ivorn;
import org.astrogrid.community.common.policy.manager.PolicyManager;
import org.astrogrid.community.common.ivorn.CommunityIvornParser ;
import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;
import org.astrogrid.community.resolver.exception.CommunityResolverException ;

/**
 * JUnit tests for the CommunityEndpointResolver.
 *
 */
public class CommunityEndpointResolverTestCase extends TestCase {

  /**
   * Test that we can resolve a service in the community.
   *
   */
  public void testResolveService() throws Exception {
    System.out.println("CommunityEndpointResolverTestCase.testResolveLocal()");
        
    Ivorn ivorn = new Ivorn("ivo://org.astrogrid.new-registry/community");
    System.out.println("  IVORN to be resolved: " + ivorn);
    
    // Create our resolver.
    CommunityEndpointResolver resolver = 
        new CommunityEndpointResolver(new MockRegistry());

    // Ask our resolver for the endpoint.
    URL found =
        resolver.resolve(ivorn,
                         "ivo://org.astrogrid/std/Community/v1.0#PolicyManager");
    System.out.println("  Found : " + found);
    assertNotNull("Null endpoint URL", found);
  }
  
  /**
   * Test that we can resolve an account in the community.
   *
   */
  public void testResolveAccount() throws Exception {
    System.out.println("CommunityEndpointResolverTestCase.testResolveLocal()");
        
    Ivorn ivorn = new Ivorn("ivo://frog@org.astrogrid.new-registry/community");
    System.out.println("  IVORN to be resolved: " + ivorn);
    
    // Create our resolver.
    CommunityEndpointResolver resolver = 
        new CommunityEndpointResolver(new MockRegistry());

    // Ask our resolver for the endpoint.
    URL found =
        resolver.resolve(ivorn,
                         "ivo://org.astrogrid/std/Community/v1.0#PolicyManager");
    System.out.println("  Found : " + found);
    assertNotNull("Null endpoint URL", found);
  }


    /**
     * Test that we can handle an unknown params.
     *
     */
    public void testResolveUnknown()
        throws Exception
        {
        System.out.println("") ;
        System.out.println("----\"----") ;
        System.out.println("CommunityEndpointResolverTestCase.testResolveUnknown()") ;
        //
        // Create our Ivorn.
        Ivorn ivorn = CommunityAccountIvornFactory.createIvorn(
            "unknown",
            "frog"
            ) ;
       System.out.println("  Ivorn : " + ivorn) ;
        //
        // Create our resolver.
        CommunityEndpointResolver resolver =
            new CommunityEndpointResolver(new MockRegistry());
        //
        // Ask our resolver for the endpoint of an unknown community.
        try {
            URL found =
                resolver.resolve(ivorn,
                                 "ivo://org.astrogrid/std/Community/v1.0#PolicyManager");
            fail("Expected CommunityResolverException") ;
            }
        catch (CommunityResolverException ouch)
            {
            System.out.println("Caught expected Exception : " + ouch) ;
            }
        }

    /**
     * Test that we can handle null params.
     *
     */
    public void testResolveNulls()
        throws Exception
        {
        System.out.println("") ;
        System.out.println("----\"----") ;
        System.out.println("CommunityEndpointResolverTestCase.testResolveNulls()") ;
        //
        // Create our Ivorn.
        Ivorn ivorn = CommunityAccountIvornFactory.createIvorn(
            "unknown",
            "frog"
            ) ;
        System.out.println("  Ivorn : " + ivorn) ;
        //
        // Create our resolver.
        CommunityEndpointResolver resolver = 
            new CommunityEndpointResolver(new MockRegistry());
        //
        // Check the resolver can handle null params.
        try {
            URL found = resolver.resolve(((Ivorn)null), (String)null) ;
            fail("Expected CommunityResolverException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            System.out.println("Caught expected Exception : " + ouch) ;
            }
        }
    

  
}
