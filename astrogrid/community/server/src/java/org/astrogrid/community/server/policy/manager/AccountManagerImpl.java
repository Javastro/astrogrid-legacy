/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/policy/manager/AccountManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/04/15 02:27:46 $</cvs:date>
 * <cvs:version>$Revision: 1.12 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: AccountManagerImpl.java,v $
 *   Revision 1.12  2004/04/15 02:27:46  dave
 *   Merged development branch, dave-dev-200404071355, into HEAD
 *
 *   Revision 1.11.14.1  2004/04/08 13:56:03  dave
 *   Updated the install tools and data files.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.policy.manager ;

import java.util.Vector ;
import java.util.Collection ;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.ObjectNotFoundException ;
import org.exolab.castor.jdo.DuplicateIdentityException ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.config.Config ;
import org.astrogrid.config.SimpleConfig ;
import org.astrogrid.config.PropertyNotFoundException ;

import org.astrogrid.community.common.policy.data.GroupData ;
import org.astrogrid.community.common.policy.data.AccountData ;
import org.astrogrid.community.common.policy.data.GroupMemberData ;

import org.astrogrid.community.common.ivorn.CommunityIvornParser ;
import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory ;

import org.astrogrid.community.common.policy.manager.AccountManager ;

import org.astrogrid.community.server.service.CommunityServiceImpl ;
import org.astrogrid.community.server.database.configuration.DatabaseConfiguration ;

import org.astrogrid.community.common.exception.CommunityPolicyException     ;
import org.astrogrid.community.common.exception.CommunityServiceException    ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

import org.astrogrid.store.VoSpaceClient ;

/**
 * The core AccountManager implementation.
 *
 */
public class AccountManagerImpl
    extends CommunityServiceImpl
    implements AccountManager
    {
    /**
     * Switch for our debug statements.
     *
     */
    protected static final boolean DEBUG_FLAG = true ;

    /**
     * Our AstroGrid configuration.
     *
     */
    protected static Config config = SimpleConfig.getSingleton() ;

    /**
     * The default public group.
     * @todo This should be in a config file, not hard coded.
     *
     */
    private static String DEFAULT_GROUP_NAME = "everyone" ;

    /**
     * The default public group.
     * @todo This should be in a config file, not hard coded.
     * @todo Find a better way of initialising it.
     *
     */
    private static Ivorn DEFAULT_GROUP_IVORN ;

    static {
        try {
            DEFAULT_GROUP_IVORN = CommunityAccountIvornFactory.createLocal(
                DEFAULT_GROUP_NAME
                ) ;
            }
        catch (Exception ouch)
            {
            }
        }

    /**
     * Public constructor, using default database configuration.
     *
     */
    public AccountManagerImpl()
        {
        super() ;
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
     * Public constructor, using a parent service.
     * @param parent A parent CommunityServiceImpl.
     *
     */
    public AccountManagerImpl(CommunityServiceImpl parent)
        {
        super(parent) ;
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
     * @todo Needs refactoring to make it more robust.
     * @todo If the 'everyone' group does not exist, then create it.
     * @todo If the account group already exists (as an AccountGroup) then don't throw a duplicate exception.
     * @todo If the account already belongs to the account group then don't throw a duplicate exception.
     * @todo If the account already belongs to the 'everyone' group then don't throw a duplicate exception.
     * @todo If the MySpace call adds the Account, store the MySpace server and Account details.
     * @todo Tidy up fragments of old data, e.g. groups, membership and permissions,
     * @todo Verify that the finally gets executed, even if a new Exception is thrown.
     *
     */
    public AccountData addAccount(AccountData account)
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerImpl.addAccount()") ;
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
        // Get the Account ident.
        CommunityIvornParser ident = new CommunityIvornParser(
            account.getIdent()
            ) ;
        //
        // If the account is local.
        if (ident.isLocal())
            {
//
// Get the string ident.
String string = ident.getAccountIdent() ;
//
// Set the Account ident.
account.setIdent(string) ;
            //
            // Create the corresponding Group object.
            GroupData group = new GroupData() ;
            group.setIdent(string) ;
            group.setType(GroupData.SINGLE_TYPE) ;
            //
            // Add the account to the group.
            GroupMemberData groupmember = new GroupMemberData() ;
            groupmember.setAccount(string) ;
            groupmember.setGroup(string) ;
            //
            // Add the account to the guest group.
//
// TODO Need better 'default' group handling.
//
            GroupMemberData guestmember = new GroupMemberData() ;
            guestmember.setAccount(string) ;
            guestmember.setGroup(
                DEFAULT_GROUP_IVORN.toString()
                ) ;
            //
            // Try performing our transaction.
            Database database = null ;
            try {
                //
                // Open our database connection.
                database = this.getDatabase() ;
                //
                // Begin a new database transaction.
                database.begin();
                //
                // Try creating the account in the database.
                database.create(account);
                //
                // Try creating the group in the database.
                database.create(group);
                //
                // Try adding the account to the groups.
                database.create(groupmember);
// TODO BUG
// Not sure why this one fails the tests.
// Something about invalid primary key.
//                        database.create(guestmember);

				//
				// Try creating the home space for the Account.
				try {
					this.allocateSpace(account) ;
					}
				catch (Exception ouch)
					{
				    //
				    // Log the exception.
				    logException(
				        ouch,
				        "AccountManagerImpl.addAccount.allocateSpace()"
				        ) ;
					}
                //
                // Commit the transaction.
                database.commit() ;
                }
            //
            // If we already have an object with that ident.
            catch (DuplicateIdentityException ouch)
                {
                //
                // Cancel the database transaction.
                rollbackTransaction(database) ;
                //
                // Throw a new Exception.
                throw new CommunityPolicyException(
                    "Duplicate Account identifier",
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
                    "AccountManagerImpl.addAccount()"
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
			// Return the new Account details.
			return account ;
            }
        //
        // If the ident is not local.
        else {
            //
            // Throw a new Exception.
            throw new CommunityPolicyException(
                "Account is not local",
                ident.toString()
                ) ;
            }
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
    protected AccountData getAccount(CommunityIvornParser ident)
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerImpl.getAccount()") ;
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
        // If the ident is local.
        if (ident.isLocal())
            {
            //
            // Try finding the Account.
            Database    database = null ;
            AccountData account  = null ;
            try {
//
// Get the string ident.
String string = ident.getAccountIdent() ;
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
			// Return the Account details.
            return account ;
            }
        //
        // If the ident is not local.
        else {
            //
            // Throw a new Exception.
            throw new CommunityPolicyException(
                "Account is not local",
                ident.toString()
                ) ;
            }
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
    public AccountData setAccount(AccountData account)
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerImpl.setAccount()") ;
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
        // Get the Account ident.
        CommunityIvornParser ident = new CommunityIvornParser(
            account.getIdent()
            ) ;
        //
        // If the ident is local.
        if (ident.isLocal())
            {
            //
            // Try update the database.
            Database database = null ;
            try {
//
// Get the string ident.
String string = ident.getAccountIdent() ;
                //
                // Open our database connection.
                database = this.getDatabase() ;
                //
                // Begin a new database transaction.
                database.begin();
                //
                // Load the Account from the database.
                AccountData data = (AccountData) database.load(AccountData.class, string) ;
                //
                // Update the account data.
                data.setHomeSpace(account.getHomeSpace()) ;
                data.setDisplayName(account.getDisplayName()) ;
                data.setDescription(account.getDescription()) ;
                data.setEmailAddress(account.getEmailAddress()) ;
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
                    "AccountManagerImpl.setAccount()"
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
			// Return the Account details.
            return account ;
            }
        //
        // If the ident is not local.
        else {
            //
            // Throw a new Exception.
            throw new CommunityPolicyException(
                "Account is not local",
                ident.toString()
                ) ;
            }
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
     * Delete an Account.
     * @param  ident The Account identifier.
     * @return The AccountData for the old Account.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @todo Need to have a mechanism for tidying up references to a remote Account.
     * @todo Need to have a mechanism for tidying up references to an old Account.
     * @todo Need to have a mechanism for notifying other Communities that the Account has been deleted.
     * @todo Verify that the finally gets executed, even if a new Exception is thrown.
     *
     */
    protected AccountData delAccount(CommunityIvornParser ident)
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerImpl.delAccount()") ;
        if (DEBUG_FLAG) System.out.println("  ident : " + ident) ;
        //
        // Check for null ident.
        if (null == ident)
            {
            throw new CommunityIdentifierException(
                "Null identifier"
                ) ;
            }

    //
    // @todo Refactor this.
    AccountData account  = null ;

        //
        // If the ident is local.
        if (ident.isLocal())
            {
            if (DEBUG_FLAG) System.out.println("  PASS : ident is local") ;
            //
            // Try update the database.
            Database    database = null ;
            try {
//
// Get the string ident.
String string = ident.getAccountIdent() ;
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
                // If we found the Account.
                if (null != account)
                    {
                    if (DEBUG_FLAG)System.out.println("  PASS : found account") ;
                    //
                    // Find the group for this account (if it exists).
                    OQLQuery groupQuery = database.getOQLQuery(
                        "SELECT groups FROM org.astrogrid.community.common.policy.data.GroupData groups WHERE groups.ident = $1"
                        );
                    //
                    // Bind the query param.
                    groupQuery.bind(string) ;
                    //
                    // Execute our query.
                    QueryResults groups = groupQuery.execute();
                    if (null != groups)
                        {
                        if (DEBUG_FLAG)System.out.println("  PASS : found groups") ;
                        }
                    else {
                        if (DEBUG_FLAG)System.out.println("  FAIL : null groups") ;
                        }
                    //
                    // Find all of the group memberships for this account.
                    OQLQuery memberQuery = database.getOQLQuery(
                        "SELECT members FROM org.astrogrid.community.common.policy.data.GroupMemberData members WHERE members.account = $1"
                        );
                    //
                    // Bind the query param.
                    memberQuery.bind(string) ;
                    //
                    // Execute our query.
                    QueryResults members = memberQuery.execute();
                    if (null != members)
                        {
                        if (DEBUG_FLAG)System.out.println("  PASS : found members") ;
                        }
                    else {
                        if (DEBUG_FLAG)System.out.println("  FAIL : null members") ;
                        }
                    //
                    // Load all the permissions for this group.
                    OQLQuery permissionQuery = database.getOQLQuery(
                        "SELECT permissions FROM org.astrogrid.community.common.policy.data.PolicyPermission permissions WHERE permissions.group = $1"
                        );
                    //
                    // Bind the query param.
                    permissionQuery.bind(string) ;
                    //
                    // Execute our query.
                    QueryResults permissions = permissionQuery.execute();
                    if (null != permissions)
                        {
                        if (DEBUG_FLAG)System.out.println("  PASS : found permissions") ;
                        }
                    else {
                        if (DEBUG_FLAG)System.out.println("  FAIL : null permissions") ;
                        }
                    //
                    // Delete the permissions.
                    while (permissions.hasMore())
                        {
                        if (DEBUG_FLAG) System.out.println("  STEP : deleting permission") ;
                        database.remove(permissions.next()) ;
                        }
                    //
                    // Delete the group memberships.
                    while (members.hasMore())
                        {
                        if (DEBUG_FLAG) System.out.println("  STEP : deleting membership") ;
                        database.remove(members.next()) ;
                        }
                    //
                    // Delete the Group.
                    while (groups.hasMore())
                        {
                        if (DEBUG_FLAG) System.out.println("  STEP : deleting group") ;
                        database.remove(groups.next()) ;
                        }
                    //
                    // Delete the Account.
                    database.remove(account) ;
                    }
                if (DEBUG_FLAG) System.out.println("  PASS : finished deleting") ;
                //
                // Commit the transaction.
                database.commit() ;
                if (DEBUG_FLAG) System.out.println("  PASS : done commit") ;
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
                    "AccountManagerImpl.delAccount()"
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
			// Return the original Account details.
	        return account ;
            }
        //
        // If the ident is not local.
        else {
            //
            // Throw a new Exception.
            throw new CommunityPolicyException(
                "Account is not local",
                ident.toString()
                ) ;
            }
        }

    /**
     * Request a list of local Accounts.
     * @return An array of AccountData objects.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @todo Return empty array rather than null.
     *
     */
    public Object[] getLocalAccounts()
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerImpl.getLocalAccounts()") ;
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
            while (results.hasMore())
                {
                collection.add(
                    (AccountData)results.next()
                    ) ;
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
	 * Allocate VoSpace space for an Account.
	 * @param account The AccountData to update.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityServiceException If the service is unable to allocate the space.
	 *
	 */
	protected void allocateSpace(AccountData account)
        throws CommunityServiceException, CommunityIdentifierException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AccountManagerImpl.allocateSpace()") ;
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
        // Check for null identifier.
        if (null == account.getIdent())
            {
            throw new CommunityIdentifierException(
                "Null account identifier"
                ) ;
            }
		//
		// If the Account home space is not set.
		if (null == account.getHomeSpace())
			{
			//
			// If we have a default home space.
			Ivorn service = this.getDefaultVoSpace() ;
			if (null != service)
				{
				//
				// Try calling the VoSpace client to create the space.
				try {
					//
					// Create a new VoSpaceClient.
					VoSpaceClient resolver = new VoSpaceClient(null) ;
					//
					// Allocate the new home space.
					Ivorn ivorn = resolver.createUser(
						service,
						account.getIdent()
						) ;
					//
					// Update the Account data
					account.setHomeSpace(
						ivorn.toString()
						) ;
					}
				catch (Throwable ouch)
					{
		            throw new CommunityServiceException(
		                "Unable to create VoSpace",
						ouch
		                ) ;
					}
				}
			}
		}

	/**
	 * The config property key for our default VoSpace ivorn.
	 *
	 */
	private static final String DEFAULT_VOSPACE_PROPERTY = "org.astrogrid.community.default.vospace" ;

    /**
     * Get the default VoSpace ivorn.
     * @return An Ivorn for the default VoSpace service.
	 * @throws CommunityServiceException If unable to get the VoSpace ivorn.
	 *
     */
    public Ivorn getDefaultVoSpace()
		throws CommunityServiceException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerImpl.getDefaultVoSpace()") ;
		//
		// Get the default identifier from our config.
		String string = null ;
		try {
	        string = (String) config.getProperty(DEFAULT_VOSPACE_PROPERTY) ;
            }
		catch (PropertyNotFoundException ouch)
			{
            throw new CommunityServiceException(
                "Default VoSpace not configured"
                ) ;
			}
        //
        // If we found the default identifier.
        if (null != string)
            {
            //
            // Try making it into an Ivorn.
            try {
            	return new Ivorn(string) ;
				}
			catch (Exception ouch)
				{
	            throw new CommunityServiceException(
	                "Unable to convert default VoSpace into Ivorn",
					ouch
	                ) ;
				}
            }
        //
        // If we didn't find the local ident.
        else {
            throw new CommunityServiceException(
                "Default VoSpace not configured"
                ) ;
            }
        }
    }
