/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/Attic/PolicyServiceImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/12 12:59:17 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyServiceImpl.java,v $
 *   Revision 1.4  2003/09/12 12:59:17  dave
 *   1) Fixed RemoteException handling in the manager and service implementations.
 *
 *   Revision 1.3  2003/09/11 03:15:06  dave
 *   1) Implemented PolicyService internals - no tests yet.
 *   2) Added getLocalAccountGroups and getRemoteAccountGroups to PolicyManager.
 *   3) Added remote access to groups.
 *
 *   Revision 1.2  2003/09/08 20:28:50  dave
 *   Added CommunityIdent, with isLocal() and isValid()
 *
 *   Revision 1.1  2003/09/03 15:23:33  dave
 *   Split API into two services, PolicyService and PolicyManager
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.server ;

import java.rmi.RemoteException ;

import org.astrogrid.community.policy.data.ServiceData ;
import org.astrogrid.community.policy.data.CommunityIdent ;
import org.astrogrid.community.policy.data.CommunityConfig ;
import org.astrogrid.community.policy.data.GroupMemberData ;
import org.astrogrid.community.policy.data.PolicyPermission  ;
import org.astrogrid.community.policy.data.PolicyCredentials ;

public class PolicyServiceImpl
	implements PolicyService
	{
	/**
	 * Switch for our debug statements.
	 *
	 */
	protected static final boolean DEBUG_FLAG = true ;

	/**
	 * Our DatabaseManager.
	 *
	 */
	private DatabaseManagerImpl databaseManager ;

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
	 * Our PermissionManager
	 *
	 */
	private PermissionManagerImpl permissionManager;

	/**
	 * Public constructor.
	 *
	 */
	public PolicyServiceImpl()
		{
		this.init() ;
		}

	/**
	 * Initialise our service.
	 *
	 */
	protected void init()
		{
		//
		// Initialise our configuration.
		CommunityConfig.setConfig(new CommunityConfigImpl()) ;
		//
		// Initialise our DatabaseManager.
		databaseManager = new DatabaseManagerImpl() ;
		//
		// Initialise our GroupManager.
		groupManager = new GroupManagerImpl() ;
		groupManager.init(databaseManager) ;
		//
		// Initialise our CommunityManager.
		communityManager = new CommunityManagerImpl() ;
		communityManager.init(databaseManager) ;
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
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("PolicyServiceImpl.getServiceStatus()") ;

		ServiceData result =  new ServiceData() ;
		result.setIdent(CommunityConfig.getConfig().getCommunityName()) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return result ;
		}

	/**
	 * Confirm access permissions
	 *
	 */
	public PolicyPermission checkPermissions(PolicyCredentials credentials, String resource, String action)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("PolicyServiceImpl.checkPermissions()") ;

		String group   = credentials.getGroup() ;
		String account = credentials.getAccount() ;

		if (DEBUG_FLAG) System.out.println("  Credentials") ;
		if (DEBUG_FLAG) System.out.println("    Group   : " + group)   ;
		if (DEBUG_FLAG) System.out.println("    Account : " + account) ;
		if (DEBUG_FLAG) System.out.println("  Resource") ;
		if (DEBUG_FLAG) System.out.println("    Name    : " + resource) ;
		if (DEBUG_FLAG) System.out.println("    Action  : " + action)   ;

		//
		// Check to see if the group has permission for the action.
		PolicyPermission permission = permissionManager.getPermission(resource, group, action) ;
		//
		// If we got a result.
		if (null != permission)
			{
			if (DEBUG_FLAG) System.out.println("PASS : Permission found") ;
			//
			// If the permission is valid.
			if (permission.isValid())
				{
				if (DEBUG_FLAG) System.out.println("PASS : Permission is valid") ;
				//
				// Check the credentials.
				PolicyCredentials checked = checkMembership(credentials) ;
				//
				// If the credentials are valid.
				if (checked.isValid())
					{
					if (DEBUG_FLAG) System.out.println("PASS : Credentials are valid") ;
					}
				//
				// If the credentials are not valid.
				else {
					if (DEBUG_FLAG) System.out.println("FAIL : Credentials not valid") ;
					permission.setStatus(PolicyPermission.STATUS_CREDENTIALS_INVALID) ;
					permission.setReason(PolicyPermission.REASON_CREDENTIALS_INVALID) ;
					}
				}
			//
			// If the permission is not granted.
			else {
				if (DEBUG_FLAG) System.out.println("FAIL : Permission not valid") ;
				}
			}
		//
		// If we didn't get a result.
		else {
			if (DEBUG_FLAG) System.out.println("FAIL : No permission found") ;
			//
			// Create a dummy permission.
			permission = new PolicyPermission(resource, group, action) ;
			permission.setStatus(PolicyPermission.STATUS_PERMISSION_UNKNOWN) ;
			permission.setReason("Permission not found") ;
			}
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return permission ;
		}

	/**
	 * Confirm group membership.
	 *
	 */
	public PolicyCredentials checkMembership(PolicyCredentials credentials)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("PolicyServiceImpl.checkMembership()") ;

		//
		// Set the status to unknown.
		credentials.setStatus(PolicyCredentials.STATUS_NOT_KNOWN) ;
		credentials.setReason("No reason given") ;

		//
		// Get CommunityIdents for the account and group.
		CommunityIdent group   = new CommunityIdent(credentials.getGroup()) ;
		CommunityIdent account = new CommunityIdent(credentials.getAccount()) ;

		if (DEBUG_FLAG) System.out.println("  Credentials") ;
		if (DEBUG_FLAG) System.out.println("    Group   : " + group) ;
		if (DEBUG_FLAG) System.out.println("    Account : " + account) ;
		//
		// If the group is local.
		if (group.isLocal())
			{
			if (DEBUG_FLAG) System.out.println("PASS : Group is local") ;
			//
			// See if there is a membership record.
			GroupMemberData membership = groupManager.getGroupMember(account, group) ;
			//
			// If there is a membership record.
			if (null != membership)
				{
				if (DEBUG_FLAG) System.out.println("PASS : Account is a member of Group") ;
				//
				// Update the credentials.
				credentials.setStatus(PolicyCredentials.STATUS_VALID) ;
				credentials.setReason("Account IS a member of Group") ;
				}
			//
			// If there is no membership record.
			else {
				if (DEBUG_FLAG) System.out.println("FAIL : Account is not a member of Group") ;
				//
				// Update the credentials.
				credentials.setStatus(PolicyCredentials.STATUS_NOT_VALID) ;
				credentials.setReason("Account is NOT a member of Group") ;
				}
			}
		//
		// If the group is not local.
		else {
			if (DEBUG_FLAG) System.out.println("PASS : Group is remote") ;
			//
			// Get a service for the remote community.
			PolicyService remote = communityManager.getPolicyService(group.getCommunity()) ;
			//
			// If we got a remote service.
			if (null != remote)
				{
				if (DEBUG_FLAG) System.out.println("PASS : Found remote service") ;
				//
				// Try asking the remote manager.
				PolicyCredentials result = null ;
				try {
					result = remote.checkMembership(credentials) ;
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
					if (DEBUG_FLAG) System.out.println("PASS : Remote service responded") ;
					//
					// If the result is valid.
					if (result.isValid())
						{
						if (DEBUG_FLAG) System.out.println("PASS : Remote response is valid") ;
						//
						// Update the credentials.
						credentials.setStatus(result.getStatus()) ;
						credentials.setReason(result.getReason()) ;
						}
					//
					// If the result is not valid.
					else {
						if (DEBUG_FLAG) System.out.println("FAIL : Remote response is not valid") ;
						//
						// Update the credentials.
						credentials.setStatus(result.getStatus()) ;
						credentials.setReason(result.getReason()) ;
						}
					}
				//
				// If we didn't get a result.
				else {
					if (DEBUG_FLAG) System.out.println("PASS : Remote service returned null") ;
					//
					// Update the credentials.
					credentials.setStatus(PolicyCredentials.STATUS_NOT_VALID) ;
					credentials.setReason("No response from community service") ;
					}
				}
			//
			// If we didn't get a remote service.
			else {
				if (DEBUG_FLAG) System.out.println("FAIL : Unknown remote service") ;
				//
				// Update the credentials.
				credentials.setStatus(PolicyCredentials.STATUS_NOT_VALID) ;
				credentials.setReason("Unknown community service") ;
				}
			}

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		return credentials ;
		}
	}
