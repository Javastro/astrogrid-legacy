/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/policy/manager/PolicyManagerCoreDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/05 17:19:59 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyManagerCoreDelegate.java,v $
 *   Revision 1.2  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.1.2.2  2004/03/04 16:09:37  dave
 *   Added PolicyService delegates
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

import java.rmi.RemoteException ;

import org.astrogrid.community.common.policy.data.AccountData ;
import org.astrogrid.community.common.policy.data.GroupData ;
import org.astrogrid.community.common.policy.data.CommunityData ;
import org.astrogrid.community.common.policy.data.ResourceData ;
import org.astrogrid.community.common.policy.data.PolicyPermission ;
import org.astrogrid.community.common.policy.data.GroupMemberData ;

import org.astrogrid.community.common.policy.manager.PolicyManager ;

import org.astrogrid.community.common.service.data.ServiceStatusData ;

/**
 * The core delegate code for our PolicyManager service.
 * This acts as a wrapper for a PolicyManager service, and handles any RemoteExceptions internally.
 *
 */
public class PolicyManagerCoreDelegate
	implements PolicyManagerDelegate
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
	public PolicyManagerCoreDelegate()
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
		this.manager = manager ;
		}

    /**
     * Create a new Account, given the Account ident.
     *
     */
    public AccountData addAccount(String ident)
		{
		AccountData result = null ;
		//
		// If we have a valid service reference.
		if (null != this.manager)
			{
			//
			// Try calling the service method.
			try {
				result = this.manager.addAccount(ident) ;
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
     * Request an Account data, given the Account ident.
     *
     */
    public AccountData getAccount(String ident)
		{
		AccountData result = null ;
		//
		// If we have a valid service reference.
		if (null != this.manager)
			{
			//
			// Try calling the service method.
			try {
				result = this.manager.getAccount(ident) ;
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
     * Update an Account data.
     *
     */
    public AccountData setAccount(AccountData account)
		{
		AccountData result = null ;
		//
		// If we have a valid service reference.
		if (null != this.manager)
			{
			//
			// Try calling the service method.
			try {
				result = this.manager.setAccount(account) ;
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
     * Delete an Account, given the Account name.
     *
     */
    public AccountData delAccount(String ident)
		{
		AccountData result = null ;
		//
		// If we have a valid service reference.
		if (null != this.manager)
			{
			//
			// Try calling the service method.
			try {
				result = this.manager.delAccount(ident) ;
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
     * Request a list of local Accounts.
     *
     */
    public Object[] getLocalAccounts()
		{
		Object[] result = null ;
		//
		// If we have a valid service reference.
		if (null != this.manager)
			{
			//
			// Try calling the service method.
			try {
				result = this.manager.getLocalAccounts() ;
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
     * Create a new Group, given the Group ident.
     *
     */
    public GroupData addGroup(String ident)
		{
		GroupData result = null ;
		//
		// If we have a valid service reference.
		if (null != this.manager)
			{
			//
			// Try calling the service method.
			try {
				result = this.manager.addGroup(ident) ;
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
     * Request an Group data, given the Group ident.
     *
     */
    public GroupData getGroup(String ident)
		{
		GroupData result = null ;
		//
		// If we have a valid service reference.
		if (null != this.manager)
			{
			//
			// Try calling the service method.
			try {
				result = this.manager.getGroup(ident) ;
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
     * Update an Group data.
     *
     */
    public GroupData setGroup(GroupData group)
		{
		GroupData result = null ;
		//
		// If we have a valid service reference.
		if (null != this.manager)
			{
			//
			// Try calling the service method.
			try {
				result = this.manager.setGroup(group) ;
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
     * Delete an Group, given the Group ident.
     *
     */
    public GroupData delGroup(String ident)
		{
		GroupData result = null ;
		//
		// If we have a valid service reference.
		if (null != this.manager)
			{
			//
			// Try calling the service method.
			try {
				result = this.manager.delGroup(ident) ;
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
     * Request a list of local Groups.
     *
     */
    public Object[] getLocalGroups()
		{
		Object[] result = null ;
		//
		// If we have a valid service reference.
		if (null != this.manager)
			{
			//
			// Try calling the service method.
			try {
				result = this.manager.getLocalGroups() ;
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
     * Get a list of local Groups that an Account belongs to, given the Account name.
     *
     */
    public Object[] getLocalAccountGroups(String account)
		{
		Object[] result = null ;
		//
		// If we have a valid service reference.
		if (null != this.manager)
			{
			//
			// Try calling the service method.
			try {
				result = this.manager.getLocalAccountGroups(account) ;
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
     * Create a new Community.
     *
     */
    public CommunityData addCommunity(String ident)
		{
		CommunityData result = null ;
		//
		// If we have a valid service reference.
		if (null != this.manager)
			{
			//
			// Try calling the service method.
			try {
				result = this.manager.addCommunity(ident) ;
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
     * Request an Community details.
     *
     */
    public CommunityData getCommunity(String ident)
		{
		CommunityData result = null ;
		//
		// If we have a valid service reference.
		if (null != this.manager)
			{
			//
			// Try calling the service method.
			try {
				result = this.manager.getCommunity(ident) ;
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
     * Update an Community details.
     *
     */
    public CommunityData setCommunity(CommunityData community)
		{
		CommunityData result = null ;
		//
		// If we have a valid service reference.
		if (null != this.manager)
			{
			//
			// Try calling the service method.
			try {
				result = this.manager.setCommunity(community) ;
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
     * Delete an Community.
     *
     */
    public CommunityData delCommunity(String ident)
		{
		CommunityData result = null ;
		//
		// If we have a valid service reference.
		if (null != this.manager)
			{
			//
			// Try calling the service method.
			try {
				result = this.manager.delCommunity(ident) ;
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
     * Request a list of Communitys.
     *
     */
    public Object[] getCommunityList()
		{
		Object[] result = null ;
		//
		// If we have a valid service reference.
		if (null != this.manager)
			{
			//
			// Try calling the service method.
			try {
				result = this.manager.getCommunityList() ;
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
     * Request a list of Accounts, given a remote Community name.
     *
     */
    public Object[] getRemoteAccounts(String community)
		{
		Object[] result = null ;
		//
		// If we have a valid service reference.
		if (null != this.manager)
			{
			//
			// Try calling the service method.
			try {
				result = this.manager.getRemoteAccounts(community) ;
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
     * Request a list of Groups, given a remote Community name.
     *
     */
    public Object[] getRemoteGroups(String community)
		{
		Object[] result = null ;
		//
		// If we have a valid service reference.
		if (null != this.manager)
			{
			//
			// Try calling the service method.
			try {
				result = this.manager.getRemoteGroups(community) ;
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
     * Add an Account to a Group, given the Account and Group names.
     *
     */
    public GroupMemberData addGroupMember(String account, String group)
		{
		GroupMemberData result = null ;
		//
		// If we have a valid service reference.
		if (null != this.manager)
			{
			//
			// Try calling the service method.
			try {
				result = this.manager.addGroupMember(account, group) ;
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
     * Remove an Account from a Group, given the Account and Group names.
     *
     */
    public GroupMemberData delGroupMember(String account, String group)
		{
		GroupMemberData result = null ;
		//
		// If we have a valid service reference.
		if (null != this.manager)
			{
			//
			// Try calling the service method.
			try {
				result = this.manager.delGroupMember(account, group) ;
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
     * Get a list of Group members, given the Group name.
     *
     */
    public Object[] getGroupMembers(String group)
		{
		Object[] result = null ;
		//
		// If we have a valid service reference.
		if (null != this.manager)
			{
			//
			// Try calling the service method.
			try {
				result = this.manager.getGroupMembers(group) ;
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
     * Get a list of remote Groups that an Account belongs to, given the Account and Community names.
     *
     */
    public Object[] getRemoteAccountGroups(String account, String community)
		{
		Object[] result = null ;
		//
		// If we have a valid service reference.
		if (null != this.manager)
			{
			//
			// Try calling the service method.
			try {
				result = this.manager.getRemoteAccountGroups(account, community) ;
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
	 * Service health check.
	 * @return ServiceStatusData with details of the Service status.
	 * TODO -refactor this to a base class
	 *
	 */
	public ServiceStatusData getServiceStatus()
		{
		ServiceStatusData result = null ;
		//
		// If we have a valid service reference.
		if (null != this.manager)
			{
			//
			// Try calling the service method.
			try {
				result = this.manager.getServiceStatus() ;
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
	}
