/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/Attic/AccountManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/06 20:10:07 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: AccountManagerImpl.java,v $
 *   Revision 1.1  2003/09/06 20:10:07  dave
 *   Split PolicyManager into separate components.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.server ;

import java.rmi.RemoteException ;

import java.util.Vector ;
import java.util.Collection ;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.PersistenceException ;
import org.exolab.castor.jdo.ObjectNotFoundException ;
import org.exolab.castor.jdo.DatabaseNotFoundException ;
import org.exolab.castor.jdo.DuplicateIdentityException ;
import org.exolab.castor.jdo.TransactionNotInProgressException ;
import org.exolab.castor.jdo.ClassNotPersistenceCapableException ;

import org.astrogrid.community.policy.data.ServiceData ;
import org.astrogrid.community.policy.data.AccountData ;

public class AccountManagerImpl
	implements AccountManager
	{
	/**
	 * Switch for our debug statements.
	 *
	 */
	protected static final boolean DEBUG_FLAG = true ;

	/**
	 * Our database connection.
	 *
	 */
	private Database database ;

	/**
	 * Public constructor.
	 *
	 */
	public AccountManagerImpl(Database database)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AccountManagerImpl()") ;

		//
		// Initialise our database.
		this.init(database) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		}

	/**
	 * Initialise our manager.
	 *
	 */
	public void init(Database database)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AccountManagerImpl.init()") ;

		this.database = database ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Create a new Account.
	 * TODO Change this to only accept the account name.
	 *
	 */
	public AccountData addAccount(AccountData account)
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AccountManagerImpl.addAccount()") ;
		if (DEBUG_FLAG) System.out.println("  ident : " + account.getIdent()) ;

		//
		// Check that the ident is valid.
		//

		//
		// Try performing our transaction.
		try {
			//
			// Begin a new database transaction.
			database.begin();
			//
			// Try creating the account in the database.
			database.create(account);
			}
		//
		// If we already have an object with that ident.
		catch (DuplicateIdentityException ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("DuplicateIdentityException in addAccount()") ;

			//
			// Set the response to null.
			account = null ;

			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("") ;
			}
		//
		// If anything else went bang.
		catch (PersistenceException ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("PersistenceException in addAccount()") ;

			//
			// Set the response to null.
			account = null ;

			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("") ;
			}
		//
		// Commit the transaction.
		finally
			{
			try {
				if (null != account)
					{
					database.commit() ;
					}
				else {
					database.rollback() ;
					}
				}
			catch (PersistenceException ouch)
				{
				if (DEBUG_FLAG) System.out.println("") ;
				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("PersistenceException in addAccount() finally clause") ;

				//
				// Set the response to null.
				account = null ;

				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("") ;
				}
			}

		// TODO
		// Need to return something to the client.
		// Possible a new DataObject ... AccountResult ?
		//

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return account ;
		}

	/**
	 * Request an Account details.
	 *
	 */
	public AccountData getAccount(String ident)
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AccountManagerImpl.getAccount()") ;
		if (DEBUG_FLAG) System.out.println("  ident : " + ident) ;

		AccountData account = null ;
		try {
			//
			// Begin a new database transaction.
			database.begin();
			//
			// Load the Account from the database.
			account = (AccountData) database.load(AccountData.class, ident) ;
			}
		//
		// If we couldn't find the object.
		catch (ObjectNotFoundException ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("ObjectNotFoundException in getAccount()") ;

			//
			// Set the response to null.
			account = null ;

			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("") ;
			}
		//
		// If anything else went bang.
		catch (PersistenceException ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("PersistenceException in getAccount()") ;

			//
			// Set the response to null.
			account = null ;

			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("") ;
			}
		//
		// Commit the transaction.
		finally
			{
			try {
				if (null != account)
					{
					database.commit() ;
					}
				else {
					database.rollback() ;
					}
				}
			catch (PersistenceException ouch)
				{
				if (DEBUG_FLAG) System.out.println("") ;
				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("PersistenceException in getAccount() finally clause") ;

				//
				// Set the response to null.
				account = null ;

				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("") ;
				}
			}

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return account ;
		}

	/**
	 * Update an Account details.
	 *
	 */
	public AccountData setAccount(AccountData account)
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AccountManagerImpl.setAccount()") ;
		if (DEBUG_FLAG) System.out.println("  Account") ;
		if (DEBUG_FLAG) System.out.println("    ident : " + account.getIdent()) ;
		if (DEBUG_FLAG) System.out.println("    desc  : " + account.getDescription()) ;

		//
		// Try update the database.
		try {
			//
			// Begin a new database transaction.
			database.begin();
			//
			// Load the Account from the database.
			AccountData data = (AccountData) database.load(AccountData.class, account.getIdent()) ;
			//
			// Update the account data.
			data.setDescription(account.getDescription()) ;
			}
		//
		// If we couldn't find the object.
		catch (ObjectNotFoundException ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("ObjectNotFoundException in setAccount()") ;

			//
			// Set the response to null.
			account = null ;

			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("") ;
			}
		//
		// If anything else went bang.
		catch (PersistenceException ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("PersistenceException in setAccount()") ;

			//
			// Set the response to null.
			account = null ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("") ;
			}
		//
		// Commit the transaction.
		finally
			{
			try {
				if (null != account)
					{
					database.commit() ;
					}
				else {
					database.rollback() ;
					}
				}
			catch (PersistenceException ouch)
				{
				if (DEBUG_FLAG) System.out.println("") ;
				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("PersistenceException in setAccount() finally clause") ;

				//
				// Set the response to null.
				account = null ;

				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("") ;
				}
			}

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return account ;
		}

	/**
	 * Request a list of Accounts.
	 *
	 */
	public Object[] getAccountList()
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AccountManagerImpl.getAccountList()") ;

		//
		// Try QUERY the database.
		Object[] array = null ;
		try {
			//
			// Begin a new database transaction.
			database.begin();
			//
			// Create our OQL query.
			OQLQuery query = database.getOQLQuery(
				"SELECT accounts FROM org.astrogrid.community.policy.data.AccountData accounts"
				);
			//
			// Execute our query.
			QueryResults results = query.execute();
			//
			// Transfer our results to a vector.
			Collection collection = new Vector() ;
			while (results.hasMore())
				{
				collection.add(results.next()) ;
				}
			//
			// Convert it into an array.
			array = collection.toArray() ;
			}
		//
		// If anything went bang.
		catch (PersistenceException ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("PersistenceException in getAccountList()") ;

			//
			// Set the response to null.
			array = null ;

			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("") ;
			}
		//
		// Commit the transaction.
		finally
			{
			try {
				if (null != array)
					{
					database.commit() ;
					}
				else {
					database.rollback() ;
					}
				}
			catch (PersistenceException ouch)
				{
				if (DEBUG_FLAG) System.out.println("") ;
				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("PersistenceException in getAccountList() finally clause") ;

				//
				// Set the response to null.
				array = null ;

				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("") ;
				}
			}

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return array ;
		}

	/**
	 * Delete an Account.
	 *
	 */
	public boolean delAccount(String ident)
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AccountManagerImpl.delAccount()") ;
		if (DEBUG_FLAG) System.out.println("  ident : " + ident) ;

		//
		// Try update the database.
		AccountData account = null ;
		try {
			//
			// Begin a new database transaction.
			database.begin();
			//
			// Load the Account from the database.
			account = (AccountData) database.load(AccountData.class, ident) ;
			//
			// Delete the account.
			database.remove(account) ;
			}
		//
		// If we couldn't find the object.
		catch (ObjectNotFoundException ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("ObjectNotFoundException in delAccount()") ;

			//
			// Set the response to null.
			account = null ;

			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("") ;
			}
		//
		// If anything else went bang.
		catch (PersistenceException ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("PersistenceException in delAccount()") ;

			//
			// Set the response to null.
			account = null ;

			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("") ;
			}
		//
		// Commit the transaction.
		finally
			{
			try {
				if (null != account)
					{
					database.commit() ;
					}
				else {
					database.rollback() ;
					}
				}
			catch (PersistenceException ouch)
				{
				if (DEBUG_FLAG) System.out.println("") ;
				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("PersistenceException in delAccount() finally clause") ;

				//
				// Set the response to null.
				account = null ;

				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("") ;
				}
			}

		if (DEBUG_FLAG) System.out.println("----\"----") ;

		return true ;
		}

	}
