/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/PolicyManager.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/19 14:43:14 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyManager.java,v $
 *   Revision 1.6  2004/03/19 14:43:14  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.5.22.1  2004/03/18 13:41:19  dave
 *   Added Exception handling to AccountManager
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
    extends Remote, CommunityService, AccountManager, GroupManager, CommunityManager, ResourceManager, PermissionManager
    {

    /**
     * Request a list of local Accounts.
     *
    public Object[] getLocalAccounts()
        throws RemoteException ;
     */

    /**
     * Request a list of Accounts, given a remote Community name.
     *
    public Object[] getRemoteAccounts(String community)
        throws RemoteException ;
     */

    /**
     * Request a list of local Groups.
     *
    public Object[] getLocalGroups()
        throws RemoteException ;
     */

    /**
     * Request a list of Groups, given a remote Community name.
     *
    public Object[] getRemoteGroups(String community)
        throws RemoteException ;
     */

    /**
     * Add an Account to a Group.
     * The group must be local, but Account can be local or remote.
     * @param  account The Account identifier.
     * @param  group   The Group identifier.
     * @return An GroupMemberData to confirm the membership.
     * @throws CommunityIdentifierException If one of the identifiers is not valid.
     * @throws CommunityPolicyException If one the identifiers is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the Group is in a remote Community and the WebService call fails.
     *
     */
    public GroupMemberData addGroupMember(String account, String group)
        throws RemoteException, CommunityServiceException, CommunityIdentifierException, CommunityPolicyException ;

    /**
     * Remove an Account from a Group, given the Account and Group names.
     *
     */
    public GroupMemberData delGroupMember(String account, String group)
        throws RemoteException ;

    /**
     * Get a list of Group members, given the Group name.
     *
     */
    public Object[] getGroupMembers(String group)
        throws RemoteException ;

    /**
     * Get a list of local Groups that an Account belongs to, given the Account name.
     *
     */
    public Object[] getLocalAccountGroups(String account)
        throws RemoteException ;

    /**
     * Get a list of remote Groups that an Account belongs to, given the Account and Community names.
     *
    public Object[] getRemoteAccountGroups(String account, String community)
        throws RemoteException ;
     */

    }
