/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/Attic/AccountManager.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/06 20:10:07 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: AccountManager.java,v $
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
	 * Create a new Account.
	 * TODO Change this to only accept the account name.
	 *
	 */
	public AccountData addAccount(AccountData account)
		throws RemoteException ;

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
	public boolean delAccount(String ident)
		throws RemoteException ;

	/**
	 * Request a list of Accounts.
	 *
	 */
	public Object[] getAccountList()
		throws RemoteException ;

	}
