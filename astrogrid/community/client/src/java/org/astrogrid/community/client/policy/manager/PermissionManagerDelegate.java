/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/policy/manager/PermissionManagerDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/19 14:43:14 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PermissionManagerDelegate.java,v $
 *   Revision 1.5  2004/03/19 14:43:14  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.4.2.1  2004/03/19 00:18:09  dave
 *   Refactored delegate Exception handling
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.policy.manager ;

import org.astrogrid.community.client.service.CommunityServiceDelegate ;

import org.astrogrid.community.common.policy.data.PolicyPermission ;
import org.astrogrid.community.common.policy.manager.PermissionManager ;
import org.astrogrid.community.common.service.data.ServiceStatusData ;

/**
 * Interface for our PermissionManager delegate.
 * This extends the PermissionManager interface, without the RemoteExceptions.
 *
 */
public interface PermissionManagerDelegate
    extends CommunityServiceDelegate
    {
    /**
     * Create a new PolicyPermission.
     *
     */
    public PolicyPermission addPermission(String resource, String group, String action) ;

    /**
     * Request a PolicyPermission.
     *
     */
    public PolicyPermission getPermission(String resource, String group, String action) ;

    /**
     * Update a PolicyPermission.
     *
     */
    public PolicyPermission setPermission(PolicyPermission permission) ;

    /**
     * Delete a PolicyPermission.
     *
     */
    public boolean delPermission(String resource, String group, String action) ;

    }
