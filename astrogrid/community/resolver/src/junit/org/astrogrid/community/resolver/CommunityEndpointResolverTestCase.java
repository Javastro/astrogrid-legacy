/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/junit/org/astrogrid/community/resolver/CommunityEndpointResolverTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/30 01:40:03 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityEndpointResolverTestCase.java,v $
 *   Revision 1.3  2004/03/30 01:40:03  dave
 *   Merged development branch, dave-dev-200403242058, into HEAD
 *
 *   Revision 1.2.2.1  2004/03/28 09:11:43  dave
 *   Convert tabs to spaces
 *
 *   Revision 1.2  2004/03/24 16:56:25  dave
 *   Merged development branch, dave-dev-200403231641, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/24 15:19:20  dave
 *   Added check for Throwable on registry call.
 *   Added more JUnit tests.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.resolver ;

import java.net.URL ;

import junit.framework.TestCase ;

import org.apache.axis.client.Call ;

import org.astrogrid.store.Ivorn ;
import org.astrogrid.common.ivorn.MockIvorn ;

import org.astrogrid.community.common.policy.manager.PolicyManager ;

import org.astrogrid.community.common.ivorn.CommunityIvornParser ;
import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory ;

import org.astrogrid.community.common.exception.CommunityIdentifierException ;
import org.astrogrid.community.resolver.exception.CommunityResolverException ;

/**
 * Test case for the CommunityEndpointResolver.
 *
 */
public class CommunityEndpointResolverTestCase
    extends TestCase
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Setup our test.
     *
     */
    public void setUp()
        throws Exception
        {
        //
        // Initialise the Axis 'local:' URL protocol.
        Call.initialize() ;
        }

    /**
     * Test that we can resolve a local address.
     *
     */
    public void testResolveLocal()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityEndpointResolverTestCase.testResolveLocal()") ;
        //
        // Create our Ivorn.
        Ivorn ivorn = CommunityAccountIvornFactory.createLocal(
            "community",
            "frog"
            ) ;
        if (DEBUG_FLAG) System.out.println("  Ivorn : " + ivorn) ;
        //
        // Create our resolver.
        CommunityEndpointResolver resolver = new CommunityEndpointResolver() ;
        //
        // Ask our resolver for the endpoint.
        URL found = resolver.resolve(ivorn, PolicyManager.class) ;
        if (DEBUG_FLAG) System.out.println("  Found : " + found) ;
        assertNotNull(
            "Null endpoint URL",
            found
            ) ;
        }

    /**
     * Test that we can handle an unknown params.
     *
     */
    public void testResolveUnknown()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityEndpointResolverTestCase.testResolveUnknown()") ;
        //
        // Create our Ivorn.
        Ivorn ivorn = CommunityAccountIvornFactory.createIvorn(
            "unknown",
            "frog"
            ) ;
        if (DEBUG_FLAG) System.out.println("  Ivorn : " + ivorn) ;
        //
        // Create our resolver.
        CommunityEndpointResolver resolver = new CommunityEndpointResolver() ;
        //
        // Ask our resolver for the endpoint of an unknown community.
        try {
            URL found = resolver.resolve(ivorn, PolicyManager.class) ;
            fail("Expected CommunityResolverException") ;
            }
        catch (CommunityResolverException ouch)
            {
            if (DEBUG_FLAG) System.out.println("Caught expected Exception : " + ouch) ;
            }
        }

    /**
     * Test that we can handle null params.
     *
     */
    public void testResolveNulls()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityEndpointResolverTestCase.testResolveNulls()") ;
        //
        // Create our Ivorn.
        Ivorn ivorn = CommunityAccountIvornFactory.createIvorn(
            "unknown",
            "frog"
            ) ;
        if (DEBUG_FLAG) System.out.println("  Ivorn : " + ivorn) ;
        //
        // Create our resolver.
        CommunityEndpointResolver resolver = new CommunityEndpointResolver() ;
        //
        // Check the resolver can handle null params.
        try {
            URL found = resolver.resolve(((Ivorn)null), null) ;
            fail("Expected CommunityResolverException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            if (DEBUG_FLAG) System.out.println("Caught expected Exception : " + ouch) ;
            }
        try {
            URL found = resolver.resolve(ivorn, null) ;
            fail("Expected CommunityResolverException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            if (DEBUG_FLAG) System.out.println("Caught expected Exception : " + ouch) ;
            }
        try {
            URL found = resolver.resolve(((Ivorn)null), PolicyManager.class) ;
            fail("Expected CommunityResolverException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            if (DEBUG_FLAG) System.out.println("Caught expected Exception : " + ouch) ;
            }
        }
    }
