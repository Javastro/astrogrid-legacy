/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/security/service/Attic/SecurityServiceImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/24 17:43:59 $</cvs:date>
 * <cvs:version>$Revision: 1.7 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityServiceImpl.java,v $
 *   Revision 1.7  2004/03/24 17:43:59  dave
 *   Fixed side effects of changes to unit tests
 *
 *   Revision 1.6  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.5.16.2  2004/03/23 14:52:27  dave
 *   Modified the mock ivorn syntax.
 *
 *   Revision 1.5.16.1  2004/03/22 15:31:10  dave
 *   Added CommunitySecurityException.
 *   Updated SecurityManager and SecurityService to use Exceptions.
 *
 *   Revision 1.5  2004/03/08 13:42:33  dave
 *   Updated Maven goals.
 *   Replaced tabs with Spaces.
 *
 *   Revision 1.4.2.1  2004/03/08 12:53:18  dave
 *   Changed tabs to spaces
 *
 *   Revision 1.4  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.3.2.5  2004/03/02 15:29:35  dave
 *   Working round Castor problem with PasswordData - objects remain in database cache
 *
 *   Revision 1.3.2.4  2004/03/01 12:49:42  dave
 *   Updated server SecurityService to match changes to interface
 *
 *   Revision 1.3.2.3  2004/02/23 19:43:47  dave
 *   Refactored DatabaseManager tests to test the interface.
 *   Refactored DatabaseManager tests to use common DatabaseManagerTest.
 *
 *   Revision 1.3.2.2  2004/02/23 08:55:20  dave
 *   Refactored CastorDatabaseConfiguration into DatabaseConfiguration
 *
 *   Revision 1.3.2.1  2004/02/22 20:03:16  dave
 *   Removed redundant DatabaseConfiguration interfaces
 *
 *   Revision 1.3  2004/02/20 21:11:05  dave
 *   Merged development branch, dave-dev-200402120832, into HEAD
 *
 *   Revision 1.2.2.2  2004/02/19 21:09:27  dave
 *   Refactored ServiceStatusData into a common package.
 *   Refactored CommunityServiceImpl constructor to take a parent service.
 *   Refactored default database for CommunityServiceImpl
 *
 *   Revision 1.2.2.1  2004/02/16 15:20:54  dave
 *   Changed tabs to spaces
 *
 *   Revision 1.2  2004/02/12 08:12:13  dave
 *   Merged development branch, dave-dev-200401131047, into HEAD
 *
 *   Revision 1.1.2.9  2004/02/06 16:14:17  dave
 *   Removed import java.rmi.Remote
 *
 *   Revision 1.1.2.8  2004/02/06 13:49:09  dave
 *   Moved CommunityManagerBase into server.common.CommunityServer.
 *   Moved getServiceStatus into server.common.CommunityServer.
 *   Moved JUnit tests to match.
 *
 *   Revision 1.1.2.7  2004/02/05 14:14:05  dave
 *   Extended service test
 *
 *   Revision 1.1.2.6  2004/02/05 13:41:02  dave
 *   Commented out service test
 *
 *   Revision 1.1.2.5  2004/02/05 13:28:28  dave
 *   Initial body for check password
 *
 *   Revision 1.1.2.4  2004/02/05 13:24:22  dave
 *   Initial body for check password
 *
 *   Revision 1.1.2.3  2004/02/05 13:16:45  dave
 *   Initial body for check password
 *
 *   Revision 1.1.2.2  2004/02/03 10:36:02  dave
 *   Added initial SecurityServiceTests.
 *
 *   Revision 1.1.2.1  2004/01/30 03:21:23  dave
 *   Added initial code for SecurityManager and SecurityService
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.security.service ;

import java.util.Vector ;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.ObjectNotFoundException ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.community.server.security.data.PasswordData ;
import org.astrogrid.community.common.security.data.SecurityToken ;
import org.astrogrid.community.common.security.service.SecurityService ;

import org.astrogrid.community.common.ivorn.CommunityIvornParser ;
import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory ;

import org.astrogrid.community.server.service.CommunityServiceImpl ;
import org.astrogrid.community.server.database.configuration.DatabaseConfiguration ;

import org.astrogrid.community.common.exception.CommunityServiceException  ;
import org.astrogrid.community.common.exception.CommunitySecurityException ;
import org.astrogrid.community.common.exception.CommunityIdentifierException  ;

/**
 * Implementation of our SecurityService service.
 *
 */
public class SecurityServiceImpl
    extends CommunityServiceImpl
    implements SecurityService
    {
    /**
     * Switch for our debug statements.
     *
     */
    protected static final boolean DEBUG_FLAG = true ;

    /**
     * Public constructor, using default database configuration.
     *
     */
    public SecurityServiceImpl()
        {
        super() ;
        }

    /**
     * Public constructor, using specific database configuration.
     *
     */
    public SecurityServiceImpl(DatabaseConfiguration config)
        {
        super(config) ;
        }

    /**
     * Public constructor, using a parent service.
     *
     */
    public SecurityServiceImpl(CommunityServiceImpl parent)
        {
        super(parent) ;
        }

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
     * Generate a new token for an account.
     * @param account The Account ident.
     * @throws CommunityIdentifierException If the identifiers are not valid.
	 * @throws CommunityServiceException If the local Community identifier is not set.
     *
     */
    protected SecurityToken createToken(CommunityIvornParser account)
		throws CommunityServiceException, CommunityIdentifierException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("SecurityServiceImpl.createToken()") ;
        String value = null ;
        synchronized (sync)
            {
            value = "TOKEN-" + counter++ ;
            }
		//
		// Create an Ivorn for the token.
		Ivorn ivorn = CommunityAccountIvornFactory.createLocal(
//			account.getAccountName(),
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
        // Return the new token.
        if (DEBUG_FLAG) System.out.println("  Token : " + token) ;
        return token ;
        }

    /**
     * Check an Account password.
     * @param account  The account ident.
     * @param password The account password.
     * @return A valid SecurityToken if the ident and password are valid.
     * @throws CommunitySecurityException If the security check fails.
     * @throws CommunityServiceException If there is an internal error in service.
     * @throws CommunityIdentifierException If the account identifier is invalid.
     * @todo Check Account is local.
     *
     */
    public SecurityToken checkPassword(String account, String password)
        throws CommunityServiceException, CommunitySecurityException, CommunityIdentifierException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("SecurityServiceImpl.checkPassword()") ;
        if (DEBUG_FLAG) System.out.println("  Ident : " + account) ;
        if (DEBUG_FLAG) System.out.println("  Pass  : " + password) ;
        //
        // Check for null account.
        if (null == account)
            {
            throw new CommunityIdentifierException(
                "Null account"
                ) ;
            }
        //
        // Check for null password.
        if (null == password)
            {
            throw new CommunityIdentifierException(
                "Null password"
                ) ;
            }
        //
        // Get the Account ident.
        CommunityIvornParser ident = new CommunityIvornParser(
            account
            ) ;
        //
        // Try searching the database.
		SecurityToken token = null ;
        Database database = null ;
        try {
            //
            // Open our database connection.
            database = this.getDatabase() ;
            //
            // Begin a new database transaction.
            database.begin();
            //
            // Try to load a matching PasswordData.
            PasswordData match = (PasswordData) database.load(PasswordData.class, ident.getAccountIdent()) ;
            if (DEBUG_FLAG)System.out.println("  PASS : Got password data") ;
            if (DEBUG_FLAG)System.out.println("    Account  : " + match.getAccount()) ;
            if (DEBUG_FLAG)System.out.println("    Password : " + match.getPassword()) ;
            //
            // Check if the password matches.
            if (password.equals(match.getPassword()))
                {
                if (DEBUG_FLAG)System.out.println("  PASS : Password matches") ;
                //
                // Create our new SecurityToken.
                token = this.createToken(ident) ;
                database.create(token);
                if (DEBUG_FLAG)System.out.println("  PASS : Got new token") ;
	            //
	            // Commit the database transaction.
	            database.commit() ;
                }
            //
            // If the password don't match.
            else {
	            //
	            // Throw a new Exception.
	            throw new CommunitySecurityException(
	                "Password invalid",
	                ident.getAccountIdent()
	                ) ;
                }
            }
		//
		// If the password check failed.
        catch (CommunitySecurityException ouch)
            {
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            //
            // Throw the Exception.
            throw ouch ;
            }
        //
        // If we couldn't find the object.
        catch (ObjectNotFoundException ouch)
            {
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            //
            // Throw a new Exception.
            throw new CommunitySecurityException(
                "Account not found",
                ident.toString()
                ) ;
            }
        //
        // If anything else went bang.
        catch (Exception ouch)
            {
            //
            // Log the exception.
            logException(
                ouch,
                "SecurityServiceImpl.checkPassword()"
                ) ;
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            //
            // Throw a new Exception.
            throw new CommunityServiceException(
                "Database transaction failed",
                ident.toString(),
                ouch
                ) ;
            }
        //
        // Close our database connection.
        finally
            {
            closeConnection(database) ;
            }
		//
		// Return the new token.
        return token ;
        }

    /**
     * Validate a SecurityToken.
     * Validates a token, and creates a new tokens issued to the same account.
     * @param token The token to validate.
     * @return A new SecurityToken if the original was valid.
     * @throws CommunitySecurityException If the security check fails.
     * @throws CommunityServiceException If there is an internal error in service.
     * @throws CommunityIdentifierException If the token is invalid.
     * @todo Refactor to call split and unpack ?
     * @todo Check Token is local.
     *
     */
    public SecurityToken checkToken(SecurityToken original)
        throws CommunityServiceException, CommunitySecurityException, CommunityIdentifierException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("SecurityServiceImpl.checkToken()") ;
        if (DEBUG_FLAG) System.out.println("  Token : " + original) ;
        //
        // Check for null token.
        if (null == original)
            {
            throw new CommunityIdentifierException(
                "Null token"
                ) ;
            }
		//
		// Mark the original as invalid.
		original.setStatus(SecurityToken.INVALID_TOKEN) ;
        //
        // Get the token value.
        CommunityIvornParser token = new CommunityIvornParser(
            original.getToken()
            ) ;
        if (DEBUG_FLAG) System.out.println("  Token   : " + token) ;
        //
        // Get the Account ident.
        CommunityIvornParser account = new CommunityIvornParser(
            original.getAccount()
            ) ;
        if (DEBUG_FLAG) System.out.println("  Account : " + account) ;

		SecurityToken result = null ;
        Database database = null ;
        try {
            //
            // Open our database connection.
            database = this.getDatabase() ;
            //
            // Begin a new database transaction.
            database.begin();
            //
            // Try loading the original token from our database.
            SecurityToken match = (SecurityToken) database.load(SecurityToken.class, original.getToken()) ;
            if (DEBUG_FLAG)System.out.println("  PASS : Got matching token") ;
            if (DEBUG_FLAG)System.out.println("  Token : " + match) ;
            //
            // If the match is still valid.
            if (match.isValid())
                {
                if (DEBUG_FLAG)System.out.println("  PASS : Original is valid") ;
                //
                // Update the original token.
// Mark as used not invalid.
                match.setStatus(SecurityToken.INVALID_TOKEN) ;
                //
                // Create a new token.
                result = this.createToken(account) ;
                database.create(result) ;
	            //
	            // Commit the database transaction.
	            database.commit() ;
				//
				// Return the new token.
				return result ;
                }
            //
            // If the original is no longer valied.
            else {
                if (DEBUG_FLAG)System.out.println("  FAIL : Original is not valid") ;
	            //
	            // Throw a new Exception.
	            throw new CommunitySecurityException(
	                "Token invalid",
	                original.getToken()
	                ) ;
                }
            }
		//
		// If the password check failed.
        catch (CommunitySecurityException ouch)
            {
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            //
            // Throw the Exception.
            throw ouch ;
            }
        //
        // If we couldn't find the object.
        catch (ObjectNotFoundException ouch)
            {
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            //
            // Throw a new Exception.
            throw new CommunitySecurityException(
                "Token not found",
                original.getToken()
                ) ;
            }
        //
        // If anything else went bang.
        catch (Exception ouch)
            {
            //
            // Log the exception.
            logException(
                ouch,
                "SecurityServiceImpl.checkToken()"
                ) ;
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            //
            // Throw a new Exception.
            throw new CommunityServiceException(
                "Database transaction failed",
                original.getToken(),
                ouch
                ) ;
            }
        //
        // Close our database connection.
        finally
            {
            closeConnection(database) ;
            }
        }

    /**
     * Split a SecurityToken.
     * Validates a token, and then creates a new set of tokens issued to the same account.
     * @throws CommunitySecurityException If the security check fails.
     * @throws CommunityServiceException If there is an internal error in service.
     * @throws CommunityIdentifierException If the token is invalid.
     * @todo Check Token is local.
     *
     */
    public Object[] splitToken(SecurityToken original, int count)
        throws CommunityServiceException, CommunitySecurityException, CommunityIdentifierException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("SecurityServiceImpl.checkToken()") ;
        if (DEBUG_FLAG) System.out.println("  Token : " + original) ;
        //
        // Check for null token.
        if (null == original)
            {
            throw new CommunityIdentifierException(
                "Null token"
                ) ;
            }
		//
		// Mark the original as invalid.
		original.setStatus(SecurityToken.INVALID_TOKEN) ;
        //
        // Get the token value.
        CommunityIvornParser token = new CommunityIvornParser(
            original.getToken()
            ) ;
        if (DEBUG_FLAG) System.out.println("  Token   : " + token) ;
        //
        // Get the Account ident.
        CommunityIvornParser account = new CommunityIvornParser(
            original.getAccount()
            ) ;
        if (DEBUG_FLAG) System.out.println("  Account : " + account) ;

		Vector vector = new Vector() ;
        Database database = null ;
        try {
            //
            // Open our database connection.
            database = this.getDatabase() ;
            //
            // Begin a new database transaction.
            database.begin();
            //
            // Try loading the original token from our database.
            SecurityToken match = (SecurityToken) database.load(SecurityToken.class, original.getToken()) ;
            if (DEBUG_FLAG)System.out.println("  PASS : Got matching token") ;
            if (DEBUG_FLAG)System.out.println("  Token : " + match) ;
            //
            // If the match is still valid.
            if (match.isValid())
                {
                if (DEBUG_FLAG)System.out.println("  PASS : Original is valid") ;
                //
                // Update the original token.
// Mark as used not invalid.
                match.setStatus(SecurityToken.INVALID_TOKEN) ;
                //
                // Create our new tokens.
                for (int i = 0 ; i < count ; i++)
                    {
                    SecurityToken result = this.createToken(account) ;
                    database.create(result) ;
                    vector.add(result) ;
                    }
	            //
	            // Commit the database transaction.
	            database.commit() ;
                }
            //
            // If the original is no longer valied.
            else {
                if (DEBUG_FLAG)System.out.println("  FAIL : Original is not valid") ;
	            //
	            // Throw a new Exception.
	            throw new CommunitySecurityException(
	                "Token invalid",
	                original.getToken()
	                ) ;
                }
            }
		//
		// If the password check failed.
        catch (CommunitySecurityException ouch)
            {
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            //
            // Throw the Exception.
            throw ouch ;
            }
        //
        // If we couldn't find the object.
        catch (ObjectNotFoundException ouch)
            {
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            //
            // Throw a new Exception.
            throw new CommunitySecurityException(
                "Token not found",
                original.getToken()
                ) ;
            }
        //
        // If anything else went bang.
        catch (Exception ouch)
            {
            //
            // Log the exception.
            logException(
                ouch,
                "SecurityServiceImpl.checkToken()"
                ) ;
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            //
            // Throw a new Exception.
            throw new CommunityServiceException(
                "Database transaction failed",
                original.getToken(),
                ouch
                ) ;
            }
        //
        // Close our database connection.
        finally
            {
            closeConnection(database) ;
            }
		//
		// Return the new token.
		return vector.toArray() ;
        }
    }
