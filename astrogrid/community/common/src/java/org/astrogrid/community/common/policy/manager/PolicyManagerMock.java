/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/PolicyManagerMock.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/22 13:03:04 $</cvs:date>
 * <cvs:version>$Revision: 1.8 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyManagerMock.java,v $
 *   Revision 1.8  2004/11/22 13:03:04  jdt
 *   Merges from Comm_KMB_585
 *
 *   Revision 1.7.22.3  2004/11/12 09:12:09  KevinBenson
 *   Still need to javadoc and check exceptions on a couple of new methods
 *   for ResourceManager and PermissionManager, but for the most part it is ready.
 *   I will also add some stylesheets around the jsp pages later.
 *
 *   Revision 1.7.22.2  2004/11/08 22:08:21  KevinBenson
 *   added groupmember and permissionmanager tests.  Changed the install.xml to use eperate file names
 *   instead of the same filename
 *
 *   Revision 1.7.22.1  2004/11/05 08:55:49  KevinBenson
 *   Moved the GroupMember out of PolicyManager in the commons and client section.
 *   Added more unit tests for GroupMember and PermissionManager for testing.
 *   Still have some errors that needs some fixing.
 *
 *   Revision 1.7  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.6.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.6  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.5.36.2  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.manager ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.rmi.RemoteException ;

import org.astrogrid.community.common.policy.data.AccountData ;
import org.astrogrid.community.common.policy.data.GroupData ;
import org.astrogrid.community.common.policy.data.ResourceData ;
import org.astrogrid.community.common.policy.data.PolicyPermission ;
import org.astrogrid.community.common.policy.data.GroupMemberData ;

import org.astrogrid.community.common.service.CommunityServiceMock ;

import org.astrogrid.community.common.exception.CommunityPolicyException     ;
import org.astrogrid.community.common.exception.CommunityServiceException    ;
import org.astrogrid.community.common.exception.CommunityResourceException   ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

/**
 * Mock implementation of our PolicyManager service.
 *
 */
public class PolicyManagerMock
    extends CommunityServiceMock
    implements PolicyManager
    {
    /**
     * Our debug logger.
     *
     */
	private static Log log = LogFactory.getLog(PolicyManagerMock.class);

    /**
     * Public constructor.
     *
     */
    public PolicyManagerMock()
        {
        super() ;
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("PolicyManagerMock()") ;
        }

    /**
     * Our AccountManager.
     *
     */
    private AccountManagerMock accountManager = new AccountManagerMock() ;

    /**
     * Add a new Account, given the Account ident.
     * @param  ident The Account identifier.
     * @return An AccountData for the Account.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is already in the database.
     *
     */
    public AccountData addAccount(String ident)
        throws CommunityIdentifierException, CommunityPolicyException
        {
        return accountManager.addAccount(ident) ;
        }

    /**
     * Add a new Account, given the Account data.
     * @param  account The AccountData to add.
     * @return A new AccountData for the Account.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is already in the database.
     *
     */
    public AccountData addAccount(AccountData account)
        throws CommunityIdentifierException, CommunityPolicyException
        {
        return accountManager.addAccount(account) ;
        }

    /**
     * Request an Account details, given the Account ident.
     * @param  ident The Account identifier.
     * @return An AccountData for the Account.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     *
     */
    public AccountData getAccount(String ident)
        throws CommunityIdentifierException, CommunityPolicyException
        {
        return accountManager.getAccount(ident) ;
        }

    /**
     * Update an Account.
     * @param  update The new Account data to update.
     * @return A new AccountData for the Account.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     *
     */
    public AccountData setAccount(AccountData update)
        throws CommunityIdentifierException, CommunityPolicyException
        {
        return accountManager.setAccount(update) ;
        }

    /**
     * Delete an Account.
     * @param  ident The Account identifier.
     * @return The AccountData for the old Account.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     *
     */
    public AccountData delAccount(String ident)
        throws CommunityIdentifierException, CommunityPolicyException
        {
        return accountManager.delAccount(ident) ;
        }

    /**
     * Request a list of local Accounts.
     * @return An array of AccountData objects.
     *
     */
    public Object[] getLocalAccounts()
        {
        return accountManager.getLocalAccounts() ;
        }


    /**
     * Our GroupManager.
     *
     */
    private GroupManagerMock groupManager = new GroupManagerMock() ;

    /**
     * Add a new Group, given the Group ident.
     * @param  ident The Group identifier.
     * @return An GroupData for the Group.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is already in the database.
     *
     */
    public GroupData addGroup(String ident)
        throws CommunityIdentifierException, CommunityPolicyException
        {
        return groupManager.addGroup(ident) ;
        }

    /**
     * Add a new Group, given the Group data.
     * @param  data The GroupData to add.
     * @return A new GroupData for the Group.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is already in the database.
     *
     */
    public GroupData addGroup(GroupData group)
        throws CommunityIdentifierException, CommunityPolicyException
        {
        return groupManager.addGroup(group) ;
        }

    /**
     * Request a Group details, given the Group ident.
     * @param  ident The Group identifier.
     * @return An GroupData for the Group.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     *
     */
    public GroupData getGroup(String ident)
        throws CommunityIdentifierException, CommunityPolicyException
        {
        return groupManager.getGroup(ident) ;
        }

    /**
     * Update a Group.
     * @param  update The new Group data to update.
     * @return A new GroupData for the Group.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     *
     */
    public GroupData setGroup(GroupData update)
        throws CommunityIdentifierException, CommunityPolicyException
        {
        return groupManager.setGroup(update) ;
        }

    /**
     * Delete a Group.
     * @param  ident The Group identifier.
     * @return The GroupData for the old Group.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     *
     */
    public GroupData delGroup(String ident)
        throws CommunityIdentifierException, CommunityPolicyException
        {
        return groupManager.delGroup(ident) ;
        }

    /**
     * Request a list of local Groups.
     * @return An array of GroupData objects.
     *
     */
    public Object[] getLocalGroups()
        {
        return groupManager.getLocalGroups() ;
        }

    /**
     * Get a list of local Groups that an Account belongs to.
     * @return An array of GroupData objects.
     *
     */
    public Object[] getLocalAccountGroups(String account)
        {
        return groupManager.getLocalAccountGroups(account) ;
        }


    /**
     * Our ResourceManager.
     *
     */
    private ResourceManagerMock resourceManager = new ResourceManagerMock() ;

    /**
     * Register a new Resource.
     * @return A new ResourceData object to represent the resource.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public ResourceData addResource()
        throws CommunityServiceException
        {
        return resourceManager.addResource() ;
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
        return resourceManager.getResource(ident) ;
        }
    
    /**
     * Request a Resource details.
     * @return The requested ResourceData object.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityResourceException If unable to locate the resource.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public Object[] getResources()
        {
        return resourceManager.getResources() ;
        }
    

    /**
     * Update a Resource details.
     * @param The ResourceData to update.
     * @return The updated ResourceData.
     * @throws CommunityIdentifierException If the resource identifier is not valid.
     * @throws CommunityResourceException If unable to locate the resource.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public ResourceData setResource(ResourceData resource)
        throws CommunityIdentifierException, CommunityResourceException, CommunityServiceException
        {
        return resourceManager.setResource(resource) ;
        }

    /**
     * Delete a Resource object.
     * @param The resource identifier.
     * @return The original ResourceData.
     * @throws CommunityIdentifierException If the resource identifier is not valid.
     * @throws CommunityResourceException If unable to locate the resource.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public ResourceData delResource(String ident)
        throws CommunityIdentifierException, CommunityResourceException, CommunityServiceException
        {
        return resourceManager.delResource(ident) ;
        }
    
    private GroupMemberManagerMock groupMemberManager = new GroupMemberManagerMock();
    
    public GroupMemberData addGroupMember(String account, String group)
    throws CommunityIdentifierException, CommunityPolicyException, CommunityServiceException
    {
        return groupMemberManager.addGroupMember(account,group);
    }
    
    public GroupMemberData delGroupMember(String account, String group)
    throws CommunityIdentifierException, CommunityPolicyException, CommunityServiceException
    {
      return groupMemberManager.delGroupMember(account,group);
    }
    
    public Object[] getGroupMembers(String group)
    throws CommunityIdentifierException, CommunityPolicyException, CommunityServiceException
    {
      return groupMemberManager.getGroupMembers(group);
    }
    
    public GroupMemberData getGroupMember(String account, String group)
    throws CommunityIdentifierException, CommunityPolicyException, CommunityServiceException
    {
      return groupMemberManager.getGroupMember(account,group);
    }    
    
    public Object[] getGroupMembers()
    throws CommunityIdentifierException, CommunityPolicyException, CommunityServiceException
    {
      return groupMemberManager.getGroupMembers();
    }
    

    /**
     * Our PermissionManager.
     *
     */
    private PermissionManagerMock permissionManager = new PermissionManagerMock() ;

    /**
     * Create a new PolicyPermission.
     *
     */
    public PolicyPermission addPermission(String resource, String group, String action)
        throws RemoteException
        {
        return permissionManager.addPermission(resource, group, action) ;
        }

    /**
     * Request a PolicyPermission.
     *
     */
    public PolicyPermission getPermission(String resource, String group, String action)
        throws RemoteException
        {
        return permissionManager.getPermission(resource, group, action) ;
        }
    
    /**
     * Request a PolicyPermission.
     *
     */
    public Object[] getPermissions()
        throws RemoteException
        {
        return permissionManager.getPermissions();
        }
    

    /**
     * Update a PolicyPermission.
     *
     */
    public PolicyPermission setPermission(PolicyPermission permission)
        {
        return permissionManager.setPermission(permission) ;
        }

    /**
     * Delete a PolicyPermission.
     *
     */
    public boolean delPermission(String resource, String group, String action)
        throws RemoteException
        {
        return permissionManager.delPermission(resource, group, action) ;
        }


    }
