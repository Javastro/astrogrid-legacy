/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/security/service/SecurityServiceMock.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.10 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityServiceMock.java,v $
 *   Revision 1.10  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.9.18.3  2004/06/17 14:50:03  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.9.18.2  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.security.service ;

import java.util.Map ;
import java.util.HashMap ;
import java.util.Vector ;

import org.astrogrid.store.Ivorn ;

import java.rmi.server.UID ;

import org.astrogrid.community.common.security.data.SecurityToken ;

import org.astrogrid.community.common.service.CommunityServiceMock ;

import org.astrogrid.community.common.ivorn.CommunityIvornParser ;
import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory ;

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
     * @todo Change this to use the UniqueIdentifier library.
     *
     */
    protected SecurityToken createToken(CommunityIvornParser account)
        throws CommunityIdentifierException
        {
        //
        // Create a new UID.
        UID uid = new UID() ;
        //
        // Create an Ivorn for the token.
        Ivorn ivorn = CommunityAccountIvornFactory.createMock(
            "",
            uid.toString()
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
