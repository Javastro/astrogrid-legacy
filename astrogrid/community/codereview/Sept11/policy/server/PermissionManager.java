/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/codereview/Sept11/policy/server/Attic/PermissionManager.java,v $</cvs:source>
 * <cvs:author>$Author: KevinBenson $</cvs:author>
 * <cvs:date>$Date: 2003/09/11 10:24:21 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PermissionManager.java,v $
 *   Revision 1.1  2003/09/11 10:24:21  KevinBenson
 *   *** empty log message ***
 *
 *   Revision 1.1  2003/09/10 02:56:03  dave
 *   Added PermissionManager and tests
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.server ;

import java.rmi.Remote ;
import java.rmi.RemoteException ;

import org.astrogrid.community.policy.data.PolicyPermission ;

public interface PermissionManager
	extends java.rmi.Remote
	{
	/**
	 * Create a new PolicyPermission.
	 *
	 */
	public PolicyPermission addPermission(String resource, String group, String action)
		throws RemoteException ;

	/**
	 * Request a PolicyPermission.
	 *
	 */
	public PolicyPermission getPermission(String resource, String group, String action)
		throws RemoteException ;

	/**
	 * Update a PolicyPermission.
	 *
	 */
	public PolicyPermission setPermission(PolicyPermission permission)
		throws RemoteException ;

	/**
	 * Delete a PolicyPermission.
	 *
	 */
	public boolean delPermission(String resource, String group, String action)
		throws RemoteException ;

	/**
	 * Request a list of PolicyPermissions for a resource.
	 *
	public Object[] getPermissionList(String resource)
		throws RemoteException;
	 */

	}
