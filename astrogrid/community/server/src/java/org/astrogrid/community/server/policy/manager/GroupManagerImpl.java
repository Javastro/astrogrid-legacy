/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/policy/manager/Attic/GroupManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/30 01:40:03 $</cvs:date>
 * <cvs:version>$Revision: 1.8 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: GroupManagerImpl.java,v $
 *   Revision 1.8  2004/03/30 01:40:03  dave
 *   Merged development branch, dave-dev-200403242058, into HEAD
 *
 *   Revision 1.7.4.1  2004/03/28 09:11:43  dave
 *   Convert tabs to spaces
 *
 *   Revision 1.7  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.6.18.3  2004/03/22 00:53:31  dave
 *   Refactored GroupManager to use Ivorn identifiers.
 *   Started removing references to CommunityManager.
 *
 *   Revision 1.6.18.2  2004/03/21 18:14:29  dave
 *   Refactored GroupManagerImpl to use Ivorn identifiers.
 *
 *   Revision 1.6.18.1  2004/03/21 06:41:41  dave
 *   Refactored to include Exception handling.
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

import org.exolab.castor.persist.spi.Complex ;

import org.astrogrid.community.common.policy.data.GroupData ;
import org.astrogrid.community.common.policy.data.GroupMemberData ;

import org.astrogrid.community.common.ivorn.CommunityIvornParser ;

import org.astrogrid.community.common.policy.manager.GroupManager ;

import org.astrogrid.community.server.service.CommunityServiceImpl ;
import org.astrogrid.community.server.database.configuration.DatabaseConfiguration ;

import org.astrogrid.community.common.exception.CommunityPolicyException     ;
import org.astrogrid.community.common.exception.CommunityServiceException    ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

// TODO remove these
import org.astrogrid.community.common.policy.data.CommunityIdent ;


public class GroupManagerImpl
    extends CommunityServiceImpl
    implements GroupManager
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
    public GroupManagerImpl()
        {
        super() ;
        }

    /**
     * Public constructor, using specific database configuration.
     * @param config A specific DatabaseConfiguration.
     *
     */
    public GroupManagerImpl(DatabaseConfiguration config)
        {
        super(config) ;
        }

    /**
     * Public constructor, using a parent service.
     * @param parent A parent CommunityServiceImpl.
     *
     */
    public GroupManagerImpl(CommunityServiceImpl parent)
        {
        super(parent) ;
        }

    /**
     * Add a new Group, given the Group ident.
     * @param  ident The Group identifier.
     * @return A GroupData for the Group.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is already in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public GroupData addGroup(String ident)
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        return this.addGroup(
            new GroupData(
                ident
                )
            ) ;
        }

    /**
     * Add a new Group, given the Group ident.
     * @param  ident The Group identifier.
     * @return An GroupData for the Group.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is already in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
    protected GroupData addGroup(CommunityIdent ident)
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupManagerImpl.addGroup()") ;
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
        // Create our new Group object.
        return this.addGroup(
            new GroupData(
                ident.toString()
                )
            ) ;
        }
     */

    /**
     * Add a new Group, given the Group data.
     * @param  group The GroupData to add.
     * @return A new GroupData for the Group.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not local.
     * @throws CommunityPolicyException If the identifier is already in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @todo Verify that the finally gets executed, even if a new Exception is thrown.
     * @todo Prevent private groups.
     *
     */
    public GroupData addGroup(GroupData group)
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupManagerImpl.addGroup()") ;
        if (DEBUG_FLAG) System.out.println("  Group : " + ((null != group) ? group.getIdent() : null)) ;
        //
        // Check for null group.
        if (null == group)
            {
            throw new CommunityIdentifierException(
                "Null group"
                ) ;
            }
        //
        // Get the Group ident.
        CommunityIvornParser ident = new CommunityIvornParser(
            group.getIdent()
            ) ;
        //
        // Set the Group ident.
        group.setIdent(
            ident.getAccountIdent()
            ) ;
        //
        // If the ident is local.
        if (ident.isLocal())
            {
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
                // Try creating the group in the database.
                database.create(group);
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
                    "Duplicate Group already exists",
                    ident.getAccountIdent()
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
                    "GroupManagerImpl.addGroup()"
                    ) ;
                //
                // Cancel the database transaction.
                rollbackTransaction(database) ;
                //
                // Throw a new Exception.
                throw new CommunityServiceException(
                    "Database transaction failed",
                    ident.getAccountIdent(),
                    ouch
                    ) ;
                }
            //
            // Close our database connection.
            finally
                {
                closeConnection(database) ;
                }
            return group ;
            }
        //
        // If the ident is not local.
        else {
            throw new CommunityPolicyException(
                "Group is not local",
                ident.getAccountIdent()
                ) ;
            }
        }

    /**
     * Request a Group details, given the Group ident.
     * @param  ident The Group identifier.
     * @return An GroupData for the Group.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @todo Refactor to use Ivorn identifiers
     *
     */
    public GroupData getGroup(String ident)
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        return this.getGroup(
            new CommunityIvornParser(
                ident
                )
            ) ;
        }

    /**
     * Request a Group details, given the Group ident.
     * @param  ident The Group identifier.
     * @return An GroupData for the Group.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not local.
     * @throws CommunityPolicyException If the identifier is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @todo Verify that the finally gets executed, even if a new Exception is thrown.
     *
     */
    protected GroupData getGroup(CommunityIvornParser ident)
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupManagerImpl.getGroup()") ;
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
            Database  database = null ;
            GroupData group    = null ;
            try {
                //
                // Open our database connection.
                database = this.getDatabase() ;
                //
                // Begin a new database transaction.
                database.begin();
                //
                // Load the Group from the database.
                group = (GroupData) database.load(GroupData.class, ident.getAccountIdent()) ;
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
                    "Group not found",
                    ident.getAccountIdent()
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
                    "GroupManagerImpl.getGroup()"
                    ) ;
                //
                // Cancel the database transaction.
                rollbackTransaction(database) ;
                //
                // Throw a new Exception.
                throw new CommunityServiceException(
                    "Database transaction failed",
                    ident.getAccountIdent(),
                    ouch
                    ) ;
                }
            //
            // Close our database connection.
            finally
                {
                closeConnection(database) ;
                }
            return group ;
            }
        //
        // If the ident is not local.
        else {
            throw new CommunityPolicyException(
                "Group is not local",
                ident.getAccountIdent()
                ) ;
            }
        }

    /**
     * Update a Group.
     * @param  account The new GroupData to update.
     * @return A new GroupData for the Group.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not local.
     * @throws CommunityPolicyException If the identifier is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @todo Verify that the finally gets executed, even if a new Exception is thrown.
     *
     */
    public GroupData setGroup(GroupData group)
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupManagerImpl.setGroup()") ;
        if (DEBUG_FLAG) System.out.println("  Group : " + ((null != group) ? group.getIdent() : null)) ;
        //
        // Check for null group.
        if (null == group)
            {
            throw new CommunityIdentifierException(
                "Null group"
                ) ;
            }
        //
        // Get the Group ident.
        CommunityIvornParser ident = new CommunityIvornParser(
            group.getIdent()
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
                // Open our database connection.
                database = this.getDatabase() ;
                //
                // Begin a new database transaction.
                database.begin();
                //
                // Load the Group from the database.
                GroupData data = (GroupData) database.load(GroupData.class, ident.getAccountIdent()) ;
                //
                // Update the group data.
                data.setDisplayName(group.getDisplayName()) ;
                data.setDescription(group.getDescription()) ;
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
                    "Group not found",
                    ident.getAccountIdent()
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
                    "GroupManagerImpl.setGroup()"
                    ) ;
                //
                // Cancel the database transaction.
                rollbackTransaction(database) ;
                //
                // Throw a new Exception.
                throw new CommunityServiceException(
                    "Database transaction failed",
                    ident.getAccountIdent(),
                    ouch
                    ) ;
                }
            //
            // Close our database connection.
            finally
                {
                closeConnection(database) ;
                }
            return group ;
            }
        //
        // If the ident is not local.
        else {
            throw new CommunityPolicyException(
                "Group is not local",
                ident.getAccountIdent()
                ) ;
            }
        }

    /**
     * Delete a Group.
     * @param  ident The Group identifier.
     * @return The GroupData for the old Group.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public GroupData delGroup(String ident)
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        return this.delGroup(
            new CommunityIvornParser(
                ident
                )
            ) ;
        }

    /**
     * Delete a Group.
     * @param  ident The Group identifier.
     * @return The GroupData for the old Group.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not local.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @todo Need to have a mechanism for notifying other Communities that the Group has been deleted.
     * @todo Verify that the finally gets executed, even if a new Exception is thrown.
     * @todo Refactor to use Ivorn identifiers.
     * @todo Prevent the client from deleting a private Group if the Account still exists.
     *
     */
    protected GroupData delGroup(CommunityIvornParser ident)
        throws CommunityServiceException, CommunityIdentifierException, CommunityPolicyException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupManagerImpl.delGroup()") ;
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
        // If the ident is local.
        if (ident.isLocal())
            {
            //
            // Try update the database.
            GroupData group    = null ;
            Database  database = null ;
            try {
                //
                // Open our database connection.
                database = this.getDatabase() ;
                //
                // Begin a new database transaction.
                database.begin();
                //
                // Load the Group from the database.
                group = (GroupData) database.load(GroupData.class, ident.getAccountIdent()) ;
                //
                // Delete the Group.
                database.remove(group) ;
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
                    "Group not found",
                    ident.getAccountIdent()
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
                    "GroupManagerImpl.delGroup()"
                    ) ;
                //
                // Cancel the database transaction.
                rollbackTransaction(database) ;
                //
                // Throw a new Exception.
                throw new CommunityServiceException(
                    "Database transaction failed",
                    ident.getAccountIdent(),
                    ouch
                    ) ;
                }
            //
            // Close our database connection.
            finally
                {
                closeConnection(database) ;
                }
            return group ;
            }
        //
        // If the ident is not local.
        else {
            throw new CommunityPolicyException(
                "Group is not local",
                ident.getAccountIdent()
                ) ;
            }
        }

    /**
     * Request a list of local Groups.
     * @return An array of GroupData objects.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @todo Return empty array rather than null.
     *
     */
    public Object[] getLocalGroups()
        throws CommunityServiceException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupManagerImpl.getLocalGroups()") ;
        //
        // Try to query the database.
        Object[] array = null ;
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
                "SELECT groups FROM org.astrogrid.community.policy.data.GroupData groups"
                );
//
// TODO
// Should only request MULTI groups.
//
            //
            // Execute our query.
            QueryResults results = query.execute();
            //
            // Transfer our results to a vector.
            Collection collection = new Vector() ;
            while (results.hasMore())
                {
                collection.add(results.next()) ;
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
            logException(
                ouch,
                "GroupManagerImpl.getLocalGroups()"
                ) ;
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            //
            // Throw a new Exception.
            throw new CommunityServiceException(
                "Database transaction failed",
                ouch
                ) ;
            }
        //
        // Close our database connection.
        finally
            {
            closeConnection(database) ;
            }
        return array ;
        }

    /**
     * Add an Account to a Group.
     * Group must be local, but Account can be local or remote.
     * @param account The Account identifier.
     * @param group   The Group identifier.
     * @return A GroupMemberData for the membership.
     * @throws CommunityIdentifierException If one of the identifiers is not valid.
     * @throws CommunityPolicyException If the group is not local.
     * @throws CommunityPolicyException If the membership already exists.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public GroupMemberData addGroupMember(String account, String group)
        throws CommunityServiceException, CommunityPolicyException, CommunityIdentifierException
        {
        return this.addGroupMember(
            new CommunityIvornParser(
                account
                ),
            new CommunityIvornParser(
                group
                )
            ) ;
        }

    /**
     * Add an Account to a Group.
     * Group must be local, but Account can be local or remote.
     * @param account The Account identifier.
     * @param group   The Group identifier.
     * @return A GroupMemberData for the membership.
     * @throws CommunityIdentifierException If one of the identifiers is not valid.
     * @throws CommunityPolicyException If the group is not local.
     * @throws CommunityPolicyException If the membership already exists.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @todo Check the actually exists.
     * @todo Check the group isn't a private group.
     *
     */
    protected GroupMemberData addGroupMember(CommunityIvornParser account, CommunityIvornParser group)
        throws CommunityServiceException, CommunityPolicyException, CommunityIdentifierException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupManagerImpl.addGroupMember()") ;
        if (DEBUG_FLAG) System.out.println("  account  : " + account) ;
        if (DEBUG_FLAG) System.out.println("  group    : " + group) ;
        //
        // Check for null account.
        if (null == account)
            {
            throw new CommunityIdentifierException(
                "Null account"
                ) ;
            }
        //
        // Check for null group.
        if (null == group)
            {
            throw new CommunityIdentifierException(
                "Null group"
                ) ;
            }
//
// Check the group isn't an account group.
//
        //
        // If the group is local.
        if (group.isLocal())
            {
            //
            // Create our new GroupMemberData.
            GroupMemberData member = new GroupMemberData() ;
            member.setAccount(
                account.getAccountIdent()
                ) ;
            member.setGroup(
                group.getAccountIdent()
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
                // Try creating the record in the database.
                database.create(member);
                //
                // Commit the transaction.
                database.commit() ;
                }
            //
            // If the account is already a member of this group.
            catch (DuplicateIdentityException ouch)
                {
                //
                // Cancel the database transaction.
                rollbackTransaction(database) ;
                //
                // Throw a new Exception.
                throw new CommunityPolicyException(
                    "GroupMembership already exists",
                    (account.getAccountIdent() + "|" + group.getAccountIdent())
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
                    "GroupManagerImpl.addGroupMember()"
                    ) ;
                //
                // Cancel the database transaction.
                rollbackTransaction(database) ;
                //
                // Throw a new Exception.
                throw new CommunityServiceException(
                    "Database transaction failed",
                    ouch
                    ) ;
                }
            //
            // Close our database connection.
            finally
                {
                closeConnection(database) ;
                }
            return member ;
            }
        //
        // If the group is not local.
        else {
            throw new CommunityPolicyException(
                "Group is not local",
                group.getAccountIdent()
                ) ;
            }
        }

    /**
     * Remove an Account from a Group.
     * Group must be local, but Account can be local or remote.
     * @param account The Account identifier.
     * @param group   The Group identifier.
     * @return A GroupMemberData for the membership.
     * @throws CommunityIdentifierException If one of the identifiers is not valid.
     * @throws CommunityPolicyException If the membership does not exist.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public GroupMemberData delGroupMember(String account, String group)
        throws CommunityServiceException, CommunityPolicyException, CommunityIdentifierException
        {
        return this.delGroupMember(
            new CommunityIvornParser(
                account
                ),
            new CommunityIvornParser(
                group
                )
            ) ;
        }

    /**
     * Remove an Account from a Group.
     * Group must be local, but Account can be local or remote.
     * @param account The Account identifier.
     * @param group   The Group identifier.
     * @return A GroupMemberData for the membership.
     * @throws CommunityIdentifierException If one of the identifiers is not valid.
     * @throws CommunityPolicyException If the membership does not exist.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @todo Check the group isn't a private group.
     *
     */
    protected GroupMemberData delGroupMember(CommunityIvornParser account, CommunityIvornParser group)
        throws CommunityServiceException, CommunityPolicyException, CommunityIdentifierException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupManagerImpl.delGroupMember()") ;
        if (DEBUG_FLAG) System.out.println("  account  : " + account) ;
        if (DEBUG_FLAG) System.out.println("  group    : " + group) ;
        //
        // Check for null account.
        if (null == account)
            {
            throw new CommunityIdentifierException(
                "Null account"
                ) ;
            }
        //
        // Check for null group.
        if (null == group)
            {
            throw new CommunityIdentifierException(
                "Null group"
                ) ;
            }
        //
        // No checks if the account ident is valid.
        // Still want to delete the record even if the ident is invalid.
        //
//
// Check the group isn't an account group.
// But, still proceed if the group does not exist.
// There is a potential mix up here.
// 1) Create a group called 'frog'
// 2) Add members to the group.
// 3) Delete the group.
// 4) Create an account called 'frog'
// 4) Now have account with members, but can't delete them.
//
        //
        // Try update the database.
        GroupMemberData member   = null ;
        Database        database = null ;
        try {
            //
            // Open our database connection.
            database = this.getDatabase() ;
            //
            // Begin a new database transaction.
            database.begin();
            //
            // Create the database key.
            Complex key = new Complex(
                new Object[]
                    {
                    account.getAccountIdent(),
                    group.getAccountIdent()
                    }
                ) ;
            //
            // Load the GroupMember from the database.
            member = (GroupMemberData) database.load(GroupMemberData.class, key) ;
            //
            // Delete the record.
            database.remove(member) ;
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
                "GroupMembership not found",
                (account.getAccountIdent() + "|" + group.getAccountIdent())
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
                "GroupManagerImpl.delGroupMember()"
                ) ;
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            //
            // Throw a new Exception.
            throw new CommunityServiceException(
                "Database transaction failed",
                ouch
                ) ;
            }
        //
        // Close our database connection.
        finally
            {
            closeConnection(database) ;
            }
        return member ;
        }

    /**
     * Request a Group Membership details.
     * Group must be local, but Account can be local or remote.
     * @param account The Account identifier.
     * @param group   The Group identifier.
     * @return A GroupMemberData for the membership.
     * @throws CommunityIdentifierException If one of the identifiers is not valid.
     * @throws CommunityPolicyException If the group is not local.
     * @throws CommunityPolicyException If the membership does not exist.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public GroupMemberData getGroupMember(String account, String group)
        throws CommunityServiceException, CommunityPolicyException, CommunityIdentifierException
        {
        return this.getGroupMember(
            new CommunityIvornParser(
                account
                ),
            new CommunityIvornParser(
                group
                )
            ) ;
        }

    /**
     * Request a Group Membership details.
     * Group must be local, but Account can be local or remote.
     * @param account The Account identifier.
     * @param group   The Group identifier.
     * @return A GroupMemberData for the membership.
     * @throws CommunityIdentifierException If one of the identifiers is not valid.
     * @throws CommunityPolicyException If the group is not local.
     * @throws CommunityPolicyException If the membership does not exist.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    protected GroupMemberData getGroupMember(CommunityIvornParser account, CommunityIvornParser group)
        throws CommunityServiceException, CommunityPolicyException, CommunityIdentifierException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupManagerImpl.getGroupMember()") ;
        if (DEBUG_FLAG) System.out.println("  account  : " + account) ;
        if (DEBUG_FLAG) System.out.println("  group    : " + group) ;
        //
        // Check for null account.
        if (null == account)
            {
            throw new CommunityIdentifierException(
                "Null account"
                ) ;
            }
        //
        // Check for null group.
        if (null == group)
            {
            throw new CommunityIdentifierException(
                "Null group"
                ) ;
            }
        //
        // If the group is local.
        if (group.isLocal())
            {
            GroupMemberData member   = null ;
            Database        database = null ;
            try {
                //
                // Open our database connection.
                database = this.getDatabase() ;
                //
                // Begin a new database transaction.
                database.begin();
                //
                // Create the database key.
                Complex key = new Complex(
                    new Object[]
                        {
                        account.getAccountIdent(),
                        group.getAccountIdent()
                        }
                    ) ;
                //
                // Load the GroupMember from the database.
                member = (GroupMemberData) database.load(GroupMemberData.class, key) ;
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
                    "GroupMembership not found",
                    (account.getAccountIdent() + "|" + group.getAccountIdent())
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
                    "GroupManagerImpl.getGroupMember()"
                    ) ;
                //
                // Cancel the database transaction.
                rollbackTransaction(database) ;
                //
                // Throw a new Exception.
                throw new CommunityServiceException(
                    "Database transaction failed",
                    ouch
                    ) ;
                }
            //
            // Close our database connection.
            finally
                {
                closeConnection(database) ;
                }
            return member ;
            }
        //
        // If the group is not local.
        else {
            throw new CommunityPolicyException(
                "Group is not local",
                group.getAccountIdent()
                ) ;
            }
        }

    /**
     * Request a list of Group Members.
     * @param group The Group identifier.
     * @return An array of GroupMemberData objects.
     * @throws CommunityIdentifierException If one of the identifiers is not valid.
     * @throws CommunityPolicyException If the group is not local.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public Object[] getGroupMembers(String group)
        throws CommunityServiceException, CommunityPolicyException, CommunityIdentifierException
        {
        return this.getGroupMembers(
            new CommunityIvornParser(
                group
                )
            ) ;
        }

    /**
     * Request a list of Group Members.
     * @param group The Group identifier.
     * @return An array of GroupMemberData objects.
     * @throws CommunityIdentifierException If one of the identifiers is not valid.
     * @throws CommunityPolicyException If the group is not local.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @todo Return empty array rather than null.
     *
     */
    protected Object[] getGroupMembers(CommunityIvornParser group)
        throws CommunityServiceException, CommunityPolicyException, CommunityIdentifierException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupManagerImpl.getGroupMembers()") ;
        if (DEBUG_FLAG) System.out.println("  group    : " + group) ;
        //
        // Check for null group.
        if (null == group)
            {
            throw new CommunityIdentifierException(
                "Null group"
                ) ;
            }
        Object[] array    = null ;
        Database database = null ;
        //
        // If the Group is local.
        if (group.isLocal())
            {
            if (DEBUG_FLAG) System.out.println("PASS : Group is local") ;
            //
            // Try to query the database.
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
                    "SELECT members FROM org.astrogrid.community.policy.data.GroupMemberData members WHERE members.group = $1"
                    );
                //
                // Bind the query param.
                query.bind(group.getAccountIdent()) ;
                //
                // Execute our query.
                QueryResults results = query.execute();
                //
                // Transfer our results to a vector.
                Collection collection = new Vector() ;
                while (results.hasMore())
                    {
                    collection.add(results.next()) ;
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
                logException(
                    ouch,
                    "GroupManagerImpl.getGroupMembers()"
                    ) ;
                //
                // Cancel the database transaction.
                rollbackTransaction(database) ;
                //
                // Throw a new Exception.
                throw new CommunityServiceException(
                    "Database transaction failed",
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
        //
        // If the group is not local.
        else {
            throw new CommunityPolicyException(
                "Group is not local",
                group.getAccountIdent()
                ) ;
            }
        return array;
        }

    /**
     * Request a list of Groups that an Account belongs to.
     * @param account The Account identifier.
     * @return An array of GroupMemberData objects.
     * @throws CommunityIdentifierException If one of the identifiers is not valid.
     * @throws CommunityServiceException If there is an internal error in the service.
     *
     */
    public Object[] getLocalAccountGroups(String account)
        throws CommunityServiceException, CommunityIdentifierException
        {
        return this.getLocalAccountGroups(
            new CommunityIvornParser(
                account
                )
            ) ;
        }

    /**
     * Request a list of Groups that an Account belongs to.
     * @param account The Account identifier.
     * @return An array of GroupMemberData objects.
     * @throws CommunityIdentifierException If one of the identifiers is not valid.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @todo Return empty array rather than null.
     *
     */
    protected Object[] getLocalAccountGroups(CommunityIvornParser account)
        throws CommunityServiceException, CommunityIdentifierException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupManagerImpl.getLocalAccountGroups()") ;
        if (DEBUG_FLAG) System.out.println("  account   : " + account) ;
        //
        // Check for null account.
        if (null == account)
            {
            throw new CommunityIdentifierException(
                "Null account"
                ) ;
            }
        Object[] array    = null ;
        Database database = null ;
        //
        // Try to query the database.
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
                "SELECT members FROM org.astrogrid.community.policy.data.GroupMemberData members WHERE members.account = $1"
                );
            //
            // Bind the query param.
            query.bind(account.getAccountIdent()) ;
            //
            // Execute our query.
            QueryResults results = query.execute();
            //
            // Transfer our results to a vector.
            Collection collection = new Vector() ;
            while (results.hasMore())
                {
                collection.add(results.next()) ;
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
            logException(
                ouch,
                "GroupManagerImpl.getLocalAccountGroups()"
                ) ;
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            //
            // Throw a new Exception.
            throw new CommunityServiceException(
                "Database transaction failed",
                ouch
                ) ;
            }
        //
        // Close our database connection.
        finally
            {
            closeConnection(database) ;
            }
        return array ;
        }
    }
