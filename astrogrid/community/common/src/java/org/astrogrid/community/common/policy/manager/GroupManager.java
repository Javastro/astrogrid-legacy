/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/GroupManager.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/23 16:34:08 $</cvs:date>
 * <cvs:version>$Revision: 1.7 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: GroupManager.java,v $
 *   Revision 1.7  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.6.18.2  2004/03/22 02:25:35  dave
 *   Updated delegate interfaces to include Exception handling.
 *
 *   Revision 1.6.18.1  2004/03/21 06:41:41  dave
 *   Refactored to include Exception handling.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.manager ;

import java.rmi.Remote ;
import java.rmi.RemoteException ;

import org.astrogrid.community.common.policy.data.GroupData ;
import org.astrogrid.community.common.service.CommunityService ;

import org.astrogrid.community.common.exception.CommunityPolicyException     ;
import org.astrogrid.community.common.exception.CommunityServiceException    ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

/**
 * Public interface to our GroupManager service.
 *
 */
public interface GroupManager
    extends Remote, CommunityService
    {

    /**
     * Add a new Group, given the Group ident.
     * @param  ident The Group identifier.
     * @return A GroupData for the Group.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is already in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the WebService call fails.
     *
     */
    public GroupData addGroup(String ident)
        throws RemoteException, CommunityServiceException, CommunityIdentifierException, CommunityPolicyException ;

    /**
     * Add a new Group, given the Group data.
     * @param  group The GroupData to add.
     * @return A new GroupData for the Group.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is already in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the WebService call fails.
     *
     */
    public GroupData addGroup(GroupData account)
        throws RemoteException, CommunityServiceException, CommunityIdentifierException, CommunityPolicyException ;

    /**
     * Request a Group details, given the Group ident.
     * @param  ident The Group identifier.
     * @return A GroupData for the Group.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the WebService call fails.
     *
     */
    public GroupData getGroup(String ident)
        throws RemoteException, CommunityServiceException, CommunityIdentifierException, CommunityPolicyException ;

    /**
     * Update a Group.
     * @param  group The new GroupData to update.
     * @return A new GroupData for the Group.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the WebService call fails.
     *
     */
    public GroupData setGroup(GroupData account)
        throws RemoteException, CommunityServiceException, CommunityIdentifierException, CommunityPolicyException ;

    /**
     * Delete a Group.
     * @param  ident The Group identifier.
     * @return The GroupData for the old Group.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the WebService call fails.
     *
     */
    public GroupData delGroup(String ident)
        throws RemoteException, CommunityServiceException, CommunityIdentifierException, CommunityPolicyException ;

    /**
     * Request a list of local Groups.
     * @return An array of GroupData objects.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the WebService call fails.
     *
     */
    public Object[] getLocalGroups()
        throws RemoteException, CommunityServiceException ;

    /**
     * Request a list of local Groups that an Account belongs to, given the Account ident.
     * @param  account The Account ifentifier.
     * @return An array of GroupData objects.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the WebService call fails.
     *
     */
    public Object[] getLocalAccountGroups(String account)
        throws RemoteException, CommunityServiceException, CommunityIdentifierException ;

    }
