/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/Attic/CommunityManager.java,v $</cvs:source>
 * <cvs:author>$Author: KevinBenson $</cvs:author>
 * <cvs:date>$Date: 2003/09/08 11:01:35 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityManager.java,v $
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
import java.util.ArrayList;

import org.astrogrid.community.policy.data.CommunityData ;

public interface CommunityManager
	extends java.rmi.Remote
	{
	/**
	 * Create a new Community.
	 * TODO Change this to only accept the community name.
	 *
	 */
	public CommunityData addCommunity(CommunityData community)
		throws RemoteException ;

	/**
	 * Request an Community details.
	 *
	 */
	public CommunityData getCommunity(String ident)
		throws RemoteException ;

	/**
	 * Update an Community details.
	 *
	 */
	public CommunityData setCommunity(CommunityData community)
		throws RemoteException ;

	/**
	 * Delete an Community.
	 *
	 */
	public boolean delCommunity(String ident)
		throws RemoteException ;

	/**
	 * Request a list of Communitys.
	 *
	 */
	public Object[] getCommunityList()
		throws RemoteException ;

	}
