/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/codereview/Sept11/policy/server/Attic/PermissionManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: KevinBenson $</cvs:author>
 * <cvs:date>$Date: 2003/09/11 10:24:21 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PermissionManagerImpl.java,v $
 *   Revision 1.1  2003/09/11 10:24:21  KevinBenson
 *   *** empty log message ***
 *
 *   Revision 1.1  2003/09/10 02:56:03  dave
 *   Added PermissionManager and tests
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
import org.astrogrid.community.policy.data.ResourceIdent ;
import org.astrogrid.community.policy.data.CommunityIdent ;
import org.astrogrid.community.policy.data.PolicyPermission ;

public class PermissionManagerImpl
	implements PermissionManager
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
	public PermissionManagerImpl()
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
	 * Create a new PolicyPermission.
	 *
	 */
	public PolicyPermission addPermission(String resourceName, String groupName, String action)
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("PermissionManagerImpl.addPermission()") ;
		if (DEBUG_FLAG) System.out.println("  resource : " + resourceName) ;
		if (DEBUG_FLAG) System.out.println("  group    : " + groupName) ;
		if (DEBUG_FLAG) System.out.println("  action   : " + action) ;

		PolicyPermission permission = null ;
		//
		// Create a ResourceIdent for our Resource.
		ResourceIdent resourceIdent = new ResourceIdent(resourceName) ;
		if (DEBUG_FLAG) System.out.println("  resource : " + resourceIdent) ;
		//
		// Create a CommunityIdent for our Group.
		CommunityIdent groupIdent = new CommunityIdent(groupName) ;
		if (DEBUG_FLAG) System.out.println("  group    : " + groupIdent) ;

		//
		// If the resource ident is valid.
		if (resourceIdent.isValid())
			{
			//
			// If the resource ident is local.
			if (resourceIdent.isLocal())
				{
				//
				// If the group ident is valid.
				if (groupIdent.isValid())
					{
					//
					// Create our new PolicyPermission.
					permission = new PolicyPermission(resourceIdent.toString(), groupIdent.toString(), action) ;
					//
					// Try adding it to the database.
					try {
						//
						// Begin a new database transaction.
						database.begin();
						//
						// Try creating it in the database.
						database.create(permission);
						}
					//
					// If we already have an object with that ident.
					catch (DuplicateIdentityException ouch)
						{
						if (DEBUG_FLAG) System.out.println("") ;
						if (DEBUG_FLAG) System.out.println("  ----") ;
						if (DEBUG_FLAG) System.out.println("DuplicateIdentityException in addPermission()") ;

						//
						// Set the response to null.
						permission = null ;

						if (DEBUG_FLAG) System.out.println("  ----") ;
						if (DEBUG_FLAG) System.out.println("") ;
						}
					//
					// If anything else went bang.
					catch (Exception ouch)
						{
						if (DEBUG_FLAG) System.out.println("") ;
						if (DEBUG_FLAG) System.out.println("  ----") ;
						if (DEBUG_FLAG) System.out.println("Generic exception in addPermission()") ;

						//
						// Set the response to null.
						permission = null ;

						if (DEBUG_FLAG) System.out.println("  ----") ;
						if (DEBUG_FLAG) System.out.println("") ;
						}
					//
					// Commit the transaction.
					finally
						{
						try {
							if (null != permission)
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
							if (DEBUG_FLAG) System.out.println("Generic exception in addPermission() finally clause") ;

							//
							// Set the response to null.
							permission = null ;

							if (DEBUG_FLAG) System.out.println("  ----") ;
							if (DEBUG_FLAG) System.out.println("") ;
							}
						}
					}
				//
				// If the group ident is not valid.
				else {
					//
					// Set the response to null.
					permission = null ;
					}
				}
			//
			// If the resource is not local.
			else {
				//
				// Set the response to null.
				permission = null ;
				}
			}
			//
			// If the resource is not valid.
		else {
			//
			// Set the response to null.
			permission = null ;
			}

		// TODO
		// Need to return something to the client.
		// Possible a new DataObject ... ?
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return permission ;
		}

	/**
	 * Request a PolicyPermission.
	 *
	 */
	public PolicyPermission getPermission(String resourceName, String groupName, String action)
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("PermissionManagerImpl.getPermission()") ;
		if (DEBUG_FLAG) System.out.println("  resource : " + resourceName) ;
		if (DEBUG_FLAG) System.out.println("  group    : " + groupName) ;
		if (DEBUG_FLAG) System.out.println("  action   : " + action) ;

		PolicyPermission result = null ;
		//
		// Create a ResourceIdent for our Resource.
		ResourceIdent resourceIdent = new ResourceIdent(resourceName) ;
		if (DEBUG_FLAG) System.out.println("  resource : " + resourceIdent) ;
		//
		// Create a CommunityIdent for our Group.
		CommunityIdent groupIdent = new CommunityIdent(groupName) ;
		if (DEBUG_FLAG) System.out.println("  group    : " + groupIdent) ;

		//
		// If the resource ident is valid.
		if (resourceIdent.isValid())
			{
			//
			// If the resource ident is local.
			if (resourceIdent.isLocal())
				{
				//
				// If the group ident is valid.
				if (groupIdent.isValid())
					{
					//
					// Create the database key.
					Complex key = new Complex(
						new Object[]
							{
							resourceIdent.toString(),
							groupIdent.toString(),
							action
							}
						) ;
					//
					// Try loading the PolicyPermission from the database.
					try {
						//
						// Begin a new database transaction.
						database.begin();
						//
						// Load the PolicyPermission from the database.
						result = (PolicyPermission) database.load(PolicyPermission.class, key) ;
						}
					//
					// If we couldn't find the object.
					catch (ObjectNotFoundException ouch)
						{
						if (DEBUG_FLAG) System.out.println("") ;
						if (DEBUG_FLAG) System.out.println("  ----") ;
						if (DEBUG_FLAG) System.out.println("ObjectNotFoundException in getPermission()") ;

						//
						// Set the response to null.
						result = null ;

						if (DEBUG_FLAG) System.out.println("  ----") ;
						if (DEBUG_FLAG) System.out.println("") ;
						}
					//
					// If anything else went bang.
					catch (Exception ouch)
						{
						if (DEBUG_FLAG) System.out.println("") ;
						if (DEBUG_FLAG) System.out.println("  ----") ;
						if (DEBUG_FLAG) System.out.println("Generic exception in getPermission()") ;

						//
						// Set the response to null.
						result = null ;

						if (DEBUG_FLAG) System.out.println("  ----") ;
						if (DEBUG_FLAG) System.out.println("") ;
						}
					//
					// Commit the transaction.
					finally
						{
						try {
							if (null != result)
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
							if (DEBUG_FLAG) System.out.println("Generic exception in getPermission() finally clause") ;

							//
							// Set the response to null.
							result = null ;

							if (DEBUG_FLAG) System.out.println("  ----") ;
							if (DEBUG_FLAG) System.out.println("") ;
							}
						}
					}
				//
				// If the group ident is not valid.
				else {
					//
					// Set the response to null.
					result = null ;
					}
				}
			//
			// If the resource is not local.
			else {
				//
				// Set the response to null.
				result = null ;
				}
			}
			//
			// If the resource is not valid.
		else {
			//
			// Set the response to null.
			result = null ;
			}

		// TODO
		// Need to return something to the client.
		// Possible a new DataObject ... ?
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return result ;
		}

	/**
	 * Update a PolicyPermission.
	 *
	 */
	public PolicyPermission setPermission(PolicyPermission permission)
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("PermissionManagerImpl.setPermission()") ;
		if (DEBUG_FLAG) System.out.println("  resource : " + permission.getResource()) ;
		if (DEBUG_FLAG) System.out.println("  group    : " + permission.getGroup()) ;
		if (DEBUG_FLAG) System.out.println("  action   : " + permission.getAction()) ;

		PolicyPermission result = null ;
		//
		// Create a ResourceIdent for our Resource.
		ResourceIdent resourceIdent = new ResourceIdent(permission.getResource()) ;
		if (DEBUG_FLAG) System.out.println("  resource : " + resourceIdent) ;
		//
		// Create a CommunityIdent for our Group.
		CommunityIdent groupIdent = new CommunityIdent(permission.getGroup()) ;
		if (DEBUG_FLAG) System.out.println("  group    : " + groupIdent) ;

		//
		// If the resource ident is valid.
		if (resourceIdent.isValid())
			{
			//
			// If the resource ident is local.
			if (resourceIdent.isLocal())
				{
				//
				// If the group ident is valid.
				if (groupIdent.isValid())
					{
					//
					// Create the database key.
					Complex key = new Complex(
						new Object[]
							{
							resourceIdent.toString(),
							groupIdent.toString(),
							permission.getAction()
							}
						) ;
					//
					// Try update the database.
					try {
						//
						// Begin a new database transaction.
						database.begin();
						//
						// Load the PolicyPermission from the database.
						result = (PolicyPermission) database.load(PolicyPermission.class, key) ;
						//
						// Update the PolicyPermission data.
						result.setStatus(permission.getStatus()) ;
						result.setReason(permission.getReason()) ;
						}
					//
					// If we couldn't find the object.
					catch (ObjectNotFoundException ouch)
						{
						if (DEBUG_FLAG) System.out.println("") ;
						if (DEBUG_FLAG) System.out.println("  ----") ;
						if (DEBUG_FLAG) System.out.println("ObjectNotFoundException in setPermission()") ;

						//
						// Set the response to null.
						result = null ;

						if (DEBUG_FLAG) System.out.println("  ----") ;
						if (DEBUG_FLAG) System.out.println("") ;
						}
					//
					// If anything else went bang.
					catch (Exception ouch)
						{
						if (DEBUG_FLAG) System.out.println("") ;
						if (DEBUG_FLAG) System.out.println("  ----") ;
						if (DEBUG_FLAG) System.out.println("Generic exception in setPermission()") ;

						//
						// Set the response to null.
						result = null ;

						if (DEBUG_FLAG) System.out.println("  ----") ;
						if (DEBUG_FLAG) System.out.println("") ;
						}
					//
					// Commit the transaction.
					finally
						{
						try {
							if (null != result)
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
							if (DEBUG_FLAG) System.out.println("Generic exception in setPermission() finally clause") ;

							//
							// Set the response to null.
							result = null ;

							if (DEBUG_FLAG) System.out.println("  ----") ;
							if (DEBUG_FLAG) System.out.println("") ;
							}
						}
					}
				//
				// If the group ident is not valid.
				else {
					//
					// Set the response to null.
					result = null ;
					}
				}
			//
			// If the resource is not local.
			else {
				//
				// Set the response to null.
				result = null ;
				}
			}
			//
			// If the resource is not valid.
		else {
			//
			// Set the response to null.
			result = null ;
			}

		// TODO
		// Need to return something to the client.
		// Possible a new DataObject ... ?
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return result ;
		}

	/**
	 * Delete a PolicyPermission.
	 *
	 */
	public boolean delPermission(String resourceName, String groupName, String action)
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("PermissionManagerImpl.delPermission()") ;
		if (DEBUG_FLAG) System.out.println("  resource : " + resourceName) ;
		if (DEBUG_FLAG) System.out.println("  group    : " + groupName) ;
		if (DEBUG_FLAG) System.out.println("  action   : " + action) ;

		PolicyPermission result = null ;
		//
		// Create a ResourceIdent for our Resource.
		ResourceIdent resourceIdent = new ResourceIdent(resourceName) ;
		if (DEBUG_FLAG) System.out.println("  resource : " + resourceIdent) ;
		//
		// Create a CommunityIdent for our Group.
		CommunityIdent groupIdent = new CommunityIdent(groupName) ;
		if (DEBUG_FLAG) System.out.println("  group    : " + groupIdent) ;

		//
		// If the resource ident is valid.
		if (resourceIdent.isValid())
			{
			//
			// If the resource ident is local.
			if (resourceIdent.isLocal())
				{
				//
				// Create the database key.
				Complex key = new Complex(
					new Object[]
						{
						resourceIdent.toString(),
						groupIdent.toString(),
						action
						}
					) ;
				//
				// Try update the database.
				try {
					//
					// Begin a new database transaction.
					database.begin();
					//
					// Load the PolicyPermission from the database.
					result = (PolicyPermission) database.load(PolicyPermission.class, key) ;
					//
					// Dete the PolicyPermission data.
					database.remove(result) ;
					}
				//
				// If we couldn't find the object.
				catch (ObjectNotFoundException ouch)
					{
					if (DEBUG_FLAG) System.out.println("") ;
					if (DEBUG_FLAG) System.out.println("  ----") ;
					if (DEBUG_FLAG) System.out.println("ObjectNotFoundException in delPermission()") ;

					//
					// Set the response to null.
					result = null ;

					if (DEBUG_FLAG) System.out.println("  ----") ;
					if (DEBUG_FLAG) System.out.println("") ;
					}
				//
				// If anything else went bang.
				catch (Exception ouch)
					{
					if (DEBUG_FLAG) System.out.println("") ;
					if (DEBUG_FLAG) System.out.println("  ----") ;
					if (DEBUG_FLAG) System.out.println("Generic exception in delPermission()") ;

					//
					// Set the response to null.
					result = null ;

					if (DEBUG_FLAG) System.out.println("  ----") ;
					if (DEBUG_FLAG) System.out.println("") ;
					}
				//
				// Commit the transaction.
				finally
					{
					try {
						if (null != result)
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
						if (DEBUG_FLAG) System.out.println("Generic exception in delPermission() finally clause") ;

						//
						// Set the response to null.
						result = null ;

						if (DEBUG_FLAG) System.out.println("  ----") ;
						if (DEBUG_FLAG) System.out.println("") ;
						}
					}
				}
			//
			// If the resource is not local.
			else {
				//
				// Set the response to null.
				result = null ;
				}
			}
			//
			// If the resource is not valid.
		else {
			//
			// Set the response to null.
			result = null ;
			}

		// TODO
		// Need to return something to the client.
		// Possible a new DataObject ... ?
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return (null != result) ;
		}
	}
