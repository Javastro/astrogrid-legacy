/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/security/manager/Attic/SecurityManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/02/12 08:12:13 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityManagerImpl.java,v $
 *   Revision 1.2  2004/02/12 08:12:13  dave
 *   Merged development branch, dave-dev-200401131047, into HEAD
 *
 *   Revision 1.1.2.4  2004/02/06 16:14:17  dave
 *   Removed import java.rmi.Remote
 *
 *   Revision 1.1.2.3  2004/02/06 13:49:09  dave
 *   Moved CommunityManagerBase into server.common.CommunityServer.
 *   Moved getServiceStatus into server.common.CommunityServer.
 *   Moved JUnit tests to match.
 *
 *   Revision 1.1.2.2  2004/01/30 18:55:37  dave
 *   Added tests for SecurityManager.setPassword
 *
 *   Revision 1.1.2.1  2004/01/30 03:21:23  dave
 *   Added initial code for SecurityManager and SecurityService
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.security.manager ;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.ObjectNotFoundException ;


import org.astrogrid.community.common.policy.data.AccountData ;

import org.astrogrid.community.common.security.data.PasswordData ;
import org.astrogrid.community.common.security.manager.SecurityManager ;

import org.astrogrid.community.server.common.CommunityServer ;
import org.astrogrid.community.server.database.DatabaseConfiguration ;

/**
 * Implementation of our SecurityManager service.
 *
 */
public class SecurityManagerImpl
	extends CommunityServer
	implements SecurityManager
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
    public SecurityManagerImpl()
        {
		super() ;
        }

    /**
     * Public constructor, using specific database configuration.
     *
     */
    public SecurityManagerImpl(DatabaseConfiguration config)
        {
		super(config) ;
        }

	/**
	 * Set an account password.
	 *
	 */
	public boolean setPassword(String ident, String value)
		{
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("SecurityManagerImpl.setPassword()") ;
        if (DEBUG_FLAG) System.out.println("  Ident : " + ident) ;
        if (DEBUG_FLAG) System.out.println("  Value : " + value) ;
		//
		// Check for null params
		if (null == ident) return false ;
		if (null == value) return false ;
		//
		// Trim spaces.
		ident = ident.trim() ;
		value = value.trim() ;
		//
		// Check for blank params.
		if (ident.length() == 0) return false ;
		if (value.length() == 0) return false ;
		//
		// Set the response to false.
		boolean  result   = false ;
		Database database = null ;
		//
		// Try update the database.
		try {
			//
			// Open our database connection.
			database = this.getDatabase() ;
			//
			// Begin a new database transaction.
			database.begin();
			//
			// Try loading the Account from the database.
			AccountData account = (AccountData) database.load(AccountData.class, ident.toString()) ;
			//
			// If we found the Account.
			if (null != account)
				{
				if (DEBUG_FLAG)System.out.println("  PASS : found account") ;
				//
				// Try loading the PasswordData.
				PasswordData data = null ;
				try {
					data = (PasswordData) database.load(PasswordData.class, ident.toString()) ;
					}
				//
				// Don't worry yf it isn't there.
				catch (ObjectNotFoundException ouch)
					{
					logExpectedException(ouch, "SecurityManagerImpl.setPassword()") ;
					}
				//
				// If we found the PasswordData.
				if (null != data)
					{
					if (DEBUG_FLAG)System.out.println("  PASS : found password") ;
					//
					// Change the password value.
					data.setPassword(value) ;
					data.setEncryption(PasswordData.NO_ENCRYPTION) ;
					}
				//
				// If we didn't find the password.
				else {
					if (DEBUG_FLAG)System.out.println("  PASS : missing password") ;
					//
					// Try to create a new PasswordData in the database.
					data = new PasswordData(ident, value) ;
					database.create(data) ;
					if (DEBUG_FLAG)System.out.println("  PASS : created password") ;
					}
				//
				// Set the response to true.
				result = true ;
				}
			//
			// If we didn't find the Account.
			else {
				if (DEBUG_FLAG)System.out.println("  FAIL : missing account") ;
				//
				// Set the response to false.
				result = false ;
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
			// Set the response to false.
			result = false ;
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
		return result ;
		}

	}
