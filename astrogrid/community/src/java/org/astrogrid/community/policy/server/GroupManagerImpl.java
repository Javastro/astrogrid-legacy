/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/Attic/GroupManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: KevinBenson $</cvs:author>
 * <cvs:date>$Date: 2003/09/08 11:01:35 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: GroupManagerImpl.java,v $
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

import org.astrogrid.community.policy.data.ServiceData ;
import org.astrogrid.community.policy.data.GroupData ;
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
	 * Our database connection.
	 *
	 */
	private Database database ;

	/**
	 * Public constructor.
	 *
	 */
	public GroupManagerImpl(Database database)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("GroupManagerImpl()") ;

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
		if (DEBUG_FLAG) System.out.println("GroupManagerImpl.init()") ;

		this.database = database ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Create a new Group.
	 * TODO Change this to only accept the group name.
	 *
	 */
	public GroupData addGroup(GroupData group)
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("GroupManagerImpl.addGroup()") ;
		if (DEBUG_FLAG) System.out.println("  ident : " + group.getIdent()) ;

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
		catch (PersistenceException ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("PersistenceException in addGroup()") ;

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
			catch (PersistenceException ouch)
				{
				if (DEBUG_FLAG) System.out.println("") ;
				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("PersistenceException in addGroup() finally clause") ;

				//
				// Set the response to null.
				group = null ;

				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("") ;
				}
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
	public GroupData getGroup(String ident)
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("GroupManagerImpl.getGroup()") ;
		if (DEBUG_FLAG) System.out.println("  ident : " + ident) ;

		GroupData group = null ;
		try {
			//
			// Begin a new database transaction.
			database.begin();
			//
			// Load the Group from the database.
			group = (GroupData) database.load(GroupData.class, ident) ;
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
		catch (PersistenceException ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("PersistenceException in getGroup()") ;

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
			catch (PersistenceException ouch)
				{
				if (DEBUG_FLAG) System.out.println("") ;
				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("PersistenceException in getGroup() finally clause") ;

				//
				// Set the response to null.
				group = null ;

				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("") ;
				}
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
		catch (PersistenceException ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("PersistenceException in setGroup()") ;

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
			catch (PersistenceException ouch)
				{
				if (DEBUG_FLAG) System.out.println("") ;
				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("PersistenceException in setGroup() finally clause") ;

				//
				// Set the response to null.
				group = null ;

				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("") ;
				}
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
      }catch (PersistenceException ouch)
         {
         if (DEBUG_FLAG) System.out.println("") ;
         if (DEBUG_FLAG) System.out.println("  ----") ;
         if (DEBUG_FLAG) System.out.println("PersistenceException in getGroupList()") ;

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
            if (DEBUG_FLAG) System.out.println("PersistenceException in getGroupList() finally clause") ;

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
		// Try QUERY the database.
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
			
		
		//
		// If anything went bang.
      }catch (PersistenceException ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("PersistenceException in getGroupList()") ;

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
				if (DEBUG_FLAG) System.out.println("PersistenceException in getGroupList() finally clause") ;

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
	public boolean delGroup(String ident)
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("GroupManagerImpl.delGroup()") ;
		if (DEBUG_FLAG) System.out.println("  ident : " + ident) ;

		//
		// Try update the database.
		GroupData group = null ;
		try {
			//
			// Begin a new database transaction.
			database.begin();
			//
			// Load the Group from the database.
			group = (GroupData) database.load(GroupData.class, ident) ;
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
		catch (PersistenceException ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("PersistenceException in delGroup()") ;

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
			catch (PersistenceException ouch)
				{
				if (DEBUG_FLAG) System.out.println("") ;
				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("PersistenceException in delGroup() finally clause") ;

				//
				// Set the response to null.
				group = null ;

				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("") ;
				}
			}

		if (DEBUG_FLAG) System.out.println("----\"----") ;

		return true ;
		}

	}
