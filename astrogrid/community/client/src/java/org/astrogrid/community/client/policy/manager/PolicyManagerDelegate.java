/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/policy/manager/PolicyManagerDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/15 07:49:30 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyManagerDelegate.java,v $
 *   Revision 1.4  2004/03/15 07:49:30  dave
 *   Merged development branch, dave-dev-200403121536, into HEAD
 *
 *   Revision 1.3.12.1  2004/03/13 17:57:20  dave
 *   Remove RemoteException(s) from delegate interfaces.
 *   Protected internal API methods.
 *
 *   Revision 1.3  2004/03/08 13:42:33  dave
 *   Updated Maven goals.
 *   Replaced tabs with Spaces.
 *
 *   Revision 1.2.2.1  2004/03/08 12:53:17  dave
 *   Changed tabs to spaces
 *
 *   Revision 1.2  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/04 13:26:17  dave
 *   1) Added Delegate interfaces.
 *   2) Added Mock implementations.
 *   3) Added MockDelegates
 *   4) Added SoapDelegates
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.policy.manager ;

import org.astrogrid.community.common.policy.data.GroupMemberData ;
import org.astrogrid.community.common.policy.manager.PolicyManager ;
import org.astrogrid.community.common.service.data.ServiceStatusData ;

/**
 * Interface for our PolicyManager delegate.
 * This extends the PolicyManager interface, without the RemoteExceptions.
 *
 */
public interface PolicyManagerDelegate
    extends AccountManagerDelegate, GroupManagerDelegate, CommunityManagerDelegate, ResourceManagerDelegate, PermissionManagerDelegate
    {

    /**
     * Request a list of local Accounts.
     *
     */
    public Object[] getLocalAccounts() ;

    /**
     * Request a list of Accounts, given a remote Community name.
     *
     */
    public Object[] getRemoteAccounts(String community) ;

    /**
     * Request a list of local Groups.
     *
     */
    public Object[] getLocalGroups() ;

    /**
     * Request a list of Groups, given a remote Community name.
     *
     */
    public Object[] getRemoteGroups(String community) ;

    /**
     * Add an Account to a Group, given the Account and Group names.
     *
     */
    public GroupMemberData addGroupMember(String account, String group) ;

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

    /**
     * Get a list of remote Groups that an Account belongs to, given the Account and Community names.
     *
     */
    public Object[] getRemoteAccountGroups(String account, String community) ;

    /**
     * Service health check.
     * @return ServiceStatusData with details of the Service status.
     *
     */
    public ServiceStatusData getServiceStatus() ;

    }
