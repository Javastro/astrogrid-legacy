package org.astrogrid.community.server.policy.manager ;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector ;
import java.util.Collection ;
import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;
import org.astrogrid.config.PropertyNotFoundException;
import org.astrogrid.community.common.exception.CommunityPolicyException;
import org.astrogrid.community.common.exception.CommunityServiceException;
import org.astrogrid.community.common.exception.CommunityIdentifierException;
import org.astrogrid.community.common.ivorn.CommunityIvornParser;
import org.astrogrid.community.common.policy.data.AccountData;
import org.astrogrid.community.common.policy.manager.AccountManager;
import org.astrogrid.community.server.CommunityConfiguration;
import org.astrogrid.community.server.database.configuration.DatabaseConfiguration;
import org.astrogrid.community.server.service.CommunityServiceImpl;
import org.astrogrid.filemanager.common.NodeIvorn;
import org.astrogrid.filemanager.common.AccountIdent;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.filemanager.client.delegate.NodeDelegate;
import org.astrogrid.filemanager.resolver.FileManagerResolverException;
import org.astrogrid.filemanager.resolver.NodeDelegateResolver;
import org.astrogrid.filemanager.resolver.NodeDelegateResolverImpl;
import org.astrogrid.store.Ivorn;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.DuplicateIdentityException;

/**
 * Server side implmenetation of the AccountManager service.
 *
 */
public class AccountManagerImpl
    extends CommunityServiceImpl
    implements AccountManager
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(AccountManagerImpl.class);

    protected boolean useMockNodeDelegate = false;

    /**
     * Public constructor, using default database configuration.
     *
     */
    public AccountManagerImpl() {
      super();
    }

    /**
     * Public constructor, using specific database configuration.
     * @param config A specific DatabaseConfiguration.
     *
     */
    public AccountManagerImpl(DatabaseConfiguration config)
        {
        super(config) ;
        }

    /**
     * Add a new Account, given the Account ident.
     * @param  ident The Account identifier.
     * @return An AccountData for the Account.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is already in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public AccountData addAccount(String ident)
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        //
        // Create a new Accountdata.
        return this.addAccount(
            new AccountData(
                ident
                )
            ) ;
        }

    /**
     * Add a new Account, given the Account data.
     * @param  account The AccountData to add.
     * @return A new AccountData for the Account.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is already in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the WebService call fails.
     */
    public AccountData addAccount(AccountData externalAccount)
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException {
      
      if (null == externalAccount) {
        throw new CommunityIdentifierException("Null account");
      }
        
      // The given account will probably have the wrong name, and
      // the AccountData class is too broken to fix it in situ.
      // Therefore, make a new account object in which the name is the
      // required primary key for the DB.
      AccountData account = internalAccount(externalAccount);
      log.debug("Recorded account is named " + account.getIdent());
            
      // Record the account. use a DB transaction s.t. either 
      // all of the information is recorded or none. This includes
      // creating the home space for the account in the remote service:
      // if the space cannot be created then the account is not
      // recorded.
      Database database = null ;
      try {
        database = this.getDatabase() ;
        database.begin();
        database.create(account);
        allocateSpace(account);
        database.commit() ;
      }
      catch (DuplicateIdentityException ouch) {
        rollbackTransaction(database);
        throw new CommunityPolicyException("The account already exists", 
                                           account.getIdent());
      }
      catch (Exception ouch) {
        logException(ouch,  "AccountManagerImpl.addAccount()");
        rollbackTransaction(database) ;
        throw new CommunityServiceException("Failed to create the account " +
                                            account.getIdent(),
                                            ouch);
      }
      finally {
        closeConnection(database);
      }
            
      return externalAccount(account);
    }

    /**
     * Request an Account details, given the Account ident.
     * @param  ident The Account identifier.
     * @return An AccountData for the Account.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public AccountData getAccount(String ident)
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        System.out.println("PolicyManager.getAccount(String) was asked for " + ident);
        return this.getAccount(
            new CommunityIvornParser(
                ident
                )
            ) ;
        }

    /**
     * Request an Account details, given the Account ident.
     * @param  ident The Account identifier.
     * @return An AccountData for the Account.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @todo Verify that the finally gets executed, even if a new Exception is thrown.
     *
     */
    public AccountData getAccount(CommunityIvornParser ident)
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        System.out.println("") ;
        System.out.println("----\"----") ;
        System.out.println("AccountManagerImpl.getAccount()") ;
        System.out.println("  Ident : " + ident) ;
        //
        // Check for null ident.
        if (null == ident)
            {
            throw new CommunityIdentifierException(
                "Null identifier"
                ) ;
            }
        
            // Try finding the Account.
            Database    database = null ;
            AccountData account  = null ;
            try {
                // Find the primary key for this account in the DB tables.
                String string = primaryKey(ident);
                System.out.println("getAccount(): looking for " + string);
                
                //
                // Open our database connection.
                database = this.getDatabase() ;
                //
                // Begin a new database transaction.
                database.begin();
                //
                // Load the Account from the database.
                account = (AccountData) database.load(AccountData.class, string) ;
                //
                // Commit the transaction.
                database.commit() ;
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
                throw new CommunityPolicyException(
                    "Account not found",
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
                    "AccountManagerImpl.getAccount()"
                    ) ;
                //
                // Cancel the database transaction.
                rollbackTransaction(database) ;
                //
                // Throw a new Exception.
                throw new CommunityServiceException(
                    "Failed to retrieve an account",
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
            // Return the Account details.
            return externalAccount(account);
        }

    /**
     * Update an Account.
     * @param  account The new AccountData to update.
     * @return A new AccountData for the Account.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @todo Verify that the finally gets executed, even if a new Exception is thrown.
     *
     */
    public AccountData setAccount(AccountData externalAccount)
        throws CommunityServiceException, 
               CommunityIdentifierException, 
               CommunityPolicyException {
      
      if (null == externalAccount) {
        throw new CommunityIdentifierException("Null account");
      }
        
      // Convert to internal form so that the primary key is right.
      AccountData account = internalAccount(externalAccount);
        
      Database database = null ;
      try {
        // Open our database connection.
        database = this.getDatabase();
          
        // Begin a new database transaction.
        database.begin();
          
        // Load the Account from the database.
        // We don't actually need to use these data, but we have to get the
        // record in a position to commit.
        AccountData data = (AccountData) database.load(AccountData.class, account.getIdent());

        // Transcribe the information.
        data.setHomeSpace(account.getHomeSpace());
        data.setDisplayName(account.getDisplayName());
        data.setDescription(account.getDescription());
        data.setEmailAddress(account.getEmailAddress());

        // Commit the transaction, thereby updating the record.
        database.commit() ;
      }
      
      catch (ObjectNotFoundException ouch) {
        rollbackTransaction(database);
        throw new CommunityPolicyException("Account not found ",
                                           externalAccount.getIdent());
      }
      
      catch (Exception ouch) {
        logException(ouch,"AccountManagerImpl.setAccount()");
        rollbackTransaction(database);
        throw new CommunityServiceException(
                    "Failed to update an account",
                    externalAccount.getIdent(),
                    ouch
                    );
      }
      
      finally {
        closeConnection(database) ;
      }
      
      return externalAccount ;
    }

    /**
     * Delete an Account.
     * @param  ident The Account identifier.
     * @return The AccountData for the old Account.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public AccountData delAccount(String ident)
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        return this.delAccount(
            new CommunityIvornParser(
                ident
                )
            ) ;
        }

    /**
     * Deletes an Account.
     * @param  ident The Account identifier.
     * @return The AccountData for the old Account.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityServiceException If there is an internal error in the service.
     */
  protected AccountData delAccount(CommunityIvornParser ident) throws CommunityServiceException, 
                                                                      CommunityIdentifierException, 
                                                                      CommunityPolicyException {
    if (null == ident){
      throw new CommunityIdentifierException("Null identifier");
    }

    AccountData account  = null;
    Database    database = null;
    try {
      String key = primaryKey(ident);
      database = this.getDatabase();
      database.begin();
      account = (AccountData) database.load(AccountData.class, key);
      database.remove(account);
      database.commit();
      return externalAccount(account);
    }
    catch (ObjectNotFoundException ouch) {
      rollbackTransaction(database);
      throw new CommunityPolicyException("Account not found", ident.toString());
    }
    catch (Exception ouch) {
      logException(ouch, "AccountManagerImpl.delAccount()");
      rollbackTransaction(database);
      throw new CommunityServiceException("Database transaction failed",
                                          ident.toString(),
                                          ouch);
    }
    finally {
      closeConnection(database);
    }  
  }

    /**
     * Request a list of local Accounts.
     * @return An array of AccountData objects (null on error).
     * @todo Return empty array rather than null.
     *
     */
    public Object[] getLocalAccounts()
        {
        System.out.println("") ;
        System.out.println("----\"----") ;
        System.out.println("AccountManagerImpl.getLocalAccounts()") ;
        //
        // Try to query the database.
        Object[] array    = null ;
        Database database = null ;
        try {
            //
            // Open our database connection.
            database = this.getDatabase() ;
            //
            // Begin a new database transaction.
            database.begin();
            //
            // Create our OQL query.
            OQLQuery query = database.getOQLQuery(
                "SELECT accounts FROM org.astrogrid.community.common.policy.data.AccountData accounts"
                );
            //
            // Execute our query.
            QueryResults results = query.execute();
            //
            // Transfer our results to a vector.
            Collection collection = new Vector() ;
            while (results.hasMore()) {
              AccountData account =  (AccountData)results.next();
              collection.add(externalAccount(account));
            }
            
            //
            // Convert it into an array.
            array = collection.toArray() ;
            //
            // Commit the transaction.
            database.commit() ;
            
            }
        //
        // If anything went bang.
        catch (Exception ouch)
            {
            //
            // Log the exception.
            logException(ouch, "AccountManagerImpl.getLocalAccounts()") ;
            //
            // Set the response to null.
            array = null ;
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
        //
        // Return the array.
        return array ;
        }

  /**
   * Returns a list of account.
   */
  public List<Account> getAccountsList() {
    Object[] o = getLocalAccounts();
    List<Account> accounts = new ArrayList<Account>(o.length);
    for (int i = 0; i < o.length; i++) {
      accounts.add(new Account((AccountData)o[i]));
    }
    return accounts;
  }

  /**
   * Tells the manager to use a mock object for its VOSpace.
   * This should be set only for unit testing.
   */
  public void useMockNodeDelegate() {
    this.useMockNodeDelegate = true;
  }
    
    /**
     * Allocates home space in MySpace for an Account.
     * If a home space for the given account already exists then it is 
     * destroyed and a new, empty space is created.
     *
     * @param account The AccountData to update.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityServiceException If the service is unable to allocate the space.
     */
    public void allocateSpace(AccountData account)
        throws CommunityServiceException, CommunityIdentifierException {
      System.out.println("") ;
      System.out.println("----\"----") ;
      System.out.println("AccountManagerImpl.allocateSpace()") ;
      System.out.println("  Account : " + ((null != account) ? account.getIdent() : null)) ;
        
      // Check for null account.
      if (null == account) {
        throw new CommunityIdentifierException("Null account");
      }
        
      // Do nothing if this account already has a homespace.
      if (account.getHomeSpace() != null) {
        return;
      }
            
      // Check for null identifier.
      if (null == account.getIdent()) {
        throw new CommunityIdentifierException("Null account identifier");
      }
      
      // Get the IVORN for the VOSpace service in which we create
      // the homespace. This call will throw an exception if
      // the VOSpace isn't configured.
      Ivorn ivorn = this.getDefaultVoSpace();
      
      // Call FileManager client to create the space.
      try {
        NodeDelegate delegate = getFileManagerDelegate(ivorn);
                    
        // Make an identity object out of the account IVORN.
        AccountIdent fmAccount = new AccountIdent(account.getIdent());
                    
        /*
                    // Delete any previous home-space for the account.
                    // If the file manager can't find that account, then it
                    // throws; but this is expected in most cases so we discard
                    // the exception.
                    try {
                      FileManagerNode node = delegate.getAccount(fmAccount);
                      System.out.println("Deleting");
                      node.delete();
                      log.info("The homespace for account " + 
                               account.getIdent() +
                               " in " +
                               ivorn +
                               " has been deleted and can now be recreated.");
                    }
                    catch (Exception e) {
                      log.info("The account " + 
                               account.getIdent() +
                               " did not have a home space in " +
                               ivorn);
                    }
                    */
        
        // Ask the degagate to create a new account.
        FileManagerNode node = delegate.addAccount(fmAccount);
        log.info("Home space for account " + 
                 account.getIdent() +
                 " has been created in " +
                 ivorn);
                    
        // Recover the concrete IVORN for the created space.
        NodeIvorn home = node.getMetadata().getNodeIvorn();
        log.info("The new space is at " + home);
        account.setHomeSpace(home.toString());
                    
      }
      
      // If the homespace was not created.
      catch (Exception ouch) {
        logException(ouch, "AccountManagerImpl.allocateSpace()");
        throw new CommunityServiceException("Unable to create VoSpace",
                                            ouch);
      }
    
    }

    /**
     * The config property key for our default VoSpace ivorn.
     *
     */
    protected static final String DEFAULT_VOSPACE_PROPERTY = "org.astrogrid.community.default.vospace" ;

  /**
   * Get the IVORN for the configured, default VOSpace.
   *
   * @return An Ivorn for the default VoSpace service.
   * @throws CommunityServiceException If unable to get the VoSpace ivorn.
   *
   */
  public Ivorn getDefaultVoSpace() throws CommunityServiceException {
    try {
      URI u = new CommunityConfiguration().getVoSpaceIvorn();
      return new Ivorn(u.toString());
    }
    catch (PropertyNotFoundException ouch) {
      throw new CommunityServiceException("Default VOSpace is not configured");
    }
    catch (Exception ouch) {
      throw new CommunityServiceException(
          "Unable to convert default VoSpace into Ivorn",
          ouch
      );
    }
  }
  
  /**
   * Lists the user names of registered accounts.
   * If the accounts database is not acessible, supresses the exceptions and
   * returns a zero-length array. This supports the account-list JSP; the method
   * is not really suitable for any other use.
   *
   * @return The list of names (never null; may have a length of zero).
   */
  public String[] getUserNames() {
    Object[] o = getLocalAccounts();
    if (o == null) {
      return new String[0];
    }
    else {
      String[] names = new String[o.length];
      for (int i = 0; i < o.length; i++) {
        AccountData ad = (AccountData) o[i];
        try {
          names[i] = new CommunityIvornParser(ad.getIdent()).getAccountName();
        }
        catch (Exception e) {
          throw new RuntimeException("Invalid account-IVORN " + 
                                     ad.getIdent() + 
                                     " was found in the community database!");
        }
      }
      return names;
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
  
  /**
   * Derives the account IVORN from the primary key of the DB.
   */
  protected String accountIvorn(String primaryKey) throws CommunityIdentifierException {
    CommunityIvornParser parser = new CommunityIvornParser(primaryKey);
    return parser.getAccountIvorn().toString();
  }
  
  /**
   * Derives the internal form of the account from the external form.
   * The internal form has a different encoding of the identity.
   */
  protected AccountData internalAccount(AccountData externalAccount) 
      throws CommunityIdentifierException {
    System.out.println("External account is named " + externalAccount.getIdent());
    CommunityIvornParser parser = 
        new CommunityIvornParser(externalAccount.getIdent());
    AccountData internalAccount = new AccountData(primaryKey(parser));
    internalAccount.setDescription(externalAccount.getDescription());
    internalAccount.setDisplayName(externalAccount.getDisplayName());
    internalAccount.setEmailAddress(externalAccount.getEmailAddress());
    internalAccount.setHomeSpace(externalAccount.getHomeSpace());
    System.out.println("Internal account is named " + internalAccount.getIdent());
    return internalAccount;
  }
  
  /**
   * Derives the external form of the account from the internal form.
   * The internal form has a different encoding of the identity.
   */
  protected AccountData externalAccount(AccountData internalAccount) 
      throws CommunityIdentifierException {
    System.out.println("Internal account is named " + internalAccount.getIdent());
    CommunityIvornParser parser = 
        new CommunityIvornParser(internalAccount.getIdent());
    AccountData externalAccount = new AccountData(parser.getAccountIvorn().toString());
    externalAccount.setDescription(internalAccount.getDescription());
    externalAccount.setDisplayName(internalAccount.getDisplayName());
    externalAccount.setEmailAddress(internalAccount.getEmailAddress());
    externalAccount.setHomeSpace(internalAccount.getHomeSpace());
    System.out.println("External account is named " + externalAccount.getIdent());
    return externalAccount;
  }
  
  /**
   * Supplies a file-manager delegate for a given MySpace service.
   * May supply a mock delegate if the object s set up correctly.
   *
   * @param fileManagerService The IVORN for the file manager service.
   * @return The delegate - never null.
   * @throws FileManagerResolverException If the file-manager service cannot be found.
   */
  protected NodeDelegate getFileManagerDelegate(Ivorn fileManagerService) 
      throws FileManagerResolverException {
    if (this.useMockNodeDelegate) {
      System.out.println("using a mock delegate");
      return new MockNodeDelegate();
    }
    else {
      NodeDelegateResolver resolver = new NodeDelegateResolverImpl(null);
      return resolver.resolve(fileManagerService);
    }
  }
  
}
