/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/Attic/PolicyManager.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/04 23:33:05 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyManager.java,v $
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
import org.astrogrid.community.policy.data.AccountData ;
import org.astrogrid.community.policy.data.PolicyPermission  ;
import org.astrogrid.community.policy.data.PolicyCredentials ;

public interface PolicyManager
	extends java.rmi.Remote
	{

	/**
	 * Service health check.
	 *
	 */
	public ServiceData getServiceStatus()
		throws RemoteException ;

	/**
	 * Create a new Account.
	 * TODO Change this to only accept the account name.
	 *
	 */
	public AccountData addAccount(AccountData account)
		throws Exception ;

	/**
	 * Request an Account details.
	 *
	 */
	public AccountData getAccount(String ident)
		throws RemoteException ;

	/**
	 * Update an Account details.
	 *
	 */
	public AccountData setAccount(AccountData account)
		throws RemoteException ;

	/**
	 * Delete an Account.
	 *
	 */
	public void delAccount(String ident)
		throws RemoteException ;

	/**
	 * Request a list of Accounts.
	 *
	 */
	public Object[] getAccountList()
		throws RemoteException ;

	}
