/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/security/service/Attic/SecurityServiceImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/02/12 08:12:13 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityServiceImpl.java,v $
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

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;

import org.astrogrid.community.common.policy.data.AccountData ;

import org.astrogrid.community.common.security.data.PasswordData ;

import org.astrogrid.community.server.common.CommunityServer ;
import org.astrogrid.community.server.database.DatabaseConfiguration ;

import org.astrogrid.community.common.security.service.SecurityService ;

/**
 * Implementation of our SecurityService service.
 *
 */
public class SecurityServiceImpl
	extends CommunityServer
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
	 * Check an Account password.
	 * Returns an AccountData if the name and password are valid.
	 *
	 */
	public AccountData checkPassword(String ident, String pass)
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
		ident = ident.trim()  ;
		pass  = pass.trim() ;
		//
		// Check for blank params.
		if (ident.length() == 0) return null ;
		if (pass.length() == 0) return null ;

		//
		// Set the response to null.
		AccountData account  = null ;
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
			// Try find a matching password data in the database.
			OQLQuery query = database.getOQLQuery(
				"SELECT data FROM org.astrogrid.community.common.security.data.PasswordData data WHERE data.account = $1 AND data.password = $2"
				);
			//
			// Bind the query param.
			query.bind(ident) ;
			query.bind(pass) ;
			//
			// Execute our query.
			QueryResults results = query.execute();
			//
			// If we got some query results.
			if (null != results)
				{
				if (DEBUG_FLAG)System.out.println("  PASS : Got results") ;
				//
				// If we found a matching object.
				if (results.hasMore())
					{
					if (DEBUG_FLAG)System.out.println("  PASS : Results have more") ;
					//
					// Load the matching password data.
					PasswordData match = (PasswordData) results.next() ;
					//
					// If we found a matching password data.
					if (null != match)
						{
						if (DEBUG_FLAG)System.out.println("  PASS : Got password data") ;
//
// TODO - This should use AccountManager
						//
						// Try loading the matching account data.
						account = (AccountData) database.load(AccountData.class, match.getAccount()) ;
						}
					//
					// If we didn't find a match.
					else {
						if (DEBUG_FLAG)System.out.println("  FAIL : Null password data") ;
						}
					}
				//
				// If our query results are empty.
				else {
					if (DEBUG_FLAG)System.out.println("  FAIL : Empty query results") ;
					}
				}
			//
			// If we didn't get any results.
			else {
				if (DEBUG_FLAG)System.out.println("  FAIL : Null query results") ;
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
			logException(ouch, "SecurityManagerImpl.setPassword()") ;
			//
			// Set the response to null.
			account = null ;
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

        if (DEBUG_FLAG) System.out.println("----\"----") ;
		return account ;
		}

	}
