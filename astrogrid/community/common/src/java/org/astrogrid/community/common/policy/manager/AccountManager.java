/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/AccountManager.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/02/12 08:12:13 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: AccountManager.java,v $
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
=======
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
>>>>>>> 1.2.4.5
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

public interface AccountManager
    extends Remote
    {
    /**
     * Create a new Account, given the Account name.
     *
     */
    public AccountData addAccount(String name)
        throws RemoteException ;
      
    /**
     * Request a password for an account
     * Removed for refactoring.
	 *
	public String getPassword(String name) throws RemoteException;
	 *
	 */

    /**
     * Update an Account password.
     *
     * Removed for refactoring.
	 *
    public AccountData setPassword(String account, String password)
        throws RemoteException ;
	 *
	 */

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
