/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/security/manager/Attic/SecurityManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/05 17:19:59 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityManagerImpl.java,v $
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

import org.astrogrid.community.server.service.CommunityServiceImpl ;
import org.astrogrid.community.server.database.configuration.DatabaseConfiguration ;

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
     * Set an account password.
     *
     */
    public boolean setPassword(String ident, String value)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("SecurityManagerImpl.setPassword()") ;
        if (DEBUG_FLAG) System.out.println("  Account : " + ident) ;
        if (DEBUG_FLAG) System.out.println("  Value   : " + value) ;
        //
        // Check for null params
        if (null == ident) return false ;
        if (null == value) return false ;
        //
        // Trim spaces.
        ident = ident.trim() ;
        value = value.trim() ;
        //
        // Check for blank params.
        if (ident.length() == 0) return false ;
        if (value.length() == 0) return false ;
        //
        // Set the response to false.
        boolean  result   = false ;
        Database database = null ;
        //
        // Try update the database.
        try {
            //
            // Open our database connection.
            database = this.getDatabase() ;
            //
            // Begin a new database transaction.
            database.begin();
            //
            // Try loading the Account from the database.
//
// TODO Use Account manager for this.
            AccountData account = (AccountData) database.load(AccountData.class, ident) ;
            //
            // If we found the Account.
            if (null != account)
                {
                if (DEBUG_FLAG)System.out.println("  PASS : found account") ;
                if (DEBUG_FLAG)System.out.println("    Account : " + account.getIdent()) ;
                //
                // Try loading the PasswordData.
                PasswordData data = null ;
                try {
                    data = (PasswordData) database.load(PasswordData.class, account.getIdent()) ;
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
                    data.setPassword(value) ;
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
                    data = new PasswordData(ident, value) ;
                    database.create(data) ;
                    if (DEBUG_FLAG)System.out.println("  PASS : created password") ;
                    if (DEBUG_FLAG)System.out.println("    Account  : " + data.getAccount()) ;
                    if (DEBUG_FLAG)System.out.println("    Password : " + data.getPassword()) ;
                    }
                //
                // Set the response to true.
                result = true ;
                }
            //
            // If we didn't find the Account.
            else {
                if (DEBUG_FLAG)System.out.println("  FAIL : missing account") ;
                //
                // Set the response to false.
                result = false ;
                }
            //
            // Commit the database transaction.
            database.commit() ;
            }
        //
        // If anything went bang.
        catch (Exception ouch)
            {
            //
            // Log the exception.
            logException(ouch, "SecurityManagerImpl.setPassword()") ;
            //
            // Set the response to false.
            result = false ;
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            }
        //
        // Close our database connection.
        finally
            {
            closeConnection(database) ;
            }
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        return result ;
        }
    }
