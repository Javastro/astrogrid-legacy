/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/policy/manager/ResourceManagerDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:19 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: ResourceManagerDelegate.java,v $
 *   Revision 1.6  2004/06/18 13:45:19  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.5.38.3  2004/06/17 15:10:03  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.5.38.2  2004/06/17 13:38:58  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.policy.manager ;

import org.astrogrid.community.client.service.CommunityServiceDelegate ;

import org.astrogrid.community.common.policy.data.ResourceData ;

import org.astrogrid.community.common.exception.CommunityServiceException  ;
import org.astrogrid.community.common.exception.CommunityResourceException ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

/**
 * Interface for our ResourceManager delegate.
 * This mirrors the ResourceManager interface, without the RemoteExceptions.
 *
 */
public interface ResourceManagerDelegate
    extends CommunityServiceDelegate
    {
    /**
     * Register a new Resource.
     * @return A new ResourceData object to represent the resource.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public ResourceData addResource()
        throws CommunityServiceException ;

    /**
     * Request the details for a Resource.
     * @param The resource identifier.
     * @return The requested ResourceData object.
     * @throws CommunityResourceException If unable to locate the resource.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws CommunityIdentifierException If the resource identifier is not valid.
     *
     */
   public ResourceData getResource(String ident)
        throws CommunityIdentifierException, CommunityResourceException, CommunityServiceException ;

    /**
     * Update the details for a Resource.
     * @param The ResourceData to update.
     * @return The updated ResourceData.
     * @throws CommunityResourceException If unable to locate the resource.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws CommunityIdentifierException If the resource identifier is not valid.
     *
     */
    public ResourceData setResource(ResourceData resource)
        throws CommunityIdentifierException, CommunityResourceException, CommunityServiceException ;

    /**
     * Delete a Resource.
     * @param The resource identifier.
     * @return The original ResourceData.
     * @throws CommunityResourceException If unable to locate the resource.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws CommunityIdentifierException If the identifier is not valid.
     *
     */
    public ResourceData delResource(String ident)
        throws CommunityIdentifierException, CommunityResourceException, CommunityServiceException ;

    }
