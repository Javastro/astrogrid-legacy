/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/codereview/Sept11/policy/server/Attic/PolicyService.java,v $</cvs:source>
 * <cvs:author>$Author: KevinBenson $</cvs:author>
 * <cvs:date>$Date: 2003/09/11 10:24:21 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyService.java,v $
 *   Revision 1.1  2003/09/11 10:24:21  KevinBenson
 *   *** empty log message ***
 *
 *   Revision 1.3  2003/09/03 15:23:33  dave
 *   Split API into two services, PolicyService and PolicyManager
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.server ;

import java.rmi.Remote ;
import java.rmi.RemoteException ;

import org.astrogrid.community.policy.data.ServiceData  ;
import org.astrogrid.community.policy.data.PolicyPermission  ;
import org.astrogrid.community.policy.data.PolicyCredentials ;

public interface PolicyService
	extends java.rmi.Remote
	{

	/**
	 * Service health check.
	 *
	 */
	public ServiceData getServiceStatus()
		throws RemoteException ;

	/**
	 * Confirm access permissions.
	 *
	 */
	public PolicyPermission checkPermissions(PolicyCredentials credentials, String resource, String action)
		throws RemoteException ;

	/**
	 * Confirm group membership.
	 *
	 */
	public PolicyCredentials checkMembership(PolicyCredentials credentials)
		throws RemoteException ;

	}
