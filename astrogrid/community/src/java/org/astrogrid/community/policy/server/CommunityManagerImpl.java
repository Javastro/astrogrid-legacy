/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/Attic/CommunityManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/06 20:10:07 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityManagerImpl.java,v $
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
import org.astrogrid.community.policy.data.CommunityData ;

public class CommunityManagerImpl
	implements CommunityManager
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
	public CommunityManagerImpl(Database database)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("CommunityManagerImpl()") ;

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
		if (DEBUG_FLAG) System.out.println("CommunityManagerImpl.init()") ;

		this.database = database ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("") ;
		}

	/**
	 * Create a new Community.
	 * TODO Change this to only accept the community name.
	 *
	 */
	public CommunityData addCommunity(CommunityData community)
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("CommunityManagerImpl.addCommunity()") ;
		if (DEBUG_FLAG) System.out.println("  ident : " + community.getIdent()) ;

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
			// Try creating the community in the database.
			database.create(community);
			}
		//
		// If we already have an object with that ident.
		catch (DuplicateIdentityException ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("DuplicateIdentityException in addCommunity()") ;

			//
			// Set the response to null.
			community = null ;

			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("") ;
			}
		//
		// If anything else went bang.
		catch (PersistenceException ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("PersistenceException in addCommunity()") ;

			//
			// Set the response to null.
			community = null ;

			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("") ;
			}
		//
		// Commit the transaction.
		finally
			{
			try {
				if (null != community)
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
				if (DEBUG_FLAG) System.out.println("PersistenceException in addCommunity() finally clause") ;

				//
				// Set the response to null.
				community = null ;

				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("") ;
				}
			}

		// TODO
		// Need to return something to the client.
		// Possible a new DataObject ... CommunityResult ?
		//

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return community ;
		}

	/**
	 * Request an Community details.
	 *
	 */
	public CommunityData getCommunity(String ident)
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("CommunityManagerImpl.getCommunity()") ;
		if (DEBUG_FLAG) System.out.println("  ident : " + ident) ;

		CommunityData community = null ;
		try {
			//
			// Begin a new database transaction.
			database.begin();
			//
			// Load the Community from the database.
			community = (CommunityData) database.load(CommunityData.class, ident) ;
			}
		//
		// If we couldn't find the object.
		catch (ObjectNotFoundException ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("ObjectNotFoundException in getCommunity()") ;

			//
			// Set the response to null.
			community = null ;

			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("") ;
			}
		//
		// If anything else went bang.
		catch (PersistenceException ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("PersistenceException in getCommunity()") ;

			//
			// Set the response to null.
			community = null ;

			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("") ;
			}
		//
		// Commit the transaction.
		finally
			{
			try {
				if (null != community)
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
				if (DEBUG_FLAG) System.out.println("PersistenceException in getCommunity() finally clause") ;

				//
				// Set the response to null.
				community = null ;

				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("") ;
				}
			}

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return community ;
		}

	/**
	 * Update an Community details.
	 *
	 */
	public CommunityData setCommunity(CommunityData community)
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("CommunityManagerImpl.setCommunity()") ;
		if (DEBUG_FLAG) System.out.println("  Community") ;
		if (DEBUG_FLAG) System.out.println("    ident   : " + community.getIdent()) ;
		if (DEBUG_FLAG) System.out.println("    desc    : " + community.getDescription()) ;
		if (DEBUG_FLAG) System.out.println("    service : " + community.getServiceUrl()) ;
		if (DEBUG_FLAG) System.out.println("    manager : " + community.getManagerUrl()) ;

		//
		// Try update the database.
		try {
			//
			// Begin a new database transaction.
			database.begin();
			//
			// Load the Community from the database.
			CommunityData data = (CommunityData) database.load(CommunityData.class, community.getIdent()) ;
			//
			// Update the community data.
			data.setManagerUrl(community.getManagerUrl()) ;
			data.setServiceUrl(community.getServiceUrl()) ;
			data.setDescription(community.getDescription()) ;
			}
		//
		// If we couldn't find the object.
		catch (ObjectNotFoundException ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("ObjectNotFoundException in setCommunity()") ;

			//
			// Set the response to null.
			community = null ;

			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("") ;
			}
		//
		// If anything else went bang.
		catch (PersistenceException ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("PersistenceException in setCommunity()") ;

			//
			// Set the response to null.
			community = null ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("") ;
			}
		//
		// Commit the transaction.
		finally
			{
			try {
				if (null != community)
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
				if (DEBUG_FLAG) System.out.println("PersistenceException in setCommunity() finally clause") ;

				//
				// Set the response to null.
				community = null ;

				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("") ;
				}
			}

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return community ;
		}

	/**
	 * Request a list of Communitys.
	 *
	 */
	public Object[] getCommunityList()
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("CommunityManagerImpl.getCommunityList()") ;

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
				"SELECT communitys FROM org.astrogrid.community.policy.data.CommunityData communitys"
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
			if (DEBUG_FLAG) System.out.println("PersistenceException in getCommunityList()") ;

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
				if (DEBUG_FLAG) System.out.println("PersistenceException in getCommunityList() finally clause") ;

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
	 * Delete an Community.
	 *
	 */
	public boolean delCommunity(String ident)
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("CommunityManagerImpl.delCommunity()") ;
		if (DEBUG_FLAG) System.out.println("  ident : " + ident) ;

		//
		// Try update the database.
		CommunityData community = null ;
		try {
			//
			// Begin a new database transaction.
			database.begin();
			//
			// Load the Community from the database.
			community = (CommunityData) database.load(CommunityData.class, ident) ;
			//
			// Delete the community.
			database.remove(community) ;
			}
		//
		// If we couldn't find the object.
		catch (ObjectNotFoundException ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("ObjectNotFoundException in delCommunity()") ;

			//
			// Set the response to null.
			community = null ;

			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("") ;
			}
		//
		// If anything else went bang.
		catch (PersistenceException ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("PersistenceException in delCommunity()") ;

			//
			// Set the response to null.
			community = null ;

			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("") ;
			}
		//
		// Commit the transaction.
		finally
			{
			try {
				if (null != community)
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
				if (DEBUG_FLAG) System.out.println("PersistenceException in delCommunity() finally clause") ;

				//
				// Set the response to null.
				community = null ;

				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("") ;
				}
			}

		if (DEBUG_FLAG) System.out.println("----\"----") ;

		return true ;
		}

	}
