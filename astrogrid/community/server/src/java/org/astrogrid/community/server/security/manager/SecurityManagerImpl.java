/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/security/manager/Attic/SecurityManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/30 01:40:03 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityManagerImpl.java,v $
 *   Revision 1.6  2004/03/30 01:40:03  dave
 *   Merged development branch, dave-dev-200403242058, into HEAD
 *
 *   Revision 1.5.4.1  2004/03/28 09:11:43  dave
 *   Convert tabs to spaces
 *
 *   Revision 1.5  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.4.18.1  2004/03/22 15:31:10  dave
 *   Added CommunitySecurityException.
 *   Updated SecurityManager and SecurityService to use Exceptions.
 *
 *   Revision 1.4  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.3.2.5  2004/03/02 15:29:35  dave
 *   Working round Castor problem with PasswordData - objects remain in database cache
 *
 *   Revision 1.3.2.4  2004/03/01 12:49:42  dave
 *   Updated server SecurityService to match changes to interface
 *
 *   Revision 1.3.2.3  2004/02/23 19:43:47  dave
 *   Refactored DatabaseManager tests to test the interface.
 *   Refactored DatabaseManager tests to use common DatabaseManagerTest.
 *
 *   Revision 1.3.2.2  2004/02/23 08:55:20  dave
 *   Refactored CastorDatabaseConfiguration into DatabaseConfiguration
 *
 *   Revision 1.3.2.1  2004/02/22 20:03:16  dave
 *   Removed redundant DatabaseConfiguration interfaces
 *
 *   Revision 1.3  2004/02/20 21:11:05  dave
 *   Merged development branch, dave-dev-200402120832, into HEAD
 *
 *   Revision 1.2.2.2  2004/02/19 21:09:27  dave
 *   Refactored ServiceStatusData into a common package.
 *   Refactored CommunityServiceImpl constructor to take a parent service.
 *   Refactored default database for CommunityServiceImpl
 *
 *   Revision 1.2.2.1  2004/02/16 15:20:54  dave
 *   Changed tabs to spaces
 *
 *   Revision 1.2  2004/02/12 08:12:13  dave
 *   Merged development branch, dave-dev-200401131047, into HEAD
 *
 *   Revision 1.1.2.4  2004/02/06 16:14:17  dave
 *   Removed import java.rmi.Remote
 *
 *   Revision 1.1.2.3  2004/02/06 13:49:09  dave
 *   Moved CommunityManagerBase into server.common.CommunityServer.
 *   Moved getServiceStatus into server.common.CommunityServer.
 *   Moved JUnit tests to match.
 *
 *   Revision 1.1.2.2  2004/01/30 18:55:37  dave
 *   Added tests for SecurityManager.setPassword
 *
 *   Revision 1.1.2.1  2004/01/30 03:21:23  dave
 *   Added initial code for SecurityManager and SecurityService
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
import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory ;

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
