/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/ResourceManager.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.7 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: ResourceManager.java,v $
 *   Revision 1.7  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.6.54.2  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.manager ;

import java.rmi.Remote ;
import java.rmi.RemoteException ;

import org.astrogrid.community.common.policy.data.ResourceData ;
import org.astrogrid.community.common.service.CommunityService ;

import org.astrogrid.community.common.exception.CommunityServiceException  ;
import org.astrogrid.community.common.exception.CommunityResourceException ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

/**
 * The public interface for ResourceManager services.
 *
 */
public interface ResourceManager
    extends Remote, CommunityService
    {
    /**
     * Register a new Resource.
     * @return A new ResourceData object to represent the resource.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the WebService call fails.
     *
     */
    public ResourceData addResource()
        throws RemoteException, CommunityServiceException ;

    /**
     * Request the details for a Resource.
     * @param The resource identifier.
     * @return The requested ResourceData object.
     * @throws CommunityResourceException If unable to locate the resource.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws CommunityIdentifierException If the resource identifier is not valid.
     * @throws RemoteException If the WebService call fails.
     *
     */
   public ResourceData getResource(String ident)
        throws RemoteException, CommunityIdentifierException, CommunityResourceException, CommunityServiceException ;

    /**
     * Update the details for a Resource.
     * @param The ResourceData to update.
     * @return The updated ResourceData.
     * @throws CommunityResourceException If unable to locate the resource.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws CommunityIdentifierException If the resource identifier is not valid.
     * @throws RemoteException If the WebService call fails.
     *
     */
    public ResourceData setResource(ResourceData resource)
        throws RemoteException, CommunityIdentifierException, CommunityResourceException, CommunityServiceException ;

    /**
     * Delete a Resource.
     * @param The resource identifier.
     * @return The original ResourceData.
     * @throws CommunityResourceException If unable to locate the resource.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws RemoteException If the WebService call fails.
     *
     */
    public ResourceData delResource(String ident)
        throws RemoteException, CommunityIdentifierException, CommunityResourceException, CommunityServiceException ;

    }
