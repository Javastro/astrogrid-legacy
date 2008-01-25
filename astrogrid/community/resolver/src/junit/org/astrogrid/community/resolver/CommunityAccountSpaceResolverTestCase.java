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
        PolicyManager manager = new PolicyManagerMockDelegate() ;
        //
        // Add the account.
        AccountData created = new AccountData(
            new CommunityIvornParser(ident).getAccountIdent()
            ) ;
        created.setHomeSpace(home.toString()) ;
        manager.addAccount(created) ;
        //
        // Create our target Ivorn.
        Ivorn target = CommunityAccountIvornFactory.createMock(
            "community",
            "frog#qwertyuiop.xml"
            ) ;
        if (DEBUG_FLAG) System.out.println("  Target : " + target) ;
        //
        // Create our resolver.
        CommunityAccountSpaceResolver resolver = 
            new CommunityAccountSpaceResolver(new MockRegistry()) ;
        //
        // Ask our resolver for the home address.
        Ivorn found = resolver.resolve(target) ;
        assertNotNull(
            "Failed to resolve home space",
            found
            ) ;
        if (DEBUG_FLAG) System.out.println("  Found : " + found) ;
        
        if (DEBUG_FLAG) System.out.println("  Found toString : " + found.toString()) ;
        //
        // Check that the home space is right.
        assertEquals(
            "Wrong ivorn for home space:",
            "ivo://org.astrogrid.mock.myspace/toad#qwertyuiop.xml",
            found.toString()
            ) ;

        }

  /**
   * Tests resolution of a home IVORN.
   * Home IVORNs don't have URI fragments expressing a MySpace path.
   * Any software that assumes a non-null path would have problems here.
   */
  public void testResolveMockHome() throws Exception {
    System.out.println("CommunityAccountSpaceResolver.testResolveMockHome()") ;
    Ivorn account = CommunityAccountIvornFactory.createMock("community","newt");
    Ivorn home = new MockIvorn("myspace", "toad");
    PolicyManager manager = new PolicyManagerMockDelegate();
    AccountData created = new AccountData(
        new CommunityIvornParser(account).getAccountIdent()
    );
    created.setHomeSpace(home.toString()) ;
    manager.addAccount(created);
    
    CommunityAccountSpaceResolver sut = 
        new CommunityAccountSpaceResolver(new MockRegistry());
    
    Ivorn found = sut.resolve(account);
    System.out.println("Home space found = " + found);
    assertNotNull("Failed to resolve home space", found);
    assertEquals("Wrong ivorn",
                 "ivo://org.astrogrid.mock.myspace/toad",
                 found.toString());
  }

}