/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/Attic/GroupManager.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/10 17:21:43 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: GroupManager.java,v $
 *   Revision 1.5  2003/09/10 17:21:43  dave
 *   Added remote functionality to groups.
 *
 *   Revision 1.4  2003/09/09 13:48:09  dave
 *   Added addGroupMember - only local accounts and groups to start with.
 *
 *   Revision 1.3  2003/09/08 20:28:50  dave
 *   Added CommunityIdent, with isLocal() and isValid()
 *
 *   Revision 1.2  2003/09/08 11:01:35  KevinBenson
 *   A check in of the Authentication authenticateToken roughdraft and some changes to the groudata and community data
 *   along with an AdministrationDelegate
 *
 *   Revision 1.1  2003/09/06 20:10:07  dave
 *   Split PolicyManager into separate components.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.server ;

import java.rmi.Remote ;
import java.rmi.RemoteException ;

import org.astrogrid.community.policy.data.GroupData ;
import java.util.ArrayList;

public interface GroupManager
	extends java.rmi.Remote
	{
	/**
	 * Create a new Group, given the Group name.
	 *
	 */
	public GroupData addGroup(String name)
		throws RemoteException ;

	/**
	 * Request an Group data, given the Group name.
	 *
	 */
	public GroupData getGroup(String name)
		throws RemoteException ;

	/**
	 * Update an Group data.
	 *
	 */
	public GroupData setGroup(GroupData account)
		throws RemoteException ;

	/**
	 * Delete an Group, given the Group name.
	 *
	 */
	public GroupData delGroup(String name)
		throws RemoteException ;

	/**
	 * Request a list of local Groups.
	 *
	 */
	public Object[] getLocalGroups()
		throws RemoteException ;

	/**
	 *
	 *
	 */
	public Object[] getAccountGroupList(String account)
		throws RemoteException;

	}
