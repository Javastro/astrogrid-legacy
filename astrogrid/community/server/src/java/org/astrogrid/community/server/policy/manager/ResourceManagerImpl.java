/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/policy/manager/Attic/ResourceManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.7 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: ResourceManagerImpl.java,v $
 *   Revision 1.7  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.6.54.3  2004/06/17 15:24:31  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.6.54.2  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.policy.manager ;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.ObjectNotFoundException ;

import org.astrogrid.community.common.identifier.ResourceIdentifier ;
import org.astrogrid.community.common.policy.data.ResourceData ;

import org.astrogrid.community.common.policy.manager.ResourceManager ;

import org.astrogrid.community.server.service.CommunityServiceImpl ;
import org.astrogrid.community.server.database.configuration.DatabaseConfiguration ;

import org.astrogrid.community.common.exception.CommunityServiceException  ;
import org.astrogrid.community.common.exception.CommunityResourceException ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

public class ResourceManagerImpl
    extends CommunityServiceImpl
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
     * Public constructor, using a parent service.
     *
     */
    public ResourceManagerImpl(CommunityServiceImpl parent)
        {
        super(parent) ;
        }

    /**
     * Register a new Resource.
     * @return A new ResourceData object to represent the resource.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public ResourceData addResource()
        throws CommunityServiceException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("ResourceManagerImpl.addResource()") ;
        Database     database = null ;
        ResourceData resource = null ;
        //
        // Create a new ResourceIdentifier for our Resource.
        ResourceIdentifier ident = new ResourceIdentifier() ;
        if (DEBUG_FLAG) System.out.println("  ident : " + ident) ;
        //
        // Create our new Resource object.
        resource = new ResourceData(ident) ;
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
        // If anything went bang.
        catch (Exception ouch)
            {
            //
            // Log the exception.
            logException(ouch, "ResourceManagerImpl.addResource()") ;
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            //
            // Throw a service exception.
            throw new CommunityServiceException(
                "Unable to create new resource",
                ouch
                ) ;
            }
        //
        // Always close our database connection.
        finally
            {
            closeConnection(database) ;
            }
        return resource ;
        }

    /**
     * Request a Resource details.
     * @param The resource identifier.
     * @return The requested ResourceData object.
     * @throws CommunityResourceException If unable to locate the resource.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws CommunityIdentifierException If the identifier is not valid.
     *
     */
    public ResourceData getResource(String ident)
        throws CommunityIdentifierException, CommunityResourceException, CommunityServiceException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("ResourceManagerImpl.getResource()") ;
        if (DEBUG_FLAG) System.out.println("  ident  : " + ident) ;
        //
        // Check for null ident.
        if (null == ident)
            {
            throw new CommunityIdentifierException(
                "Null identifier"
                ) ;
            }
        Database     database = null ;
        ResourceData resource = null ;
        try {
            //
            // Open our database connection.
            database = this.getDatabase() ;
            //
            // Begin a new database transaction.
            database.begin();
            //
            // Load the Resource from the database.
            resource = (ResourceData) database.load(ResourceData.class, ident) ;
            //
            // Commit the transaction.
            database.commit() ;
            }
        //
        // If we couldn't find the object.
        catch (ObjectNotFoundException ouch)
            {
            //
            // Log the exception.
            logException(ouch, "ResourceManagerImpl.getResource()") ;
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            //
            // Throw a resource exception.
            throw new CommunityResourceException(
                "Unable to locate resource",
                ident
                ) ;
            }
        //
        // If anything else went bang.
        catch (Exception ouch)
            {
            //
            // Log the exception.
            logException(ouch, "ResourceManagerImpl.getResource()") ;
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            //
            // Throw a service exception.
            throw new CommunityServiceException(
                "Unable to locate resource",
                ouch
                ) ;
            }
        //
        // Always close our database connection.
        finally
            {
            closeConnection(database) ;
            }
        return resource ;
        }

    /**
     * Update a Resource details.
     * @param The ResourceData to update.
     * @return The updated ResourceData.
     * @throws CommunityResourceException If unable to locate the resource.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws CommunityIdentifierException If the resource identifier is not valid.
     *
     */
    public ResourceData setResource(ResourceData update)
        throws CommunityIdentifierException, CommunityResourceException, CommunityServiceException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("ResourceManagerImpl.setResource()") ;
        //
        // Check for null update.
        if (null == update)
            {
            throw new CommunityResourceException(
                "Null resource"
                ) ;
            }
        if (DEBUG_FLAG) System.out.println("    ident : " + update.getIdent()) ;
        //
        // Check for null ident.
        if (null == update.getIdent())
            {
            throw new CommunityIdentifierException(
                "Null identifier"
                ) ;
            }
        Database     database = null ;
        ResourceData resource = null ;
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
            resource = (ResourceData) database.load(ResourceData.class, update.getIdent()) ;
            //
            // Update the resource data.
            resource.setDescription(update.getDescription()) ;
            //
            // Commit the transaction.
            database.commit() ;
            }
        //
        // If we couldn't find the object.
        catch (ObjectNotFoundException ouch)
            {
            //
            // Log the exception.
            logException(ouch, "ResourceManagerImpl.setResource()") ;
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            //
            // Throw a resource exception.
            throw new CommunityResourceException(
                "Unable to locate resource",
                update.getIdent()
                ) ;
            }
        //
        // If anything else went bang.
        catch (Exception ouch)
            {
            //
            // Log the exception.
            logException(ouch, "ResourceManagerImpl.setResource()") ;
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            //
            // Throw a service exception.
            throw new CommunityServiceException(
                "Unable to update resource",
                ouch
                ) ;
            }
        //
        // Always close our database connection.
        finally
            {
            closeConnection(database) ;
            }
        return resource ;
        }

    /**
     * Delete a Resource object.
     * @param The resource identifier.
     * @return The original ResourceData.
     * @throws CommunityResourceException If unable to locate the resource.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws CommunityIdentifierException If the resource identifier is not valid.
     *
     */
    public ResourceData delResource(String ident)
        throws CommunityIdentifierException, CommunityResourceException, CommunityServiceException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("ResourceManagerImpl.delResource()") ;
        if (DEBUG_FLAG) System.out.println("    ident  : " + ident) ;
        //
        // Check for null ident.
        if (null == ident)
            {
            throw new CommunityIdentifierException(
                "Null identifier"
                ) ;
            }
        Database     database = null ;
        ResourceData resource = null ;
        try {
            //
            // Open our database connection.
            database = this.getDatabase() ;
            //
            // Begin a new database transaction.
            database.begin();
            //
            // Load the Resource from the database.
            resource = (ResourceData) database.load(ResourceData.class, ident) ;
            //
            // Delete the Resource.
            database.remove(resource) ;
            //
            // Commit the transaction.
            database.commit() ;
            }
        //
        // If we couldn't find the object.
        catch (ObjectNotFoundException ouch)
            {
            //
            // Log the exception.
            logException(ouch, "ResourceManagerImpl.delResource()") ;
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            //
            // Throw a resource exception.
            throw new CommunityResourceException(
                "Unable to locate resource",
                ident
                ) ;
            }
        //
        // If anything else went bang.
        catch (Exception ouch)
            {
            //
            // Log the exception.
            logException(ouch, "ResourceManagerImpl.delResource()") ;
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            //
            // Throw a service exception.
            throw new CommunityServiceException(
                "Unable to update resource",
                ouch
                ) ;
            }
        //
        // Always close our database connection.
        finally
            {
            closeConnection(database) ;
            }
        return resource ;
        }
    }
