/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/policy/manager/PolicyManagerDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/19 14:43:14 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyManagerDelegate.java,v $
 *   Revision 1.5  2004/03/19 14:43:14  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.4.2.2  2004/03/19 00:18:09  dave
 *   Refactored delegate Exception handling
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.policy.manager ;

import org.astrogrid.community.client.service.CommunityServiceDelegate ;

import org.astrogrid.community.common.policy.data.GroupMemberData ;
import org.astrogrid.community.common.policy.manager.PolicyManager ;

import org.astrogrid.community.common.exception.CommunityPolicyException     ;
import org.astrogrid.community.common.exception.CommunityServiceException    ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

/**
 * Interface for our PolicyManager delegate.
 * This mirrors the PolicyManager interface without the RemoteExceptions.
 * @see PolicyManager
 *
 */
public interface PolicyManagerDelegate
    extends CommunityServiceDelegate, AccountManagerDelegate, GroupManagerDelegate, CommunityManagerDelegate, ResourceManagerDelegate, PermissionManagerDelegate
    {

    /**
     * Add an Account to a Group.
     * The group must be local, but Account can be local or remote.
     * @param  account The Account identifier.
     * @param  group   The Group identifier.
     * @return An GroupMemberData to confirm the membership.
     * @throws CommunityIdentifierException If one of the identifiers is not valid.
     * @throws CommunityPolicyException If one the identifiers is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public GroupMemberData addGroupMember(String account, String group)
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException ;

    /**
     * Remove an Account from a Group, given the Account and Group names.
     *
     */
    public GroupMemberData delGroupMember(String account, String group) ;

    /**
     * Get a list of Group members, given the Group name.
     *
     */
    public Object[] getGroupMembers(String group) ;

    /**
     * Get a list of local Groups that an Account belongs to, given the Account name.
     *
     */
    public Object[] getLocalAccountGroups(String account) ;

    }
