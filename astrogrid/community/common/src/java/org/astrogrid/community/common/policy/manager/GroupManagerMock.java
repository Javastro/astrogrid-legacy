/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/GroupManagerMock.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/15 07:49:30 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: GroupManagerMock.java,v $
 *   Revision 1.4  2004/03/15 07:49:30  dave
 *   Merged development branch, dave-dev-200403121536, into HEAD
 *
 *   Revision 1.3.12.1  2004/03/13 17:57:20  dave
 *   Remove RemoteException(s) from delegate interfaces.
 *   Protected internal API methods.
 *
 *   Revision 1.3  2004/03/08 13:42:33  dave
 *   Updated Maven goals.
 *   Replaced tabs with Spaces.
 *
 *   Revision 1.2.2.1  2004/03/08 12:53:17  dave
 *   Changed tabs to spaces
 *
 *   Revision 1.2  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/04 13:26:17  dave
 *   1) Added Delegate interfaces.
 *   2) Added Mock implementations.
 *   3) Added MockDelegates
 *   4) Added SoapDelegates
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.manager ;

import java.util.Map ;
import java.util.HashMap ;

import org.astrogrid.community.common.policy.data.GroupData ;
import org.astrogrid.community.common.service.CommunityServiceMock ;

/**
 * Mock implementation of our GroupManager service.
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
    private Map map = new HashMap() ;

    /**
     * Create a new Group, given the Group ident.
     *
     */
    public GroupData addGroup(String ident)
        {
        //
        // Check for null ident.
        if (null == ident)
            {
            // TODO
            // Throw an exception ?
            //
            // Return null for now.
            return null ;
            }
        //
        // Check if we already have an existing Group.
        if (null != this.getGroup(ident))
            {
            // TODO
            // Throw a duplicate exception ?
            //
            // Return null for now.
            return null ;
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
     * Request an Group data, given the Group ident.
     *
     */
    public GroupData getGroup(String ident)
        {
        //
        // Check for null ident.
        if (null == ident)
            {
            // TODO
            // Throw an exception ?
            //
            // Return null for now.
            return null ;
            }
        //
        // Lookup the Group in our map.
        return (GroupData) map.get(ident) ;
        }

    /**
     * Update an Group data.
     *
     */
    public GroupData setGroup(GroupData group)
        {
        //
        // Check for null param.
        if (null == group)
            {
            //
            // Throw an exception ?
            //
            // Return null for now.
            return null ;
            }
        //
        // If we don't have an existing Group.
        if (null == this.getGroup(group.getIdent()))
            {
            //
            // Throw an exception ?
            //
            // Return null for now.
            return null ;
            }
        //
        // Replace the existing Group with the new data.
        map.put(group.getIdent(), group) ;
        //
        // Return the new group.
        return group ;
        }

    /**
     * Delete an Group, given the Group ident.
     *
     */
    public GroupData delGroup(String ident)
        {
        //
        // Check for null ident.
        if (null == ident)
            {
            //
            // Throw an exception ?
            //
            // Return null for now.
            return null ;
            }
        //
        // Try to find the group.
        GroupData group = this.getGroup(ident) ;
        //
        // If we didn't find a matching Group.
        if (null == group)
            {
            //
            // Throw an exception ?
            //
            // Return null for now.
            return null ;
            }
        //
        // Remove the Group from our map.
        map.remove(group.getIdent()) ;
        //
        // Return the old Group.
        return group ;
        }

    /**
     * Request a list of local Groups.
     *
     */
    public Object[] getLocalGroups()
        {
        return map.values().toArray() ;
        }

    /**
     * Get a list of local Groups that an Account belongs to.
     * TODO - filter the list by account ?
     *
     */
    public Object[] getLocalAccountGroups(String account)
        {
        //
        // TODO filter the results.
        return map.values().toArray() ;
        }

    }
