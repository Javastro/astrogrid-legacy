/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/Attic/AccountManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/09 10:57:47 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: AccountManagerImpl.java,v $
 *   Revision 1.3  2003/09/09 10:57:47  dave
 *   Added corresponding SINGLE Group to addAccount and delAccount.
 *
 *   Revision 1.2  2003/09/08 20:28:50  dave
 *   Added CommunityIdent, with isLocal() and isValid()
 *
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
import org.astrogrid.community.policy.data.GroupData ;
import org.astrogrid.community.policy.data.AccountData ;
import org.astrogrid.community.policy.data.CommunityIdent ;
import org.astrogrid.community.policy.data.CommunityConfig ;

public class AccountManagerImpl
	implements AccountManager
	{
	/**
	 * Switch for our debug statements.
	 *
	 */
	protected static final boolean DEBUG_FLAG = true ;

	/**
	 * Our database manager.
	 *
	 */
	private DatabaseManager databaseManager ;

	/**
	 * Our database connection.
	 *
	 */
	private Database database ;

	/**
	 * Public constructor.
	 *
	 */
	public AccountManagerImpl()
		{
		}

	/**
	 * Initialise our manager.
	 *
	 */
	public void init(DatabaseManager databaseManager)
		{
		//
		// Keep a reference to our database connection.
		this.databaseManager = databaseManager ;
		this.database = databaseManager.getDatabase() ;
		}

	/**
	 * Create a new Account.
	 *
	 */
	public AccountData addAccount(String name)
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AccountManagerImpl.addAccount()") ;
		if (DEBUG_FLAG) System.out.println("  name  : " + name) ;

		GroupData group = null ;
		AccountData account = null ;
		//
		// Create a CommunityIdent for our Account.
		CommunityIdent ident = new CommunityIdent(name) ;
		if (DEBUG_FLAG) System.out.println("  ident : " + ident) ;
		//
		// If the ident is valid.
		if (ident.isValid())
			{
			//
			// If the ident is local.
			if (ident.isLocal())
				{
				//
				// Create our new Account object.
				account = new AccountData(ident.toString()) ;
				//
				// Create the corresponding Group object.
				group = new GroupData(ident.toString()) ;
				group.setType(GroupData.SINGLE_TYPE) ;
				//
				// Try performing our transaction.
				try {
					//
					// Begin a new database transaction.
					database.begin();
					//
					// Try creating the account in the database.
					database.create(account);
					//
					// Try creating the group in the database.
					database.create(group);
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
					group   = null ;
					account = null ;

					if (DEBUG_FLAG) System.out.println("  ----") ;
					if (DEBUG_FLAG) System.out.println("") ;
					}
				//
				// If anything else went bang.
				catch (Exception ouch)
					{
					if (DEBUG_FLAG) System.out.println("") ;
					if (DEBUG_FLAG) System.out.println("  ----") ;
					if (DEBUG_FLAG) System.out.println("Exception in addAccount()") ;

					//
					// Set the response to null.
					group   = null ;
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
					catch (Exception ouch)
						{
						if (DEBUG_FLAG) System.out.println("") ;
						if (DEBUG_FLAG) System.out.println("  ----") ;
						if (DEBUG_FLAG) System.out.println("Exception in addAccount() finally clause") ;

						//
						// Set the response to null.
						group   = null ;
						account = null ;

						if (DEBUG_FLAG) System.out.println("  ----") ;
						if (DEBUG_FLAG) System.out.println("") ;
						}
					}
				}
			//
			// If the ident is not local.
			else {
				//
				// Set the response to null.
				group   = null ;
				account = null ;
				}
			}
			//
			// If the ident is not valid.
		else {
			//
			// Set the response to null.
			group   = null ;
			account = null ;
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
	public AccountData getAccount(String name)
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AccountManagerImpl.getAccount()") ;
		if (DEBUG_FLAG) System.out.println("  name  : " + name) ;

		AccountData account = null ;
		//
		// Create a CommunityIdent for our Account.
		CommunityIdent ident = new CommunityIdent(name) ;
		if (DEBUG_FLAG) System.out.println("  ident : " + ident) ;
		//
		// If the ident is valid.
		if (ident.isValid())
			{
			//
			// If the ident is local.
			if (ident.isLocal())
				{
				try {
					//
					// Begin a new database transaction.
					database.begin();
					//
					// Load the Account from the database.
					account = (AccountData) database.load(AccountData.class, ident.toString()) ;
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
				catch (Exception ouch)
					{
					if (DEBUG_FLAG) System.out.println("") ;
					if (DEBUG_FLAG) System.out.println("  ----") ;
					if (DEBUG_FLAG) System.out.println("Exception in getAccount()") ;

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
					catch (Exception ouch)
						{
						if (DEBUG_FLAG) System.out.println("") ;
						if (DEBUG_FLAG) System.out.println("  ----") ;
						if (DEBUG_FLAG) System.out.println("Exception in getAccount() finally clause") ;

						//
						// Set the response to null.
						account = null ;

						if (DEBUG_FLAG) System.out.println("  ----") ;
						if (DEBUG_FLAG) System.out.println("") ;
						}
					}
				}
			//
			// If the ident is not local.
			else {
				//
				// Set the response to null.
				account = null ;
				}
			}
			//
			// If the ident is not valid.
		else {
			//
			// Set the response to null.
			account = null ;
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
		// Get the account ident.
		CommunityIdent ident = new CommunityIdent(account.getIdent()) ;
		//
		// If the ident is valid.
		if (ident.isValid())
			{
			//
			// If the ident is local.
			if (ident.isLocal())
				{
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
				catch (Exception ouch)
					{
					if (DEBUG_FLAG) System.out.println("") ;
					if (DEBUG_FLAG) System.out.println("  ----") ;
					if (DEBUG_FLAG) System.out.println("Exception in setAccount()") ;

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
					catch (Exception ouch)
						{
						if (DEBUG_FLAG) System.out.println("") ;
						if (DEBUG_FLAG) System.out.println("  ----") ;
						if (DEBUG_FLAG) System.out.println("Exception in setAccount() finally clause") ;

						//
						// Set the response to null.
						account = null ;

						if (DEBUG_FLAG) System.out.println("  ----") ;
						if (DEBUG_FLAG) System.out.println("") ;
						}
					}
				}
			//
			// If the ident is not local.
			else {
				//
				// Set the response to null.
				account = null ;
				}
			}
			//
			// If the ident is not valid.
		else {
			//
			// Set the response to null.
			account = null ;
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
		// Try to query the database.
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
		catch (Exception ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("Exception in getAccountList()") ;

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
			catch (Exception ouch)
				{
				if (DEBUG_FLAG) System.out.println("") ;
				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("Exception in getAccountList() finally clause") ;

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
	public boolean delAccount(String name)
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AccountManagerImpl.delAccount()") ;
		if (DEBUG_FLAG) System.out.println("  name  : " + name) ;

		//
		// Create a CommunityIdent for our Account.
		CommunityIdent ident = new CommunityIdent(name) ;
		if (DEBUG_FLAG) System.out.println("  ident : " + ident) ;
		//
		// If the ident is valid.
		if (ident.isValid())
			{
			//
			// If the ident is local.
			if (ident.isLocal())
				{
				//
				// Try update the database.
				GroupData   group   = null ;
				AccountData account = null ;
				try {
					//
					// Begin a new database transaction.
					database.begin();
					//
					// Load the Account and Group from the database.
					account = (AccountData) database.load(AccountData.class, ident.toString()) ;
					group   = (GroupData)   database.load(GroupData.class,   ident.toString()) ;
					//
					// Delete the Account and Group together.
					database.remove(account) ;
					database.remove(group)   ;
//
// TODO
// Should remove the Account even if the Group does not exist.
//
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
					group   = null ;
					account = null ;

					if (DEBUG_FLAG) System.out.println("  ----") ;
					if (DEBUG_FLAG) System.out.println("") ;
					}
				//
				// If anything else went bang.
				catch (Exception ouch)
					{
					if (DEBUG_FLAG) System.out.println("") ;
					if (DEBUG_FLAG) System.out.println("  ----") ;
					if (DEBUG_FLAG) System.out.println("Exception in delAccount()") ;

					//
					// Set the response to null.
					group   = null ;
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
					catch (Exception ouch)
						{
						if (DEBUG_FLAG) System.out.println("") ;
						if (DEBUG_FLAG) System.out.println("  ----") ;
						if (DEBUG_FLAG) System.out.println("Exception in delAccount() finally clause") ;

						//
						// Set the response to null.
						group   = null ;
						account = null ;

						if (DEBUG_FLAG) System.out.println("  ----") ;
						if (DEBUG_FLAG) System.out.println("") ;
						}
					}
				}
			//
			// If the ident is not local.
			else {
				//
				// Set the response to null.
				//
				}
			}
			//
			// If the ident is not valid.
		else {
			//
			// Set the response to null.
			//
			}

		if (DEBUG_FLAG) System.out.println("----\"----") ;

		return true ;
		}

	}
