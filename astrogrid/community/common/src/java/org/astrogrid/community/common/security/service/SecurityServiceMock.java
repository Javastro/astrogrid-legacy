/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/security/service/SecurityServiceMock.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/24 17:43:59 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityServiceMock.java,v $
 *   Revision 1.6  2004/03/24 17:43:59  dave
 *   Fixed side effects of changes to unit tests
 *
 *   Revision 1.5  2004/03/24 16:56:25  dave
 *   Merged development branch, dave-dev-200403231641, into HEAD
 *
 *   Revision 1.4.2.2  2004/03/24 16:53:57  dave
 *   Added test password to SecurityServiceMoc.k
 *
 *   Revision 1.4.2.1  2004/03/23 19:28:23  dave
 *   Added test to check token status is valid.
 *
 *   Revision 1.4  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.3.16.2  2004/03/23 14:52:27  dave
 *   Modified the mock ivorn syntax.
 *
 *   Revision 1.3.16.1  2004/03/22 15:31:10  dave
 *   Added CommunitySecurityException.
 *   Updated SecurityManager and SecurityService to use Exceptions.
 *
 *   Revision 1.3  2004/03/08 13:42:33  dave
 *   Updated Maven goals.
 *   Replaced tabs with Spaces.
 *
 *   Revision 1.2.2.1  2004/03/08 12:53:17  dave
 *   Changed tabs to spaces
 *
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

import org.astrogrid.store.Ivorn ;

import org.astrogrid.community.common.security.data.SecurityToken ;

import org.astrogrid.community.common.service.CommunityServiceMock ;

import org.astrogrid.community.common.ivorn.CommunityIvornParser ;
import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory ;

import org.astrogrid.community.common.exception.CommunityServiceException  ;
import org.astrogrid.community.common.exception.CommunitySecurityException ;
import org.astrogrid.community.common.exception.CommunityIdentifierException  ;

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
	 * Our test password.
	 *
	 */
	private static String secret = null ;

	/**
	 * Access to out test password.
	 *
	 */
	public static String getPassword()
		{
		return secret ;
		}

	/**
	 * Access to out test password.
	 *
	 */
	public static void setPassword(String value)
		{
		secret = value ;
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
     * @throws CommunityIdentifierException If the new Ivorn is invalid
     *
     */
    protected SecurityToken createToken(String ident)
		throws CommunityIdentifierException
		{
        //
        // Get the Account ident.
        CommunityIvornParser ivorn = new CommunityIvornParser(
            ident
            ) ;
        if (DEBUG_FLAG) System.out.println("  Ivorn : " + ivorn) ;
		//
		// Generate a new token.
		return this.createToken(ivorn) ;
		}

    /**
     * Generate a new token.
     * @param ident - The Account ident.
     * @throws CommunityIdentifierException If the new Ivorn is invalid
     *
     */
    protected SecurityToken createToken(CommunityIvornParser account)
		throws CommunityIdentifierException
        {
        String value = null ;
        synchronized (sync)
            {
            value = "MOCK-TOKEN-" + counter++ ;
            }
		//
		// Create an Ivorn for the token.
		Ivorn ivorn = CommunityAccountIvornFactory.createMock(
			"",
//			(account.getAccountName() + "/" + value)
			value
			) ;
        //
        // Issue a new Security token to the account.
        SecurityToken token = new SecurityToken(
        	account.getAccountIdent(),
        	ivorn.toString()
        	) ;
        //
        // Mark the token as valid.
        token.setStatus(SecurityToken.VALID_TOKEN) ;
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
     * @throws CommunityIdentifierException If the new Ivorn is invalid
     * @todo Actually check the password ?
     *
     */
    public SecurityToken checkPassword(String ident, String value)
		throws CommunitySecurityException, CommunityIdentifierException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("SecurityServiceMock.checkPassword()") ;
        if (DEBUG_FLAG) System.out.println("  Ident : " + ident) ;
        if (DEBUG_FLAG) System.out.println("  Value : " + value) ;
		//
		// If we have a test password.
		if (null != secret)
			{
			//
			// Check the password matches.
			if (secret.equals(value))
				{
		        //
		        // Return a new token.
		        return this.createToken(ident) ;
				}
			//
			// If the passwords don't match.
			else {
				//
				// Throw an Exception
				throw new CommunitySecurityException(
					"Invalid password"
					) ;
				}
			}
		//
		// If we don't have a test password.
		else {
	        //
	        // Just return a new token.
	        return this.createToken(ident) ;
			}
        }

    /**
     * Validate a SecurityToken.
     * Validates a token, and creates a new tokens issued to the same account.
     * Note, this uses the original token, which now becomes invalid.
     * The client should use the new token for subsequent calls to the service.
     * @param The token to validate.
     * @return A new SecurityToken if the original was valid.
     * @throws CommunitySecurityException If the original token is not valid.
     * @throws CommunityIdentifierException If the new Ivorn is invalid
     *
     */
    public SecurityToken checkToken(SecurityToken original)
		throws CommunitySecurityException, CommunityIdentifierException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("SecurityServiceMock.checkToken()") ;
        if (DEBUG_FLAG) System.out.println("  Token : " + original) ;
		//
		// Mark the original as invalid.
		original.setStatus(SecurityToken.INVALID_TOKEN) ;
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
			throw new CommunitySecurityException(
				"Original token not valid"
				) ;
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
     * @throws CommunitySecurityException If the original token is not valid.
     * @throws CommunityIdentifierException If the new Ivorn is invalid
     *
     */
    public Object[] splitToken(SecurityToken original, int count)
		throws CommunitySecurityException, CommunityIdentifierException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("SecurityServiceMock.splitToken()") ;
        if (DEBUG_FLAG) System.out.println("  Token : " + original) ;
        if (DEBUG_FLAG) System.out.println("  Count : " + count) ;
		//
		// Mark the original as invalid.
		original.setStatus(SecurityToken.INVALID_TOKEN) ;
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
			throw new CommunitySecurityException(
				"Original token not valid"
				) ;
            }
        }
    }
