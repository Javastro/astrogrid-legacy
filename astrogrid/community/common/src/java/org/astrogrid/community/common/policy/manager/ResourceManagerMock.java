/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/ResourceManagerMock.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/22 13:03:04 $</cvs:date>
 * <cvs:version>$Revision: 1.7 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: ResourceManagerMock.java,v $
 *   Revision 1.7  2004/11/22 13:03:04  jdt
 *   Merges from Comm_KMB_585
 *
 *   Revision 1.6.22.1  2004/11/12 09:12:09  KevinBenson
 *   Still need to javadoc and check exceptions on a couple of new methods
 *   for ResourceManager and PermissionManager, but for the most part it is ready.
 *   I will also add some stylesheets around the jsp pages later.
 *
 *   Revision 1.6  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.5.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.5  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.4.40.3  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.manager ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.util.Map ;
import java.util.HashMap ;

import org.astrogrid.community.common.policy.data.ResourceData ;
import org.astrogrid.community.common.service.CommunityServiceMock ;

import org.astrogrid.community.common.identifier.ResourceIdentifier ;

import org.astrogrid.community.common.exception.CommunityServiceException  ;
import org.astrogrid.community.common.exception.CommunityResourceException ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

/**
 * Mock implementation of our ResourceManager service.
 *
 */
public class ResourceManagerMock
    extends CommunityServiceMock
    implements ResourceManager
    {
    /**
     * Our debug logger.
     *
     */
	private static Log log = LogFactory.getLog(ResourceManagerMock.class);

    /**
     * Switch for testing service exceptions.
     * Set this to true, and the service calls will throw CommunityServiceExceptions.
     *
     */
    public static boolean SERVICE_EXCEPTIONS = false ;

    /**
     * Public constructor.
     *
     */
    public ResourceManagerMock()
        {
        super() ;
        }

    /**
     * Our hash table of values.
     *
     */
    private static Map map = new HashMap() ;

    /**
     * Reset our map.
     *
     */
    public static void reset()
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("ResourceManagerMock.reset()") ;
        map.clear() ;
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("ResourceManagerMock.addResource()") ;
        //
        // Check for CommunityServiceException tests.
        if (SERVICE_EXCEPTIONS)
            {
            throw new CommunityServiceException("Mock exception test") ;
            }
        //
        // Create a new Resource.
        ResourceData resource = new ResourceData(
            new ResourceIdentifier()
            ) ;
        //
        // Add it to our map.
        map.put(resource.getIdent(), resource) ;
        //
        // Return the new Resource.
        return resource ;
        }

    /**
     * Request a Resource details.
     * @param The resource identifier.
     * @return The requested ResourceData object.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityResourceException If unable to locate the resource.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public ResourceData getResource(String ident)
        throws CommunityIdentifierException, CommunityResourceException, CommunityServiceException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("ResourceManagerMock.getResource()") ;
        log.debug("  Ident : " + ident) ;
        //
        // Check for CommunityServiceException tests.
        if (SERVICE_EXCEPTIONS)
            {
            throw new CommunityServiceException("Mock exception test") ;
            }
        //
        // Check for null ident.
        if (null == ident)
            {
            throw new CommunityIdentifierException(
                "Null identifier"
                ) ;
            }
        //
        // Lookup the Resource in our map.
        ResourceData resource = (ResourceData) map.get(ident) ;
        //
        // If we found a matching resource.
        if (null != resource)
            {
            return resource ;
            }
        //
        // If we didn't find a matching resource.
        else {
            throw new CommunityResourceException(
                "Unable to locate resource",
                ident
                ) ;
            }
        }
    
    /**
     * Request a Resource details.
     * @param The resource identifier.
     * @return The requested ResourceData object.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityResourceException If unable to locate the resource.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public Object[] getResources() {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("ResourceManagerMock.getResources()") ;
        return null;
        
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
    public ResourceData setResource(ResourceData resource)
        throws CommunityIdentifierException, CommunityResourceException, CommunityServiceException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("ResourceManagerMock.setResource()") ;
        log.debug("  Resource : " + resource) ;
        //
        // Check for CommunityServiceException tests.
        if (SERVICE_EXCEPTIONS)
            {
            throw new CommunityServiceException("Mock exception test") ;
            }
        //
        // Check for null resource.
        if (null == resource)
            {
            throw new CommunityResourceException(
                "Null resource"
                ) ;
            }
        //
        // Check for null ident.
        if (null == resource.getIdent())
            {
            throw new CommunityIdentifierException(
                "Null identifier"
                ) ;
            }
        //
        // Check if we already have this resource.
        if (map.containsKey(resource.getIdent()))
            {
            //
            // Replace the existing Resource.
            map.put(resource.getIdent(), resource) ;
            //
            // Return the new resource.
            return resource ;
            }
        //
        // If we don't have a matching resource.
        else {
            throw new CommunityResourceException(
                "Unable to locate resource",
                resource.getIdent()
                ) ;
            }
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("ResourceManagerMock.delResource()") ;
        log.debug("  Ident : " + ident) ;
        //
        // Check for CommunityServiceException tests.
        if (SERVICE_EXCEPTIONS)
            {
            throw new CommunityServiceException("Mock exception test") ;
            }
        //
        // Check for null ident.
        if (null == ident)
            {
            throw new CommunityIdentifierException(
                "Null identifier"
                ) ;
            }
        //
        // Try to find the original resource.
        ResourceData original = (ResourceData) map.get(ident) ;
        //
        // If we found a matching resource.
        if (null != original)
            {
            //
            // Remove the original resource.
            map.remove(ident) ;
            //
            // Return the original resource.
            return original ;
            }
        //
        // If we didn't find a matching resource.
        else {
            throw new CommunityResourceException(
                "Unable to locate resource",
                ident
                ) ;
            }
        }
    }
