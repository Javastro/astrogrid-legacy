/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/security/manager/Attic/SecurityManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.7 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityManagerImpl.java,v $
 *   Revision 1.7  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.6.32.2  2004/06/17 15:24:31  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.6.32.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.security.manager ;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.ObjectNotFoundException ;

import org.astrogrid.community.common.policy.data.AccountData ;

import org.astrogrid.community.server.security.data.PasswordData ;
import org.astrogrid.community.common.security.manager.SecurityManager ;

import org.astrogrid.community.common.ivorn.CommunityIvornParser ;

import org.astrogrid.community.server.service.CommunityServiceImpl ;
import org.astrogrid.community.server.database.configuration.DatabaseConfiguration ;

import org.astrogrid.community.common.exception.CommunityServiceException  ;
import org.astrogrid.community.common.exception.CommunitySecurityException ;
import org.astrogrid.community.common.exception.CommunityIdentifierException  ;

/**
 * Implementation of our SecurityManager service.
 *
 */
public class SecurityManagerImpl
    extends CommunityServiceImpl
    implements SecurityManager
    {
    /**
     * Switch for our debug statements.
     *
     */
    protected static final boolean DEBUG_FLAG = true ;

    /**
     * Public constructor, using default database configuration.
     *
     */
    public SecurityManagerImpl()
        {
        super() ;
        }

    /**
     * Public constructor, using specific database configuration.
     *
     */
    public SecurityManagerImpl(DatabaseConfiguration config)
        {
        super(config) ;
        }

    /**
     * Public constructor, using a parent service.
     *
     */
    public SecurityManagerImpl(CommunityServiceImpl parent)
        {
        super(parent) ;
        }

    /**
     * Set an Account password.
     * @param account  The account ident.
     * @param password The account password.
     * @return True if the password was set.
     * @throws CommunitySecurityException If the password change fails.
     * @throws CommunityServiceException If there is an internal error in service.
     * @throws CommunityIdentifierException If the account identifier is invalid.
     * @todo Check Account is local.
     *
     */
    public boolean setPassword(String account, String password)
        throws CommunityServiceException, CommunitySecurityException, CommunityIdentifierException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("SecurityManagerImpl.setPassword()") ;
        if (DEBUG_FLAG) System.out.println("  Account : " + account) ;
        if (DEBUG_FLAG) System.out.println("  Value   : " + password) ;
        //
        // Check for null account.
        if (null == account)
            {
            throw new CommunityIdentifierException(
                "Null account"
                ) ;
            }
        //
        // Check for null password.
        if (null == password)
            {
            throw new CommunityIdentifierException(
                "Null password"
                ) ;
            }
        //
        // Get the Account ident.
        CommunityIvornParser ident = new CommunityIvornParser(
            account
            ) ;
        //
        // Set the response to false.
        boolean result = false ;
        //
        // Try update the database.
        Database database = null ;
        try {
            //
            // Open our database connection.
            database = this.getDatabase() ;
            //
            // Begin a new database transaction.
            database.begin();
            //
            // Try loading the Account from the database.
// Do we need this ?
            AccountData check = (AccountData) database.load(AccountData.class, ident.getAccountIdent()) ;
            if (DEBUG_FLAG)System.out.println("  PASS : found account") ;
            //
            // Try loading the PasswordData.
            PasswordData data = null ;
            try {
                data = (PasswordData) database.load(PasswordData.class, ident.getAccountIdent()) ;
                }
            //
            // Don't worry if it isn't there.
            catch (ObjectNotFoundException ouch)
                {
                logExpectedException(ouch, "SecurityManagerImpl.setPassword()") ;
                }
            //
            // If we found the PasswordData.
            if (null != data)
                {
                if (DEBUG_FLAG)System.out.println("  PASS : found password") ;
                if (DEBUG_FLAG)System.out.println("    Account  : " + data.getAccount()) ;
                if (DEBUG_FLAG)System.out.println("    Password : " + data.getPassword()) ;
                //
                // Change the password value.
                data.setPassword(password) ;
                data.setEncryption(PasswordData.NO_ENCRYPTION) ;
                if (DEBUG_FLAG)System.out.println("  PASS : changed password") ;
                if (DEBUG_FLAG)System.out.println("    Account  : " + data.getAccount()) ;
                if (DEBUG_FLAG)System.out.println("    Password : " + data.getPassword()) ;
                }
            //
            // If we didn't find the password.
            else {
                if (DEBUG_FLAG)System.out.println("  PASS : missing password") ;
                //
                // Try to create a new PasswordData in the database.
                data = new PasswordData(ident.getAccountIdent(), password) ;
                database.create(data) ;
                if (DEBUG_FLAG)System.out.println("  PASS : created password") ;
                if (DEBUG_FLAG)System.out.println("    Account  : " + data.getAccount()) ;
                if (DEBUG_FLAG)System.out.println("    Password : " + data.getPassword()) ;
                }
            //
            // Commit the database transaction.
            database.commit() ;
            //
            // Set the response to true.
            result = true ;
            }
        //
        // If anything went bang.
        catch (Exception ouch)
            {
            //
            // Log the exception.
            logException(
                ouch,
                "SecurityManagerImpl.setPassword()"
                ) ;
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            //
            // Throw a new Exception.
            throw new CommunityServiceException(
                "Database transaction failed",
                ident.toString(),
                ouch
                ) ;
            }
        //
        // Close our database connection.
        finally
            {
            closeConnection(database) ;
            }
        return result ;
        }
    }
