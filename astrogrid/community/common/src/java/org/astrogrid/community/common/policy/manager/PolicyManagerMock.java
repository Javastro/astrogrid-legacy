/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/PolicyManagerMock.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/23 16:34:08 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyManagerMock.java,v $
 *   Revision 1.5  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.4.2.3  2004/03/22 00:53:31  dave
 *   Refactored GroupManager to use Ivorn identifiers.
 *   Started removing references to CommunityManager.
 *
 *   Revision 1.4.2.2  2004/03/21 06:41:41  dave
 *   Refactored to include Exception handling.
 *
 *   Revision 1.4.2.1  2004/03/20 06:54:11  dave
 *   Added addAccount(AccountData) to PolicyManager et al.
 *   Added XML loader for AccountData.
 *
 *   Revision 1.4  2004/03/19 14:43:14  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.3.14.1  2004/03/18 13:41:19  dave
 *   Added Exception handling to AccountManager
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
 *   Revision 1.1.2.2  2004/03/05 14:03:23  dave
 *   Added first client side SOAP test - SecurityServiceSoapDelegateTestCase
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
package org.astrogrid.community.common.policy.manager ;

import java.rmi.RemoteException ;

import org.astrogrid.community.common.policy.data.AccountData ;
import org.astrogrid.community.common.policy.data.GroupData ;
import org.astrogrid.community.common.policy.data.ResourceData ;
import org.astrogrid.community.common.policy.data.PolicyPermission ;
import org.astrogrid.community.common.policy.data.GroupMemberData ;

import org.astrogrid.community.common.service.CommunityServiceMock ;

import org.astrogrid.community.common.exception.CommunityPolicyException     ;
import org.astrogrid.community.common.exception.CommunityServiceException    ;
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
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Public constructor.
     *
     */
    public PolicyManagerMock()
        {
        super() ;
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyManagerMock()") ;
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
     * Our CommunityManager.
     *
    private CommunityManagerMock communityManager = new CommunityManagerMock() ;
     */

    /**
     * Add a new Community, given the Account ident.
     * @param  ident The Community identifier.
     * @return A CommunityData for the Community.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is already in the database.
     *
    public CommunityData addCommunity(String ident)
        throws CommunityIdentifierException, CommunityPolicyException
        {
        return communityManager.addCommunity(ident) ;
        }
     */

    /**
     * Request a Community details, given the Community ident.
     * @param  ident The Community identifier.
     * @return A CommunityData for the Community.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     *
    public CommunityData getCommunity(String ident)
        throws CommunityIdentifierException, CommunityPolicyException
        {
        return communityManager.getCommunity(ident) ;
        }
     */

    /**
     * Update a Community.
     * @param  update The new CommunityData to update.
     * @return A new CommunityData for the Community.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     *
    public CommunityData setCommunity(CommunityData update)
        throws CommunityIdentifierException, CommunityPolicyException
        {
        return communityManager.setCommunity(update) ;
        }
     */

    /**
     * Delete a Community.
     * @param  ident The Community identifier.
     * @return The CommunityData for the old Community.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     *
    public CommunityData delCommunity(String ident)
        throws CommunityIdentifierException, CommunityPolicyException
        {
        return communityManager.delCommunity(ident) ;
        }
     */

    /**
     * Request a list of Communities.
     * @return An array of CommunityData objects.
     *
    public Object[] getCommunityList()
        {
        return communityManager.getCommunityList() ;
        }
     */

    /**
     * Our ResourceManager.
     *
     */
    private ResourceManagerMock resourceManager = new ResourceManagerMock() ;

   /**
    * Create a new Resource.
    *
    */
   public ResourceData addResource(String ident)
        throws RemoteException
        {
        return resourceManager.addResource(ident) ;
        }

   /**
    * Request an Resource details.
    *
    */
   public ResourceData getResource(String ident)
        throws RemoteException
        {
        return resourceManager.getResource(ident) ;
        }

   /**
    * Update an Resource details.
    *
    */
   public ResourceData setResource(ResourceData resource)
        throws RemoteException
        {
        return resourceManager.setResource(resource) ;
        }

   /**
    * Delete an Resource.
    *
    */
   public boolean delResource(String ident)
        throws RemoteException
        {
        return resourceManager.delResource(ident) ;
        }

   /**
    * Request a list of Resources.
    *
    */
   public Object[] getResourceList()
        throws RemoteException
        {
        return resourceManager.getResourceList() ;
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
     * Update a PolicyPermission.
     *
     */
    public PolicyPermission setPermission(PolicyPermission permission)
        throws RemoteException
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

    /**
     * Request a list of Accounts, given a remote Community name.
     *
     */
    public Object[] getRemoteAccounts(String community)
        {
        // TODO - return something.
        return null ;
        }

    /**
     * Request a list of Groups, given a remote Community name.
     *
     */
    public Object[] getRemoteGroups(String community)
        {
        // TODO - return something.
        return null ;
        }

    /**
     * Add an Account to a Group, given the Account and Group names.
     *
     */
    public GroupMemberData addGroupMember(String account, String group)
        {
        // TODO - return something.
        return null ;
        }

    /**
     * Remove an Account from a Group, given the Account and Group names.
     *
     */
    public GroupMemberData delGroupMember(String account, String group)
        {
        // TODO - return something.
        return null ;
        }

    /**
     * Get a list of Group members, given the Group name.
     *
     */
    public Object[] getGroupMembers(String group)
        {
        // TODO - return something.
        return null ;
        }

    /**
     * Get a list of remote Groups that an Account belongs to, given the Account and Community names.
     *
    public Object[] getRemoteAccountGroups(String account, String community)
        {
        // TODO - return something.
        return null ;
        }
     */
    }
