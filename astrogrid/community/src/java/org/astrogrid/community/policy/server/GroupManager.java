/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/Attic/GroupManager.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/06 20:10:07 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: GroupManager.java,v $
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

public interface GroupManager
	extends java.rmi.Remote
	{
	/**
	 * Create a new Group.
	 * TODO Change this to only accept the account name.
	 *
	 */
	public GroupData addGroup(GroupData account)
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
		throws RemoteException ;

	}
