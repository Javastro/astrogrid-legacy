/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/Attic/GroupManager.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/09 13:48:09 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: GroupManager.java,v $
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
	 * Create a new Group.
	 *
	 */
	public GroupData addGroup(String ident)
		throws RemoteException ;

	/**
	 * Request an Group details.
	 *
	 */
	public GroupData getGroup(String ident)
		throws RemoteException ;

	/**
	 * Update an Group details.
	 *
	 */
	public GroupData setGroup(GroupData account)
		throws RemoteException ;

	/**
	 * Delete an Group.
	 *
	 */
	public boolean delGroup(String ident)
		throws RemoteException ;

	/**
	 * Request a list of Groups.
	 *
	 */
	public Object[] getGroupList()
		throws RemoteException;

	/**
	 * Comment ??
	 *
	 */
	public Object[] getAccountGroupList(String account)
		throws RemoteException;

	}
