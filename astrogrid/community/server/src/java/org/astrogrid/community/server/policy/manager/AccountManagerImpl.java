/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/policy/manager/AccountManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/05 17:19:59 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: AccountManagerImpl.java,v $
 *   Revision 1.6  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.5.2.3  2004/02/23 19:43:47  dave
 *   Refactored DatabaseManager tests to test the interface.
 *   Refactored DatabaseManager tests to use common DatabaseManagerTest.
 *
 *   Revision 1.5.2.2  2004/02/23 08:55:20  dave
 *   Refactored CastorDatabaseConfiguration into DatabaseConfiguration
 *
 *   Revision 1.5.2.1  2004/02/22 20:03:16  dave
 *   Removed redundant DatabaseConfiguration interfaces
 *
 *   Revision 1.5  2004/02/20 21:11:05  dave
 *   Merged development branch, dave-dev-200402120832, into HEAD
 *
 *   Revision 1.4.2.2  2004/02/19 21:09:26  dave
 *   Refactored ServiceStatusData into a common package.
 *   Refactored CommunityServiceImpl constructor to take a parent service.
 *   Refactored default database for CommunityServiceImpl
 *
 *   Revision 1.4.2.1  2004/02/16 15:20:54  dave
 *   Changed tabs to spaces
 *
 *   Revision 1.4  2004/02/12 08:12:13  dave
 *   Merged development branch, dave-dev-200401131047, into HEAD
 *
 *   Revision 1.2.4.9  2004/02/06 13:49:09  dave
 *   Moved CommunityManagerBase into server.common.CommunityServer.
 *   Moved getServiceStatus into server.common.CommunityServer.
 *   Moved JUnit tests to match.
 *
 *   Revision 1.2.4.8  2004/01/27 18:55:08  dave
 *   Removed unused imports listed in PMD report
 *
 *   Revision 1.2.4.7  2004/01/27 05:52:27  dave
 *   Added GroupManagerTest
 *
 *   Revision 1.2.4.6  2004/01/27 05:19:17  dave
 *   Moved Exception logging into CommunityManagerBase
 *   Replaced if(null == database) with DatabaseNotFoundException
 *
 *   Revision 1.2.4.5  2004/01/27 03:54:28  dave
 *   Changed database name to database config in CommunityManagerBase
 *
 *   Revision 1.2.4.4  2004/01/26 23:23:23  dave
 *   Changed CommunityManagerImpl to use the new DatabaseManager.
 *   Moved rollback and close into CommunityManagerBase.
 *
 *   Revision 1.2.4.3  2004/01/26 15:16:57  dave
 *   Created CommunityManagerBase to handle database connections
 *
 *   Revision 1.2.4.2  2004/01/26 13:18:08  dave
 *   Added new DatabaseManager to enable local JUnit testing
 *
 *   Revision 1.2.4.1  2004/01/17 13:54:18  dave
 *   Removed password from AccountData
 *
 *   Revision 1.2  2004/01/07 10:45:45  dave
 *   Merged development branch, dave-dev-20031224, back into HEAD
 *
 *   Revision 1.1.2.2  2004/01/05 06:47:18  dave
 *   Moved policy data classes into policy.data package
 *
 *   Revision 1.1.2.1  2003/12/24 05:54:48  dave
 *   Initial Maven friendly structure (only part of the service implemented)
 *
 *   Revision 1.17  2003/11/06 15:35:26  dave
 *   Replaced tabs with spaces
 *
 *   Revision 1.16  2003/10/10 13:27:51  KevinBenson
 *   commented out the setPassword(null)
 *
 *   Revision 1.15  2003/10/09 01:38:30  dave
 *   Added JUnite tests for policy delegates
 *
 *   Revision 1.14  2003/10/07 20:40:25  KevinBenson
 *   conforms to new myspace delegate
 *
 *   Revision 1.13  2003/09/30 16:07:30  pah
 *   removed the password nulling in getAccount() - it was being written back to the database
 *
 *   Revision 1.12  2003/09/24 21:56:06  dave
 *   Added setPassword() to AccountManager
 *
 *   Revision 1.11  2003/09/17 19:47:21  dave
 *   1) Fixed classnotfound problems in the build.
 *   2) Added the JUnit task to add the initial accounts and groups.
 *   3) Got the build to work together with the portal.
 *   4) Fixed some bugs in the Account handling.
 *
 *   Revision 1.10  2003/09/17 09:16:10  KevinBenson
 *   Added the Myspace call
 *
 *   Revision 1.9  2003/09/15 16:05:45  KevinBenson
 *   *** empty log message ***
 *
 *   Revision 1.8  2003/09/13 02:18:52  dave
 *   Extended the jConfig configuration code.
 *
 *   Revision 1.7  2003/09/12 12:59:17  dave
 *   1) Fixed RemoteException handling in the manager and service implementations.
 *
 *   Revision 1.6  2003/09/10 17:21:43  dave
 *   Added remote functionality to groups.
 *
 *   Revision 1.5  2003/09/10 06:03:27  dave
 *   Added remote capability to Accounts
 *
 *   Revision 1.4  2003/09/09 16:48:48  KevinBenson
 *   added setpassword in their on the update
 *
 *   Revision 1.3  2003/09/09 10:57:47  dave
 *   Added corresponding SINGLE Group to addAccount and delAccount.
 *
 *   Revision 1.2  2003/09/08 20:28:50  dave
 *   Added CommunityIdent, with isLocal() and isValid()
 *
 *   Revision 1.1  2003/09/06 20:10:07  dave
 *   Split PolicyManager into separate components.
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

// TODO - resolve MySpace dependency
// import org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate;

import org.astrogrid.community.common.policy.data.GroupData ;
import org.astrogrid.community.common.policy.data.AccountData ;
import org.astrogrid.community.common.policy.data.GroupMemberData ;
import org.astrogrid.community.common.policy.data.CommunityIdent ;

import org.astrogrid.community.common.policy.manager.AccountManager ;

import org.astrogrid.community.common.config.CommunityConfig;
import org.astrogrid.community.server.service.CommunityServiceImpl ;
import org.astrogrid.community.server.database.configuration.DatabaseConfiguration ;

/**
 * A class to handle Accounts.
 * This needs refactoring to make it more robust.
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
     * The default public group.
     * This should be moved to the GroupManager.
     *
     */
    private static String DEFAULT_GROUP = "everyone" ;

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
     *
     */
    public AccountManagerImpl(DatabaseConfiguration config)
        {
        super(config) ;
        }

    /**
     * Public constructor, using a parent service.
     *
     */
    public AccountManagerImpl(CommunityServiceImpl parent)
        {
        super(parent) ;
        }

    /**
     * Create a new Account, given the Account name.
     *
     */
    public AccountData addAccount(String name)
        {
        return this.addAccount(new CommunityIdent(name)) ;
        }

    /**
     * Create a new Account, given the Account ident.
     * This needs refactoring to make it more robust.
     * TODO 1) If the 'everyone' group does not exist, then create it.
     * TODO 2) If the account group already exists (as an AccountGroup) then don't throw a duplicate exception.
     * TODO 3) If the account already belongs to the account group then don't throw a duplicate exception.
     * TODO 4) If the account already belongs to the 'everyone' group then don't throw a duplicate exception.
     * TODO 5) If the MySpace call adds the Account, store the MySpace server and Account details.
     *
     * If fragments of the account data already exist, e.g. groups, membership and permissions,
     * should we tidy them up before creating the new account or leave them as-is ?
     *
     */
    protected AccountData addAccount(CommunityIdent ident)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerImpl.addAccount()") ;
        if (DEBUG_FLAG) System.out.println("  ident : " + ident) ;

        Database    database = null ;
        AccountData account  = null ;
        GroupData   group    = null ;
        //
        // If the ident is valid.
        if (ident.isValid())
            {
            //
            // If the ident is local.
            if (ident.isLocal())
                {
                //
                // Create our new Account object.
                account = new AccountData() ;
                account.setIdent(ident.toString()) ;
                //
                // Create the corresponding Group object.
                group = new GroupData() ;
                group.setIdent(ident.toString()) ;
                group.setType(GroupData.SINGLE_TYPE) ;
                //
                // Add the account to the group.
                GroupMemberData groupmember = new GroupMemberData() ;
                groupmember.setAccount(ident.toString()) ;
                groupmember.setGroup(ident.toString()) ;
                //
                // Add the account to the guest group.
//
// TODO Need better 'default' group handling.
//
                GroupMemberData guestmember = new GroupMemberData() ;
                guestmember.setAccount(ident.toString()) ;
                guestmember.setGroup(new CommunityIdent(DEFAULT_GROUP)) ;
                //
                // Try performing our transaction.
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
                    // Commit the transaction.
                    database.commit() ;
                    }
                //
                // If we already have an object with that ident.
// TODO
// The only reason to treat this differently is that we might one day report it differently to the client.
                catch (DuplicateIdentityException ouch)
                    {
                    //
                    // Log the exception.
                    logException(ouch, "AccountManagerImpl.addAccount()") ;
                    //
                    // Set the response to null.
                    group   = null ;
                    account = null ;
                    //
                    // Cancel the database transaction.
                    rollbackTransaction(database) ;
                    }
                //
                // If anything else went bang.
                catch (Exception ouch)
                    {
                    //
                    // Log the exception.
                    logException(ouch, "AccountManagerImpl.addAccount()") ;
                    //
                    // Set the response to null.
                    group   = null ;
                    account = null ;
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
                }
            //
            // If the ident is not local.
            else {
                //
                // Set the response to null.
                group   = null ;
                account = null ;
                }
            }
            //
            // If the ident is not valid.
        else {
            //
            // Set the response to null.
            group   = null ;
            account = null ;
            }

        if (DEBUG_FLAG) System.out.println("----\"----") ;

//
// Need to store the MySpace URL.
//
        //
        // Add the user to the local myspace.
        if(account != null)
            {
            String myspaceUrl = CommunityConfig.getProperty("myspace.url.webservice");
            //
            // If we have a local MySpace service.
            if ((null != myspaceUrl) && (myspaceUrl.length() > 0))
                {
/*
 * TODO - resolve MySpace dependency
                try {
                    MySpaceManagerDelegate msmd = new MySpaceManagerDelegate(myspaceUrl);
                    Vector tempVector = new Vector();
                    tempVector.add("serv1");
                    msmd.createUser(ident.getName(),ident.getCommunity(),"credential",tempVector);
                    }
                catch(Exception ouch)
                    {
                    ouch.printStackTrace();
                    }
 *
 */
                }
            }
        return account ;
        }

    /**
     * Request an Account data, given the Account name.
     *
     */
    public AccountData getAccount(String name)
        {
        return this.getAccount(new CommunityIdent(name)) ;
        }

    /**
     * Request an Account data, given the Account ident.
     *
     */
    protected AccountData getAccount(CommunityIdent ident)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerImpl.getAccount()") ;
        if (DEBUG_FLAG) System.out.println("  ident : " + ident) ;

        Database    database = null ;
        AccountData account  = null ;
        //
        // If the ident is valid.
        if (ident.isValid())
            {
            //
            // If the ident is local.
            if (ident.isLocal())
                {
                try {
                    //
                    // Open our database connection.
                    database = this.getDatabase() ;
                    //
                    // Begin a new database transaction.
                    database.begin();
                    //
                    // Load the Account from the database.
                    account = (AccountData) database.load(AccountData.class, ident.toString()) ;
                    //
                    // Commit the transaction.
                    database.commit() ;
                    }
                //
                // If we couldn't find the object.
// TODO
// The only reason to treat this differently is that we might one day report it differently to the client.
                catch (ObjectNotFoundException ouch)
                    {
                    //
                    // Log the exception.
                    logException(ouch, "AccountManagerImpl.getAccount()") ;
                    //
                    // Set the response to null.
                    account = null ;
                    //
                    // Cancel the database transaction.
                    rollbackTransaction(database) ;
                    }
                //
                // If anything else went bang.
                catch (Exception ouch)
                    {
                    //
                    // Log the exception.
                    logException(ouch, "AccountManagerImpl.getAccount()") ;
                    //
                    // Set the response to null.
                    account = null ;
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
                }
            //
            // If the ident is not local.
            else {
                //
                // Set the response to null.
                account = null ;
                }
            }
            //
            // If the ident is not valid.
        else {
            //
            // Set the response to null.
            account = null ;
            }
        // TODO
        // Need to return something to the client.
        // Possibly a new DataObject ... ?
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        return account ;
        }

    /**
     * Update an existing Account data.
     *
     */
    public AccountData setAccount(AccountData account)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerImpl.setAccount()") ;
        if (DEBUG_FLAG) System.out.println("  Account") ;
        if (DEBUG_FLAG) System.out.println("    ident : " + account.getIdent()) ;
        if (DEBUG_FLAG) System.out.println("    desc  : " + account.getDescription()) ;
        //
        // Create a CommunityIdent from the account.
        CommunityIdent ident = new CommunityIdent(account.getIdent()) ;
        Database database = null ;
        //
        // If the ident is valid.
        if (ident.isValid())
            {
            //
            // If the ident is local.
            if (ident.isLocal())
                {
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
                    // Load the Account from the database.
                    AccountData data = (AccountData) database.load(AccountData.class, account.getIdent()) ;
                    //
                    // Update the account data.
                    data.setDescription(account.getDescription()) ;
                    //
                    // Commit the transaction.
                    database.commit() ;
                    }
                //
                // If we couldn't find the object.
// TODO
// The only reason to treat this differently is that we might one day report it differently to the client.
                catch (ObjectNotFoundException ouch)
                    {
                    //
                    // Log the exception.
                    logException(ouch, "AccountManagerImpl.setAccount()") ;
                    //
                    // Set the response to null.
                    account = null ;
                    //
                    // Cancel the database transaction.
                    rollbackTransaction(database) ;
                    }
                //
                // If anything else went bang.
                catch (Exception ouch)
                    {
                    //
                    // Log the exception.
                    logException(ouch, "AccountManagerImpl.setAccount()") ;
                    //
                    // Set the response to null.
                    account = null ;
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
                }
            //
            // If the ident is not local.
            else {
                //
                // Set the response to null.
                account = null ;
                }
            }
            //
            // If the ident is not valid.
        else {
            //
            // Set the response to null.
            account = null ;
            }

        // TODO
        // Need to return something to the client.
        // Possibly a new DataObject ... ?
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        return account ;
        }

    /**
     * Delete an Account, given the Account name.
     *
     */
    public AccountData delAccount(String name)
        {
        return this.delAccount(new CommunityIdent(name)) ;
        }

    /**
     * Delete an Account, given the Account ident.
     *
     */
    protected AccountData delAccount(CommunityIdent ident)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("AccountManagerImpl.delAccount()") ;
        if (DEBUG_FLAG) System.out.println("  ident : " + ident) ;
        Database    database = null ;
        AccountData account  = null ;
        //
        // If the ident is valid.
        if (ident.isValid())
            {
            if (DEBUG_FLAG) System.out.println("  PASS : ident is valid") ;
            //
            // If the ident is local.
            if (ident.isLocal())
                {
                if (DEBUG_FLAG) System.out.println("  PASS : ident is local") ;
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
                    // Load the Account from the database.
                    account = (AccountData) database.load(AccountData.class, ident.toString()) ;
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
                        groupQuery.bind(ident.toString()) ;
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
                        memberQuery.bind(ident.toString()) ;
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
                        permissionQuery.bind(ident.toString()) ;
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
                // If anything went bang.
                catch (Exception ouch)
                    {
                    //
                    // Log the exception.
                    logException(ouch, "AccountManagerImpl.delAccount()") ;
                    //
                    // Set the response to null.
                    account = null ;
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
                }
            //
            // If the ident is not local.
            else {
                //
                // Set the response to null.
                account = null ;
//
// TODO
// Actually, this could be a call from a remote community to tell us that it is deleting an Account.
// In which case, we need to tidy up our groups and permissions tables ....
//
                }
            }
            //
            // If the ident is not valid.
        else {
            account = null ;
            }

//
// TODO .. should we notify other community services that the Account has gone ?
//

//
// TODO
// Should this be inside our database transaction ?
        String myspaceUrl = CommunityConfig.getProperty("myspace.url.webservice");
        //
        // If we have a local MySpace service.
        if ((null != myspaceUrl) && (myspaceUrl.length() > 0))
            {
/*
 * TODO - resolve MySpace dependency
            MySpaceManagerDelegate msmd = new MySpaceManagerDelegate(myspaceUrl);
            try {
                msmd.deleteUser(ident.getName(),ident.getCommunity(),"credential");
                }
            catch(Exception ouch)
                {
                ouch.printStackTrace();
                }
 *
 */
            }

        // TODO
        // Need to return something to the client.
        // Possibly a new DataObject ... ?
        if (DEBUG_FLAG) System.out.println("----\"----") ;

        return account ;
        }

    /**
     * Request a list of local Accounts.
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
            AccountData ad = (AccountData)results.next();
                collection.add(ad) ;
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
        // TODO
        // Need to return something to the client.
        // Possibly a new DataObject ... ?
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        return array ;
        }
    }
