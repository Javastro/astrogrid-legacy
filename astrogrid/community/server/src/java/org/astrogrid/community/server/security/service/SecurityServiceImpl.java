/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/security/service/Attic/SecurityServiceImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/05 17:19:59 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityServiceImpl.java,v $
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

import org.astrogrid.community.server.security.data.PasswordData ;
import org.astrogrid.community.common.security.data.SecurityToken ;
import org.astrogrid.community.common.security.service.SecurityService ;

import org.astrogrid.community.server.service.CommunityServiceImpl ;
import org.astrogrid.community.server.database.configuration.DatabaseConfiguration ;

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
//
// TODO - Replace this with a real implemetation.
// Possibly refactor it into a helper class SecurityTokenGenerator ?
//
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
	 * TODO - Refactor this using a helper.
	 * @param ident - The Account ident.
	 *
	 */
	protected SecurityToken createToken(String ident)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("SecurityServiceImpl.createToken()") ;
		String value = null ;
		synchronized (sync)
			{
			value = "MOCK-TOKEN-" + counter++ ;
			}
		//
		// Issue a new Security token to the account.
		SecurityToken token = new SecurityToken(ident, value) ;
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
     * @param ident - The account ident.
     * @param pass - The account password.
     * @return A valid SecurityToken if the ident and password are valid.
     *
     */
    public SecurityToken checkPassword(String ident, String pass)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("SecurityServiceImpl.checkPassword()") ;
        if (DEBUG_FLAG) System.out.println("  Ident : " + ident) ;
        if (DEBUG_FLAG) System.out.println("  Pass  : " + pass) ;

        //
        // Check for null params
        if (null == ident) return null ;
        if (null == pass)  return null ;
        //
        // Trim spaces.
        ident = ident.trim() ;
        pass  = pass.trim()  ;
        //
        // Check for blank params.
        if (ident.length() == 0) return null ;
        if (pass.length() == 0) return null ;

        //
        // Set the response to null.
        SecurityToken token  = null ;
        Database    database = null ;
        //
        // Try searching the database.
        try {
            //
            // Open our database connection.
            database = this.getDatabase() ;
            //
            // Begin a new database transaction.
            database.begin();

			//
			// Try to load a matching PasswordData.
			PasswordData match = (PasswordData) database.load(PasswordData.class, ident) ;
			//
			// If we found a matching password data.
			if (null != match)
				{
				if (DEBUG_FLAG)System.out.println("  PASS : Got password data") ;
				if (DEBUG_FLAG)System.out.println("    Account  : " + match.getAccount()) ;
				if (DEBUG_FLAG)System.out.println("    Password : " + match.getPassword()) ;
				//
				// Check if the password matches.
				if (pass.equals(match.getPassword()))
					{
					if (DEBUG_FLAG)System.out.println("  PASS : Password matches") ;
					//
					// Create our new SecurityToken.
					token = this.createToken(ident) ;
					if (null != token)
						{
						if (DEBUG_FLAG)System.out.println("  PASS : Got new token") ;
						}
					database.create(token);
					}
				//
				// If the password don't match.
				else {
					if (DEBUG_FLAG)System.out.println("  FAIL : Password invalid") ;
					}
				}
			//
			// If we didn't find a match.
			else {
				if (DEBUG_FLAG)System.out.println("  FAIL : Password not in database") ;
				}
            //
            // Commit the database transaction.
            database.commit() ;
            }
        //
        // If anything went bang.
        catch (Exception ouch)
            {
            //
            // Log the exception.
            logException(ouch, "SecurityManagerImpl.checkPassword()") ;
            //
            // Set the response to null.
            token = null ;
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            }
        //
        // Close our database connection.
        finally
            {
            closeConnection(database) ;
            }
        return token ;
        }

    /**
     * Validate a SecurityToken.
     * Validates a token, and creates a new tokens issued to the same account.
     * Note, this uses the original token, which now becomes invalid.
     * The client should use the new token for subsequent calls to the service.
     * @param original - The token to validate.
     * @return A new SecurityToken if the original was valid.
     *
     */
	public SecurityToken checkToken(SecurityToken original)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("SecurityServiceImpl.checkToken()") ;
		if (DEBUG_FLAG) System.out.println("  Token : " + original) ;

		SecurityToken result = null ;
		Database    database = null ;
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
				match.setStatus(SecurityToken.INVALID_TOKEN) ;
				//
				// Create a new token.
				result = this.createToken(match.getAccount()) ;
				database.create(result) ;
				}
			//
			// If the original is no longer valied.
			else {
				if (DEBUG_FLAG)System.out.println("  FAIL : Original is not valid") ;
				//
				// TODO Throw an Exception ?
				// Just set the response to null for now.
				result = null ;
				}
			//
			// Commit the database transaction.
			database.commit() ;
			}
        //
        // If anything went bang.
        catch (Exception ouch)
            {
            //
            // Log the exception.
            logException(ouch, "SecurityManagerImpl.checkToken()") ;
            //
            // Set the response to null.
            result = null ;
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            }
        //
        // Close our database connection.
        finally
            {
            closeConnection(database) ;
            }
		return result ;
		}

    /**
     * Split a SecurityToken.
     * Validates a token, and then creates a new set of tokens issued to the same account.
     * Note, this uses the original token, which now becomes invalid.
     * The client should use the first token in the array for subsequent calls to the service.
     * @param - The token to validate.
     * @param - The number of new tokens required.
     * @return An array of new tokens.
     *
     */
    public Object[] splitToken(SecurityToken original, int count)
		{
		Vector   vector   = new Vector() ;
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
			if (DEBUG_FLAG)System.out.println("  PASS  : Got matching token") ;
			if (DEBUG_FLAG)System.out.println("  Token : " + match) ;
			//
			// If the match is still valid.
			if (match.isValid())
				{
				if (DEBUG_FLAG)System.out.println("  PASS : Original is valid") ;
				//
				// Update the original token.
				match.setStatus(SecurityToken.INVALID_TOKEN) ;
				//
				// Create our new tokens.
				for (int i = 0 ; i < count ; i++)
					{
					SecurityToken token = this.createToken(match.getAccount()) ;
					database.create(token) ;
					vector.add(token) ;
					}
				}
			//
			// If the original is no longer valied.
			else {
				if (DEBUG_FLAG)System.out.println("  FAIL : Original is not valid") ;
				//
				// TODO Throw an Exception ?
				// Just set the response to null for now.
				vector = null ;
				}
			//
			// Commit the database transaction.
			database.commit() ;
			}
        //
        // If anything went bang.
        catch (Exception ouch)
            {
            //
            // Log the exception.
            logException(ouch, "SecurityManagerImpl.checkToken()") ;
            //
            // Set the response to null.
            vector = null ;
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            }
        //
        // Close our database connection.
        finally
            {
            closeConnection(database) ;
            }
		return (null != vector) ? vector.toArray() : null ;
		}

    }
