/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/AccountManagerMock.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.8 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: AccountManagerMock.java,v $
 *   Revision 1.8  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.7.36.3  2004/06/17 14:50:01  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.7.36.2  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.manager ;

import java.util.Map ;
import java.util.HashMap ;

import org.astrogrid.community.common.exception.CommunityPolicyException ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

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
     * @todo Refactor to use the common logging.
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
     * Our hash table of values.
     *
     */
    private static Map map = new HashMap() ;

    /**
     * Reset our map.
     *
     */
    public static void reset()
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerMock.reset()") ;
        map.clear() ;
        }

    /**
     * Add a new Account, given the Account ident.
     * @param  ident The Account identifier.
     * @return An AccountData for the Account.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is already in the database.
     *
     */
    public AccountData addAccount(String ident)
        throws CommunityIdentifierException, CommunityPolicyException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerMock.addAccount()") ;
        if (DEBUG_FLAG) System.out.println("  Ident : " + ident) ;
        //
        // Check for null ident.
        if (null == ident)
            {
            throw new CommunityIdentifierException(
                "Null identifier"
                ) ;
            }
        //
        // Check if we already have an existing Account.
        if (map.containsKey(ident))
            {
            throw new CommunityPolicyException(
                "Duplicate account",
                ident
                ) ;
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
     * Add a new Account, given the Account data.
     * @param  data The AccountData to add.
     * @return A new AccountData for the Account.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is already in the database.
     *
     */
    public AccountData addAccount(AccountData account)
        throws CommunityIdentifierException, CommunityPolicyException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerMock.addAccount()") ;
        if (DEBUG_FLAG) System.out.println("  Account : " + ((null != account) ? account.getIdent() : null)) ;
        //
        // Check for null account.
        if (null == account)
            {
            throw new CommunityIdentifierException(
                "Null account"
                ) ;
            }
        //
        // Check for null ident.
        if (null == account.getIdent())
            {
            throw new CommunityIdentifierException(
                "Null identifier"
                ) ;
            }
        //
        // Check if we already have an existing Account.
        if (map.containsKey(account.getIdent()))
            {
            throw new CommunityPolicyException(
                "Duplicate account",
                account.getIdent()
                ) ;
            }
        //
        // Add it to our map.
        map.put(account.getIdent(), account) ;
        //
        // Return the new Account.
        return account ;
        }

    /**
     * Request an Account details, given the Account ident.
     * @param  ident The Account identifier.
     * @return An AccountData for the Account.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     *
     */
    public AccountData getAccount(String ident)
        throws CommunityIdentifierException, CommunityPolicyException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerMock.getAccount()") ;
        if (DEBUG_FLAG) System.out.println("  Ident : " + ident) ;
        //
        // Check for null ident.
        if (null == ident)
            {
            throw new CommunityIdentifierException(
                "Null identifier"
                ) ;
            }
        //
        // Lookup the Account in our map.
        AccountData account = (AccountData) map.get(ident) ;
        //
        // If we found an Account.
        if (null != account)
            {
            return account ;
            }
        //
        // If we didn't find an Account.
        else {
            throw new CommunityPolicyException(
                "Account not found",
                ident
                ) ;
            }
        }

    /**
     * Update an Account.
     * @param  update The new Account data to update.
     * @return A new AccountData for the Account.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     *
     */
    public AccountData setAccount(AccountData update)
        throws CommunityIdentifierException, CommunityPolicyException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerMock.setAccount()") ;
        if (DEBUG_FLAG) System.out.println("  Ident : " + ((null != update) ? update.getIdent() : null)) ;
        //
        // Check for null data.
        if (null == update)
            {
            throw new CommunityIdentifierException(
                "Null inout data"
                ) ;
            }
        //
        // Check for null ident.
        if (null == update.getIdent())
            {
            throw new CommunityIdentifierException(
                "Null inout data"
                ) ;
            }
        //
        // Lookup the Account in our map.
        AccountData found = this.getAccount(update.getIdent()) ;
        //
        // Replace the existing Account with the new data.
        map.put(found.getIdent(), update) ;
        //
        // Return the new data.
        return update ;
        }

    /**
     * Delete an Account.
     * @param  ident The Account identifier.
     * @return The AccountData for the old Account.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     *
     */
    public AccountData delAccount(String ident)
        throws CommunityIdentifierException, CommunityPolicyException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerMock.delAccount()") ;
        if (DEBUG_FLAG) System.out.println("  Ident : " + ident) ;
        //
        // Check for null ident.
        if (null == ident)
            {
            throw new CommunityIdentifierException(
                "Null identifier"
                ) ;
            }
        //
        // Try to find the Account.
        AccountData account = this.getAccount(ident) ;
        //
        // If we found an Account.
        if (null != account)
            {
            //
            // Remove the Account from our map.
            map.remove(account.getIdent()) ;
            //
            // Return the old account.
            return account ;
            }
        //
        // If we didn't find an Account.
        else {
            throw new CommunityPolicyException(
                "Account not found",
                ident
                ) ;
            }
        }

    /**
     * Request a list of local Accounts.
     * @return An array of AccountData objects.
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
