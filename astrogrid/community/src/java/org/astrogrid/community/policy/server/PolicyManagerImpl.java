/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/Attic/PolicyManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/10 02:56:03 $</cvs:date>
 * <cvs:version>$Revision: 1.12 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyManagerImpl.java,v $
 *   Revision 1.12  2003/09/10 02:56:03  dave
 *   Added PermissionManager and tests
 *
 *   Revision 1.11  2003/09/10 00:08:45  dave
 *   Added getGroupMembers, ResourceIdent and JUnit tests for ResourceManager
 *
 *   Revision 1.10  2003/09/09 19:13:32  KevinBenson
 *   New resource managerr stuff
 *
 *   Revision 1.9  2003/09/09 14:51:47  dave
 *   Added delGroupMember - only local accounts and groups to start with.
 *
 *   Revision 1.8  2003/09/09 13:48:09  dave
 *   Added addGroupMember - only local accounts and groups to start with.
 *
 *   Revision 1.7  2003/09/09 10:57:47  dave
 *   Added corresponding SINGLE Group to addAccount and delAccount.
 *
 *   Revision 1.6  2003/09/08 20:28:50  dave
 *   Added CommunityIdent, with isLocal() and isValid()
 *
 *   Revision 1.5  2003/09/08 11:01:35  KevinBenson
 *   A check in of the Authentication authenticateToken roughdraft and some changes to the groudata and community data
 *   along with an AdministrationDelegate
 *
 *   Revision 1.4  2003/09/06 20:10:07  dave
 *   Split PolicyManager into separate components.
 *
 *   Revision 1.3  2003/09/04 23:33:05  dave
 *   Implemented the core account manager methods - needs data object to return results
 *
 *   Revision 1.2  2003/09/03 15:23:33  dave
 *   Split API into two services, PolicyService and PolicyManager
 *
 *   Revision 1.1  2003/09/03 06:39:13  dave
 *   Rationalised things into one set of SOAP stubs and one set of data objects for both client and server.
 *
 *   Revision 1.1  2003/08/28 17:33:56  dave
 *   Initial policy prototype
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.server ;

import java.rmi.RemoteException ;

import java.util.Vector ;
import java.util.Collection ;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.PersistenceException ;
import org.exolab.castor.jdo.ObjectNotFoundException ;
import org.exolab.castor.jdo.DatabaseNotFoundException ;
import org.exolab.castor.jdo.DuplicateIdentityException ;
import org.exolab.castor.jdo.TransactionNotInProgressException ;
import org.exolab.castor.jdo.ClassNotPersistenceCapableException ;

import org.astrogrid.community.policy.data.GroupData ;
import org.astrogrid.community.policy.data.ServiceData ;
import org.astrogrid.community.policy.data.AccountData ;
import org.astrogrid.community.policy.data.ResourceData;
import org.astrogrid.community.policy.data.CommunityData ;
import org.astrogrid.community.policy.data.CommunityIdent ;
import org.astrogrid.community.policy.data.CommunityConfig ;
import org.astrogrid.community.policy.data.GroupMemberData ;
import org.astrogrid.community.policy.data.PolicyPermission ;

public class PolicyManagerImpl
	implements PolicyManager
	{
	/**
	 * Switch for our debug statements.
	 *
	 */
	protected static final boolean DEBUG_FLAG = true ;

	/**
	 * Public constructor.
	 *
	 */
	public PolicyManagerImpl()
		{
		this.init() ;
		}

	/**
	 * Our DatabaseManager.
	 *
	 */
	private DatabaseManagerImpl databaseManager ;

	/**
	 * Our AccountManager.
	 *
	 */
	private AccountManagerImpl accountManager ;

	/**
	 * Our GroupManager.
	 *
	 */
	private GroupManagerImpl groupManager ;

	/**
	 * Our CommunityManager.
	 *
	 */
	private CommunityManagerImpl communityManager ;

	/**
	 * Our ResourceManager
	 *
	 */
	private ResourceManagerImpl resourceManager;

	/**
	 * Our PermissionManager
	 *
	 */
	private PermissionManagerImpl permissionManager;

	/**
	 * Initialise our service.
	 *
	 */
	public void init()
		{
		//
		// Initialise our configuration.
		CommunityConfig.setConfig(new CommunityConfigImpl()) ;
		//
		// Initialise our DatabaseManager.
		databaseManager = new DatabaseManagerImpl() ;
		//
		// Initialise our AccountManager.
		accountManager = new AccountManagerImpl() ;
		accountManager.init(databaseManager) ;
		//
		// Initialise our GroupManager.
		groupManager = new GroupManagerImpl() ;
		groupManager.init(databaseManager) ;
		//
		// Initialise our CommunityManager.
		communityManager = new CommunityManagerImpl() ;
		communityManager.init(databaseManager) ;
		//
		// Initialise our ResourceManager.
		resourceManager = new ResourceManagerImpl();
		resourceManager.init(databaseManager);
		//
		// Initialise our PermissionManager.
		permissionManager = new PermissionManagerImpl();
		permissionManager.init(databaseManager);
		}

	/**
	 * Service health check.
	 *
	 */
	public ServiceData getServiceStatus()
		throws RemoteException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("PolicyManagerImpl.getServiceStatus()") ;

		ServiceData result =  new ServiceData() ;
		result.setIdent(CommunityConfig.getConfig().getCommunityName()) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return result ;
		}

	/**
	 * Create a new Account.
	 *
	 */
	public AccountData addAccount(String ident)
		throws RemoteException
		{
		return accountManager.addAccount(ident) ;
		}

	/**
	 * Request an Account details.
	 *
	 */
	public AccountData getAccount(String ident)
		throws RemoteException
		{
		return accountManager.getAccount(ident) ;
		}

	/**
	 * Update an Account details.
	 *
	 */
	public AccountData setAccount(AccountData account)
		throws RemoteException
		{
		return accountManager.setAccount(account) ;
		}

	/**
	 * Delete an Account.
	 *
	 */
	public boolean delAccount(String ident)
		throws RemoteException
		{
		return accountManager.delAccount(ident) ;
		}

	/**
	 * Request a list of Accounts.
	 *
	 */
	public Object[] getAccountList()
		throws RemoteException
		{
		return accountManager.getAccountList() ;     
      }
      
      /**
       * Create a new Resource.
       *
       */
      public ResourceData addResource(String name)
         throws RemoteException {
            return resourceManager.addResource(name);
         }

      /**
       * Request an Resource details.
       *
       */
      public ResourceData getResource(String ident)
         throws RemoteException {
            return resourceManager.getResource(ident);
         }

      /**
       * Update an Resource details.
       *
       */
      public ResourceData setResource(ResourceData resource)
         throws RemoteException {
            return resourceManager.setResource(resource);
         }

      /**
       * Delete an Resource.
       *
       */
      public boolean delResource(String ident)
         throws RemoteException {
            return resourceManager.delResource(ident);
            
         }

      /**
       * Request a list of Resources.
       *
       */
      public Object[] getResourceList()
         throws RemoteException {
            return resourceManager.getResourceList();
         }
      

	/**
	 * Create a new Group.
	 *
	 */
	public GroupData addGroup(String ident)
		throws RemoteException
		{
		return groupManager.addGroup(ident) ;
		}

	/**
	 * Request an Group details.
	 *
	 */
	public GroupData getGroup(String ident)
		throws RemoteException
		{
		return groupManager.getGroup(ident) ;
		}

	/**
	 * Update an Group details.
	 *
	 */
	public GroupData setGroup(GroupData group)
		throws RemoteException
		{
		return groupManager.setGroup(group) ;
		}

	/**
	 * Delete an Group.
	 *
	 */
	public boolean delGroup(String ident)
		throws RemoteException
		{
		return groupManager.delGroup(ident) ;
		}

	/**
	 * Request a list of Groups.
	 *
	 */
	public Object[] getGroupList()
		throws RemoteException
		{
		return groupManager.getGroupList() ;
		}

	/**
	* Request a list of Groups.
	*
	*/
	public Object[] getAccountGroupList(String account)
		throws RemoteException
		{
		return groupManager.getAccountGroupList(account) ;
		}

	/**
	 * Create a new Community.
	 *
	 */
	public CommunityData addCommunity(String ident)
		throws RemoteException
		{
		return communityManager.addCommunity(ident) ;
		}

	/**
	 * Request an Community details.
	 *
	 */
	public CommunityData getCommunity(String ident)
		throws RemoteException
		{
		return communityManager.getCommunity(ident) ;
		}

	/**
	 * Update an Community details.
	 *
	 */
	public CommunityData setCommunity(CommunityData community)
		throws RemoteException
		{
		return communityManager.setCommunity(community) ;
		}

	/**
	 * Delete an Community.
	 *
	 */
	public boolean delCommunity(String ident)
		throws RemoteException
		{
		return communityManager.delCommunity(ident) ;
		}

	/**
	 * Request a list of Communitys.
	 *
	 */
	public Object[] getCommunityList()
		throws RemoteException
		{
		return communityManager.getCommunityList() ;
		}

	/**
	 * Add a member to a Group.
	 *
	 */
	public GroupMemberData addGroupMember(String accountName, String groupName)
		throws RemoteException
		{

		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("PolicyManagerImpl.addGroupMember()") ;
		if (DEBUG_FLAG) System.out.println("  account  : " + accountName) ;
		if (DEBUG_FLAG) System.out.println("  group    : " + groupName) ;

		GroupMemberData member = null ;
		//
		// Create a CommunityIdent for the Group and Account
		CommunityIdent groupIdent   = new CommunityIdent(groupName) ;
		CommunityIdent accountIdent = new CommunityIdent(accountName) ;
		if (DEBUG_FLAG) System.out.println("  account : " + accountIdent) ;
		if (DEBUG_FLAG) System.out.println("  group   : " + groupIdent) ;
		//
		// If the Group is local.
		if (groupIdent.isLocal())
			{
			if (DEBUG_FLAG) System.out.println("PASS : Group is local") ;
			//
			// Try loading the local Group.
			GroupData groupData = this.getGroup(groupIdent.toString()) ;
			//
			// If the local Group exists.
			if (null != groupData)
				{
				if (DEBUG_FLAG) System.out.println("PASS : Group is found") ;
				//
				// If the group is an account only group.
				// TODO Need a better test for this.
				if (groupData.SINGLE_TYPE.equals(groupData.getType()))
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
					// If the Account is local
					if (accountIdent.isLocal())
						{
						if (DEBUG_FLAG) System.out.println("PASS : Account is local") ;
						//
						// Try loading the local Account.
						AccountData accountData = this.getAccount(accountIdent.toString()) ;
						//
						// If the local Account exists.
						if (null != accountData)
							{
							if (DEBUG_FLAG) System.out.println("PASS : Account is found") ;
							//
							// Add the membership record.
							member = groupManager.addGroupMember(accountIdent, groupIdent) ;
							if (DEBUG_FLAG) System.out.println("PASS : Member added : " + member) ;
							}
						//
						// If the local Account does not exist.
						else {
							//
							// Fail : Unknown Account
							if (DEBUG_FLAG) System.out.println("FAIL : Account is unknown") ;
							}
						}
					//
					// If the Account is not local.
					else {
						if (DEBUG_FLAG) System.out.println("PASS : Account is remote") ;
						//
						// Try loading the remote Community.
						CommunityData communityData = null ;
						//
						// If the remote community exists.
						if (null != communityData)
							{
							if (DEBUG_FLAG) System.out.println("PASS : Community is found") ;
							//
							// Try requesting the remote Account.
							AccountData accountData = null ;
							//
							// If the remote Account exists.
							if (null != accountData)
								{
								if (DEBUG_FLAG) System.out.println("PASS : Account is found") ;
								//
								// Add the membership record.
								member = groupManager.addGroupMember(accountIdent, groupIdent) ;
								if (DEBUG_FLAG) System.out.println("PASS : Member added : " + member) ;
								}
							//
							// If the remote Account does not exist.
							else {
								//
								// Fail : Unknown Account
								if (DEBUG_FLAG) System.out.println("FAIL : Account is unknown") ;
								}
							}
						//
						// If the remote community does not exist.
						else {
							//
							// Fail : Unknown Community
							if (DEBUG_FLAG) System.out.println("FAIL : Community is unknown") ;
							}
						}
					}
				}
			//
			// If the local Group does not exist.
			else {
				//
				// Fail : Unknown Group
				if (DEBUG_FLAG) System.out.println("FAIL : Group is unknown") ;
				}
			}
		//
		// If the Group is not local.
		else {
			//
			// Fail : Unknown Group
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
	 * Remove a member from a Group.
	 *
	 */
	public boolean delGroupMember(String accountName, String groupName)
		throws RemoteException
		{
		boolean result = false ;

		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("PolicyManagerImpl.delGroupMember()") ;
		if (DEBUG_FLAG) System.out.println("  account  : " + accountName) ;
		if (DEBUG_FLAG) System.out.println("  group    : " + groupName) ;
		//
		// Create a CommunityIdent for the Group and Account
		CommunityIdent groupIdent   = new CommunityIdent(groupName) ;
		CommunityIdent accountIdent = new CommunityIdent(accountName) ;
		if (DEBUG_FLAG) System.out.println("  account : " + accountIdent) ;
		if (DEBUG_FLAG) System.out.println("  group   : " + groupIdent) ;
//
// TODO
// Check this isn't an account group.
//
		//
		// No checking required, just remove the group.
		result = groupManager.delGroupMember(accountIdent, groupIdent) ;

		//
		// TODO
		// Should return a DataObject with status response.
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return result ;
		}

	/**
	 * Get a list of group members.
	 *
	 */
	public Object[] getGroupMembers(String groupName)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("GroupManagerImpl.getGroupMembers()") ;
		if (DEBUG_FLAG) System.out.println("  group    : " + groupName) ;

		Object[] array = null ;
		//
		// Create a CommunityIdent for the Group.
		CommunityIdent groupIdent   = new CommunityIdent(groupName) ;
		if (DEBUG_FLAG) System.out.println("  group   : " + groupIdent) ;

		//
		// If the Group is local.
		if (groupIdent.isLocal())
			{
			if (DEBUG_FLAG) System.out.println("PASS : Group is local") ;
			//
			// Request the list of members.
			array = groupManager.getGroupMembers(groupIdent) ;
			}
		//
		// If the Group is not local.
		else {
			//
			// Fail : Unknown Group
			// Pass the request on to the remote service ??
			if (DEBUG_FLAG) System.out.println("FAIL : Group is remote") ;
			}

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return array;
		}

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
	 * Request a list of PolicyPermissions for a resource.
	 *
	public Object[] getPermissionList(String resource)
		throws RemoteException;
	 */


	}



