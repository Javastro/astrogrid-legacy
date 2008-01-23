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
            
            // Try loading the PasswordData.
            String primaryKey = primaryKey(ident);
            System.out.println("Loading PasswordData with JDO identity " + primaryKey);
            PasswordData data = null;
            try {
                data = (PasswordData) database.load(PasswordData.class, primaryKey);
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
                data = new PasswordData(primaryKey, password) ;
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
    
  /**
   * Derives from the account IVORN the primary key for the DB tables.
   * This key is an old form of account IVORN in which the account name
   * is the whole of the resource key.
   */
  protected String primaryKey(CommunityIvornParser parser) {
    return "ivo://" + 
           parser.getIvorn().toUri().getHost() +
           "/" +
           parser.getAccountName();
  }
    
}
