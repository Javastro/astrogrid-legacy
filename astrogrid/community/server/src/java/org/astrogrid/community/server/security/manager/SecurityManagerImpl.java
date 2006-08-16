/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/security/manager/Attic/SecurityManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2006/08/16 09:44:17 $</cvs:date>
 * <cvs:version>$Revision: 1.10 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityManagerImpl.java,v $
 *   Revision 1.10  2006/08/16 09:44:17  clq2
 *   gtr_community_1722
 *
 *   Revision 1.9.114.1  2006/08/13 17:05:40  gtr
 *   This was changed as part of the big update to introduce a certificate authority.
 *
 *   Revision 1.9  2005/01/07 14:14:25  jdt
 *   merged from Reg_KMB_787
 *
 *   Revision 1.8.34.1  2004/12/16 11:38:23  KevinBenson
 *   fixed a small bug on the jsp on editing passwords
 *
 *   Revision 1.8  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.7.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
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

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

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
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(SecurityManagerImpl.class);

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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("SecurityManagerImpl.setPassword()") ;
        log.debug("  Account : " + account) ;
        log.debug("  Value   : " + password) ;
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
            //AccountData check = (AccountData) database.load(AccountData.class, ident.getAccountIdent()) ;
            //log.debug("  PASS : found account") ;
            //
            // Try loading the PasswordData.
            log.debug("Loading PasswordData with JDO identity " + ident.getAccountIdent());
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
                log.debug("  PASS : found password") ;
                log.debug("    Account  : " + data.getAccount()) ;
                log.debug("    Password : " + data.getPassword()) ;
                //
                // Change the password value.
                data.setPassword(password) ;
                data.setEncryption(PasswordData.NO_ENCRYPTION) ;
                log.debug("  PASS : changed password") ;
                log.debug("    Account  : " + data.getAccount()) ;
                log.debug("    Password : " + data.getPassword()) ;
                }
            //
            // If we didn't find the password.
            else {
                log.debug("  PASS : missing password") ;
                //
                // Try to create a new PasswordData in the database.
                data = new PasswordData(ident.getAccountIdent(), password) ;
                database.create(data) ;
                log.debug("  PASS : created password") ;
                log.debug("    Account  : " + data.getAccount()) ;
                log.debug("    Password : " + data.getPassword()) ;
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
