/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/GroupManagerMock.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/23 16:34:08 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: GroupManagerMock.java,v $
 *   Revision 1.5  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.4.4.1  2004/03/21 06:41:41  dave
 *   Refactored to include Exception handling.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.manager ;

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
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupManagerMock.reset()") ;
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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupManagerMock.addGroup()") ;
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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupManagerMock.addGroup()") ;
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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupManagerMock.getGroup()") ;
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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupManagerMock.setGroup()") ;
        if (DEBUG_FLAG) System.out.println("  Ident : " + ((null != update) ? update.getIdent() : null)) ;
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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupManagerMock.delGroup()") ;
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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupManagerMock.getLocalGroups()") ;
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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("GroupManagerMock.getAccountGroups()") ;
        return map.values().toArray() ;
        }
    }
