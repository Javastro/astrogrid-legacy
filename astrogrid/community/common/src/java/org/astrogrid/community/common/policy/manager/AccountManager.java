/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/AccountManager.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/12 15:22:17 $</cvs:date>
 * <cvs:version>$Revision: 1.7 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: AccountManager.java,v $
 *   Revision 1.7  2004/03/12 15:22:17  dave
 *   Merged development branch, dave-dev-200403101018, into HEAD
 *
 *   Revision 1.6.12.1  2004/03/10 13:32:00  dave
 *   Added home space to AccountData.
 *   Improved null param checking in AccountManager.
 *   Improved null param checking in AccountManager tests.
 *
 *   Revision 1.6  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.5.2.1  2004/02/26 13:10:28  dave
 *   Added AccountManagerMock
 *
 *   Revision 1.5  2004/02/20 21:11:05  dave
 *   Merged development branch, dave-dev-200402120832, into HEAD
 *
 *   Revision 1.4.2.2  2004/02/19 21:09:26  dave
 *   Refactored ServiceStatusData into a common package.
 *   Refactored CommunityServiceImpl constructor to take a parent service.
 *   Refactored default database for CommunityServiceImpl
 *
 *   Revision 1.4.2.1  2004/02/16 15:20:54  dave
 *   Changed tabs to spaces
 *
 *   Revision 1.4  2004/02/12 08:12:13  dave
 *   Merged development branch, dave-dev-200401131047, into HEAD
 *
 *   Revision 1.3  2004/02/12 06:56:45  dave
 *   Merged development branch, dave-dev-200401131047, into HEAD
 *
 *   Revision 1.2.4.5  2004/02/06 16:19:04  dave
 *   Replaced import java.rmi.Remote
 *   Replaced import java.rmi.RemoteException
 *
 *   Revision 1.2.4.4  2004/02/06 16:15:49  dave
 *   Removed import java.rmi.RemoteException
 *
 *   Revision 1.2.4.3  2004/02/06 16:14:17  dave
 *   Removed import java.rmi.Remote
 *
 *   Revision 1.2.4.2  2004/02/06 16:06:05  dave
 *   Commented out Remote import
 *
 *   Revision 1.2.4.1  2004/01/17 13:54:18  dave
 *   Removed password from AccountData
 *
 *   Revision 1.2  2004/01/07 10:45:38  dave
 *   Merged development branch, dave-dev-20031224, back into HEAD
 *
 *   Revision 1.1.2.2  2004/01/05 06:47:18  dave
 *   Moved policy data classes into policy.data package
 *
 *   Revision 1.1.2.1  2003/12/24 05:54:48  dave
 *   Initial Maven friendly structure (only part of the service implemented)
 *
 *   Revision 1.7  2003/11/06 15:35:26  dave
 *   Replaced tabs with spaces
 *
 *   Revision 1.6  2003/09/24 21:56:06  dave
 *   Added setPassword() to AccountManager
 *
 *   Revision 1.5  2003/09/15 16:05:45  KevinBenson
 *   *** empty log message ***
 *
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
package org.astrogrid.community.common.policy.manager ;

import java.rmi.Remote ;
import java.rmi.RemoteException ;

import org.astrogrid.community.common.policy.data.AccountData ;

import org.astrogrid.community.common.service.CommunityService ;

public interface AccountManager
    extends Remote, CommunityService
    {
    /**
     * Create a new Account, given the Account ident.
	 * This will return null if you try to create an Account with the same ident as an existing Account.
     * @param ident The Account ident.
	 * @return A valid AccountData if the Account was created, null if the Account was not created.
     * @TODO Add Exceptions.
     * @TODO Add syntax checking.
     *
     */
    public AccountData addAccount(String ident)
        throws RemoteException ;
      
    /**
     * Request an Account data, given the Account ident.
	 * This will return null if you request details for a non-existent Account.
     * @param ident The Account ident.
	 * @return A valid AccountData if the Account exists, null if the Account does not exist.
     * @TODO Add Exceptions.
     *
     */
    public AccountData getAccount(String ident)
        throws RemoteException ;

    /**
     * Update an Account data.
	 * This will return null if you try to set the details for an non-existent Account.
     * @param account The new AccountData.
	 * @return A valid AccountData if the update succeded, null if the update failed.
     * @TODO Add Exceptions.
     *
     */
    public AccountData setAccount(AccountData account)
        throws RemoteException ;

    /**
     * Delete an Account, given the Account ident.
	 * This will return null if you try to delete a non-existent Account.
     * @param ident The Account ident.
	 * @return A valid AccountData for the old Account if the delete succeded, null if the delete failed.
     * @TODO Add Exceptions.
     *
     */
    public AccountData delAccount(String ident)
        throws RemoteException ;

    /**
     * Request a list of local Accounts.
	 * @return An array of AccountData objects, one for each Account in the database.
     * @TODO Add Exceptions.
     *
     */
    public Object[] getLocalAccounts()
        throws RemoteException ;

    }
