/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/ResourceManager.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/22 13:03:04 $</cvs:date>
 * <cvs:version>$Revision: 1.8 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: ResourceManager.java,v $
 *   Revision 1.8  2004/11/22 13:03:04  jdt
 *   Merges from Comm_KMB_585
 *
 *   Revision 1.7.104.1  2004/11/12 09:12:09  KevinBenson
 *   Still need to javadoc and check exceptions on a couple of new methods
 *   for ResourceManager and PermissionManager, but for the most part it is ready.
 *   I will also add some stylesheets around the jsp pages later.
 *
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
    * Request the details for a Resource.
    * @param The resource identifier.
    * @return The requested ResourceData object.
    * @throws CommunityResourceException If unable to locate the resource.
    * @throws CommunityServiceException If there is an internal error in the service.
    * @throws CommunityIdentifierException If the resource identifier is not valid.
    * @throws RemoteException If the WebService call fails.
    *
    */
  public Object[] getResources() throws RemoteException ;
   

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
