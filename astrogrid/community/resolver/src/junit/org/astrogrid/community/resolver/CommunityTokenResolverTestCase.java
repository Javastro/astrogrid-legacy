/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/junit/org/astrogrid/community/resolver/Attic/CommunityTokenResolverTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/23 16:34:08 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityTokenResolverTestCase.java,v $
 *   Revision 1.2  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/23 15:59:49  dave
 *   Added test case for CommunityTokenResolver.
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
		CommunityPasswordResolver resolver = new CommunityPasswordResolver() ;
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
		CommunityTokenResolver resolver = new CommunityTokenResolver() ;
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
