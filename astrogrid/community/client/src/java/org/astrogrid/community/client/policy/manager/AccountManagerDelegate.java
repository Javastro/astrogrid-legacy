/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/policy/manager/AccountManagerDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/15 07:49:30 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: AccountManagerDelegate.java,v $
 *   Revision 1.4  2004/03/15 07:49:30  dave
 *   Merged development branch, dave-dev-200403121536, into HEAD
 *
 *   Revision 1.3.12.2  2004/03/13 17:57:20  dave
 *   Remove RemoteException(s) from delegate interfaces.
 *   Protected internal API methods.
 *
 *   Revision 1.3.12.1  2004/03/13 16:08:08  dave
 *   Added CommunityAccountResolver and CommunityEndpointResolver.
 *
 *   Revision 1.3  2004/03/08 13:42:33  dave
 *   Updated Maven goals.
 *   Replaced tabs with Spaces.
 *
 *   Revision 1.2.2.1  2004/03/08 12:53:17  dave
 *   Changed tabs to spaces
 *
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

import org.astrogrid.community.common.policy.manager.AccountManager ;

import org.astrogrid.community.common.service.data.ServiceStatusData ;

/**
 * Interface for our AccountManager delegate.
 * This extends the AccountManager interface, without the RemoteExceptions.
 *
 */
public interface AccountManagerDelegate
//    extends AccountManager
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
    public AccountData addAccount(String ident) ;

    /**
     * Request an Account data, given the Account ident.
     * This will return null if you request details for a non-existent Account.
     * @param ident The Account ident.
     * @return A valid AccountData if the Account exists, null if the Account does not exist.
     * @TODO Add Exceptions.
     *
     */
    public AccountData getAccount(String ident) ;

    /**
     * Update an Account data.
     * This will return null if you try to set the details for an non-existent Account.
     * @param account The new AccountData.
     * @return A valid AccountData if the update succeded, null if the update failed.
     * @TODO Add Exceptions.
     *
     */
    public AccountData setAccount(AccountData account) ;

    /**
     * Delete an Account, given the Account ident.
     * This will return null if you try to delete a non-existent Account.
     * @param ident The Account ident.
     * @return A valid AccountData for the old Account if the delete succeded, null if the delete failed.
     * @TODO Add Exceptions.
     *
     */
    public AccountData delAccount(String ident) ;

    /**
     * Request a list of local Accounts.
     * @return An array of AccountData objects, one for each Account in the database.
     * @TODO Add Exceptions.
     *
     */
    public Object[] getLocalAccounts() ;

    }
