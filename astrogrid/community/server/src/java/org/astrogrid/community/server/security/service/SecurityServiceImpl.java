package org.astrogrid.community.server.security.service ;

import java.security.AccessControlException;
import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.util.Vector ;

import java.rmi.server.UID ;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.ObjectNotFoundException ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.community.server.security.data.PasswordData ;
import org.astrogrid.community.common.security.data.SecurityToken ;
import org.astrogrid.community.common.security.service.SecurityService ;

import org.astrogrid.community.common.ivorn.CommunityIvornParser ;
import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory ;

import org.astrogrid.community.server.service.CommunityServiceImpl ;
import org.astrogrid.community.server.database.configuration.DatabaseConfiguration ;

import org.astrogrid.community.common.exception.CommunityServiceException  ;
import org.astrogrid.community.common.exception.CommunitySecurityException ;
import org.astrogrid.community.common.exception.CommunityIdentifierException  ;

/**
 * Implementation of our SecurityService service.
 *
 */
public class SecurityServiceImpl
    extends CommunityServiceImpl
    implements SecurityService
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(SecurityServiceImpl.class);

    /**
     * Public constructor, using default database configuration.
     *
     */
    public SecurityServiceImpl()
        {
        super() ;
        }

    /**
     * Public constructor, using specific database configuration.
     *
     */
    public SecurityServiceImpl(DatabaseConfiguration config)
        {
        super(config) ;
        }

    /**
     * Public constructor, using a parent service.
     *
     */
    public SecurityServiceImpl(CommunityServiceImpl parent)
        {
        super(parent) ;
        }

    /**
     * Generate a new token for an account.
     * @param account The Account ident.
     * @throws CommunityIdentifierException If the identifiers are not valid.
     * @throws CommunityServiceException If the local Community identifier is not set.
     *
     */
    protected SecurityToken createToken(CommunityIvornParser account)
        throws CommunityServiceException, CommunityIdentifierException
        {
       //
        // Create a new UID.
        UID uid = new UID() ;
        //
        // Create an Ivorn for the token.
        Ivorn ivorn = CommunityAccountIvornFactory.createLocal(
            uid.toString()
            ) ;
        //
        // Issue a new Security token to the account.
        SecurityToken token = new SecurityToken(
            account.getAccountIdent(),
            ivorn.toString()
            ) ;
        //
        // Mark the token as valid.
        token.setStatus(SecurityToken.VALID_TOKEN) ;
        //
        // Return the new token.
        log.debug(" SecurityServiceImpl.createToken() Token : " + token) ;
        return token ;
        }

    /**
     * Check an Account password.
     * @param account  The account ident.
     * @param password The account password.
     * @return A valid SecurityToken if the ident and password are valid.
     * @throws CommunitySecurityException If the security check fails.
     * @throws CommunityServiceException If there is an internal error in service.
     * @throws CommunityIdentifierException If the account identifier is invalid.
     * @todo Check Account is local.
     *
     */
    public SecurityToken checkPassword(String account, String password)
        throws CommunityServiceException, CommunitySecurityException, CommunityIdentifierException
        {
        log.debug("SecurityServiceImpl.checkPassword() Ident=" + account+" Pass=" + password) ;
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
        // Try searching the database.
        SecurityToken token = null ;
        Database database = null ;
        try {
            //
            // Open our database connection.
            database = this.getDatabase() ;
            //
            // Begin a new database transaction.
            database.begin();
            
            String primaryKey = primaryKey(ident);
            System.out.println("Looking for the password for " + primaryKey);
            //
            // Try to load a matching PasswordData.
            PasswordData match = (PasswordData) database.load(PasswordData.class, primaryKey) ;
            log.debug("  PASS : Got password data") ;
            log.debug("    Account  : " + match.getAccount()) ;
            log.debug("    Password : " + match.getPassword()) ;
            //
            // Check if the password matches.
            if (password.equals(match.getPassword()))
                {
                log.debug("  PASS : Password matches") ;
               //
                // Create our new SecurityToken.
                token = this.createToken(ident) ;
                database.create(token);
                log.debug("  PASS : Got new token") ;
                //
                // Commit the database transaction.
                database.commit() ;
                log.info("successful login for "+match.getAccount());
                }
            //
            // If the password don't match.
            else {
                //
                // Throw a new Exception.
                log.warn("failed login for " + match.getAccount());
                throw new CommunitySecurityException(
                    "Password invalid",
                    ident.getAccountIdent()
                    ) ;
                }
            }
        //
        // If the password check failed.
        catch (CommunitySecurityException ouch)
            {
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            //
            // Throw the Exception.
            throw ouch ;
            }
        //
        // If we couldn't find the object.
        catch (ObjectNotFoundException ouch)
            {
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            //
            // Throw a new Exception.
            throw new CommunitySecurityException(
                "Account password not found",
                ident.toString()
                ) ;
            }
        //
        // If anything else went bang.
        catch (Exception ouch)
            {
            //
            // Log the exception.
            logException(
                ouch,
                "SecurityServiceImpl.checkPassword()"
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
        //
        // Return the new token.
        return token ;
        }

    /**
     * Validate a SecurityToken.
     * Validates a token, and creates a new tokens issued to the same account.
     * @param token The token to validate.
     * @return A new SecurityToken if the original was valid.
     * @throws CommunitySecurityException If the security check fails.
     * @throws CommunityServiceException If there is an internal error in service.
     * @throws CommunityIdentifierException If the token is invalid.
     * @todo Refactor to call split and unpack ?
     * @todo Check Token is local.
     * @deprecated Don't use tokens.
     */
    public SecurityToken checkToken(SecurityToken original)
        throws CommunityServiceException, CommunitySecurityException, CommunityIdentifierException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("SecurityServiceImpl.checkToken()") ;
        log.debug("  Token : " + original) ;
        //
        // Check for null token.
        if (null == original)
            {
            throw new CommunityIdentifierException(
                "Null token"
                ) ;
            }
        //
        // Mark the original as invalid.
        original.setStatus(SecurityToken.INVALID_TOKEN) ;
        //
        // Get the token value.
        CommunityIvornParser token = new CommunityIvornParser(
            original.getToken()
            ) ;
        log.debug("  Token   : " + token) ;
        //
        // Get the Account ident.
        CommunityIvornParser account = new CommunityIvornParser(
            original.getAccount()
            ) ;
        log.debug("  Account : " + account) ;

        SecurityToken result = null ;
        Database database = null ;
        try {
            //
            // Open our database connection.
            database = this.getDatabase() ;
            //
            // Begin a new database transaction.
            database.begin();
            //
            // Try loading the original token from our database.
            SecurityToken match = (SecurityToken) database.load(SecurityToken.class, original.getToken()) ;
            log.debug("  PASS : Got matching token") ;
            log.debug("  Token : " + match) ;
            //
            // If the match is still valid.
            if (match.isValid())
                {
                log.debug("  PASS : Original is valid") ;
                //
                // Update the original token.
// Mark as used not invalid.
                match.setStatus(SecurityToken.INVALID_TOKEN) ;
                //
                // Create a new token.
                result = this.createToken(account) ;
                database.create(result) ;
                //
                // Commit the database transaction.
                database.commit() ;
                //
                // Return the new token.
                return result ;
                }
            //
            // If the original is no longer valied.
            else {
                log.debug("  FAIL : Original is not valid") ;
                //
                // Throw a new Exception.
                throw new CommunitySecurityException(
                    "Token invalid",
                    original.getToken()
                    ) ;
                }
            }
        //
        // If the password check failed.
        catch (CommunitySecurityException ouch)
            {
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            //
            // Throw the Exception.
            throw ouch ;
            }
        //
        // If we couldn't find the object.
        catch (ObjectNotFoundException ouch)
            {
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            //
            // Throw a new Exception.
            throw new CommunitySecurityException(
                "Token not found",
                original.getToken()
                ) ;
            }
        //
        // If anything else went bang.
        catch (Exception ouch)
            {
            //
            // Log the exception.
            logException(
                ouch,
                "SecurityServiceImpl.checkToken()"
                ) ;
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            //
            // Throw a new Exception.
            throw new CommunityServiceException(
                "Database transaction failed",
                original.getToken(),
                ouch
                ) ;
            }
        //
        // Close our database connection.
        finally
            {
            closeConnection(database) ;
            }
        }

    /**
     * Split a SecurityToken.
     * Validates a token, and then creates a new set of tokens issued to the same account.
     * @throws CommunitySecurityException If the security check fails.
     * @throws CommunityServiceException If there is an internal error in service.
     * @throws CommunityIdentifierException If the token is invalid.
     * @todo Check Token is local.
     *
     */
    public Object[] splitToken(SecurityToken original, int count)
        throws CommunityServiceException, CommunitySecurityException, CommunityIdentifierException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("SecurityServiceImpl.checkToken()") ;
        log.debug("  Token : " + original) ;
        //
        // Check for null token.
        if (null == original)
            {
            throw new CommunityIdentifierException(
                "Null token"
                ) ;
            }
        //
        // Mark the original as invalid.
        original.setStatus(SecurityToken.INVALID_TOKEN) ;
        //
        // Get the token value.
        CommunityIvornParser token = new CommunityIvornParser(
            original.getToken()
            ) ;
        log.debug("  Token   : " + token) ;
        //
        // Get the Account ident.
        CommunityIvornParser account = new CommunityIvornParser(
            original.getAccount()
            ) ;
        log.debug("  Account : " + account) ;

        Vector vector = new Vector() ;
        Database database = null ;
        try {
            //
            // Open our database connection.
            database = this.getDatabase() ;
            //
            // Begin a new database transaction.
            database.begin();
            //
            // Try loading the original token from our database.
            SecurityToken match = (SecurityToken) database.load(SecurityToken.class, original.getToken()) ;
            log.debug("  PASS : Got matching token") ;
            log.debug("  Token : " + match) ;
            //
            // If the match is still valid.
            if (match.isValid())
                {
                log.debug("  PASS : Original is valid") ;
                //
                // Update the original token.
// Mark as used not invalid.
                match.setStatus(SecurityToken.INVALID_TOKEN) ;
                //
                // Create our new tokens.
                for (int i = 0 ; i < count ; i++)
                    {
                    SecurityToken result = this.createToken(account) ;
                    database.create(result) ;
                    vector.add(result) ;
                    }
                //
                // Commit the database transaction.
                database.commit() ;
                }
            //
            // If the original is no longer valied.
            else {
                log.debug("  FAIL : Original is not valid") ;
                //
                // Throw a new Exception.
                throw new CommunitySecurityException(
                    "Token invalid",
                    original.getToken()
                    ) ;
                }
            }
        //
        // If the password check failed.
        catch (CommunitySecurityException ouch)
            {
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            //
            // Throw the Exception.
            throw ouch ;
            }
        //
        // If we couldn't find the object.
        catch (ObjectNotFoundException ouch)
            {
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            //
            // Throw a new Exception.
            throw new CommunitySecurityException(
                "Token not found",
                original.getToken()
                ) ;
            }
        //
        // If anything else went bang.
        catch (Exception ouch)
            {
            //
            // Log the exception.
            logException(
                ouch,
                "SecurityServiceImpl.checkToken()"
                ) ;
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            //
            // Throw a new Exception.
            throw new CommunityServiceException(
                "Database transaction failed",
                original.getToken(),
                ouch
                ) ;
            }
        //
        // Close our database connection.
        finally
            {
            closeConnection(database) ;
            }
        //
        // Return the new token.
        return vector.toArray() ;
        }

  /**
   * Checks a password against the password database.
   * The given user-name is assumed to be part of this community;
   * e.g. "fred" rather than "ivo://fred@foo.bar/community".
   *
   * @throws AccessControlException If the password does not match the user name.
   * @throws AccessControlException If the user name does not match a local account.
   */
  public void authenticate(String userName, String password)
      throws AccessControlException {
    try {

      // Form the IVORN for the account.
      String account = "ivo://" +
                       userName +
                       "@" + 
                       CommunityIvornParser.getLocalIdent();
      
      // Check that the password matches the account.
      checkPassword(account, password);
    }
    catch (Exception e) {
      throw new AccessControlException("Password check failed for " + userName);
    }
    
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
