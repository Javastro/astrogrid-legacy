/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/GroupManagerMock.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.7 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: GroupManagerMock.java,v $
 *   Revision 1.7  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.6.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.6  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.5.36.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.manager ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.util.Map ;
import java.util.HashMap ;

import org.astrogrid.community.common.policy.data.GroupData ;
import org.astrogrid.community.common.service.CommunityServiceMock ;

import org.astrogrid.community.common.exception.CommunityPolicyException     ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

/**
 * Mock implementation of the GroupManager service.
 *
 */
public class GroupManagerMock
    extends CommunityServiceMock
    implements GroupManager
    {
    /**
     * Our debug logger.
     *
     */
	private static Log log = LogFactory.getLog(GroupManagerMock.class);

    /**
     * Public constructor.
     *
     */
    public GroupManagerMock()
        {
        super() ;
        }

    /**
     * Our hash table of values.
     *
     */
    private static Map map = new HashMap() ;

    /**
     * Reset our map.
     *
     */
    public static void reset()
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerMock.reset()") ;
        map.clear() ;
        }

    /**
     * Add a new Group, given the Group ident.
     * @param  ident The Group identifier.
     * @return An GroupData for the Group.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is already in the database.
     *
     */
    public GroupData addGroup(String ident)
        throws CommunityIdentifierException, CommunityPolicyException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerMock.addGroup()") ;
        log.debug("  Ident : " + ident) ;
        //
        // Check for null ident.
        if (null == ident)
            {
            throw new CommunityIdentifierException(
                "Null identifier"
                ) ;
            }
        //
        // Check if we already have an existing Group.
        if (map.containsKey(ident))
            {
            throw new CommunityPolicyException(
                "Duplicate group",
                ident
                ) ;
            }
        //
        // Create a new Group.
        GroupData group = new GroupData(ident) ;
        //
        // Add it to our map.
        map.put(group.getIdent(), group) ;
        //
        // Return the new Group.
        return group ;
        }

    /**
     * Add a new Group, given the Group data.
     * @param  data The GroupData to add.
     * @return A new GroupData for the Group.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is already in the database.
     *
     */
    public GroupData addGroup(GroupData group)
        throws CommunityIdentifierException, CommunityPolicyException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerMock.addGroup()") ;
        log.debug("  Group : " + ((null != group) ? group.getIdent() : null)) ;
        //
        // Check for null group.
        if (null == group)
            {
            throw new CommunityIdentifierException(
                "Null group"
                ) ;
            }
        //
        // Check for null ident.
        if (null == group.getIdent())
            {
            throw new CommunityIdentifierException(
                "Null identifier"
                ) ;
            }
        //
        // Check if we already have an existing Group.
        if (map.containsKey(group.getIdent()))
            {
            throw new CommunityPolicyException(
                "Duplicate group",
                group.getIdent()
                ) ;
            }
        //
        // Add it to our map.
        map.put(group.getIdent(), group) ;
        //
        // Return the new Group.
        return group ;
        }

    /**
     * Request a Group details, given the Group ident.
     * @param  ident The Group identifier.
     * @return An GroupData for the Group.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     *
     */
    public GroupData getGroup(String ident)
        throws CommunityIdentifierException, CommunityPolicyException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerMock.getGroup()") ;
        log.debug("  Ident : " + ident) ;
        //
        // Check for null ident.
        if (null == ident)
            {
            throw new CommunityIdentifierException(
                "Null identifier"
                ) ;
            }
        //
        // Lookup the Group in our map.
        GroupData group = (GroupData) map.get(ident) ;
        //
        // If we found a Group.
        if (null != group)
            {
            return group ;
            }
        //
        // If we didn't find a Group.
        else {
            throw new CommunityPolicyException(
                "Group not found",
                ident
                ) ;
            }
        }

    /**
     * Update a Group.
     * @param  update The new Group data to update.
     * @return A new GroupData for the Group.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     *
     */
    public GroupData setGroup(GroupData update)
        throws CommunityIdentifierException, CommunityPolicyException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerMock.setGroup()") ;
        log.debug("  Ident : " + ((null != update) ? update.getIdent() : null)) ;
        //
        // Check for null data.
        if (null == update)
            {
            throw new CommunityIdentifierException(
                "Null inout data"
                ) ;
            }
        //
        // Check for null ident.
        if (null == update.getIdent())
            {
            throw new CommunityIdentifierException(
                "Null inout data"
                ) ;
            }
        //
        // Lookup the Group in our map.
        GroupData found = this.getGroup(update.getIdent()) ;
        //
        // Replace the existing Group with the new data.
        map.put(found.getIdent(), update) ;
        //
        // Return the new data.
        return update ;
        }

    /**
     * Delete a Group.
     * @param  ident The Group identifier.
     * @return The GroupData for the old Group.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     *
     */
    public GroupData delGroup(String ident)
        throws CommunityIdentifierException, CommunityPolicyException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerMock.delGroup()") ;
        log.debug("  Ident : " + ident) ;
        //
        // Check for null ident.
        if (null == ident)
            {
            throw new CommunityIdentifierException(
                "Null identifier"
                ) ;
            }
        //
        // Try to find the Group.
        GroupData group = this.getGroup(ident) ;
        //
        // If we found an Group.
        if (null != group)
            {
            //
            // Remove the Group from our map.
            map.remove(group.getIdent()) ;
            //
            // Return the old group.
            return group ;
            }
        //
        // If we didn't find an Group.
        else {
            throw new CommunityPolicyException(
                "Group not found",
                ident
                ) ;
            }
        }

    /**
     * Request a list of local Groups.
     * @return An array of GroupData objects.
     *
     */
    public Object[] getLocalGroups()
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerMock.getLocalGroups()") ;
        return map.values().toArray() ;
        }

    /**
     * Get a list of local Groups that an Account belongs to.
     * @return An array of GroupData objects.
     * @todo Filter the results for matches.
     *
     */
    public Object[] getLocalAccountGroups(String account)
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("GroupManagerMock.getAccountGroups()") ;
        return map.values().toArray() ;
        }
    }
