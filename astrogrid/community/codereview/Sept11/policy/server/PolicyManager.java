/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/codereview/Sept11/policy/server/Attic/PolicyManager.java,v $</cvs:source>
 * <cvs:author>$Author: KevinBenson $</cvs:author>
 * <cvs:date>$Date: 2003/09/11 10:24:21 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyManager.java,v $
 *   Revision 1.1  2003/09/11 10:24:21  KevinBenson
 *   *** empty log message ***
 *
 *   Revision 1.13  2003/09/11 03:15:06  dave
 *   1) Implemented PolicyService internals - no tests yet.
 *   2) Added getLocalAccountGroups and getRemoteAccountGroups to PolicyManager.
 *   3) Added remote access to groups.
 *
 *   Revision 1.12  2003/09/10 17:21:43  dave
 *   Added remote functionality to groups.
 *
 *   Revision 1.11  2003/09/10 06:03:27  dave
 *   Added remote capability to Accounts
 *
 *   Revision 1.10  2003/09/10 02:56:03  dave
 *   Added PermissionManager and tests
 *
 *   Revision 1.9  2003/09/10 00:08:45  dave
 *   Added getGroupMembers, ResourceIdent and JUnit tests for ResourceManager
 *
 *   Revision 1.8  2003/09/09 19:13:32  KevinBenson
 *   New resource managerr stuff
 *
 *   Revision 1.7  2003/09/09 14:51:47  dave
 *   Added delGroupMember - only local accounts and groups to start with.
 *
 *   Revision 1.6  2003/09/09 13:48:09  dave
 *   Added addGroupMember - only local accounts and groups to start with.
 *
 *   Revision 1.5  2003/09/08 20:28:50  dave
 *   Added CommunityIdent, with isLocal() and isValid()
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

import java.rmi.Remote ;
import java.rmi.RemoteException ;

import org.astrogrid.community.policy.data.ServiceData ;
import org.astrogrid.community.policy.data.GroupMemberData ;

/**
 * Interface for our PolicyManager service.
 *
 */
public interface PolicyManager
	extends java.rmi.Remote, AccountManager, GroupManager, CommunityManager, ResourceManager, PermissionManager
	{

	/**
	 * Service health check.
	 *
	 */
	public ServiceData getServiceStatus()
		throws RemoteException ;

	/**
	 * Request a list of local Accounts.
	 *
	 */
	public Object[] getLocalAccounts()
		throws RemoteException ;

	/**
	 * Request a list of Accounts, given a remote Community name.
	 *
	 */
	public Object[] getRemoteAccounts(String community)
		throws RemoteException ;

	/**
	 * Request a list of local Groups.
	 *
	 */
	public Object[] getLocalGroups()
		throws RemoteException ;

	/**
	 * Request a list of Groups, given a remote Community name.
	 *
	 */
	public Object[] getRemoteGroups(String community)
		throws RemoteException ;

	/**
	 * Add an Account to a Group, given the Account and Group names.
	 *
	 */
	public GroupMemberData addGroupMember(String account, String group)
		throws RemoteException;

	/**
	 * Remove an Account from a Group, given the Account and Group names.
	 *
	 */
	public GroupMemberData delGroupMember(String account, String group)
		throws RemoteException;

	/**
	 * Get a list of Group members, given the Group name.
	 *
	 */
	public Object[] getGroupMembers(String group)
		throws RemoteException;

	/**
	 * Get a list of local Groups that an Account belongs to, given the Account name.
	 *
	 */
	public Object[] getLocalAccountGroups(String account)
		throws RemoteException;

	/**
	 * Get a list of remote Groups that an Account belongs to, given the Account and Community names.
	 *
	 */
	public Object[] getRemoteAccountGroups(String account, String community)
		throws RemoteException;

	}
