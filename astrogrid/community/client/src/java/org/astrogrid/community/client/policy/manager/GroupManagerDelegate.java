/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/policy/manager/GroupManagerDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/19 14:43:14 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: GroupManagerDelegate.java,v $
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

import org.astrogrid.community.common.policy.data.GroupData ;
import org.astrogrid.community.common.policy.manager.GroupManager ;
import org.astrogrid.community.common.service.data.ServiceStatusData ;

/**
 * Interface for our GroupManager delegate.
 * This extends the GroupManager interface, without the RemoteExceptions.
 *
 */
public interface GroupManagerDelegate
    extends CommunityServiceDelegate
    {

    /**
     * Create a new Group, given the Group ident.
     *
     */
    public GroupData addGroup(String ident) ;

    /**
     * Request an Group data, given the Group ident.
     *
     */
    public GroupData getGroup(String ident) ;

    /**
     * Update an Group data.
     *
     */
    public GroupData setGroup(GroupData group) ;

    /**
     * Delete a Group, given the Group ident.
     *
     */
    public GroupData delGroup(String ident) ;

    /**
     * Request a list of local Groups.
     *
     */
    public Object[] getLocalGroups() ;

    /**
     * Get a list of local Groups that an Account belongs to, given the Account ident.
     *
     */
    public Object[] getLocalAccountGroups(String account) ;

    }
