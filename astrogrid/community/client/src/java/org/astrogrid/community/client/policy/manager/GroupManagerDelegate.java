/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/policy/manager/GroupManagerDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/08 13:42:33 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: GroupManagerDelegate.java,v $
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
package org.astrogrid.community.client.policy.manager ;

import org.astrogrid.community.common.policy.data.GroupData ;
import org.astrogrid.community.common.policy.manager.GroupManager ;
import org.astrogrid.community.common.service.data.ServiceStatusData ;

/**
 * Interface for our GroupManager delegate.
 * This extends the GroupManager interface, without the RemoteExceptions.
 *
 */
public interface GroupManagerDelegate
    extends GroupManager
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
