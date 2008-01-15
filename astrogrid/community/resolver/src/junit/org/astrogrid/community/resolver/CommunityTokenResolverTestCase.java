/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/junit/org/astrogrid/community/resolver/Attic/CommunityTokenResolverTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: gtr $</cvs:author>
 * <cvs:date>$Date: 2008/01/15 22:57:49 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityTokenResolverTestCase.java,v $
 *   Revision 1.5  2008/01/15 22:57:49  gtr
 *   community-gtr-2491 is merged
 *
 *   Revision 1.4.250.1  2008/01/15 14:32:24  gtr
 *   I fixed it to be independent of configuration files.
 *
 *   Revision 1.4  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.3.32.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.resolver ;

import junit.framework.TestCase ;

import org.astrogrid.store.Ivorn ;
import org.astrogrid.common.ivorn.MockIvorn ;

import org.astrogrid.community.common.security.data.SecurityToken ;

import org.astrogrid.community.common.ivorn.CommunityIvornParser ;
import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory ;


/**
 * Test case for the CommunityTokenResolver.
 *
 */
public class CommunityTokenResolverTestCase
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
        if (DEBUG_FLAG) System.out.println("CommunityTokenResolverTestCase.testResolveMock()") ;
        //
        // Create our Account Ivorn.
        Ivorn ident = CommunityAccountIvornFactory.createMock(
            "community",
            "frog"
            ) ;
        if (DEBUG_FLAG) System.out.println("  Ident  : " + ident) ;
        //
        // Create our password resolver.
        CommunityPasswordResolver resolver = 
            new CommunityPasswordResolver(new MockRegistry()) ;
        //
        // Ask our resolver to check the password
        SecurityToken token = resolver.checkPassword(
            ident.toString(),
            "test-password"
            ) ;
        //
        // Check we got a token back.
        assertNotNull(
            "Null token",
            token
            ) ;
        if (DEBUG_FLAG) System.out.println("  Token : " + token) ;
        //
        // Check we can validate the token.
        checkToken(token) ;
        }

    /**
     * Check that we can validate a token.
     *
     */
    public void checkToken(SecurityToken original)
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityTokenResolverTestCase.checkToken()") ;
        if (DEBUG_FLAG) System.out.println("  Original : " + original) ;
        //
        // Create our token resolver.
        CommunityTokenResolver resolver = 
            new CommunityTokenResolver(new MockRegistry()) ;
        //
        // Ask our resolver to validate the token
        SecurityToken result = resolver.checkToken(
            original
            ) ;
        //
        // Check we got a token back.
        assertNotNull(
            "Null token",
            result
            ) ;
        if (DEBUG_FLAG) System.out.println("  Result   : " + result) ;
        }
    }
