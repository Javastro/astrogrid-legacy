/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/AccountManager.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/19 14:43:14 $</cvs:date>
 * <cvs:version>$Revision: 1.9 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: AccountManager.java,v $
 *   Revision 1.9  2004/03/19 14:43:14  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.8.2.4  2004/03/19 04:51:46  dave
 *   Updated resolver with new Exceptions
 *
 *   Revision 1.8.2.3  2004/03/18 13:41:19  dave
 *   Added Exception handling to AccountManager
 *
 *   Revision 1.8.2.2  2004/03/17 13:50:23  dave
 *   Refactored Community exceptions
 *
 *   Revision 1.8.2.1  2004/03/17 01:08:48  dave
 *   Added AccountNotFoundException
 *   Added DuplicateAccountException
 *   Added InvalidIdentifierException
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.manager ;

import java.rmi.Remote ;
import java.rmi.RemoteException ;

import org.astrogrid.community.common.exception.CommunityPolicyException     ;
import org.astrogrid.community.common.exception.CommunityServiceException    ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

import org.astrogrid.community.common.policy.data.AccountData ;

import org.astrogrid.community.common.service.CommunityService ;

/**
 * Public interface for the AccountManager services.
 * @todo Add addAccount(AccountData)
 *
 */
public interface AccountManager
    extends Remote, CommunityService
    {
    /**
     * Add a new Account, given the Account ident.
     * @param  ident The Account identifier.
     * @return An AccountData for the Account.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is already in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the WebService call fails.
     *
     */
    public AccountData addAccount(String ident)
        throws RemoteException, CommunityServiceException, CommunityIdentifierException, CommunityPolicyException ;
      
    /**
     * Request an Account details, given the Account ident.
     * @param  ident The Account identifier.
     * @return An AccountData for the Account.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the WebService call fails.
     *
     */
    public AccountData getAccount(String ident)
        throws RemoteException, CommunityServiceException, CommunityIdentifierException, CommunityPolicyException ;

    /**
     * Update an Account.
     * @param  account The new AccountData to update.
     * @return A new AccountData for the Account.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the WebService call fails.
     *
     */
    public AccountData setAccount(AccountData account)
        throws RemoteException, CommunityServiceException, CommunityIdentifierException, CommunityPolicyException ;

    /**
     * Delete an Account.
     * @param  ident The Account identifier.
     * @return The AccountData for the old Account.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the WebService call fails.
     * @todo Need to have a mechanism for tidying up references to a remote Account
     * @todo Need to have a mechanism for tidying up references to an old Account
     *
     */
    public AccountData delAccount(String ident)
        throws RemoteException, CommunityServiceException, CommunityIdentifierException, CommunityPolicyException ;

    /**
     * Request a list of local Accounts.
     * @return An array of AccountData objects.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the WebService call fails.
     *
     */
    public Object[] getLocalAccounts()
        throws RemoteException, CommunityServiceException ;

    }
