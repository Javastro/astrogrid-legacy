/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/policy/manager/PolicyManagerCoreDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/23 16:34:08 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyManagerCoreDelegate.java,v $
 *   Revision 1.6  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.5.2.3  2004/03/22 02:25:35  dave
 *   Updated delegate interfaces to include Exception handling.
 *
 *   Revision 1.5.2.2  2004/03/22 00:53:31  dave
 *   Refactored GroupManager to use Ivorn identifiers.
 *   Started removing references to CommunityManager.
 *
 *   Revision 1.5.2.1  2004/03/20 06:54:11  dave
 *   Added addAccount(AccountData) to PolicyManager et al.
 *   Added XML loader for AccountData.
 *
 *   Revision 1.5  2004/03/19 14:43:14  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.4.2.3  2004/03/19 03:31:21  dave
 *   Changed AccountManagerMock to recognise DatabaseManager reset()
 *
 *   Revision 1.4.2.2  2004/03/19 00:18:09  dave
 *   Refactored delegate Exception handling
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.policy.manager ;

import java.rmi.RemoteException ;

import org.astrogrid.community.client.service.CommunityServiceCoreDelegate ;

import org.astrogrid.community.common.policy.data.AccountData ;
import org.astrogrid.community.common.policy.data.GroupData ;
//import org.astrogrid.community.common.policy.data.CommunityData ;
import org.astrogrid.community.common.policy.data.ResourceData ;
import org.astrogrid.community.common.policy.data.PolicyPermission ;
import org.astrogrid.community.common.policy.data.GroupMemberData ;

import org.astrogrid.community.common.policy.manager.PolicyManager ;

import org.astrogrid.community.common.exception.CommunityPolicyException     ;
import org.astrogrid.community.common.exception.CommunityServiceException    ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

/**
 * The core delegate for our PolicyManager service.
 * This acts as a wrapper for a PolicyManager service, and converts RemoteExceptions into CommunityServiceException.
 * @todo Refactor this as a number of smaller classes.
 *
 */
public class PolicyManagerCoreDelegate
    extends CommunityServiceCoreDelegate
    implements PolicyManager, PolicyManagerDelegate
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
    protected PolicyManagerCoreDelegate()
        {
        }

    /**
     * Our PolicyManager service.
     *
     */
    private PolicyManager manager = null ;

    /**
     * Get a reference to our PolicyManager service.
     *
     */
    protected PolicyManager getPolicyManager()
        {
        return this.manager ;
        }

    /**
     * Set our our PolicyManager service.
     *
     */
    protected void setPolicyManager(PolicyManager manager)
        {
		this.setCommunityService(manager) ;
        this.manager = manager ;
        }

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
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                return this.manager.addAccount(ident) ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
				//
				// Try converting the Exception.
				convertCommunityException(ouch) ;
				//
				// If we get this far, then we don't know what it is.
				throw new CommunityServiceException(
					"WebService call failed - unexpected Exception type",
					ouch
					) ;
                }
            }
		//
		// If we don't have a valid service.
		else {
			throw new CommunityServiceException(
				"Service not initialised"
				) ;
			}
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
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                return this.manager.addAccount(account) ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
				//
				// Try converting the Exception.
				convertCommunityException(ouch) ;
				//
				// If we get this far, then we don't know what it is.
				throw new CommunityServiceException(
					"WebService call failed - unexpected Exception type",
					ouch
					) ;
                }
            }
		//
		// If we don't have a valid service.
		else {
			throw new CommunityServiceException(
				"Service not initialised"
				) ;
			}
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
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                return this.manager.getAccount(ident) ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
				//
				// Try converting the Exception.
				convertCommunityException(ouch) ;
				//
				// If we get this far, then we don't know what it is.
				throw new CommunityServiceException(
					"WebService call failed - unexpected Exception type",
					ouch
					) ;
                }
            }
		//
		// If we don't have a valid service.
		else {
			throw new CommunityServiceException(
				"Service not initialised"
				) ;
			}
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
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                return this.manager.setAccount(account) ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
				//
				// Try converting the Exception.
				convertCommunityException(ouch) ;
				//
				// If we get this far, then we don't know what it is.
				throw new CommunityServiceException(
					"WebService call failed - unexpected Exception type",
					ouch
					) ;
                }
            }
		//
		// If we don't have a valid service.
		else {
			throw new CommunityServiceException(
				"Service not initialised"
				) ;
			}
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
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                return this.manager.delAccount(ident) ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
				//
				// Try converting the Exception.
				convertCommunityException(ouch) ;
				//
				// If we get this far, then we don't know what it is.
				throw new CommunityServiceException(
					"WebService call failed - unexpected Exception type",
					ouch
					) ;
                }
            }
		//
		// If we don't have a valid service.
		else {
			throw new CommunityServiceException(
				"Service not initialised"
				) ;
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
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                return this.manager.getLocalAccounts() ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
				//
				// Try converting the Exception.
				convertServiceException(ouch) ;
				//
				// If we get this far, then we don't know what it is.
				throw new CommunityServiceException(
					"WebService call failed - unexpected Exception type",
					ouch
					) ;
                }
            }
		//
		// If we don't have a valid service.
		else {
			throw new CommunityServiceException(
				"Service not initialised"
				) ;
			}
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
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                return this.manager.addGroup(ident) ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
				//
				// Try converting the Exception.
				convertServiceException(ouch) ;
				//
				// If we get this far, then we don't know what it is.
				throw new CommunityServiceException(
					"WebService call failed - unexpected Exception type",
					ouch
					) ;
                }
            }
		//
		// If we don't have a valid service.
		else {
			throw new CommunityServiceException(
				"Service not initialised"
				) ;
			}
        }

    /**
     * Add a new Group.
     * @param  group The GroupData to add.
     * @return A new GroupData for the Group.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is already in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public GroupData addGroup(GroupData data)
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                return this.manager.addGroup(data) ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
				//
				// Try converting the Exception.
				convertServiceException(ouch) ;
				//
				// If we get this far, then we don't know what it is.
				throw new CommunityServiceException(
					"WebService call failed - unexpected Exception type",
					ouch
					) ;
                }
            }
		//
		// If we don't have a valid service.
		else {
			throw new CommunityServiceException(
				"Service not initialised"
				) ;
			}
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
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
		{
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                return this.manager.getGroup(ident) ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
				//
				// Try converting the Exception.
				convertServiceException(ouch) ;
				//
				// If we get this far, then we don't know what it is.
				throw new CommunityServiceException(
					"WebService call failed - unexpected Exception type",
					ouch
					) ;
                }
            }
		//
		// If we don't have a valid service.
		else {
			throw new CommunityServiceException(
				"Service not initialised"
				) ;
			}
        }

    /**
     * Update a Group.
     * @param  data The new GroupData to update.
     * @return A new GroupData for the Group.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public GroupData setGroup(GroupData data)
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                return this.manager.setGroup(data) ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
				//
				// Try converting the Exception.
				convertServiceException(ouch) ;
				//
				// If we get this far, then we don't know what it is.
				throw new CommunityServiceException(
					"WebService call failed - unexpected Exception type",
					ouch
					) ;
                }
            }
		//
		// If we don't have a valid service.
		else {
			throw new CommunityServiceException(
				"Service not initialised"
				) ;
			}
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
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException 
        {
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                return this.manager.delGroup(ident) ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
				//
				// Try converting the Exception.
				convertServiceException(ouch) ;
				//
				// If we get this far, then we don't know what it is.
				throw new CommunityServiceException(
					"WebService call failed - unexpected Exception type",
					ouch
					) ;
                }
            }
		//
		// If we don't have a valid service.
		else {
			throw new CommunityServiceException(
				"Service not initialised"
				) ;
			}
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
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                return this.manager.getLocalGroups() ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
				//
				// Try converting the Exception.
				convertServiceException(ouch) ;
				//
				// If we get this far, then we don't know what it is.
				throw new CommunityServiceException(
					"WebService call failed - unexpected Exception type",
					ouch
					) ;
                }
            }
		//
		// If we don't have a valid service.
		else {
			throw new CommunityServiceException(
				"Service not initialised"
				) ;
			}
        }

    /**
     * Request a list of local Groups that an Account belongs to, given the Account ident.
     * @param  account The Account ifentifier.
     * @return An array of GroupData objects.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public Object[] getLocalAccountGroups(String account)
        throws CommunityServiceException, CommunityIdentifierException
        {
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                return this.manager.getLocalAccountGroups(account) ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
				//
				// Try converting the Exception.
				convertServiceException(ouch) ;
				//
				// If we get this far, then we don't know what it is.
				throw new CommunityServiceException(
					"WebService call failed - unexpected Exception type",
					ouch
					) ;
                }
            }
		//
		// If we don't have a valid service.
		else {
			throw new CommunityServiceException(
				"Service not initialised"
				) ;
			}
        }

    /**
     * Add a new Community, given the Account ident.
     * @param  ident The Community identifier.
     * @return A CommunityData for the Community.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is already in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
    public CommunityData addCommunity(String ident)
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                return this.manager.addCommunity(ident) ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
				throw new CommunityServiceException(
					"WebService call failed",
					ouch
					) ;
                }
            }
		//
		// If we don't have a valid service.
		else {
			throw new CommunityServiceException(
				"Service not initialised"
				) ;
			}
        }
     */

    /**
     * Request a Community details, given the Community ident.
     * @param  ident The Community identifier.
     * @return A CommunityData for the Community.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
    public CommunityData getCommunity(String ident)
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                return this.manager.getCommunity(ident) ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
				throw new CommunityServiceException(
					"WebService call failed",
					ouch
					) ;
                }
            }
		//
		// If we don't have a valid service.
		else {
			throw new CommunityServiceException(
				"Service not initialised"
				) ;
			}
        }
     */

    /**
     * Update a Community.
     * @param  community The new CommunityData to update.
     * @return A new CommunityData for the Community.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
    public CommunityData setCommunity(CommunityData community)
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                return this.manager.setCommunity(community) ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
				throw new CommunityServiceException(
					"WebService call failed",
					ouch
					) ;
                }
            }
		//
		// If we don't have a valid service.
		else {
			throw new CommunityServiceException(
				"Service not initialised"
				) ;
			}
        }
     */

    /**
     * Delete a Community.
     * @param  ident The Community identifier.
     * @return The CommunityData for the old Community.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
    public CommunityData delCommunity(String ident)
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                return this.manager.delCommunity(ident) ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
				throw new CommunityServiceException(
					"WebService call failed",
					ouch
					) ;
                }
            }
		//
		// If we don't have a valid service.
		else {
			throw new CommunityServiceException(
				"Service not initialised"
				) ;
			}
        }
     */

    /**
     * Request a list of Communities.
     * @return An array of CommunityData objects.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
    public Object[] getCommunityList()
        throws CommunityServiceException
        {
        Object[] result = null ;
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                return this.manager.getCommunityList() ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
				throw new CommunityServiceException(
					"WebService call failed",
					ouch
					) ;
                }
            }
		//
		// If we don't have a valid service.
		else {
			throw new CommunityServiceException(
				"Service not initialised"
				) ;
			}
        }
     */

   /**
    * Create a new Resource.
    *
    */
   public ResourceData addResource(String ident)
        {
        ResourceData result = null ;
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                result = this.manager.addResource(ident) ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
                //
                // Unpack the RemoteException, and re-throw the real Exception.
                //
                }
            }
        return result ;
        }

   /**
    * Request an Resource details.
    *
    */
   public ResourceData getResource(String ident)
        {
        ResourceData result = null ;
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                result = this.manager.getResource(ident) ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
                //
                // Unpack the RemoteException, and re-throw the real Exception.
                //
                }
            }
        return result ;
        }

   /**
    * Update an Resource details.
    *
    */
   public ResourceData setResource(ResourceData resource)
        {
        ResourceData result = null ;
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                result = this.manager.setResource(resource) ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
                //
                // Unpack the RemoteException, and re-throw the real Exception.
                //
                }
            }
        return result ;
        }

   /**
    * Delete an Resource.
    *
    */
   public boolean delResource(String ident)
        {
        boolean result = false ;
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                result = this.manager.delResource(ident) ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
                //
                // Unpack the RemoteException, and re-throw the real Exception.
                //
                }
            }
        return result ;
        }

   /**
    * Request a list of Resources.
    *
    */
   public Object[] getResourceList()
        {
        Object[] result = null ;
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                result = this.manager.getResourceList() ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
                //
                // Unpack the RemoteException, and re-throw the real Exception.
                //
                }
            }
        return result ;
        }

    /**
     * Create a new PolicyPermission.
     *
     */
    public PolicyPermission addPermission(String resource, String group, String action)
        {
        PolicyPermission result = null ;
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                result = this.manager.addPermission(resource, group, action) ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
                //
                // Unpack the RemoteException, and re-throw the real Exception.
                //
                }
            }
        return result ;
        }

    /**
     * Request a PolicyPermission.
     *
     */
    public PolicyPermission getPermission(String resource, String group, String action)
        {
        PolicyPermission result = null ;
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                result = this.manager.getPermission(resource, group, action) ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
                //
                // Unpack the RemoteException, and re-throw the real Exception.
                //
                }
            }
        return result ;
        }

    /**
     * Update a PolicyPermission.
     *
     */
    public PolicyPermission setPermission(PolicyPermission permission)
        {
        PolicyPermission result = null ;
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                result = this.manager.setPermission(permission) ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
                //
                // Unpack the RemoteException, and re-throw the real Exception.
                //
                }
            }
        return result ;
        }

    /**
     * Delete a PolicyPermission.
     *
     */
    public boolean delPermission(String resource, String group, String action)
        {
        boolean result = false ;
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                result = this.manager.delPermission(resource, group, action) ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
                //
                // Unpack the RemoteException, and re-throw the real Exception.
                //
                }
            }
        return result ;
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
     *
     */
    public GroupMemberData addGroupMember(String account, String group)
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                return this.manager.addGroupMember(account, group) ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
				//
				// Try converting the Exception.
				convertServiceException(ouch) ;
				//
				// If we get this far, then we don't know what it is.
				throw new CommunityServiceException(
					"WebService call failed - unexpected Exception type",
					ouch
					) ;
                }
            }
		//
		// If we don't have a valid service.
		else {
			throw new CommunityServiceException(
				"Service not initialised"
				) ;
			}
        }

    /**
     * Remove an Account from a Group.
     * The group must be local, but Account can be local or remote.
     * @param  account The Account identifier.
     * @param  group   The Group identifier.
     * @return A GroupMemberData to confirm the membership.
     * @throws CommunityIdentifierException If one of the identifiers is not valid.
     * @throws CommunityPolicyException If one the identifiers is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public GroupMemberData delGroupMember(String account, String group)
        throws CommunityServiceException, CommunityPolicyException, CommunityIdentifierException
        {
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                return this.manager.delGroupMember(account, group) ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
				//
				// Try converting the Exception.
				convertServiceException(ouch) ;
				//
				// If we get this far, then we don't know what it is.
				throw new CommunityServiceException(
					"WebService call failed - unexpected Exception type",
					ouch
					) ;
                }
            }
		//
		// If we don't have a valid service.
		else {
			throw new CommunityServiceException(
				"Service not initialised"
				) ;
			}
        }

    /**
     * Request a list of Group members.
     * The group must be local.
     * @param  group   The Group identifier.
     * @return An array of GroupMemberData objects..
     * @throws CommunityIdentifierException If one of the identifiers is not valid.
     * @throws CommunityPolicyException If one the identifiers is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public Object[] getGroupMembers(String group)
        throws CommunityServiceException, CommunityPolicyException, CommunityIdentifierException
        {
        //
        // If we have a valid service reference.
        if (null != this.manager)
            {
            //
            // Try calling the service method.
            try {
                return this.manager.getGroupMembers(group) ;
                }
            //
            // Catch anything that went BANG.
            catch (RemoteException ouch)
                {
				//
				// Try converting the Exception.
				convertServiceException(ouch) ;
				//
				// If we get this far, then we don't know what it is.
				throw new CommunityServiceException(
					"WebService call failed - unexpected Exception type",
					ouch
					) ;
                }
            }
		//
		// If we don't have a valid service.
		else {
			throw new CommunityServiceException(
				"Service not initialised"
				) ;
			}
        }
    }
