/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/Attic/AccountManager.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/10 17:21:43 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: AccountManager.java,v $
 *   Revision 1.4  2003/09/10 17:21:43  dave
 *   Added remote functionality to groups.
 *
 *   Revision 1.3  2003/09/10 06:03:27  dave
 *   Added remote capability to Accounts
 *
 *   Revision 1.2  2003/09/08 20:28:50  dave
 *   Added CommunityIdent, with isLocal() and isValid()
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

import org.astrogrid.community.policy.data.AccountData ;

public interface AccountManager
	extends java.rmi.Remote
	{
	/**
	 * Create a new Account, given the Account name.
	 *
	 */
	public AccountData addAccount(String name)
		throws RemoteException ;

	/**
	 * Request an Account data, given the Account name.
	 *
	 */
	public AccountData getAccount(String name)
		throws RemoteException ;

	/**
	 * Update an Account data.
	 *
	 */
	public AccountData setAccount(AccountData account)
		throws RemoteException ;

	/**
	 * Delete an Account, given the Account name.
	 *
	 */
	public AccountData delAccount(String name)
		throws RemoteException ;

	/**
	 * Request a list of local Accounts.
	 *
	 */
	public Object[] getLocalAccounts()
		throws RemoteException ;

	}
