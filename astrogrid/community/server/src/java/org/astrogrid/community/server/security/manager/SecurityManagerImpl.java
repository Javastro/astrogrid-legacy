package org.astrogrid.community.server.security.manager ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;
import org.astrogrid.config.SimpleConfig;

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
   * Sets the password for an account.
   * The password is written to the secrets table of the community DB.
   *
   * @param userName The user name (plain; not in an IVORN).
   * @param password The account password.
   * @throws CommunitySecurityException If the password change fails.
   * @throws CommunityServiceException If there is an internal error in service.
   */
  public boolean setPassword(String userName, 
                             String password) throws CommunityServiceException, 
                                                     CommunitySecurityException {
    assert(userName != null);
    assert(password != null);
    String primaryKey = primaryKey(userName);
    log.debug("Setting the password for user " + userName + ", primary key " + primaryKey);

    // Update the password in the database.
    // This makes a new connection and runs a transaction. The error handling
    // ensures that the transaction is either committed or rolled back and that
    // the connection is always closed before the end of the method.
    // In the transaction, an attempt is made to read and update an existing
    // record. If this fails, a new record is created.
    Database database = null ;
    try {
      database = this.getDatabase();
      database.begin();
      PasswordData data = null;
      try {
        data = (PasswordData) database.load(PasswordData.class, primaryKey);
      }
      catch (ObjectNotFoundException ouch) {
        // This is OK: it means we're setting the password for the first time.
      }
      if (null != data) {
        log.debug("This user already has a password, which shall be changed.");
        data.setPassword(password);
        data.setEncryption(PasswordData.NO_ENCRYPTION);
      }
      else {
        log.debug("This user doesn't have a password yet.");
        data = new PasswordData(primaryKey, password);
        database.create(data);
      }
      database.commit();
    }
    catch (Exception ouch) {
      log.error("Password change failed: " + ouch);
      rollbackTransaction(database);
      throw new CommunityServiceException("Password change failed",
                                          userName,
                                          ouch);
    }
    finally {
      closeConnection(database) ;
    }
    
    // This is pointless - the method either returns true or throws - but the
    // interface definition requires it.
    return true;
  }

  /**
   * Derives the primary key for the DB tables from the user name and
   * configured community name.
   */
  protected String primaryKey(String userName) {
    String community = 
        SimpleConfig.getSingleton().getString("org.astrogrid.community.ident");
    int slash = community.indexOf("/");
    String authority = (slash == -1)? community : community.substring(0, slash);
    return "ivo://" + authority + "/" + userName;
  }
  
  /**
   * Exposes the password for a user account.
   * <p>
   * This method supports the certificate-authority servlet which
   * uses the password of an existing account to protect that account's
   * credentials. The method should definitely not be exposed as a web service!
   *
   * @param userName The name of the account, not in IVORN form, e.g. fred rather than ivo://fred@foo/
   */
  public String getPassword(String userName) throws CommunityServiceException {
    
    // Get the password record from the community database.
    PasswordData data;
    Database database = null ;
    try {
      database = this.getDatabase();
      database.begin();
      String primaryKey = primaryKey(userName);
      System.out.println("Loading PasswordData with JDO identity " + primaryKey);
      data = (PasswordData) (database.load(PasswordData.class, primaryKey));
      database.commit();
    }
    catch (Exception e) {
      rollbackTransaction(database);
      throw new CommunityServiceException("Failed to read the password for " + userName, e);
    }
    finally {
      closeConnection(database);
    }
    
    return data.getPassword();
  }
    
}
