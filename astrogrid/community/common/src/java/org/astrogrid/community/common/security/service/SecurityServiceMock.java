/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/security/service/SecurityServiceMock.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/05 17:19:59 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityServiceMock.java,v $
 *   Revision 1.2  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.1.2.5  2004/03/05 16:28:28  dave
 *   Added SecurityManager delegate test casees.
 *   Refactored Maven JUnit properties.
 *
 *   Revision 1.1.2.4  2004/03/05 14:03:23  dave
 *   Added first client side SOAP test - SecurityServiceSoapDelegateTestCase
 *
 *   Revision 1.1.2.3  2004/03/02 15:29:35  dave
 *   Working round Castor problem with PasswordData - objects remain in database cache
 *
 *   Revision 1.1.2.2  2004/03/01 12:49:42  dave
 *   Updated server SecurityService to match changes to interface
 *
 *   Revision 1.1.2.1  2004/02/27 16:22:14  dave
 *   Added SecurityService interface, mock and test
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.security.service ;

import java.util.Map ;
import java.util.HashMap ;
import java.util.Vector ;

import org.astrogrid.community.common.security.data.SecurityToken ;

import org.astrogrid.community.common.service.CommunityServiceMock ;

/**
 * Mock implementation of our SecurityService service.
 *
 */
public class SecurityServiceMock
	extends CommunityServiceMock
	implements SecurityService
	{
	/**
	 * Switch for our debug statements.
	 *
	 */
	private static boolean DEBUG_FLAG = true ;

	/**
	 * Public constructor.
	 *
	 */
	public SecurityServiceMock()
		{
		super() ;
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("SecurityServiceMock()") ;
		}

	/**
	 * Our hash table of tokens.
	 *
	 */
	protected static Map map = new HashMap() ;

	/**
	 * Our token instance counter.
	 *
	 */
	private static int counter = 0 ;

	/**
	 * Our sync object.
	 *
	 */
	private static Object sync = new Object() ;

	/**
	 * Generate a new token.
	 * @param ident - The Account ident.
	 *
	 */
	protected SecurityToken createToken(String account)
		{
		String value = null ;
		synchronized (sync)
			{
			value = "MOCK-TOKEN-" + counter++ ;
			}
		//
		// Issue a new Security token to the account.
		SecurityToken token = new SecurityToken(account, value) ;
		//
		// Add the token to our map.
		map.put(token.getToken(), token) ;
		//
		// Return the new token.
		return token ;
		}

    /**
     * Check an Account password.
     * @param account The account ident.
     * @param pass The account password.
     * @return A valid SecurityToken if the ident and password are valid.
     *
     */
    public SecurityToken checkPassword(String ident, String value)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("SecurityServiceMock.checkPassword()") ;
		if (DEBUG_FLAG) System.out.println("  Ident : " + ident) ;
		if (DEBUG_FLAG) System.out.println("  Value : " + value) ;
		//
		// Just return a new token.
		return this.createToken(ident) ;
		}

    /**
     * Validate a SecurityToken.
     * Validates a token, and creates a new tokens issued to the same account.
     * Note, this uses the original token, which now becomes invalid.
     * The client should use the new token for subsequent calls to the service.
     * @param The token to validate.
     * @return A new SecurityToken if the original was valid.
     *
     */
    public SecurityToken checkToken(SecurityToken original)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("SecurityServiceMock.checkToken()") ;
		if (DEBUG_FLAG) System.out.println("  Token : " + original) ;
		//
		// See if we still have the orginal
		SecurityToken match = (SecurityToken) map.get(original.getToken()) ;
		//
		// If we do have the original.
		if (null != match)
			{
			//
			// Remove the original from our map
			map.remove(match.getToken()) ;
			//
			// Generate a new token.
			return this.createToken(original.getAccount()) ;
			}
		//
		// If we don't have the original.
		else {
			//
			// Throw an Exception ?
			// Just return null for now.
			return null ;
			}
		}

    /**
     * Split a SecurityToken.
     * Validates a token, and then creates a new set of tokens issued to the same account.
     * Note, this uses the original token, which now becomes invalid.
     * The client should use the first token in the array for subsequent calls to the service.
     * @param The token to validate.
     * @param The number of new tokens required.
     * @return An array of new tokens.
     *
     */
    public Object[] splitToken(SecurityToken original, int count)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("SecurityServiceMock.splitToken()") ;
		if (DEBUG_FLAG) System.out.println("  Token : " + original) ;
		if (DEBUG_FLAG) System.out.println("  Count : " + count) ;
		//
		// See if we still have the orginal
		SecurityToken match = (SecurityToken) map.get(original.getToken()) ;
		//
		// If we do have the original.
		if (null != match)
			{
			//
			// Remove the original from our map
			map.remove(match.getToken()) ;
			//
			// Generate a new set of tokens.
			Vector vector = new Vector() ;
			for (int i = 0 ; i < count ; i++)
				{
				vector.add(
					this.createToken(original.getAccount())
					) ;
				}
			return vector.toArray() ;
			}
		//
		// If we don't have the original.
		else {
			//
			// Throw an Exception ?
			// Just return null for now.
			return null ;
			}
		}
    }
