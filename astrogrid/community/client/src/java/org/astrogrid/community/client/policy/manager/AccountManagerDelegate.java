/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/policy/manager/AccountManagerDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/05 17:19:59 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: AccountManagerDelegate.java,v $
 *   Revision 1.2  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
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

import org.astrogrid.community.common.policy.data.AccountData ;
//import org.astrogrid.community.common.policy.data.GroupData ;
//import org.astrogrid.community.common.policy.data.CommunityData ;
//import org.astrogrid.community.common.policy.data.ResourceData ;
//import org.astrogrid.community.common.policy.data.PolicyPermission ;
//import org.astrogrid.community.common.policy.data.GroupMemberData ;

import org.astrogrid.community.common.policy.manager.AccountManager ;

import org.astrogrid.community.common.service.data.ServiceStatusData ;

/**
 * Interface for our AccountManager delegate.
 * This extends the AccountManager interface, without the RemoteExceptions.
 *
 */
public interface AccountManagerDelegate
    extends AccountManager
    {

	}
