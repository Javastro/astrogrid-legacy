/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/PolicyManager.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.8 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyManager.java,v $
 *   Revision 1.8  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.7.36.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.manager ;

import java.rmi.Remote ;
import java.rmi.RemoteException ;

import org.astrogrid.community.common.service.CommunityService ;

import org.astrogrid.community.common.policy.data.GroupMemberData ;

import org.astrogrid.community.common.exception.CommunityPolicyException     ;
import org.astrogrid.community.common.exception.CommunityServiceException    ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

/**
 * Interface for our PolicyManager service.
 *
 */
public interface PolicyManager
    extends Remote, CommunityService, AccountManager, GroupManager, ResourceManager, PermissionManager
    {
    /**
     * Add an Account to a Group.
     * The group must be local, but Account can be local or remote.
     * @param  account The Account identifier.
     * @param  group   The Group identifier.
     * @return A GroupMemberData to confirm the membership.
     * @throws CommunityIdentifierException If one of the identifiers is not valid.
     * @throws CommunityPolicyException If one the identifiers is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the Group is in a remote Community and the WebService call fails.
     *
     */
    public GroupMemberData addGroupMember(String account, String group)
        throws RemoteException, CommunityServiceException, CommunityIdentifierException, CommunityPolicyException ;

    /**
     * Remove an Account from a Group.
     * The group must be local, but Account can be local or remote.
     * @param  account The Account identifier.
     * @param  group   The Group identifier.
     * @return A GroupMemberData to confirm the membership.
     * @throws CommunityIdentifierException If one of the identifiers is not valid.
     * @throws CommunityPolicyException If one the identifiers is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the Group is in a remote Community and the WebService call fails.
     *
     */
    public GroupMemberData delGroupMember(String account, String group)
        throws RemoteException, CommunityServiceException, CommunityPolicyException, CommunityIdentifierException ;

    /**
     * Request a list of Group members.
     * The group must be local.
     * @param  group   The Group identifier.
     * @return An array of GroupMemberData objects..
     * @throws CommunityIdentifierException If one of the identifiers is not valid.
     * @throws CommunityPolicyException If one the identifiers is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the Group is in a remote Community and the WebService call fails.
     *
     */
    public Object[] getGroupMembers(String group)
        throws RemoteException, CommunityServiceException, CommunityPolicyException, CommunityIdentifierException ;

    /**
     * Request a list of Accounts, given a remote Community name.
     *
    public Object[] getRemoteAccounts(String community)
        throws RemoteException ;
     */

    /**
     * Request a list of Groups, given a remote Community name.
     *
    public Object[] getRemoteGroups(String community)
        throws RemoteException ;
     */

    /**
     * Get a list of remote Groups that an Account belongs to, given the Account and Community names.
     *
    public Object[] getRemoteAccountGroups(String account, String community)
        throws RemoteException ;
     */

    }
