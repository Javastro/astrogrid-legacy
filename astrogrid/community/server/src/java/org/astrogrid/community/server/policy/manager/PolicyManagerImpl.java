/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/policy/manager/PolicyManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/19 14:43:15 $</cvs:date>
 * <cvs:version>$Revision: 1.8 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyManagerImpl.java,v $
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
import org.astrogrid.community.common.policy.data.CommunityData ;
import org.astrogrid.community.common.policy.data.GroupMemberData ;
import org.astrogrid.community.common.policy.data.PolicyPermission ;
import org.astrogrid.community.common.policy.data.CommunityIdent ;

import org.astrogrid.community.common.config.CommunityConfig ;

import org.astrogrid.community.common.policy.manager.PolicyManager ;

import org.astrogrid.community.server.service.CommunityServiceImpl ;
import org.astrogrid.community.server.database.configuration.DatabaseConfiguration ;

import org.astrogrid.community.common.exception.CommunityPolicyException     ;
import org.astrogrid.community.common.exception.CommunityServiceException    ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

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
        communityManager  = new CommunityManagerImpl(this)  ;
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
     */
    private CommunityManagerImpl communityManager ;

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
     * @throws RemoteException If the Account is in a remote Community and the WebService call fails.
     * @todo Use resolver to find the remote service.
     *
     */
    public AccountData addAccount(String ident)
        throws RemoteException, CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        //
        // Check for null ident.
        if (null == ident)
            {
            throw new CommunityIdentifierException(
				"Null identifier"
            	) ;
            }
		//
		// Parse the ident and process the result.
        return this.addAccount(new CommunityIdent(ident)) ;
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
     */
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
                //
                // Use our local manager.
                return accountManager.addAccount(ident) ;
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
     */
    public AccountData getAccount(String ident)
        throws RemoteException, CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        //
        // Check for null ident.
        if (null == ident)
            {
            throw new CommunityIdentifierException(
				"Null identifier"
            	) ;
            }
		//
		// Parse the ident and process that.
        return this.getAccount(new CommunityIdent(ident)) ;
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
     */
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

    /**
     * Update an Account.
     * @param  account The new AccountData to update.
     * @return A new AccountData for the Account.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the Account is in a remote Community and the WebService call fails.
     * @todo Use CommunityIvornParser to handle the identifier.
     * @todo Use resolver to find the remote service.
     *
     */
    public AccountData setAccount(AccountData account)
        throws RemoteException, CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyManagerImpl.setAccount()") ;
        if (DEBUG_FLAG) System.out.println("  ident : " + account.getIdent()) ;
        //
        // Check for null account.
        if (null == account)
            {
            throw new CommunityIdentifierException(
				"Null account"
            	) ;
            }
        //
        // Check for null ident.
        if (null == account.getIdent())
            {
            throw new CommunityIdentifierException(
				"Null identifier"
            	) ;
            }
        CommunityIdent ident = new CommunityIdent(account.getIdent()) ;
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
                return accountManager.setAccount(account) ;
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
                    return remote.setAccount(account) ;
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
     */
    public AccountData delAccount(String ident)
        throws RemoteException, CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        //
        // Check for null ident.
        if (null == ident)
            {
            throw new CommunityIdentifierException(
				"Null identifier"
            	) ;
            }
        return this.delAccount(new CommunityIdent(ident)) ;
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
     */
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
     * Create a new Group, given the Group name.
     *
     */
    public GroupData addGroup(String name)
        {
        return this.addGroup(new CommunityIdent(name)) ;
        }

    /**
     * Create a new Group, given the Group ident.
     *
     */
    protected GroupData addGroup(CommunityIdent ident)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyManagerImpl.addGroup()") ;
        if (DEBUG_FLAG) System.out.println("  ident : " + ident) ;

        GroupData result = null ;
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
                result = groupManager.addGroup(ident) ;
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
                    //
                    // Try asking the remote manager.
                    try {
                        result = remote.addGroup(ident.toString()) ;
                        }
                    //
                    // Catch a remote Exception from the SOAP call.
                    catch (RemoteException ouch)
                        {
                        if (DEBUG_FLAG) System.out.println("FAIL : Remote service call failed.") ;
                        result = null ;
                        }
                    //
                    // If we got a result.
                    if (null != result)
                        {
                        if (DEBUG_FLAG) System.out.println("PASS : Created remote group") ;
                        }
                    //
                    // If we didn't get a result.
                    else {
                        if (DEBUG_FLAG) System.out.println("FAIL : Failed to create remote group") ;
                        }
                    }
                //
                // If we didn't get a remote manager.
                else {
                    if (DEBUG_FLAG) System.out.println("FAIL : Unknown remote manager") ;
                    result = null ;
                    }
                }
            }
        //
        // If the ident is not valid.
        else {
            if (DEBUG_FLAG) System.out.println("FAIL : Ident not valid") ;
            result = null ;
            }

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        return result ;
        }

    /**
     * Request a Group data, given the Group name.
     *
     */
    public GroupData getGroup(String name)
        {
        return this.getGroup(new CommunityIdent(name)) ;
        }

    /**
     * Request a Group data, given the Group ident.
     *
     */
    protected GroupData getGroup(CommunityIdent ident)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyManagerImpl.getGroup()") ;
        if (DEBUG_FLAG) System.out.println("  ident : " + ident) ;

        GroupData result = null ;
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
                result = groupManager.getGroup(ident) ;
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
                    //
                    // Try asking the remote manager.
                    try {
                        result = remote.getGroup(ident.toString()) ;
                        }
                    //
                    // Catch a remote Exception from the SOAP call.
                    catch (RemoteException ouch)
                        {
                        if (DEBUG_FLAG) System.out.println("FAIL : Remote service call failed.") ;
                        result = null ;
                        }
                    //
                    // If we got a result.
                    if (null != result)
                        {
                        if (DEBUG_FLAG) System.out.println("PASS : Found remote group") ;
                        }
                    //
                    // If we didn't get a result.
                    else {
                        if (DEBUG_FLAG) System.out.println("FAIL : Unknown remote group") ;
                        }
                    }
                //
                // If we didn't get a remote manager.
                else {
                    if (DEBUG_FLAG) System.out.println("FAIL : Unknown remote manager") ;
                    result = null ;
                    }
                }
            }
        //
        // If the ident is not valid.
        else {
            if (DEBUG_FLAG) System.out.println("FAIL : Ident not valid") ;
            result = null ;
            }

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        return result ;
        }

    /**
     * Update an existing Group data.
     *
     */
    public GroupData setGroup(GroupData group)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyManagerImpl.setGroup()") ;
        if (DEBUG_FLAG) System.out.println("  ident : " + group.getIdent()) ;

        GroupData result = null ;
        CommunityIdent ident = new CommunityIdent(group.getIdent()) ;
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
                result = groupManager.setGroup(group) ;
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
                    //
                    // Try asking the remote manager.
                    try {
                        result = remote.setGroup(group) ;
                        }
                    //
                    // Catch a remote Exception from the SOAP call.
                    catch (RemoteException ouch)
                        {
                        if (DEBUG_FLAG) System.out.println("FAIL : Remote service call failed.") ;
                        result = null ;
                        }
                    //
                    // If we got a result.
                    if (null != result)
                        {
                        if (DEBUG_FLAG) System.out.println("PASS : Found remote group") ;
                        }
                    //
                    // If we didn't get a result.
                    else {
                        if (DEBUG_FLAG) System.out.println("FAIL : Unknown remote group") ;
                        }
                    }
                //
                // If we didn't get a remote manager.
                else {
                    if (DEBUG_FLAG) System.out.println("FAIL : Unknown remote manager") ;
                    result = null ;
                    }
                }
            }
        //
        // If the ident is not valid.
        else {
            if (DEBUG_FLAG) System.out.println("FAIL : Ident not valid") ;
            result = null ;
            }

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        return result ;
        }

    /**
     * Delete an Group, given the Group name.
     *
     */
    public GroupData delGroup(String name)
        {
        return this.delGroup(new CommunityIdent(name)) ;
        }

    /**
     * Delete an Group, given the Group ident.
     *
     */
    protected GroupData delGroup(CommunityIdent ident)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyManagerImpl.delGroup()") ;
        if (DEBUG_FLAG) System.out.println("  ident : " + ident) ;

        GroupData result = null ;
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
                result = groupManager.delGroup(ident) ;
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
                    //
                    // Try asking the remote manager.
                    try {
                        result = remote.delGroup(ident.toString()) ;
                        }
                    //
                    // Catch a remote Exception from the SOAP call.
                    catch (RemoteException ouch)
                        {
                        if (DEBUG_FLAG) System.out.println("FAIL : Remote service call failed.") ;
                        result = null ;
                        }
                    //
                    // If we got a result.
                    if (null != result)
                        {
                        if (DEBUG_FLAG) System.out.println("PASS : Found remote group") ;
                        }
                    //
                    // If we didn't get a result.
                    else {
                        if (DEBUG_FLAG) System.out.println("FAIL : Unknown remote group") ;
                        }
                    }
                //
                // If we didn't get a remote manager.
                else {
                    if (DEBUG_FLAG) System.out.println("FAIL : Unknown remote manager") ;
                    result = null ;
                    }
                }
            }
        //
        // If the ident is not valid.
        else {
            if (DEBUG_FLAG) System.out.println("FAIL : Ident not valid") ;
            result = null ;
            }

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        return result ;
        }

    /**
     * Request a list of local Groups.
     *
     */
    public Object[] getLocalGroups()
        {
        return groupManager.getLocalGroups() ;
        }

    /**
     * Request a list of Groups, given a remote Community name.
     *
     */
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
        throws RemoteException, CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        return this.addGroupMember(new CommunityIdent(account), new CommunityIdent(group)) ;
        }

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
    protected GroupMemberData addGroupMember(CommunityIdent account, CommunityIdent group)
        throws RemoteException, CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyManagerImpl.addGroupMember()") ;
        if (DEBUG_FLAG) System.out.println("  account  : " + account) ;
        if (DEBUG_FLAG) System.out.println("  group    : " + group) ;

        GroupMemberData member = null ;
        //
        // If the Group is local.
        if (group.isLocal())
            {
            if (DEBUG_FLAG) System.out.println("PASS : Group is local") ;
            //
            // Try loading the local Group.
            GroupData grp = this.getGroup(group) ;
            //
            // If the local Group exists.
            if (null != grp)
                {
                if (DEBUG_FLAG) System.out.println("PASS : Group is found") ;
                //
                // If the group is an account only group.
                // TODO Need a better test for this.
                if (GroupData.SINGLE_TYPE.equals(grp.getType()))
                    {
                    //
                    // Fail : Account only group.
                    if (DEBUG_FLAG) System.out.println("FAIL : Group is account only") ;
                    }
                //
                // If the group is not an account only group.
                else {
                    if (DEBUG_FLAG) System.out.println("PASS : Group is valid") ;
                    //
                    // Try loading the Account data.
                    AccountData acc = this.getAccount(account) ;
                    //
                    // If the Account exists (local or remote).
                    if (null != acc)
                        {
                        if (DEBUG_FLAG) System.out.println("PASS : Account is found") ;
                        //
                        // Add the membership record.
                        member = groupManager.addGroupMember(account, group) ;
                        //
                        // If the membership record was created.
                        if (null != member)
                            {
                            if (DEBUG_FLAG) System.out.println("PASS : GroupMember added") ;
                            if (DEBUG_FLAG) System.out.println("  Account : " + member.getAccount()) ;
                            if (DEBUG_FLAG) System.out.println("  Group   : " + member.getGroup()) ;
                            }
                        //
                        // If the membership record was not created.
                        else {
                            if (DEBUG_FLAG) System.out.println("FAIL : GroupMember not added") ;
                            }
                        }
                    //
                    // If the account does not exist.
                    else {
                        if (DEBUG_FLAG) System.out.println("FAIL : Account is unknown") ;
                        }
                    }
                }
            //
            // If the local Group does not exist.
            else {
                if (DEBUG_FLAG) System.out.println("FAIL : Group is unknown") ;
                }
            }
        //
        // If the Group is not local.
        else {
            //
            // Pass the request on to the remote service ??
            if (DEBUG_FLAG) System.out.println("FAIL : Group is remote") ;
            }
        //
        // TODO
        // Should return a DataObject with status response.
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        return member ;
        }

    /**
     * Remove an Account from a Group, given the Account and Group names.
     *
     */
    public GroupMemberData delGroupMember(String account, String group)
        {
        return this.delGroupMember(new CommunityIdent(account), new CommunityIdent(group)) ;
        }

    /**
     * Remove an Account from a Group, given the Account and Group idents.
     * Group must be local, but Account can be anything (local, remote, or deleted).
     *
     */
    protected GroupMemberData delGroupMember(CommunityIdent account, CommunityIdent group)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyManagerImpl.delGroupMember()") ;
        if (DEBUG_FLAG) System.out.println("  account  : " + account) ;
        if (DEBUG_FLAG) System.out.println("  group    : " + group) ;

        GroupMemberData member = null ;
        //
        // If the Group is local.
        if (group.isLocal())
            {
            if (DEBUG_FLAG) System.out.println("PASS : Group is local") ;
            //
            // Try loading the local Group.
            GroupData grp = this.getGroup(group) ;
            //
            // If the local Group exists.
            if (null != grp)
                {
                if (DEBUG_FLAG) System.out.println("PASS : Group is found") ;
                //
                // If the group is an account only group.
                // TODO Need a better test for this.
                if (GroupData.SINGLE_TYPE.equals(grp.getType()))
                    {
                    //
                    // Fail : Account only group.
                    if (DEBUG_FLAG) System.out.println("FAIL : Group is account") ;
                    }
                //
                // If the group is not an account only group.
                else {
                    if (DEBUG_FLAG) System.out.println("PASS : Group is valid") ;
                    //
                    // Delete the membership record.
                    member = groupManager.delGroupMember(account, group) ;
                    //
                    // If the membership record was found.
                    if (null != member)
                        {
                        if (DEBUG_FLAG) System.out.println("PASS : GroupMember removed") ;
                        if (DEBUG_FLAG) System.out.println("  Account : " + member.getAccount()) ;
                        if (DEBUG_FLAG) System.out.println("  Group   : " + member.getGroup()) ;
                        }
                    //
                    // If the membership record was not found.
                    else {
                        if (DEBUG_FLAG) System.out.println("FAIL : GroupMember unknown") ;
                        }
                    }
                }
            //
            // If the local Group does not exist.
            else {
                if (DEBUG_FLAG) System.out.println("FAIL : Group is unknown") ;
                }
            }
        //
        // If the Group is not local.
        else {
            //
            // Pass the request on to the remote service ??
            if (DEBUG_FLAG) System.out.println("FAIL : Group is remote") ;
            }
        //
        // TODO
        // Should return a DataObject with status response.
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        return member ;
        }

    /**
     * Get a list of Group members, given the Group name.
     *
     */
    public Object[] getGroupMembers(String name)
        {
        return this.getGroupMembers(new CommunityIdent(name)) ;
        }

    /**
     * Get a list of Group members, given the Group ident.
     * Group can be local or remote.
     *
     */
    protected Object[] getGroupMembers(CommunityIdent ident)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("PolicyManagerImpl.getGroupMembers()") ;
        if (DEBUG_FLAG) System.out.println("  group    : " + ident) ;

        Object[] results = null ;
        //
        // If the Group is local.
        if (ident.isLocal())
            {
            if (DEBUG_FLAG) System.out.println("PASS : Group is local") ;
            //
            // Request the list of members.
            results = groupManager.getGroupMembers(ident) ;
            //
            // If we got a result.
            if (null != results)
                {
                if (DEBUG_FLAG) System.out.println("PASS : Found local groups") ;
                }
            //
            // If we didn't get a result.
            else {
                if (DEBUG_FLAG) System.out.println("FAIL : Missing local groups") ;
                }
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
                try {
                    results = remote.getGroupMembers(ident.toString()) ;
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
        return results;
        }

    /**
     * Get a list of local Groups that an Account belongs to, given the Account name.
     *
     */
    public Object[] getLocalAccountGroups(String account)
        {
        return this.getLocalAccountGroups(new CommunityIdent(account)) ;
        }

    /**
     * Get a list of local Groups that an Account belongs to, given the Account ident.
     *
     */
    protected Object[] getLocalAccountGroups(CommunityIdent account)
        {
        return groupManager.getLocalAccountGroups(account) ;
        }

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
     */
    public CommunityData addCommunity(String ident)
        {
        return communityManager.addCommunity(ident) ;
        }

    /**
     * Request a Community details.
     *
     */
    public CommunityData getCommunity(String ident)
        {
        return communityManager.getCommunity(ident) ;
        }

    /**
     * Update a Community details.
     *
     */
    public CommunityData setCommunity(CommunityData community)
        {
        return communityManager.setCommunity(community) ;
        }

    /**
     * Delete a Community.
     *
     */
    public CommunityData delCommunity(String ident)
        {
        return communityManager.delCommunity(ident) ;
        }

    /**
     * Request a list of Communitys.
     *
     */
    public Object[] getCommunityList()
        {
        return communityManager.getCommunityList() ;
        }

      
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



