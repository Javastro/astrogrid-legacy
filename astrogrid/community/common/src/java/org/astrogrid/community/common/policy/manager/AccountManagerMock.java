/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/AccountManagerMock.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/08 13:42:33 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: AccountManagerMock.java,v $
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
 *   Revision 1.1.2.4  2004/03/05 14:03:23  dave
 *   Added first client side SOAP test - SecurityServiceSoapDelegateTestCase
 *
 *   Revision 1.1.2.3  2004/03/04 13:26:17  dave
 *   1) Added Delegate interfaces.
 *   2) Added Mock implementations.
 *   3) Added MockDelegates
 *   4) Added SoapDelegates
 *
 *   Revision 1.1.2.2  2004/02/26 14:32:59  dave
 *   Added AccountManagerTest and initial AccountManagerTestCase.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.manager ;

import java.util.Map ;
import java.util.HashMap ;

import org.astrogrid.community.common.policy.data.AccountData ;
import org.astrogrid.community.common.service.CommunityServiceMock ;

/**
 * Mock implementation of our AccountManager service.
 *
 */
public class AccountManagerMock
    extends CommunityServiceMock
    implements AccountManager
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Public constructor.
     *
     */
    public AccountManagerMock()
        {
        super() ;
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerMock()") ;
        }

    /**
     * Our hash table of Accounts.
     *
     */
    private Map map = new HashMap() ;

    /**
     * Create a new Account.
     * @param ident - The Account identifier.
     *
     */
    public AccountData addAccount(String ident)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerMock.addAccount()") ;
        if (DEBUG_FLAG) System.out.println("  Ident : " + ident) ;
        //
        // Check for null ident.
        if (null == ident)
            {
            // TODO
            // Throw an exception ?
            //
            // Return null for now.
            return null ;
            }
        //
        // Check if we already have an existing Account.
        if (null != this.getAccount(ident))
            {
            // TODO
            // Throw a duplicate exception ?
            //
            // Return null for now.
            return null ;
            }
        //
        // Create a new Account.
        AccountData account = new AccountData(ident) ;
        //
        // Add it to our map.
        map.put(account.getIdent(), account) ;
        //
        // Return the new Account.
        return account ;
        }

    /**
     * Request a specific Account.
     * @param ident - The Account identifier.
     *
     */
    public AccountData getAccount(String ident)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerMock.getAccount()") ;
        if (DEBUG_FLAG) System.out.println("  Ident : " + ident) ;
        //
        // Check for null ident.
        if (null == ident)
            {
            // TODO
            // Throw an exception ?
            //
            // Return null for now.
            return null ;
            }
        //
        // Lookup the Account in our map.
        return (AccountData) map.get(ident) ;
        }

    /**
     * Update an Account.
     * @param account - The updated Account data to store.
     *
     */
    public AccountData setAccount(AccountData account)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerMock.setAccount()") ;
        if (DEBUG_FLAG) System.out.println("  Account : " + account) ;
        //
        // Check for null param.
        if (null == account)
            {
            //
            // Throw an exception ?
            //
            // Return null for now.
            return null ;
            }
        //
        // If we don't have an existing Account.
        if (null == this.getAccount(account.getIdent()))
            {
            //
            // Throw an exception ?
            //
            // Return null for now.
            return null ;
            }
        //
        // Replace the existing Account with the new data.
        map.put(account.getIdent(), account) ;
        //
        // Return the new account.
        return account ;
        }

    /**
     * Delete an Account.
     * @param ident - The Account identifier.
     *
     */
    public AccountData delAccount(String ident)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerMock.delAccount()") ;
        if (DEBUG_FLAG) System.out.println("  Ident : " + ident) ;
        //
        // Check for null ident.
        if (null == ident)
            {
            //
            // Throw an exception ?
            //
            // Return null for now.
            return null ;
            }
        //
        // Try to find the Account.
        AccountData account = this.getAccount(ident) ;
        //
        // If we didn't find a matching Account.
        if (null == account)
            {
            //
            // Throw an exception ?
            //
            // Return null for now.
            return null ;
            }
        //
        // Remove the Account from our map.
        map.remove(account.getIdent()) ;
        //
        // Return the old Account.
        return account ;
        }

    /**
     * Request a list of local Accounts.
     *
     */
    public Object[] getLocalAccounts()
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerMock.getLocalAccounts()") ;
        return map.values().toArray() ;
        }
    }
