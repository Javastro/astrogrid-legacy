/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/policy/manager/PolicyManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: gtr $</cvs:author>
 * <cvs:date>$Date: 2008/01/24 16:56:43 $</cvs:date>
 * <cvs:version>$Revision: 1.15 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyManagerImpl.java,v $
 *   Revision 1.15  2008/01/24 16:56:43  gtr
 *   branch community-gtr-2521 is merged
 *
 *   Revision 1.14.150.1  2008/01/24 16:31:13  gtr
 *   2008.0a5.
 *
 *   Revision 1.14  2004/11/22 13:03:04  jdt
 *   Merges from Comm_KMB_585
 *
 *   Revision 1.13  2004/10/29 15:50:05  jdt
 *   merges from Community_AdminInterface (bug 579)
 *
 *   Revision 1.12.18.1  2004/10/18 22:10:28  KevinBenson
 *   some bug fixes to the PermissionManager.  Also made it throw some exceptions.
 *   Made  it and GroupManagerImnpl use the Resolver objects to actually get a group(PermissionManageriMnpl)
 *   or account (GroupMember) from the other community.  Changed also for it to grab a ResourceData from the
 *   database to verifity it is in our database.  Add a few of these resolver dependencies as well.
 *   And last but not least fixed the GroupMemberData object to get rid of a few set methods so Castor
 *   will now work correctly in Windows
 *
 *   Revision 1.12  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.11.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.11  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.10.32.3  2004/06/17 15:24:31  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.10.32.2  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.policy.manager ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.rmi.RemoteException ;

import org.astrogrid.community.common.policy.data.GroupData ;
import org.astrogrid.community.common.policy.data.AccountData ;
import org.astrogrid.community.common.policy.data.ResourceData;
import org.astrogrid.community.common.policy.data.GroupMemberData ;
import org.astrogrid.community.common.policy.data.PolicyPermission ;

import org.astrogrid.community.common.policy.manager.PolicyManager ;

import org.astrogrid.community.server.service.CommunityServiceImpl ;
import org.astrogrid.community.server.database.configuration.DatabaseConfiguration ;

import org.astrogrid.community.common.exception.CommunityPolicyException     ;
import org.astrogrid.community.common.exception.CommunityServiceException    ;
import org.astrogrid.community.common.exception.CommunityResourceException ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

/**
 * Server side implementation of the PolicyManager service.
 *
 */
public class PolicyManagerImpl
    extends CommunityServiceImpl
    implements PolicyManager
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(PolicyManagerImpl.class);

    /**
     * Public constructor, using default database configuration.
     *
     */
    public PolicyManagerImpl()
        {
        super() ;
        //
        // Initialise our local managers.
        initManagers() ;
        }

    /**
     * Public constructor, using specific database configuration.
     *
     */
    public PolicyManagerImpl(DatabaseConfiguration config)
        {
        super(config) ;
        //
        // Initialise our local managers.
        initManagers() ;
        }

    /**
     * Public constructor, using a parent service.
     *
     */
    public PolicyManagerImpl(CommunityServiceImpl parent)
        {
        super(parent) ;
        //
        // Initialise our local managers.
        initManagers() ;
        }

    /**
     * Initialise our local managers, passing a reference to 'this' as their parent.
     * @todo Refactor this into default initialiser.
     *
     */
    private void initManagers()
        {
        accountManager    = new AccountManagerImpl(this)    ;
        resourceManager   = new ResourceManagerImpl(this)   ;
        permissionManager = new PermissionManagerImpl(this) ;

        /*
         * The GroupManager needs access to the current AccountManagerImpl because Castor maintains an
         * in-memory cache of AccountData objects, with read-write locks.
         */
        groupManager = new GroupManagerImpl(this, accountManager) ;
        permissionManager = new PermissionManagerImpl(this,groupManager, resourceManager);

        }

    /**
     * Our GroupManager.
     *
     */
    private GroupManagerImpl groupManager ;

    /**
     * Our AccountManager.
     *
     */
    private AccountManagerImpl accountManager ;

    /**
     * Our ResourceManager
     *
     */
    private ResourceManagerImpl resourceManager ;

    /**
     * Our PermissionManager
     *
     */
    private PermissionManagerImpl permissionManager ;

    /**
     * Add a new Account, given the Account ident.
     * @param  ident The Account identifier.
     * @return An AccountData for the Account.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is already in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public AccountData addAccount(String ident)
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        return accountManager.addAccount(ident) ;
        }

    /**
     * Add a new Account, given the Account data.
     * @param  account The AccountData to add.
     * @return A new AccountData for the Account.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is already in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public AccountData addAccount(AccountData account)
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        return accountManager.addAccount(account) ;
        }

    /**
     * Request an Account details, given the Account ident.
     * @param  ident The Account identifier.
     * @return An AccountData for the Account.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public AccountData getAccount(String ident)
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        System.out.println("PolicyManager.getAccount() was asked for " + ident);
        return accountManager.getAccount(ident) ;
        }

    /**
     * Update an Account.
     * @param  account The new AccountData to update.
     * @return A new AccountData for the Account.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public AccountData setAccount(AccountData account)
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        return accountManager.setAccount(account) ;
        }

    /**
     * Delete an Account.
     * @param  ident The Account identifier.
     * @return The AccountData for the old Account.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public AccountData delAccount(String ident)
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        return accountManager.delAccount(ident) ;
        }

    /**
     * Request a list of local Accounts.
     * @return An array of AccountData objects.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public Object[] getLocalAccounts()
        throws CommunityServiceException
        {
        return accountManager.getLocalAccounts() ;
        }

    /**
     * Add a new Group.
     * @param  ident The Group identifier.
     * @return A GroupData for the Group.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is already in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public GroupData addGroup(String ident)
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        return groupManager.addGroup(ident) ;
        }

    /**
     * Add a new Group.
     * @param  group The Group data.
     * @return A GroupData for the Group.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is already in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public GroupData addGroup(GroupData group)
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        return groupManager.addGroup(group) ;
        }

    /**
     * Request a Group details.
     * @param  ident The Group identifier.
     * @return A GroupData for the Group.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public GroupData getGroup(String ident)
        throws RemoteException, CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        return groupManager.getGroup(ident) ;
        }

    /**
     * Update a Group.
     * @param  group The new GroupData to update.
     * @return A new GroupData for the Group.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public GroupData setGroup(GroupData group)
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        return groupManager.setGroup(group) ;
        }

    /**
     * Delete a Group.
     * @param  ident The Group identifier.
     * @return The GroupData for the old Group.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public GroupData delGroup(String ident)
        throws RemoteException, CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        return groupManager.delGroup(ident) ;
        }

    /**
     * Request a list of local Groups.
     * @return An array of GroupData objects.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public Object[] getLocalGroups()
        throws CommunityServiceException
        {
        return groupManager.getLocalGroups() ;
        }

    /**
     * Add an Account to a Group.
     * The group must be local, but Account can be local or remote.
     * @param  account The Account identifier.
     * @param  group   The Group identifier.
     * @return A GroupMemberData to confirm the membership.
     * @throws CommunityIdentifierException If one of the identifiers is not valid.
     * @throws CommunityPolicyException If one the identifiers is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public GroupMemberData addGroupMember(String account, String group)
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        return groupManager.addGroupMember(account, group) ;
        }

    /**
     * Remove an Account from a Group.
     * The group must be local, but Account can be local or remote.
     * @param  account The Account identifier.
     * @param  group   The Group identifier.
     * @return A GroupMemberData to confirm the membership.
     * @throws CommunityIdentifierException If one of the identifiers is not valid.
     * @throws CommunityPolicyException If one the identifiers is not in the database.
     * @throws CommunityPolicyException If the Group is private, for a single Account only.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public GroupMemberData delGroupMember(String account, String group)
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        return groupManager.delGroupMember(account, group) ;
        }

    /**
     * Request a list of Group Members.
     * @param group The Group identifier.
     * @return An array of GroupMemberData objects.
     * @throws CommunityIdentifierException If one of the identifiers is not valid.
     * @throws CommunityPolicyException If the group is not local.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public Object[] getGroupMembers(String group)
        throws RemoteException, CommunityServiceException, CommunityPolicyException, CommunityIdentifierException
        {
        return groupManager.getGroupMembers(group) ;
        }
    
    

    /**
     * Request a list of Group Members.
     * @param group The Group identifier.
     * @return An array of GroupMemberData objects.
     * @throws CommunityIdentifierException If one of the identifiers is not valid.
     * @throws CommunityPolicyException If the group is not local.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public GroupMemberData getGroupMember(String account, String group)
        throws RemoteException, CommunityServiceException, CommunityPolicyException, CommunityIdentifierException
        {
        return groupManager.getGroupMember(account, group) ;
        }    
    
    /**
     * Request a list of Group Members.
     * @param group The Group identifier.
     * @return An array of GroupMemberData objects.
     * @throws CommunityIdentifierException If one of the identifiers is not valid.
     * @throws CommunityPolicyException If the group is not local.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public Object[] getGroupMembers()
        throws RemoteException, CommunityServiceException, CommunityPolicyException, CommunityIdentifierException
        {
        return groupManager.getGroupMembers() ;
        }
    

    /**
     * Request a list of Groups that an Account belongs to.
     * @param account The Account identifier.
     * @return An array of GroupMemberData objects.
     * @throws CommunityIdentifierException If one of the identifiers is not valid.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public Object[] getLocalAccountGroups(String account)
        throws CommunityServiceException, CommunityIdentifierException
        {
        return groupManager.getLocalAccountGroups(account) ;
        }

    /**
     * Register a new Resource.
     * @return A new ResourceData object to represent the resource.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the WebService call fails.
     *
     */
    public ResourceData addResource()
        throws RemoteException, CommunityServiceException
        {
        return resourceManager.addResource();
        }

    /**
     * Request a Resource details.
     * @param The resource identifier.
     * @return The requested ResourceData object.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityResourceException If unable to locate the resource.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the WebService call fails.
     *
     */
   public ResourceData getResource(String ident)
        throws RemoteException, CommunityIdentifierException, CommunityResourceException, CommunityServiceException
        {
        return resourceManager.getResource(ident);
        }
   
   /**
    * Request a Resource details.
    * @return The requested ResourceData object.
    * @throws CommunityIdentifierException If the identifier is not valid.
    * @throws CommunityResourceException If unable to locate the resource.
    * @throws CommunityServiceException If there is an internal error in the service.
    * @throws RemoteException If the WebService call fails.
    *
    */
  public Object[] getResources() throws RemoteException
       {
       return resourceManager.getResources();
       }
   

    /**
     * Update a Resource details.
     * @param The ResourceData to update.
     * @return The updated ResourceData.
     * @throws CommunityIdentifierException If the resource identifier is not valid.
     * @throws CommunityResourceException If unable to locate the resource.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the WebService call fails.
     *
     */
    public ResourceData setResource(ResourceData resource)
        throws RemoteException, CommunityIdentifierException, CommunityResourceException, CommunityServiceException
        {
        return resourceManager.setResource(resource);
        }

    /**
     * Delete a Resource object.
     * @param The resource identifier.
     * @return The original ResourceData.
     * @throws CommunityIdentifierException If the resource identifier is not valid.
     * @throws CommunityResourceException If unable to locate the resource.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the WebService call fails.
     *
     */
    public ResourceData delResource(String ident)
        throws RemoteException, CommunityIdentifierException, CommunityResourceException, CommunityServiceException
        {
        return resourceManager.delResource(ident);
        }

    /**
     * Create a new PolicyPermission.
     *
     */
    public PolicyPermission addPermission(String resource, String group, String action)
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        return permissionManager.addPermission(resource, group, action) ;
        }

    /**
     * Request a PolicyPermission.
     *
     */
    public PolicyPermission getPermission(String resource, String group, String action)
    throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        return permissionManager.getPermission(resource, group, action) ;
        }
    
    /**
     * Request a PolicyPermission.
     *
     */
    public Object[] getPermissions() {
        return permissionManager.getPermissions() ;
    }
    

    /**
     * Update a PolicyPermission.
     *
     */
    public PolicyPermission setPermission(PolicyPermission permission)
    throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        return permissionManager.setPermission(permission) ;
        }

    /**
     * Delete a PolicyPermission.
     *
     */
    public boolean delPermission(String resource, String group, String action)
       throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException    
        {
        return permissionManager.delPermission(resource, group, action) ;
        }

    }



