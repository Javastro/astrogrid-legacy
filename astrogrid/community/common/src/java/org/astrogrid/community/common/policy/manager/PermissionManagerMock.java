/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/PermissionManagerMock.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PermissionManagerMock.java,v $
 *   Revision 1.5  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.4.40.2  2004/06/17 14:50:03  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.4.40.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.manager ;

import java.util.Map ;
import java.util.HashMap ;

import org.astrogrid.community.common.policy.data.PolicyPermission ;
import org.astrogrid.community.common.service.CommunityServiceMock ;

/**
 * Mock implementation of our PermissionManager service.
 *
 */
public class PermissionManagerMock
    extends CommunityServiceMock
    implements PermissionManager
    {

    /**
     * Public constructor.
     *
     */
    public PermissionManagerMock()
        {
        super() ;
        }

    /**
     * Our hash table of values.
     *
     */
    private Map map = new HashMap() ;

    /**
     * Create a hash key for a PolicyPermission.
     * Appends the three key components to make a single key.
     *
     */
    protected String getHashKey(String resource, String group, String action)
        {
        return group + "," + action + "," + resource ;
        }

    /**
     * Create a hash key for a PolicyPermission.
     * Appends the three key components to make a single key.
     *
     */
    protected String getHashKey(PolicyPermission data)
        {
        return this.getHashKey(
            data.getGroup(),
            data.getAction(),
            data.getResource()
            ) ;
        }

    /**
     * Create a new PolicyPermission.
     *
     */
    public PolicyPermission addPermission(String resource, String group, String action)
        {
        //
        // TODO Check for null params.
        String ident = this.getHashKey(resource, group, action) ;
        //
        // Check if we already have an existing Permission.
        if (null != this.getPermission(ident))
            {
            // TODO
            // Throw a duplicate exception ?
            //
            // Return null for now.
            return null ;
            }
        //
        // Create a new Permission.
        PolicyPermission permission = new PolicyPermission(resource, group, action) ;
        //
        // Add it to our map.
        map.put(ident, permission) ;
        //
        // Return the new Permission.
        return permission ;
        }

    /**
     * Request a PolicyPermission.
     *
     */
    protected PolicyPermission getPermission(String ident)
        {
        //
        // Lookup the Account in our map.
        return (PolicyPermission) map.get(ident) ;
        }

    /**
     * Request a PolicyPermission.
     *
     */
    public PolicyPermission getPermission(String resource, String group, String action)
        {
        //
        // TODO Check for null params.

        //
        // Lookup the Account in our map.
        return this.getPermission(
            this.getHashKey(resource, group, action)
            ) ;
        }

    /**
     * Update a PolicyPermission.
     *
     */
    public PolicyPermission setPermission(PolicyPermission permission)
        {
        //
        // TODO Check for null params.
        String ident = this.getHashKey(permission) ;
        //
        // If we don't have an existing Permission.
        if (null == this.getPermission(ident))
            {
            //
            // Throw an exception ?
            //
            // Return null for now.
            return null ;
            }
        //
        // Replace the existing Permission with the new data.
        map.put(ident, permission) ;
        //
        // Return the new Permission.
        return permission ;
        }

    /**
     * Delete a PolicyPermission.
     *
     */
    public boolean delPermission(String resource, String group, String action)
        {
        //
        // TODO Check for null params.
        String ident = this.getHashKey(resource, group, action) ;
        //
        // Try to find the permission.
        PolicyPermission permission = this.getPermission(ident) ;
        //
        // If we didn't find a matching Permission.
        if (null == permission)
            {
            //
            // Throw an exception ?
            //
            // Return null for now.
            // TODO - refactor API
            return false ;
            }
        //
        // Remove the Permission from our map.
        map.remove(ident) ;
        //
        // Return the old Permission.
        // TODO - refactor API
        return true ;
        }
    }
