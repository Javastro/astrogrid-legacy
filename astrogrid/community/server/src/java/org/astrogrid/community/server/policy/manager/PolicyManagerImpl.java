/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/policy/manager/PolicyManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/23 16:34:08 $</cvs:date>
 * <cvs:version>$Revision: 1.9 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyManagerImpl.java,v $
 *   Revision 1.9  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.8.2.6  2004/03/22 15:31:10  dave
 *   Added CommunitySecurityException.
 *   Updated SecurityManager and SecurityService to use Exceptions.
 *
 *   Revision 1.8.2.5  2004/03/22 00:53:31  dave
 *   Refactored GroupManager to use Ivorn identifiers.
 *   Started removing references to CommunityManager.
 *
 *   Revision 1.8.2.4  2004/03/21 18:14:29  dave
 *   Refactored GroupManagerImpl to use Ivorn identifiers.
 *
 *   Revision 1.8.2.3  2004/03/21 17:13:54  dave
 *   Added Ivorn handling to AccountManagerImpl.
 *   Simplified Account handling in PolicyManagerImpl.
 *
 *   Revision 1.8.2.2  2004/03/21 06:41:41  dave
 *   Refactored to include Exception handling.
 *
 *   Revision 1.8.2.1  2004/03/20 06:54:11  dave
 *   Added addAccount(AccountData) to PolicyManager et al.
 *   Added XML loader for AccountData.
 *
 *   Revision 1.8  2004/03/19 14:43:15  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.7.14.1  2004/03/18 13:41:19  dave
 *   Added Exception handling to AccountManager
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.policy.manager ;

import java.rmi.RemoteException ;

import org.astrogrid.community.common.policy.data.GroupData ;
import org.astrogrid.community.common.policy.data.AccountData ;
import org.astrogrid.community.common.policy.data.ResourceData;
//import org.astrogrid.community.common.policy.data.CommunityData ;
import org.astrogrid.community.common.policy.data.GroupMemberData ;
import org.astrogrid.community.common.policy.data.PolicyPermission ;


import org.astrogrid.community.common.policy.manager.PolicyManager ;

import org.astrogrid.community.server.service.CommunityServiceImpl ;
import org.astrogrid.community.server.database.configuration.DatabaseConfiguration ;

import org.astrogrid.community.common.exception.CommunityPolicyException     ;
import org.astrogrid.community.common.exception.CommunityServiceException    ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

//
// todo - Remove these
import org.astrogrid.community.common.config.CommunityConfig ;
import org.astrogrid.community.common.policy.data.CommunityIdent ;

public class PolicyManagerImpl
    extends CommunityServiceImpl
    implements PolicyManager
    {
    /**
     * Switch for our debug statements.
     *
     */
    protected static final boolean DEBUG_FLAG = true ;

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
        groupManager      = new GroupManagerImpl(this)      ;
        accountManager    = new AccountManagerImpl(this)    ;
        resourceManager   = new ResourceManagerImpl(this)   ;
//        communityManager  = new CommunityManagerImpl(this)  ;
        permissionManager = new PermissionManagerImpl(this) ;
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
     * Our CommunityManager.
     *
    private CommunityManagerImpl communityManager ;
     */

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
     * Add a new Account, given the Account ident.
     * @param  ident The Account identifier.
     * @return An AccountData for the Account.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is already in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the Account is in a remote Community and the WebService call fails.
     * @todo Use CommunityIvornParser to handle the identifier.
     * @todo Use resolver to find the remote service.
     *
    protected AccountData addAccount(CommunityIdent ident)
        throws RemoteException, CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyManagerImpl.addAccount()") ;
        if (DEBUG_FLAG) System.out.println("  ident : " + ident) ;
        //
        // Check for null ident.
        if (null == ident)
            {
            throw new CommunityIdentifierException(
				"Null identifier"
            	) ;
            }
        //
        // If the ident is valid.
        if (ident.isValid())
            {
            if (DEBUG_FLAG) System.out.println("PASS : Ident is valid") ;
            //
            // If the ident is local.
            if (ident.isLocal())
                {
                if (DEBUG_FLAG) System.out.println("PASS : Ident is local") ;
                }
            //
            // If the ident is not local.
            else {
                if (DEBUG_FLAG) System.out.println("PASS : Ident is remote") ;
                //
                // Get a manager for the remote community.
                PolicyManager remote = communityManager.getPolicyManager(ident.getCommunity()) ;
                //
                // If we got a remote manager.
                if (null != remote)
                    {
                    if (DEBUG_FLAG) System.out.println("PASS : Found remote manager") ;
                    if (DEBUG_FLAG) System.out.println("Asking remote manager to create the account") ;
                    return remote.addAccount(ident.toString()) ;
                    }
                //
                // If we didn't get a remote manager.
                else {
                    if (DEBUG_FLAG) System.out.println("FAIL : Unknown remote manager") ;
return null ;
                    }
                }
            }
        //
        // If the ident is not valid.
        else {
            if (DEBUG_FLAG) System.out.println("FAIL : Ident not valid") ;
return null ;
            }
        }
     */

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
        return accountManager.getAccount(ident) ;
        }

    /**
     * Request an Account details, given the Account ident.
     * @param  ident The Account identifier.
     * @return An AccountData for the Account.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the Account is in a remote Community and the WebService call fails.
     * @todo Use CommunityIvornParser to handle the identifier.
     * @todo Use resolver to find the remote service.
     *
    protected AccountData getAccount(CommunityIdent ident)
        throws RemoteException, CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyManagerImpl.getAccount()") ;
        if (DEBUG_FLAG) System.out.println("  ident : " + ident) ;
        //
        // Check for null ident.
        if (null == ident)
            {
            throw new CommunityIdentifierException(
				"Null identifier"
            	) ;
            }
        //
        // If the ident is valid.
        if (ident.isValid())
            {
            if (DEBUG_FLAG) System.out.println("PASS : Ident is valid") ;
            //
            // If the ident is local.
            if (ident.isLocal())
                {
                if (DEBUG_FLAG) System.out.println("PASS : Ident is local") ;
                //
                // Use our local manager.
                return accountManager.getAccount(ident) ;
                }
            //
            // If the ident is not local.
            else {
                if (DEBUG_FLAG) System.out.println("PASS : Ident is remote") ;
                //
                // Get a manager for the remote community.
                PolicyManager remote = communityManager.getPolicyManager(ident.getCommunity()) ;
                //
                // If we got a remote manager.
                if (null != remote)
                    {
                    if (DEBUG_FLAG) System.out.println("PASS : Found remote manager") ;
                    if (DEBUG_FLAG) System.out.println("Asking remote manager for the account") ;
                    return remote.getAccount(ident.toString()) ;
                    }
                //
                // If we didn't get a remote manager.
                else {
                    if (DEBUG_FLAG) System.out.println("FAIL : Unknown remote manager") ;
return null ;
                    }
                }
            }
        //
        // If the ident is not valid.
        else {
            if (DEBUG_FLAG) System.out.println("FAIL : Ident not valid") ;
return null ;
            }
        }
     */

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
     * Delete an Account.
     * @param  ident The Account identifier.
     * @return The AccountData for the old Account.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the Account is in a remote Community and the WebService call fails.
     * @todo Use CommunityIvornParser to handle the identifier.
     * @todo Use resolver to find the remote service.
     *
    protected AccountData delAccount(CommunityIdent ident)
        throws RemoteException, CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyManagerImpl.delAccount()") ;
        if (DEBUG_FLAG) System.out.println("  ident : " + ident) ;
        //
        // Check for null ident.
        if (null == ident)
            {
            throw new CommunityIdentifierException(
				"Null identifier"
            	) ;
            }
        //
        // If the ident is valid.
        if (ident.isValid())
            {
            if (DEBUG_FLAG) System.out.println("PASS : Ident is valid") ;
            //
            // If the ident is local.
            if (ident.isLocal())
                {
                if (DEBUG_FLAG) System.out.println("PASS : Ident is local") ;
                //
                // Use our local manager.
                return accountManager.delAccount(ident) ;
                }
            //
            // If the ident is not local.
            else {
                if (DEBUG_FLAG) System.out.println("PASS : Ident is remote") ;
                //
                // Get a manager for the remote community.
                PolicyManager remote = communityManager.getPolicyManager(ident.getCommunity()) ;
                //
                // If we got a remote manager.
                if (null != remote)
                    {
                    if (DEBUG_FLAG) System.out.println("PASS : Found remote manager") ;
                    if (DEBUG_FLAG) System.out.println("Asking remote manager to update the account") ;
                    return remote.delAccount(ident.toString()) ;
                    }
                //
                // If we didn't get a remote manager.
                else {
                    if (DEBUG_FLAG) System.out.println("FAIL : Unknown remote manager") ;
return null ;
                    }
                }
            }
        //
        // If the ident is not valid.
        else {
            if (DEBUG_FLAG) System.out.println("FAIL : Ident not valid") ;
return null ;
            }
        }
     */

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
     * Request a list of Accounts from another Community.
     * @param  ident The Community identifier.
     * @return An array of AccountData objects.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @todo Use CommunityIvornParser to handle the identifier.
     * @todo Use resolver to find the remote service.
     *
    public Object[] getRemoteAccounts(String name)
		throws CommunityServiceException, CommunityIdentifierException
        {
        Object[] results = null ;
        //
        // If the community is local.
        if (CommunityConfig.getCommunityName().equals(name))
            {
            if (DEBUG_FLAG) System.out.println("PASS : Community is local") ;
            //
            // Call our local manager.
            return accountManager.getLocalAccounts() ;
            }
        //
        // If the community is remote.
        else {
            if (DEBUG_FLAG) System.out.println("PASS : Community is remote") ;
            //
            // Get a manager for the remote community.
            PolicyManager remote = communityManager.getPolicyManager(name) ;
            //
            // If we got a remote manager.
            if (null != remote)
                {
                if (DEBUG_FLAG) System.out.println("PASS : Found remote manager") ;
                if (DEBUG_FLAG) System.out.println("Asking remote manager for accounts") ;
                return remote.getLocalAccounts() ;
                }
            //
            // If we didn't get a remote manager.
            else {
                if (DEBUG_FLAG) System.out.println("FAIL : Unknown remote manager") ;
return null ;
                }
            }
        }
     */

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
     * Add a new Group, given the Group ident.
     * @param  ident The Group identifier.
     * @return A GroupData for the Group.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is already in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the Account is in a remote Community and the WebService call fails.
     * @todo Use resolver to find the remote service.
     *
    protected GroupData addGroup(CommunityIdent ident)
        throws RemoteException, CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyManagerImpl.addGroup()") ;
        if (DEBUG_FLAG) System.out.println("  Group : " + ident) ;
        //
        // Check for null ident.
        if (null == ident)
            {
            throw new CommunityIdentifierException(
				"Null identifier"
            	) ;
            }
        //
        // If the ident is valid.
        if (ident.isValid())
            {
            if (DEBUG_FLAG) System.out.println("PASS : Ident is valid") ;
            //
            // If the ident is local.
            if (ident.isLocal())
                {
                if (DEBUG_FLAG) System.out.println("PASS : Ident is local") ;
                //
                // Use our local manager.
                return groupManager.addGroup(ident) ;
                }
            //
            // If the ident is not local.
            else {
                if (DEBUG_FLAG) System.out.println("PASS : Ident is remote") ;
                //
                // Get a manager for the remote community.
                PolicyManager remote = communityManager.getPolicyManager(ident.getCommunity()) ;
                //
                // If we got a remote manager.
                if (null != remote)
                    {
                    if (DEBUG_FLAG) System.out.println("PASS : Found remote manager") ;
                    return remote.addGroup(ident.toString()) ;
                    }
                //
                // If we didn't get a remote manager.
                else {
                    if (DEBUG_FLAG) System.out.println("FAIL : Unknown remote manager") ;
return null ;
                    }
                }
            }
        //
        // If the ident is not valid.
        else {
            if (DEBUG_FLAG) System.out.println("FAIL : Ident not valid") ;
return null ;
            }
        }
     */

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
     * Request a Group details, given the Group ident.
     * @param  ident The Group identifier.
     * @return A GroupData for the Group.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the Group is in a remote Community and the WebService call fails.
     * @todo Use CommunityIvornParser to handle the identifier.
     * @todo Use resolver to find the remote service.
     *
    protected GroupData getGroup(CommunityIdent ident)
        throws RemoteException, CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyManagerImpl.getGroup()") ;
        if (DEBUG_FLAG) System.out.println("  Group : " + ident) ;
        //
        // Check for null ident.
        if (null == ident)
            {
            throw new CommunityIdentifierException(
				"Null identifier"
            	) ;
            }
        //
        // If the ident is valid.
        if (ident.isValid())
            {
            if (DEBUG_FLAG) System.out.println("PASS : Ident is valid") ;
            //
            // If the ident is local.
            if (ident.isLocal())
                {
                if (DEBUG_FLAG) System.out.println("PASS : Ident is local") ;
                //
                // Use our local manager.
                return groupManager.getGroup(ident) ;
                }
            //
            // If the ident is not local.
            else {
                if (DEBUG_FLAG) System.out.println("PASS : Ident is remote") ;
                //
                // Get a manager for the remote community.
                PolicyManager remote = communityManager.getPolicyManager(ident.getCommunity()) ;
                //
                // If we got a remote manager.
                if (null != remote)
                    {
                    if (DEBUG_FLAG) System.out.println("PASS : Found remote manager") ;
                    return remote.getGroup(ident.toString()) ;
                    }
                //
                // If we didn't get a remote manager.
                else {
                    if (DEBUG_FLAG) System.out.println("FAIL : Unknown remote manager") ;
return null ;
                    }
                }
            }
        //
        // If the ident is not valid.
        else {
            if (DEBUG_FLAG) System.out.println("FAIL : Ident not valid") ;
return null ;
            }
        }
     */

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
     * Delete a Group.
     * @param  ident The Group identifier.
     * @return The GroupData for the old Group.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the Group is in a remote Community and the WebService call fails.
     * @todo Use CommunityIvornParser to handle the identifier.
     * @todo Use resolver to find the remote service.
     *
    protected GroupData delGroup(CommunityIdent ident)
        throws RemoteException, CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyManagerImpl.delGroup()") ;
        if (DEBUG_FLAG) System.out.println("  Group : " + ident) ;
        //
        // Check for null ident.
        if (null == ident)
            {
            throw new CommunityIdentifierException(
				"Null identifier"
            	) ;
            }
        //
        // If the ident is valid.
        if (ident.isValid())
            {
            if (DEBUG_FLAG) System.out.println("PASS : Ident is valid") ;
            //
            // If the ident is local.
            if (ident.isLocal())
                {
                if (DEBUG_FLAG) System.out.println("PASS : Ident is local") ;
                //
                // Use our local manager.
                return groupManager.delGroup(ident) ;
                }
            //
            // If the ident is not local.
            else {
                if (DEBUG_FLAG) System.out.println("PASS : Ident is remote") ;
                //
                // Get a manager for the remote community.
                PolicyManager remote = communityManager.getPolicyManager(ident.getCommunity()) ;
                //
                // If we got a remote manager.
                if (null != remote)
                    {
                    if (DEBUG_FLAG) System.out.println("PASS : Found remote manager") ;
                    return remote.delGroup(ident.toString()) ;
                    }
                //
                // If we didn't get a remote manager.
                else {
                    if (DEBUG_FLAG) System.out.println("FAIL : Unknown remote manager") ;
return null ;
                    }
                }
            }
        //
        // If the ident is not valid.
        else {
            if (DEBUG_FLAG) System.out.println("FAIL : Ident not valid") ;
return null ;
            }
        }
     */

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
     * Request a list of Groups, given a remote Community name.
     *
    public Object[] getRemoteGroups(String name)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyManagerImpl.getRemoteGroups()") ;
        if (DEBUG_FLAG) System.out.println("  community : " + name) ;
        Object[] results = null ;
        //
        // If the community is local.
        if (CommunityConfig.getCommunityName().equals(name))
            {
            if (DEBUG_FLAG) System.out.println("PASS : Community is local") ;
            //
            // Call our local manager.
            results = groupManager.getLocalGroups() ;
            }
        //
        // If the community is remote.
        else {
            if (DEBUG_FLAG) System.out.println("PASS : Community is remote") ;
            //
            // Get a manager for the remote community.
            PolicyManager remote = communityManager.getPolicyManager(name) ;
            //
            // If we got a remote manager.
            if (null != remote)
                {
                if (DEBUG_FLAG) System.out.println("PASS : Found remote manager") ;
                //
                // Try asking the remote manager.
                try {
                    results = remote.getLocalGroups() ;
                    }
                //
                // Catch a remote Exception from the SOAP call.
                catch (RemoteException ouch)
                    {
                    if (DEBUG_FLAG) System.out.println("FAIL : Remote service call failed.") ;
                    results = null ;
                    }
                //
                // If we got a result.
                if (null != results)
                    {
                    if (DEBUG_FLAG) System.out.println("PASS : Found remote groups") ;
                    }
                //
                // If we didn't get a result.
                else {
                    if (DEBUG_FLAG) System.out.println("FAIL : Missing remote groups") ;
                    }
                }
            //
            // If we didn't get a remote manager.
            else {
                if (DEBUG_FLAG) System.out.println("FAIL : Unknown remote manager") ;
                results = null ;
                }
            }
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        return results ;
        }
     */

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
     * Add an Account to a Group.
     * The group must be local, but Account can be local or remote.
     * @param  account The Account identifier.
     * @param  group   The Group identifier.
     * @return A GroupMemberData to confirm the membership.
     * @throws CommunityIdentifierException If one of the identifiers is not valid.
     * @throws CommunityPolicyException If one the identifiers is not in the database.
     * @throws CommunityPolicyException If the Group is private, for a single Account only.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the Group is in a remote Community and the WebService call fails.
     *
    protected GroupMemberData addGroupMember(CommunityIdent account, CommunityIdent group)
        throws RemoteException, CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyManagerImpl.addGroupMember()") ;
        if (DEBUG_FLAG) System.out.println("  Account  : " + account) ;
        if (DEBUG_FLAG) System.out.println("  Group    : " + group) ;
        //
        // Check for null account.
        if (null == account)
            {
            throw new CommunityIdentifierException(
				"Null account"
            	) ;
            }
        //
        // Check for null group.
        if (null == group)
            {
            throw new CommunityIdentifierException(
				"Null group"
            	) ;
            }
        //
        // If the group is local.
        if (group.isLocal())
            {
	        //
	        // Try loading the Account.
			AccountData accountCheck = this.getAccount(account) ;
	        if (DEBUG_FLAG) System.out.println("PASS : Account is found") ;
	        //
	        // Try loading the Group.
			GroupData groupCheck = this.getGroup(group) ;
	        if (DEBUG_FLAG) System.out.println("PASS : Group is found") ;
	        //
	        // If the group is a private group
	        if (GroupData.SINGLE_TYPE.equals(groupCheck.getType()))
	            {
				throw new CommunityPolicyException(
					"Can't add an Account to a private Group",
					group.toString()
					) ;
	            }
	        if (DEBUG_FLAG) System.out.println("PASS : Group is valid") ;
	        //
	        // Add the membership record.
	        return groupManager.addGroupMember(account, group) ;
			}
		//
		// If the group is not local.
		else {
return null ;
			}
        }
     */

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
     * Remove an Account from a Group.
     * The group must be local, but Account can be local or remote.
     * @param  account The Account identifier.
     * @param  group   The Group identifier.
     * @return A GroupMemberData to confirm the membership.
     * @throws CommunityIdentifierException If one of the identifiers is not valid.
     * @throws CommunityPolicyException If one the identifiers is not in the database.
     * @throws CommunityPolicyException If the Group is private, for a single Account only.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the Group is in a remote Community and the WebService call fails.
     *
    protected GroupMemberData delGroupMember(CommunityIdent account, CommunityIdent group)
        throws RemoteException, CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyManagerImpl.delGroupMember()") ;
        if (DEBUG_FLAG) System.out.println("  account  : " + account) ;
        if (DEBUG_FLAG) System.out.println("  group    : " + group) ;
        //
        // Check for null account.
        if (null == account)
            {
            throw new CommunityIdentifierException(
				"Null account"
            	) ;
            }
        //
        // Check for null group.
        if (null == group)
            {
            throw new CommunityIdentifierException(
				"Null group"
            	) ;
            }
        //
        // If the Group is local.
        if (group.isLocal())
            {
            if (DEBUG_FLAG) System.out.println("PASS : Group is local") ;
            //
            // Try loading the Group.
            GroupData check = this.getGroup(group) ;
            if (DEBUG_FLAG) System.out.println("PASS : Group is found") ;
            //
            // If the group is an account only group.
            if (GroupData.SINGLE_TYPE.equals(check.getType()))
                {
				throw new CommunityPolicyException(
					"Can't remove Account from a private Group",
					group.toString()
					) ;
                }
            if (DEBUG_FLAG) System.out.println("PASS : Group is valid") ;
            //
            // Delete the membership record.
            return groupManager.delGroupMember(account, group) ;
            }
        //
        // If the Group is not local.
        else {
return null ;
			}
        }
     */

    /**
     * Request a list of Group Members.
     * @param group The Group identifier.
     * @return An array of GroupMemberData objects.
     * @throws CommunityIdentifierException If one of the identifiers is not valid.
     * @throws CommunityPolicyException If the group is not local.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public Object[] getGroupMembers(String ident)
        throws RemoteException, CommunityServiceException, CommunityPolicyException, CommunityIdentifierException
        {
        return groupManager.getGroupMembers(ident) ;
        }

    /**
     * Request a list of Group Members.
     * @param group The Group identifier.
     * @return An array of GroupMemberData objects.
     * @throws CommunityIdentifierException If one of the identifiers is not valid.
     * @throws CommunityPolicyException If the group is not local.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException if the WebService call for a remote group fails.
     *
    protected Object[] getGroupMembers(CommunityIdent ident)
        throws RemoteException, CommunityServiceException, CommunityPolicyException, CommunityIdentifierException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyManagerImpl.getGroupMembers()") ;
        if (DEBUG_FLAG) System.out.println("  Ident : " + ident) ;
        //
        // Check for null ident.
        if (null == ident)
            {
            throw new CommunityIdentifierException(
				"Null group"
            	) ;
            }
        //
        // If the Group is local.
        if (ident.isLocal())
            {
            if (DEBUG_FLAG) System.out.println("PASS : Group is local") ;
            //
            // Request the list of members.
            return groupManager.getGroupMembers(ident) ;
            }
        //
        // If the Group is not local.
        else {
            //
            // Get a manager for the remote community.
            PolicyManager remote = communityManager.getPolicyManager(ident.toString()) ;
            //
            // If we got a remote manager.
            if (null != remote)
                {
                if (DEBUG_FLAG) System.out.println("PASS : Found remote manager") ;
                //
                // Try asking the remote manager.
                return remote.getGroupMembers(ident.toString()) ;
                }
            //
            // If we didn't get a remote manager.
            else {
                if (DEBUG_FLAG) System.out.println("FAIL : Unknown remote manager") ;
return null ;
                }
            }
        }
     */

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
     * Request a list of Groups that an Account belongs to.
     * @param account The Account identifier.
     * @return An array of GroupMemberData objects.
     * @throws CommunityIdentifierException If one of the identifiers is not valid.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
    protected Object[] getLocalAccountGroups(CommunityIdent account)
        throws CommunityServiceException, CommunityIdentifierException
        {
        return groupManager.getLocalAccountGroups(account) ;
        }
     */

    /**
     * Get a list of remote Groups that an Account belongs to, given the Account and Community names.
     *
    public Object[] getRemoteAccountGroups(String account, String community)
        {
        return this.getRemoteAccountGroups(new CommunityIdent(account), community) ;
        }
     */

    /**
     * Get a list of remote Groups that an Account belongs to, given the Account and Community idents.
     *
    protected Object[] getRemoteAccountGroups(CommunityIdent account, String community)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyManagerImpl.getRemoteAccountGroups()") ;
        if (DEBUG_FLAG) System.out.println("  account   : " + account) ;
        if (DEBUG_FLAG) System.out.println("  community : " + community) ;
        Object[] results = null ;
        //
        // If the community is local.
        if (CommunityConfig.getCommunityName().equals(community))
            {
            if (DEBUG_FLAG) System.out.println("PASS : Community is local") ;
            //
            // Call our local manager.
            results = groupManager.getLocalAccountGroups(account) ;
            }
        //
        // If the Community is not local.
        else {
            if (DEBUG_FLAG) System.out.println("PASS : Community is remote") ;
            //
            // Get a manager for the remote community.
            PolicyManager remote = communityManager.getPolicyManager(community) ;
            //
            // If we got a remote manager.
            if (null != remote)
                {
                if (DEBUG_FLAG) System.out.println("PASS : Found remote manager") ;
                //
                // Try asking the remote manager.
                try {
                    results = remote.getLocalAccountGroups(account.toString()) ;
                    }
                //
                // Catch a remote Exception from the SOAP call.
                catch (RemoteException ouch)
                    {
                    if (DEBUG_FLAG) System.out.println("FAIL : Remote service call failed.") ;
                    results = null ;
                    }
                //
                // If we got a result.
                if (null != results)
                    {
                    if (DEBUG_FLAG) System.out.println("PASS : Found remote groups") ;
                    }
                //
                // If we didn't get a result.
                else {
                    if (DEBUG_FLAG) System.out.println("FAIL : Missing remote groups") ;
                    }
                }
            //
            // If we didn't get a remote manager.
            else {
                if (DEBUG_FLAG) System.out.println("FAIL : Unknown remote manager") ;
                results = null ;
                }
            }
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        return results ;
        }
     */

    /**
     * Create a new Community.
     *
    public CommunityData addCommunity(String ident)
        {
        return communityManager.addCommunity(ident) ;
        }
     */

    /**
     * Request a Community details.
     *
    public CommunityData getCommunity(String ident)
        {
        return communityManager.getCommunity(ident) ;
        }
     */

    /**
     * Update a Community details.
     *
    public CommunityData setCommunity(CommunityData community)
        {
        return communityManager.setCommunity(community) ;
        }
     */

    /**
     * Delete a Community.
     *
    public CommunityData delCommunity(String ident)
        {
        return communityManager.delCommunity(ident) ;
        }
     */

    /**
     * Request a list of Communitys.
     *
    public Object[] getCommunityList()
        {
        return communityManager.getCommunityList() ;
        }
     */

      /**
       * Create a new Resource.
       *
       */
      public ResourceData addResource(String name)
         {
            return resourceManager.addResource(name);
         }

      /**
       * Request an Resource details.
       *
       */
      public ResourceData getResource(String ident) {
            return resourceManager.getResource(ident);
         }

      /**
       * Update an Resource details.
       *
       */
      public ResourceData setResource(ResourceData resource) {
            return resourceManager.setResource(resource);
         }

      /**
       * Delete an Resource.
       *
       */
      public boolean delResource(String ident) {
            return resourceManager.delResource(ident);
            
         }

      /**
       * Request a list of Resources.
       *
       */
      public Object[] getResourceList() {
            return resourceManager.getResourceList();
         }
      

    /**
     * Create a new PolicyPermission.
     *
     */
    public PolicyPermission addPermission(String resource, String group, String action)
        {
        return permissionManager.addPermission(resource, group, action) ;
        }

    /**
     * Request a PolicyPermission.
     *
     */
    public PolicyPermission getPermission(String resource, String group, String action)
        {
        return permissionManager.getPermission(resource, group, action) ;
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
        {
        return permissionManager.delPermission(resource, group, action) ;
        }

    /**
     * Request a list of PolicyPermissions for a resource.
     *
    public Object[] getPermissionList(String resource)
     */

    }



