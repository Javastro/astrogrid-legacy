/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/codereview/Sept11/policy/server/Attic/CommunityManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: KevinBenson $</cvs:author>
 * <cvs:date>$Date: 2003/09/11 10:24:21 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityManagerImpl.java,v $
 *   Revision 1.1  2003/09/11 10:24:21  KevinBenson
 *   *** empty log message ***
 *
 *   Revision 1.5  2003/09/11 03:15:06  dave
 *   1) Implemented PolicyService internals - no tests yet.
 *   2) Added getLocalAccountGroups and getRemoteAccountGroups to PolicyManager.
 *   3) Added remote access to groups.
 *
 *   Revision 1.4  2003/09/10 06:03:27  dave
 *   Added remote capability to Accounts
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

import java.net.URL ;

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

import java.util.ArrayList;
import org.astrogrid.community.policy.data.ServiceData ;
import org.astrogrid.community.policy.data.CommunityData ;
import org.astrogrid.community.policy.data.CommunityConfig ;

public class CommunityManagerImpl
	implements CommunityManager
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
	public CommunityManagerImpl()
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
	 * Create a new Community.
	 *
	 */
	public CommunityData addCommunity(String name)
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("CommunityManagerImpl.addCommunity()") ;
		if (DEBUG_FLAG) System.out.println("  name : " + name) ;

		//
		// Check that the name is valid.
		//

		//
		// Create the new community.
		CommunityData community = new CommunityData(name) ;
		//
		// Set the default endpoint urls.
		community.setServiceUrl("http://" + community.getIdent() + ":8080/axis/services/PolicyService") ;
		community.setManagerUrl("http://" + community.getIdent() + ":8080/axis/services/PolicyManager") ;
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
		catch (Exception ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("Exception in addCommunity()") ;

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
			catch (Exception ouch)
				{
				if (DEBUG_FLAG) System.out.println("") ;
				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("Exception in addCommunity() finally clause") ;

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
		catch (Exception ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("Exception in getCommunity()") ;

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
			catch (Exception ouch)
				{
				if (DEBUG_FLAG) System.out.println("") ;
				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("Exception in getCommunity() finally clause") ;

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
		catch (Exception ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("Exception in setCommunity()") ;

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
			catch (Exception ouch)
				{
				if (DEBUG_FLAG) System.out.println("") ;
				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("Exception in setCommunity() finally clause") ;

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
		catch (Exception ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("Exception in getCommunityList()") ;

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
				if (DEBUG_FLAG) System.out.println("Exception in getCommunityList() finally clause") ;

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
	public CommunityData delCommunity(String ident)
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("CommunityManagerImpl.delCommunity()") ;
		if (DEBUG_FLAG) System.out.println("  ident : " + ident) ;

		CommunityData community = null ;
		//
		// Try update the database.
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
		catch (Exception ouch)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("  ----") ;
			if (DEBUG_FLAG) System.out.println("Exception in delCommunity()") ;

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
			catch (Exception ouch)
				{
				if (DEBUG_FLAG) System.out.println("") ;
				if (DEBUG_FLAG) System.out.println("  ----") ;
				if (DEBUG_FLAG) System.out.println("Exception in delCommunity() finally clause") ;

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
	 * Get a PolicyManager for a remote community.
	 *
	 */
	public PolicyManager getPolicyManager(String name)
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("CommunityManagerImpl.getPolicyManager()") ;
		if (DEBUG_FLAG) System.out.println("  name : " + name) ;
		//
		// Get the CommunityData.
		CommunityData community = this.getCommunity(name) ;
		//
		// If we found the CommunityData.
		if (null != community)
			{
			if (DEBUG_FLAG) System.out.println("PASS : Found CommunityData") ;
			return getPolicyManager(community) ;
			}
		//
		// If we didn't find the CommunityData.
		else {
			if (DEBUG_FLAG) System.out.println("FAIL : Unknown CommunityData") ;
			return null ;
			}
		}

	/**
	 * Get a PolicyManager for a remote community.
	 *
	 */
	public PolicyManager getPolicyManager(CommunityData community)
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("CommunityManagerImpl.getPolicyManager()") ;
		if (DEBUG_FLAG) System.out.println("  ident : " + community.getIdent()) ;
		PolicyManager manager = null ;
		//
		// If we have a manager URL.
		if (null != community.getManagerUrl())
			{
			if (DEBUG_FLAG) System.out.println("PASS : Found manager URL " + community.getManagerUrl()) ;
			//
			// Try creating our manager.
			try {
				PolicyManagerService locator = new PolicyManagerServiceLocator() ;
				manager = locator.getPolicyManager(new URL(community.getManagerUrl())) ;
				}
			catch (Exception ouch)
				{
				if (DEBUG_FLAG) System.out.println("Exception when creating remote manager.") ;
				if (DEBUG_FLAG) System.out.println("  Exception : " + ouch) ;
				if (DEBUG_FLAG) System.out.println("  Message   : " + ouch.getMessage()) ;
				}
			if (DEBUG_FLAG) System.out.println("PASS : Created manager ...") ;
			}
		//
		// If we don't have a manager URL
		else {
			if (DEBUG_FLAG) System.out.println("FAIL : NULL manager URL") ;
			}
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return manager ;
		}

	/**
	 * Get a PolicyService for a remote community.
	 *
	 */
	public PolicyService getPolicyService(String name)
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("CommunityManagerImpl.getPolicyService()") ;
		if (DEBUG_FLAG) System.out.println("  name : " + name) ;
		//
		// Get the CommunityData.
		CommunityData community = this.getCommunity(name) ;
		//
		// If we found the CommunityData.
		if (null != community)
			{
			if (DEBUG_FLAG) System.out.println("PASS : Found CommunityData") ;
			return getPolicyService(community) ;
			}
		//
		// If we didn't find the CommunityData.
		else {
			if (DEBUG_FLAG) System.out.println("FAIL : Unknown CommunityData") ;
			return null ;
			}
		}

	/**
	 * Get a PolicyService for a remote community.
	 *
	 */
	public PolicyService getPolicyService(CommunityData community)
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("CommunityManagerImpl.getPolicyService()") ;
		if (DEBUG_FLAG) System.out.println("  ident : " + community.getIdent()) ;
		PolicyService service = null ;
		//
		// If we have a service URL.
		if (null != community.getServiceUrl())
			{
			if (DEBUG_FLAG) System.out.println("PASS : Found service URL " + community.getServiceUrl()) ;
			//
			// Try creating our service.
			try {
				PolicyServiceService locator = new PolicyServiceServiceLocator() ;
				service = locator.getPolicyService(new URL(community.getServiceUrl())) ;
				}
			catch (Exception ouch)
				{
				if (DEBUG_FLAG) System.out.println("Exception when creating remote service.") ;
				if (DEBUG_FLAG) System.out.println("  Exception : " + ouch) ;
				if (DEBUG_FLAG) System.out.println("  Message   : " + ouch.getMessage()) ;
				}
			if (DEBUG_FLAG) System.out.println("PASS : Created service ...") ;
			}
		//
		// If we don't have a service URL
		else {
			if (DEBUG_FLAG) System.out.println("FAIL : NULL service URL") ;
			}
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return service ;
		}
	}
