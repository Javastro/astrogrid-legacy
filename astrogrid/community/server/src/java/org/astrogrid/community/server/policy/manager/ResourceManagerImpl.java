/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/policy/manager/Attic/ResourceManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/02/12 08:12:13 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: ResourceManagerImpl.java,v $
 *   Revision 1.4  2004/02/12 08:12:13  dave
 *   Merged development branch, dave-dev-200401131047, into HEAD
 *
 *   Revision 1.2.4.3  2004/02/06 13:49:09  dave
 *   Moved CommunityManagerBase into server.common.CommunityServer.
 *   Moved getServiceStatus into server.common.CommunityServer.
 *   Moved JUnit tests to match.
 *
 *   Revision 1.2.4.2  2004/01/27 18:55:08  dave
 *   Removed unused imports listed in PMD report
 *
 *   Revision 1.2.4.1  2004/01/27 07:10:11  dave
 *   Refactored ResourceManagerImpl
 *
 *   Revision 1.2  2004/01/07 10:45:45  dave
 *   Merged development branch, dave-dev-20031224, back into HEAD
 *
 *   Revision 1.1.2.2  2004/01/05 06:47:18  dave
 *   Moved policy data classes into policy.data package
 *
 *   Revision 1.1.2.1  2003/12/24 05:54:48  dave
 *   Initial Maven friendly structure (only part of the service implemented)
 *
 *   Revision 1.4  2003/09/13 02:18:52  dave
 *   Extended the jConfig configuration code.
 *
 *   Revision 1.3  2003/09/12 12:59:17  dave
 *   1) Fixed RemoteException handling in the manager and service implementations.
 *
 *   Revision 1.2  2003/09/10 00:08:45  dave
 *   Added getGroupMembers, ResourceIdent and JUnit tests for ResourceManager
 *
 *   Revision 1.1  2003/09/09 19:13:32  KevinBenson
 *   New resource managerr stuff
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.policy.manager ;

import java.util.Vector ;
import java.util.Collection ;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.ObjectNotFoundException ;
import org.exolab.castor.jdo.DuplicateIdentityException ;

import org.astrogrid.community.common.policy.data.ResourceData ;
import org.astrogrid.community.common.policy.data.ResourceIdent ;

import org.astrogrid.community.common.policy.manager.ResourceManager ;

import org.astrogrid.community.server.common.CommunityServer ;
import org.astrogrid.community.server.database.DatabaseConfiguration ;

public class ResourceManagerImpl
	extends CommunityServer
	implements ResourceManager
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
    public ResourceManagerImpl()
        {
		super() ;
        }

    /**
     * Public constructor, using specific database configuration.
     *
     */
    public ResourceManagerImpl(DatabaseConfiguration config)
        {
		super(config) ;
        }

	/**
	 * Create a new Resource.
	 *
	 */
	public ResourceData addResource(String name)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("ResourceManagerImpl.addResource()") ;
		if (DEBUG_FLAG) System.out.println("  name  : " + name) ;

		Database     database = null ;
		ResourceData resource = null ;
		//
		// Create a ResourceIdent for our Resource.
		ResourceIdent ident = new ResourceIdent(name) ;
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
				// Create our new Resource object.
				resource = new ResourceData() ;
				resource.setIdent(ident.toString()) ;
				//
				//
				// Try performing our transaction.
				try {
					//
					// Open our database connection.
					database = this.getDatabase() ;
					//
					// Begin a new database transaction.
					database.begin();
					//
					// Try creating the resource in the database.
					database.create(resource);
					//
					// Commit the transaction.
					database.commit() ;
					}
				//
				// If we already have an object with that ident.
// TODO
// The only reason to treat this differently is that we might one day report it differently to the client.
				catch (DuplicateIdentityException ouch)
					{
					//
					// Log the exception.
					logException(ouch, "ResourceManagerImpl.addResource()") ;
					//
					// Set the response to null.
					resource = null ;
					//
					// Cancel the database transaction.
					rollbackTransaction(database) ;
					}
				//
				// If anything else went bang.
				catch (Exception ouch)
					{
					//
					// Log the exception.
					logException(ouch, "ResourceManagerImpl.addResource()") ;
					//
					// Set the response to null.
					resource = null ;
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
				}
			//
			// If the ident is not local.
			else {
				resource = null ;
				}
			}
			//
			// If the ident is not valid.
		else {
			//
			resource = null ;
			}

		// TODO
		// Need to return something to the client.
		// Possible a new DataObject ... ResourceResult ?
		//

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return resource ;
		}

	/**
	 * Request an Resource details.
	 *
	 */
	public ResourceData getResource(String name)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("ResourceManagerImpl.getResource()") ;
		if (DEBUG_FLAG) System.out.println("  name  : " + name) ;

        Database     database = null ;
		ResourceData resource = null ;
		//
		// Create a ResourceIdent for our Resource.
		ResourceIdent ident = new ResourceIdent(name) ;
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
					// Open our database connection.
					database = this.getDatabase() ;
					//
					// Begin a new database transaction.
					database.begin();
					//
					// Load the Resource from the database.
					resource = (ResourceData) database.load(ResourceData.class, ident.toString()) ;
					//
					// Commit the transaction.
					database.commit() ;
					}
				//
				// If we couldn't find the object.
// TODO
// The only reason to treat this differently is that we might one day report it differently to the client.
				catch (ObjectNotFoundException ouch)
					{
					//
					// Log the exception.
					logException(ouch, "ResourceManagerImpl.getResource()") ;
					//
					// Set the response to null.
					resource = null ;
					//
					// Cancel the database transaction.
					rollbackTransaction(database) ;
					}
				//
				// If anything else went bang.
				catch (Exception ouch)
					{
					//
					// Log the exception.
					logException(ouch, "ResourceManagerImpl.getResource()") ;
					//
					// Set the response to null.
					resource = null ;
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
				}
			//
			// If the ident is not local.
			else {
				//
				// Set the response to null.
				resource = null ;
				}
			}
			//
			// If the ident is not valid.
		else {
			//
			// Set the response to null.
			resource = null ;
			}

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return resource ;
		}

	/**
	 * Update an Resource details.
	 *
	 */
	public ResourceData setResource(ResourceData resource)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("ResourceManagerImpl.setResource()") ;
		if (DEBUG_FLAG) System.out.println("  Resource") ;
		if (DEBUG_FLAG) System.out.println("	 ident : " + resource.getIdent()) ;
		if (DEBUG_FLAG) System.out.println("	 desc  : " + resource.getDescription()) ;

        Database database = null ;
		//
		// Get the resource ident.
		ResourceIdent ident = new ResourceIdent(resource.getIdent()) ;
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
					// Open our database connection.
					database = this.getDatabase() ;
					//
					// Begin a new database transaction.
					database.begin();
					//
					// Load the Resource from the database.
					ResourceData data = (ResourceData) database.load(ResourceData.class, resource.getIdent()) ;
					//
					// Update the resource data.
					data.setDescription(resource.getDescription()) ;
					//
					// Commit the transaction.
					database.commit() ;
					}
				//
				// If we couldn't find the object.
// TODO
// The only reason to treat this differently is that we might one day report it differently to the client.
				catch (ObjectNotFoundException ouch)
					{
					//
					// Log the exception.
					logException(ouch, "ResourceManagerImpl.setResource()") ;
					//
					// Set the response to null.
					resource = null ;
					//
					// Cancel the database transaction.
					rollbackTransaction(database) ;
					}
				//
				// If anything else went bang.
				catch (Exception ouch)
					{
					//
					// Log the exception.
					logException(ouch, "ResourceManagerImpl.setResource()") ;
					//
					// Set the response to null.
					resource = null ;
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
				}
			//
			// If the ident is not local.
			else {
				//
				// Set the response to null.
				resource = null ;
				}
			}
			//
			// If the ident is not valid.
		else {
			//
			// Set the response to null.
			resource = null ;
			}

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return resource ;
		}

	/**
	 * Request a list of Resources.
	 *
	 */
	public Object[] getResourceList()
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("ResourceManagerImpl.getResourceList()") ;

		//
		// Try to query the database.
        Database database = null ;
		Object[] array    = null ;
		try {
			//
			// Open our database connection.
			database = this.getDatabase() ;
			//
			// Begin a new database transaction.
			database.begin();
			//
			// Create our OQL query.
			OQLQuery query = database.getOQLQuery(
				"SELECT resources FROM org.astrogrid.community.policy.data.ResourceData resources"
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
			// Commit the transaction.
			database.commit() ;
			}
		//
		// If anything went bang.
		catch (Exception ouch)
			{
			//
			// Log the exception.
			logException(ouch, "ResourceManagerImpl.getResourceList()") ;
			//
			// Set the response to null.
			array = null ;
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
		return array ;
		}

	/**
	 * Delete an Resource.
	 *
	 */
	public boolean delResource(String name)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("ResourceManagerImpl.delResource()") ;
		if (DEBUG_FLAG) System.out.println("  name  : " + name) ;

        Database     database = null ;
		ResourceData resource = null ;
		//
		// Create a ResourceIdent for our Resource.
		ResourceIdent ident = new ResourceIdent(name) ;
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
					// Open our database connection.
					database = this.getDatabase() ;
					//
					// Begin a new database transaction.
					database.begin();
					//
					// Load the Resource from the database.
					resource = (ResourceData) database.load(ResourceData.class, ident.toString()) ;
					//
					// Delete the Resource.
					database.remove(resource) ;
					//
					// Commit the transaction.
					database.commit() ;
					}
				//
				// If we couldn't find the object.
// TODO
// The only reason to treat this differently is that we might one day report it differently to the client.
				catch (ObjectNotFoundException ouch)
					{
					//
					// Log the exception.
					logException(ouch, "ResourceManagerImpl.delResource()") ;
					//
					// Set the response to null.
					resource = null ;
					//
					// Cancel the database transaction.
					rollbackTransaction(database) ;
					}
				//
				// If anything else went bang.
				catch (Exception ouch)
					{
					//
					// Log the exception.
					logException(ouch, "ResourceManagerImpl.delResource()") ;
					//
					// Set the response to null.
					resource = null ;
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
				}
			//
			// If the ident is not local.
			else {
				//
				// Set the response to null.
				resource = null ;
				}
			}
			//
			// If the ident is not valid.
		else {
			//
			// Set the response to null.
			resource = null ;
			}

		if (DEBUG_FLAG) System.out.println("----\"----") ;
//
// TODO broken API, we should return the deleted resource.
		return (null != resource) ;
		}

	}
