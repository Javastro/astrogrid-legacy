/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/Attic/AccountManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: KevinBenson $</cvs:author>
 * <cvs:date>$Date: 2003/10/10 13:27:51 $</cvs:date>
 * <cvs:version>$Revision: 1.16 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: AccountManagerImpl.java,v $
 *   Revision 1.16  2003/10/10 13:27:51  KevinBenson
 *   commented out the setPassword(null)
 *
 *   Revision 1.15  2003/10/09 01:38:30  dave
 *   Added JUnite tests for policy delegates
 *
 *   Revision 1.14  2003/10/07 20:40:25  KevinBenson
 *   conforms to new myspace delegate
 *
 *   Revision 1.13  2003/09/30 16:07:30  pah
 *   removed the password nulling in getAccount() - it was being written back to the database
 *
 *   Revision 1.12  2003/09/24 21:56:06  dave
 *   Added setPassword() to AccountManager
 *
 *   Revision 1.11  2003/09/17 19:47:21  dave
 *   1) Fixed classnotfound problems in the build.
 *   2) Added the JUnit task to add the initial accounts and groups.
 *   3) Got the build to work together with the portal.
 *   4) Fixed some bugs in the Account handling.
 *
 *   Revision 1.10  2003/09/17 09:16:10  KevinBenson
 *   Added the Myspace call
 *
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

import org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate;

import org.astrogrid.community.policy.data.ServiceData ;
import org.astrogrid.community.policy.data.GroupData ;
import org.astrogrid.community.policy.data.AccountData ;
import org.astrogrid.community.policy.data.CommunityIdent ;
import org.astrogrid.community.policy.data.GroupMemberData ;

import org.astrogrid.community.common.CommunityConfig;

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
				// Create the default group membership objects.
// TODO
// Need to add the account to the account group.
// Need to add the account to the guest group.
// Only do this if the guest group exists.
				GroupMemberData groupmember = new GroupMemberData(ident.toString(), ident.toString()) ;
				GroupMemberData guestmember = new GroupMemberData(ident.toString(), "guest") ;

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
					//
					// Try adding the account to the groups.
// TODO
// Need to add the account to the account group.
// Need to add the account to the guest group.
					database.create(groupmember);
					database.create(guestmember);
					}
				//
				// If we already have an object with that ident.
				catch (DuplicateIdentityException ouch)
					{
					if (DEBUG_FLAG) System.out.println("") ;
					if (DEBUG_FLAG) System.out.println("  ----") ;
					if (DEBUG_FLAG) System.out.println("DuplicateIdentityException in addAccount()") ;
					//ouch.printStackTrace();
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
					//ouch.printStackTrace();
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

		//
		// Add the user to the local myspace.
		if(account != null)
			{
			String myspaceUrl = CommunityConfig.getProperty("myspace.url.webservice");
			//
			// If we have a local MySpace service.
			if ((null != myspaceUrl) && (myspaceUrl.length() > 0))
				{
				try {
					MySpaceManagerDelegate msmd = new MySpaceManagerDelegate(myspaceUrl);
					Vector tempVector = new Vector();
					tempVector.add("serv1");
					msmd.createUser(ident.getName(),ident.getCommunity(),"credential",tempVector);
					}
				catch(Exception ouch)
					{
					ouch.printStackTrace();
					}
				}
			}
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
	 * Update an Account password.
	 *
	 */
	public AccountData setPassword(String account, String password)
		{
		return setPassword(new CommunityIdent(account), password) ;
		}

	/**
	 * Update an existing Account data.
	 *
	 */
	public AccountData setPassword(CommunityIdent ident, String password)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AccountManagerImpl.setPassword()") ;
		if (DEBUG_FLAG) System.out.println("  Account") ;
		if (DEBUG_FLAG) System.out.println("    ident : " + ident) ;
		if (DEBUG_FLAG) System.out.println("    pass  : " + password) ;

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
					// Load the Account from the database.
					account = (AccountData) database.load(AccountData.class, ident.toString()) ;
					//
					// Update the account data.
					account.setPassword(password);
					}
				//
				// If we couldn't find the object.
				catch (ObjectNotFoundException ouch)
					{
					if (DEBUG_FLAG) System.out.println("") ;
					if (DEBUG_FLAG) System.out.println("  ----") ;
					if (DEBUG_FLAG) System.out.println("ObjectNotFoundException in setPassword()") ;

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
					if (DEBUG_FLAG) System.out.println("Exception in setPassword()") ;
					if (DEBUG_FLAG) System.out.println("  Exception  : " + ouch) ;

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
						if (DEBUG_FLAG) System.out.println("Exception in setPassword() finally clause") ;

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
					//
					// Always set the password to null. FIXME - why was this done?  - pah - I think It might be an error that is causing authentication to fail remember that it is done in a castor transaction, so it will be persisted back to the database....
//					account.setPassword(null);
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
					// Ignore the password, use setPassword() to change it.
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

		AccountData account = null ;

		//
		// If the ident is valid.
		if (ident.isValid())
			{
			if (DEBUG_FLAG) System.out.println("  PASS : ident is valid") ;
			//
			// If the ident is local.
			if (ident.isLocal())
				{
				if (DEBUG_FLAG) System.out.println("  PASS : ident is local") ;
				//
				// Try update the database.
				try {
					//
					// Begin a new database transaction.
					database.begin();

					//
					// Load the Account from the database.
					account = (AccountData) database.load(AccountData.class, ident.toString()) ;
					//
					// If we found the Account.
					if (null != account)
						{
						if (DEBUG_FLAG)System.out.println("  PASS : found account") ;
						//
						// Find the group for this account (if it exists).
						OQLQuery groupQuery = database.getOQLQuery(
							"SELECT groups FROM org.astrogrid.community.policy.data.GroupData groups WHERE groups.ident = $1"
							);
						//
						// Bind the query param.
						groupQuery.bind(ident.toString()) ;
						//
						// Execute our query.
						QueryResults groups = groupQuery.execute();
						if (null != groups)
							{
							if (DEBUG_FLAG)System.out.println("  PASS : found groups") ;
							}
						else {
							if (DEBUG_FLAG)System.out.println("  FAIL : null groups") ;
							}

						//
						// Find all of the group memberships for this account.
						OQLQuery memberQuery = database.getOQLQuery(
							"SELECT members FROM org.astrogrid.community.policy.data.GroupMemberData members WHERE members.account = $1"
							);
						//
						// Bind the query param.
						memberQuery.bind(ident.toString()) ;
						//
						// Execute our query.
						QueryResults members = memberQuery.execute();
						if (null != members)
							{
							if (DEBUG_FLAG)System.out.println("  PASS : found members") ;
							}
						else {
							if (DEBUG_FLAG)System.out.println("  FAIL : null members") ;
							}

						//
						// Load all the permissions for this group.
						OQLQuery permissionQuery = database.getOQLQuery(
							"SELECT permissions FROM org.astrogrid.community.policy.data.PolicyPermission permissions WHERE permissions.group = $1"
							);
						//
						// Bind the query param.
						permissionQuery.bind(ident.toString()) ;
						//
						// Execute our query.
						QueryResults permissions = permissionQuery.execute();
						if (null != permissions)
							{
							if (DEBUG_FLAG)System.out.println("  PASS : found permissions") ;
							}
						else {
							if (DEBUG_FLAG)System.out.println("  FAIL : null permissions") ;
							}
						//
						// Delete the permissions.
						while (permissions.hasMore())
							{
							if (DEBUG_FLAG) System.out.println("  STEP : deleting permission") ;
							database.remove(permissions.next()) ;
							}
						//
						// Delete the group memberships.
						while (members.hasMore())
							{
							if (DEBUG_FLAG) System.out.println("  STEP : deleting membership") ;
							database.remove(members.next()) ;
							}
						//
						// Delete the Group.
						while (groups.hasMore())
							{
							if (DEBUG_FLAG) System.out.println("  STEP : deleting group") ;
							database.remove(groups.next()) ;
							}
						//
						// Delete the Account.
						database.remove(account) ;
						}
					if (DEBUG_FLAG) System.out.println("  PASS : finished deleting") ;
					//
					// Commit the transaction.
					database.commit() ;
					if (DEBUG_FLAG) System.out.println("  PASS : done commit") ;
					}
				//
				// If anything went bang.
				catch (Exception ouch)
					{
					if (DEBUG_FLAG) System.out.println("") ;
					if (DEBUG_FLAG) System.out.println("  ----") ;
					if (DEBUG_FLAG) System.out.println("Exception in delAccount()") ;
					if (DEBUG_FLAG) System.out.println("  Exception : " + ouch) ;
					if (DEBUG_FLAG) System.out.println("  Message   : " + ouch.getMessage()) ;

					//
					// Rollback the transaction.
					try {
						database.rollback() ;
						}
					catch (Exception any)
						{
						if (DEBUG_FLAG) System.out.println("") ;
						if (DEBUG_FLAG) System.out.println("Exception in delAccount() rollback") ;
						}
					if (DEBUG_FLAG) System.out.println("  ----") ;
					if (DEBUG_FLAG) System.out.println("") ;
					}
				}
			//
			// If the ident is not local.
			else {
				account = null ;
//
// TODO
// Actually, this could be a call from a remote community to tell us that it is deleting an Account.
// In which case, we need to tidy up our groups and permissions tables ....
//
				}
			}
			//
			// If the ident is not valid.
		else {
			account = null ;
			}

//
// TODO .. should we notify other community services that the Account has gone ?
//

//
// TODO
// Should this be inside our database transaction ?
		String myspaceUrl = CommunityConfig.getProperty("myspace.url.webservice");
		//
		// If we have a local MySpace service.
		if ((null != myspaceUrl) && (myspaceUrl.length() > 0))
			{
			MySpaceManagerDelegate msmd = new MySpaceManagerDelegate(myspaceUrl);
			try {
				msmd.deleteUser(ident.getName(),ident.getCommunity(),"credential");
				}
			catch(Exception ouch)
				{
				ouch.printStackTrace();
				}
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
          //  ad.setPassword(null);
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
