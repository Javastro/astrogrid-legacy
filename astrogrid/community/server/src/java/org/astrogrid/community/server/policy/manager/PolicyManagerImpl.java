package org.astrogrid.community.server.policy.manager ;

import java.rmi.RemoteException ;

import java.security.GeneralSecurityException;
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
import org.astrogrid.community.server.CommunityConfiguration;
import org.astrogrid.community.server.sso.CredentialStore;

/**
 * Server side implementation of the PolicyManager service.
 *
 */
public class PolicyManagerImpl extends CommunityServiceImpl implements PolicyManager {

  /**
   * Public constructor, using default database configuration.
   */
  public PolicyManagerImpl() throws CommunityServiceException {
    super();
    initManagers(new VOSpace());
  }

  /**
   * Public constructor, using specific database configuration.
   */
  public PolicyManagerImpl(DatabaseConfiguration config) throws CommunityServiceException {
    super(config);
    initManagers(new VOSpace());
  }

  /**
   * Public constructor, using specific database configuration and VOSpace.
   */
  public PolicyManagerImpl(DatabaseConfiguration config, VOSpace vospace) {
    super(config);
    initManagers(vospace);
  }

    /**
     * Initialise our local managers, passing a reference to 'this' as their parent.
     * @todo Refactor this into default initialiser.
     *
     */
    private void initManagers(VOSpace vospace) {
      try {
        DatabaseConfiguration dbConfig = this.getDatabaseConfiguration();
        accountManager = new AccountManagerImpl(new CommunityConfiguration(),
                                                dbConfig,
                                                new CredentialStore(dbConfig),
                                                vospace);
      } catch (GeneralSecurityException ex) {
        throw new RuntimeException("Failed to construct a PolicymanagerImpl", ex);
      }
    }

    /**
     * Our AccountManager.
     *
     */
    private AccountManagerImpl accountManager;

    /**
     * Add a new Account, given the Account ident.
     * @param ident The Account identifier.
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
      throw new CommunityServiceException("This operation is not supported.");
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
      throw new CommunityServiceException("This operation is not supported.");
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
      throw new CommunityServiceException("This operation is not supported.");
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
      throw new CommunityServiceException("This operation is not supported.");
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
      throw new CommunityServiceException("This operation is not supported.");
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
      throw new CommunityServiceException("This operation is not supported.");
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
      throw new CommunityServiceException("This operation is not supported.");
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
      throw new CommunityServiceException("This operation is not supported.");
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
      throw new CommunityServiceException("This operation is not supported.");
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
      throw new CommunityServiceException("This operation is not supported.");
        }    
    
    /**
     * Request a list of Group Members.
     *
     * @return An array of GroupMemberData objects.
     * @throws CommunityIdentifierException If one of the identifiers is not valid.
     * @throws CommunityPolicyException If the group is not local.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public Object[] getGroupMembers()
        throws RemoteException, CommunityServiceException, CommunityPolicyException, CommunityIdentifierException
        {
      throw new CommunityServiceException("This operation is not supported.");
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
      throw new CommunityServiceException("This operation is not supported.");
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
      throw new CommunityServiceException("This operation is not supported.");
        }

    /**
     * Request a Resource details.
     * @param ident The resource identifier.
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
     throw new CommunityServiceException("This operation is not supported.");
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
    throw new RemoteException("This operation is not supported.");
       }
   

    /**
     * Update a Resource details.
     * @param resource The ResourceData to update.
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
      throw new CommunityServiceException("This operation is not supported.");
        }

    /**
     * Delete a Resource object.
     * @param ident The resource identifier.
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
      throw new CommunityServiceException("This operation is not supported.");
        }

    /**
     * Create a new PolicyPermission.
     *
     */
    public PolicyPermission addPermission(String resource, String group, String action)
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
      throw new CommunityServiceException("This operation is not supported.");
        }

    /**
     * Request a PolicyPermission.
     *
     */
    public PolicyPermission getPermission(String resource, String group, String action)
    throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        throw new CommunityServiceException("This operation is not supported.");
        }
    
    /**
     * Request a PolicyPermission.
     *
     */
    public Object[] getPermissions() {
      return new Object[0];
    }
    

    /**
     * Update a PolicyPermission.
     *
     */
    public PolicyPermission setPermission(PolicyPermission permission)
    throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
      throw new CommunityServiceException("This operation is not supported.");
        }

    /**
     * Delete a PolicyPermission.
     *
     */
    public boolean delPermission(String resource, String group, String action)
       throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException    
        {
        throw new CommunityServiceException("This operation is not supported.");
        }

 }



