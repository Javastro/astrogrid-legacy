/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/Attic/AccountManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: KevinBenson $</cvs:author>
 * <cvs:date>$Date: 2003/09/15 16:05:45 $</cvs:date>
 * <cvs:version>$Revision: 1.9 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: AccountManagerImpl.java,v $
 *   Revision 1.9  2003/09/15 16:05:45  KevinBenson
 *   *** empty log message ***
 *
 *   Revision 1.8  2003/09/13 02:18:52  dave
 *   Extended the jConfig configuration code.
 *
 *   Revision 1.7  2003/09/12 12:59:17  dave
 *   1) Fixed RemoteException handling in the manager and service implementations.
 *
 *   Revision 1.6  2003/09/10 17:21:43  dave
 *   Added remote functionality to groups.
 *
 *   Revision 1.5  2003/09/10 06:03:27  dave
 *   Added remote capability to Accounts
 *
 *   Revision 1.4  2003/09/09 16:48:48  KevinBenson
 *   added setpassword in their on the update
 *
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

//import java.rmi.RemoteException ;

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
	 * Create a new Account, given the Account name.
	 *
	 */
	public AccountData addAccount(String name)
		{
		return this.addAccount(new CommunityIdent(name)) ;
		}

	/**
	 * Create a new Account, given the Account ident.
	 *
	 */
	protected AccountData addAccount(CommunityIdent ident)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AccountManagerImpl.addAccount()") ;
		if (DEBUG_FLAG) System.out.println("  ident : " + ident) ;

		AccountData account = null ;
		GroupData   group   = null ;
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
               ouch.printStackTrace();
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
               ouch.printStackTrace();
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
                  ouch.printStackTrace();
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
		// Possibly a new DataObject ... ?
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return account ;
		}

   public String getPassword(String name) {
      return this.getPassword(new CommunityIdent(name));
   }
   
   protected String getPassword(CommunityIdent ident) {
      if (DEBUG_FLAG) System.out.println("") ;
      if (DEBUG_FLAG) System.out.println("----\"----") ;
      if (DEBUG_FLAG) System.out.println("AccountManagerImpl.getPassword()") ;
      if (DEBUG_FLAG) System.out.println("  ident : " + ident) ;

      AccountData account = null ;
      String password = null;
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
               password = account.getPassword();
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
               password = null;

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
               password = null;

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
                  password = null;


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
            password = null;

            }
         }
         //
         // If the ident is not valid.
      else {
         //
         // Set the response to null.
         account = null ;
         password = null;

         }

      // TODO
      // Need to return something to the client.
      // Possibly a new DataObject ... ?
      if (DEBUG_FLAG) System.out.println("----\"----") ;
      return password;
   }

	/**
	 * Request an Account data, given the Account name.
	 *
	 */
	public AccountData getAccount(String name)
		{
		return this.getAccount(new CommunityIdent(name)) ;
		}

	/**
	 * Request an Account data, given the Account ident.
	 *
	 */
	protected AccountData getAccount(CommunityIdent ident)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AccountManagerImpl.getAccount()") ;
		if (DEBUG_FLAG) System.out.println("  ident : " + ident) ;

		AccountData account = null ;
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
               account.setPassword(null);
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

		// TODO
		// Need to return something to the client.
		// Possibly a new DataObject ... ?
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return account ;
		}

	/**
	 * Update an existing Account data.
	 *
	 */
	public AccountData setAccount(AccountData account)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AccountManagerImpl.setAccount()") ;
		if (DEBUG_FLAG) System.out.println("  Account") ;
		if (DEBUG_FLAG) System.out.println("    ident : " + account.getIdent()) ;
		if (DEBUG_FLAG) System.out.println("    desc  : " + account.getDescription()) ;
		//
		// Create a CommunityIdent from the account.
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
//
// Not nice.
// We ought to have a separate https method just for this.
					data.setPassword(account.getPassword());
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

		// TODO
		// Need to return something to the client.
		// Possibly a new DataObject ... ?
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return account ;
		}

	/**
	 * Delete an Account, given the Account name.
	 *
	 */
	public AccountData delAccount(String name)
		{
		return this.delAccount(new CommunityIdent(name)) ;
		}

	/**
	 * Delete an Account, given the Account ident.
	 *
	 */
	protected AccountData delAccount(CommunityIdent ident)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AccountManagerImpl.delAccount()") ;
		if (DEBUG_FLAG) System.out.println("  ident : " + ident) ;

		GroupData   group   = null ;
		AccountData account = null ;
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
		// Possibly a new DataObject ... ?
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return account ;
		}

	/**
	 * Request a list of local Accounts.
	 *
	 */
	public Object[] getLocalAccounts()
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AccountManagerImpl.getLocalAccounts()") ;

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
            AccountData ad = (AccountData)results.next();
            ad.setPassword(null);
				collection.add(ad) ;
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
			if (DEBUG_FLAG) System.out.println("Exception in getLocalAccounts()") ;

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
				if (DEBUG_FLAG) System.out.println("Exception in getLocalAccounts() finally clause") ;

				//
				// Set the response to null.
				array = null ;

				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("") ;
				}
			}

		// TODO
		// Need to return something to the client.
		// Possibly a new DataObject ... ?
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return array ;
		}
	}
