/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/Attic/GroupManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/09 14:51:47 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: GroupManagerImpl.java,v $
 *   Revision 1.6  2003/09/09 14:51:47  dave
 *   Added delGroupMember - only local accounts and groups to start with.
 *
 *   Revision 1.5  2003/09/09 13:48:09  dave
 *   Added addGroupMember - only local accounts and groups to start with.
 *
 *   Revision 1.4  2003/09/09 10:57:47  dave
 *   Added corresponding SINGLE Group to addAccount and delAccount.
 *
 *   Revision 1.3  2003/09/08 20:28:50  dave
 *   Added CommunityIdent, with isLocal() and isValid()
 *
 *   Revision 1.2  2003/09/08 11:01:35  KevinBenson
 *   A check in of the Authentication authenticateToken roughdraft and some changes to the groudata and community data
 *   along with an AdministrationDelegate
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

import org.exolab.castor.persist.spi.Complex ;

import org.astrogrid.community.policy.data.ServiceData ;
import org.astrogrid.community.policy.data.GroupData ;
import org.astrogrid.community.policy.data.CommunityIdent ;
import org.astrogrid.community.policy.data.CommunityConfig ;
import org.astrogrid.community.policy.data.GroupMemberData ;

import java.util.ArrayList;

public class GroupManagerImpl
	implements GroupManager
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
	public GroupManagerImpl()
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
	 * Create a new Group.
	 *
	 */
	public GroupData addGroup(String name)
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("GroupManagerImpl.addGroup()") ;
		if (DEBUG_FLAG) System.out.println("  name  : " + name) ;

		GroupData group = null ;
		//
		// Create a CommunityIdent for our Group.
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
				// Create our new Group object.
				group = new GroupData(ident.toString()) ;
				//
				// Try performing our transaction.
				try {
					//
					// Begin a new database transaction.
					database.begin();
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
					if (DEBUG_FLAG) System.out.println("DuplicateIdentityException in addGroup()") ;

					//
					// Set the response to null.
					group = null ;

					if (DEBUG_FLAG) System.out.println("  ----") ;
					if (DEBUG_FLAG) System.out.println("") ;
					}
				//
				// If anything else went bang.
				catch (Exception ouch)
					{
					if (DEBUG_FLAG) System.out.println("") ;
					if (DEBUG_FLAG) System.out.println("  ----") ;
					if (DEBUG_FLAG) System.out.println("Generic exception in addGroup()") ;

					//
					// Set the response to null.
					group = null ;

					if (DEBUG_FLAG) System.out.println("  ----") ;
					if (DEBUG_FLAG) System.out.println("") ;
					}
				//
				// Commit the transaction.
				finally
					{
					try {
						if (null != group)
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
						if (DEBUG_FLAG) System.out.println("Generic exception in addGroup() finally clause") ;

						//
						// Set the response to null.
						group = null ;

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
				group = null ;
				}
			}
			//
			// If the ident is not valid.
		else {
			//
			// Set the response to null.
			group = null ;
			}

		// TODO
		// Need to return something to the client.
		// Possible a new DataObject ... GroupResult ?
		//

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return group ;
		}

	/**
	 * Request an Group details.
	 *
	 */
	public GroupData getGroup(String name)
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("GroupManagerImpl.getGroup()") ;
		if (DEBUG_FLAG) System.out.println("  name  : " + name) ;

		GroupData group = null ;
		//
		// Create a CommunityIdent for our Group.
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
					// Load the Group from the database.
					group = (GroupData) database.load(GroupData.class, ident.toString()) ;
					}
				//
				// If we couldn't find the object.
				catch (ObjectNotFoundException ouch)
					{
					if (DEBUG_FLAG) System.out.println("") ;
					if (DEBUG_FLAG) System.out.println("  ----") ;
					if (DEBUG_FLAG) System.out.println("ObjectNotFoundException in getGroup()") ;

					//
					// Set the response to null.
					group = null ;

					if (DEBUG_FLAG) System.out.println("  ----") ;
					if (DEBUG_FLAG) System.out.println("") ;
					}
				//
				// If anything else went bang.
				catch (Exception ouch)
					{
					if (DEBUG_FLAG) System.out.println("") ;
					if (DEBUG_FLAG) System.out.println("  ----") ;
					if (DEBUG_FLAG) System.out.println("Generic exception in getGroup()") ;

					//
					// Set the response to null.
					group = null ;

					if (DEBUG_FLAG) System.out.println("  ----") ;
					if (DEBUG_FLAG) System.out.println("") ;
					}
				//
				// Commit the transaction.
				finally
					{
					try {
						if (null != group)
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
						if (DEBUG_FLAG) System.out.println("Generic exception in getGroup() finally clause") ;

						//
						// Set the response to null.
						group = null ;

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
				group = null ;
				}
			}
			//
			// If the ident is not valid.
		else {
			//
			// Set the response to null.
			group = null ;
			}

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return group ;
		}

	/**
	 * Update an Group details.
	 *
	 */
	public GroupData setGroup(GroupData group)
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("GroupManagerImpl.setGroup()") ;
		if (DEBUG_FLAG) System.out.println("  Group") ;
		if (DEBUG_FLAG) System.out.println("    ident : " + group.getIdent()) ;
		if (DEBUG_FLAG) System.out.println("    desc  : " + group.getDescription()) ;

		//
		// Get the group ident.
		CommunityIdent ident = new CommunityIdent(group.getIdent()) ;
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
					// Load the Group from the database.
					GroupData data = (GroupData) database.load(GroupData.class, group.getIdent()) ;
					//
					// Update the group data.
					data.setDescription(group.getDescription()) ;
					}
				//
				// If we couldn't find the object.
				catch (ObjectNotFoundException ouch)
					{
					if (DEBUG_FLAG) System.out.println("") ;
					if (DEBUG_FLAG) System.out.println("  ----") ;
					if (DEBUG_FLAG) System.out.println("ObjectNotFoundException in setGroup()") ;

					//
					// Set the response to null.
					group = null ;

					if (DEBUG_FLAG) System.out.println("  ----") ;
					if (DEBUG_FLAG) System.out.println("") ;
					}
				//
				// If anything else went bang.
				catch (Exception ouch)
					{
					if (DEBUG_FLAG) System.out.println("") ;
					if (DEBUG_FLAG) System.out.println("  ----") ;
					if (DEBUG_FLAG) System.out.println("Generic exception in setGroup()") ;

					//
					// Set the response to null.
					group = null ;

					if (DEBUG_FLAG) System.out.println("  ----") ;
					if (DEBUG_FLAG) System.out.println("") ;
					}
				//
				// Commit the transaction.
				finally
					{
					try {
						if (null != group)
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
						if (DEBUG_FLAG) System.out.println("Generic exception in setGroup() finally clause") ;

						//
						// Set the response to null.
						group = null ;

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
				group = null ;
				}
			}
			//
			// If the ident is not valid.
		else {
			//
			// Set the response to null.
			group = null ;
			}

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return group ;
		}
      

   public Object[] getAccountGroupList(String account) throws RemoteException {
      if (DEBUG_FLAG) System.out.println("") ;
      if (DEBUG_FLAG) System.out.println("----\"----") ;
      if (DEBUG_FLAG) System.out.println("GroupManagerImpl.getGroupList()") ;

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
            "SELECT groups FROM org.astrogrid.community.policy.data.GroupData groups where ident = $1 and type = $2"
            );
         query.bind(account);
         query.bind(GroupData.MULTI_TYPE);
         
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
      //
      // If anything went bang.
      }catch (Exception ouch)
         {
         if (DEBUG_FLAG) System.out.println("") ;
         if (DEBUG_FLAG) System.out.println("  ----") ;
         if (DEBUG_FLAG) System.out.println("Generic exception in getGroupList()") ;

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
            if (DEBUG_FLAG) System.out.println("Generic exception in getGroupList() finally clause") ;

            //
            // Set the response to null.
            array = null ;

            if (DEBUG_FLAG) System.out.println("  ----") ;
            if (DEBUG_FLAG) System.out.println("") ;
            }
         }

      if (DEBUG_FLAG) System.out.println("----\"----") ;
      return array;
      
   }

	/**
	 * Request a list of Groups.
	 *
	 */
	public Object[] getGroupList()
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("GroupManagerImpl.getGroupList()") ;

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
				"SELECT groups FROM org.astrogrid.community.policy.data.GroupData groups"
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
			if (DEBUG_FLAG) System.out.println("Generic exception in getGroupList()") ;

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
				if (DEBUG_FLAG) System.out.println("Generic exception in getGroupList() finally clause") ;

				//
				// Set the response to null.
				array = null ;

				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("") ;
				}
			}

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return array;
		}

	/**
	 * Delete an Group.
	 *
	 */
	public boolean delGroup(String name)
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("GroupManagerImpl.delGroup()") ;
		if (DEBUG_FLAG) System.out.println("  name  : " + name) ;
//
// TODO
// Prevent the client from deleting an Account Group if the Account still exists.
//
		//
		// Create a CommunityIdent for our Group.
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
				GroupData group = null ;
				try {
					//
					// Begin a new database transaction.
					database.begin();
					//
					// Load the Group from the database.
					group = (GroupData) database.load(GroupData.class, ident.toString()) ;
					//
					// Delete the group.
					database.remove(group) ;
					}
				//
				// If we couldn't find the object.
				catch (ObjectNotFoundException ouch)
					{
					if (DEBUG_FLAG) System.out.println("") ;
					if (DEBUG_FLAG) System.out.println("  ----") ;
					if (DEBUG_FLAG) System.out.println("ObjectNotFoundException in delGroup()") ;

					//
					// Set the response to null.
					group = null ;

					if (DEBUG_FLAG) System.out.println("  ----") ;
					if (DEBUG_FLAG) System.out.println("") ;
					}
				//
				// If anything else went bang.
				catch (Exception ouch)
					{
					if (DEBUG_FLAG) System.out.println("") ;
					if (DEBUG_FLAG) System.out.println("  ----") ;
					if (DEBUG_FLAG) System.out.println("Generic exception in delGroup()") ;

					//
					// Set the response to null.
					group = null ;

					if (DEBUG_FLAG) System.out.println("  ----") ;
					if (DEBUG_FLAG) System.out.println("") ;
					}
				//
				// Commit the transaction.
				finally
					{
					try {
						if (null != group)
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
						if (DEBUG_FLAG) System.out.println("Generic exception in delGroup() finally clause") ;

						//
						// Set the response to null.
						group = null ;

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
//
// TODO
// Should return a DataObject with status response.
		return true ;
		}

	/**
	 * Add an Account to a Group.
	 * This is not part of the GroupManager interface, and should only be called from the PolicyManager.
	 *
	 */
	public GroupMemberData addGroupMember(CommunityIdent account, CommunityIdent group)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("GroupManagerImpl.addGroupMember()") ;
		if (DEBUG_FLAG) System.out.println("  account  : " + account) ;
		if (DEBUG_FLAG) System.out.println("  group    : " + group) ;
		//
		// Exit if the account is not valid.
		if (false == account.isValid())
			{
			//
			// TODO
			// Should return a DataObject with the reason.
			if (DEBUG_FLAG) System.out.println("Exit - Account is not valid") ;
			return null ;
			}
		//
		// Exit if the group is not valid.
		if (false == group.isValid())
			{
			//
			// TODO
			// Should return a DataObject with the reason.
			if (DEBUG_FLAG) System.out.println("Exit - Group is not valid") ;
			return null ;
			}
		//
		// Exit if the group ident is not local.
		if (false == group.isLocal())
			{
			//
			// TODO
			// Should return a DataObject with the reason.
			if (DEBUG_FLAG) System.out.println("Exit - Group is not local") ;
			return null ;
			}
		//
		// Create our new GroupMemberData.
		GroupMemberData member = new GroupMemberData(account.toString(), group.toString()) ;
		//
		// Try performing our transaction.
		try {
			//
			// Begin a new database transaction.
			database.begin();
			//
			// Try creating the record in the database.
			database.create(member);
			}
		//
		// If the account is already a member of this group.
		catch (DuplicateIdentityException ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("DuplicateIdentityException in addGroupMember()") ;

			//
			// Set the response to null.
			member = null ;

			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("") ;
			}
		//
		// If anything else went bang.
		catch (Exception ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("Generic exception in addGroupMember()") ;

			//
			// Set the response to null.
			member = null ;

			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("") ;
			}
		//
		// Commit the transaction.
		finally
			{
			try {
				if (null != group)
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
				if (DEBUG_FLAG) System.out.println("Generic exception in addGroup() finally clause") ;

				//
				// Set the response to null.
				member = null ;

				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("") ;
				}
			}
		//
		// TODO
		// Should return a DataObject with status response.
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return member ;
		}

	/**
	 * Remove an Account from a Group.
	 * This is not part of the GroupManager interface, and should only be called from the PolicyManager.
	 *
	 */
	public boolean delGroupMember(CommunityIdent account, CommunityIdent group)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("GroupManagerImpl.delGroupMember()") ;
		if (DEBUG_FLAG) System.out.println("  account  : " + account) ;
		if (DEBUG_FLAG) System.out.println("  group    : " + group) ;

		//
		// No checks if the ident is valid.
		// Still want to delete the record even if the ident is invalid.
		//

		//
		// Try update the database.
		GroupMemberData member = null ;
		try {
			//
			// Begin a new database transaction.
			database.begin();
			//
			// Create the database key.
			Complex key = new Complex(
				new Object[]
					{
					account.toString(),
					group.toString()
					}
				) ;
			//
			// Load the GroupMember from the database.
			member = (GroupMemberData) database.load(GroupMemberData.class, key) ;
			//
			// Delete the record.
			database.remove(member) ;
			}
		//
		// If we couldn't find the object.
		catch (ObjectNotFoundException ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("ObjectNotFoundException in delGroupMember()") ;

			//
			// Set the response to null.
			member = null ;

			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("") ;
			}
		//
		// If anything else went bang.
		catch (Exception ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("Generic exception in delGroupMember()") ;

			//
			// Set the response to null.
			member = null ;

			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("") ;
			}
		//
		// Commit the transaction.
		finally
			{
			try {
				if (null != member)
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
				if (DEBUG_FLAG) System.out.println("Generic exception in delGroupMember() finally clause") ;

				//
				// Set the response to null.
				member = null ;

				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("") ;
				}
			}

		//
		// TODO
		// Should return a DataObject with status response.
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return (null != member) ;
		}

	}
