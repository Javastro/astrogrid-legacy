/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/PermissionManager.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/04/04 15:51:47 $</cvs:date>
 * <cvs:version>$Revision: 1.7 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PermissionManager.java,v $
 *   Revision 1.7  2004/04/04 15:51:47  dave
 *   Merged development branch, dave-dev-200404011434, into HEAD
 *
 *   Revision 1.6.30.1  2004/04/04 15:50:07  dave
 *   Changed SecurityToken generator to use RMI GUID values.
 *
 *   Revision 1.6  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.5.2.1  2004/03/04 13:26:17  dave
 *   1) Added Delegate interfaces.
 *   2) Added Mock implementations.
 *   3) Added MockDelegates
 *   4) Added SoapDelegates
 *
 *   Revision 1.5  2004/02/20 21:11:05  dave
 *   Merged development branch, dave-dev-200402120832, into HEAD
 *
 *   Revision 1.4.2.1  2004/02/19 21:09:26  dave
 *   Refactored ServiceStatusData into a common package.
 *   Refactored CommunityServiceImpl constructor to take a parent service.
 *   Refactored default database for CommunityServiceImpl
 *
 *   Revision 1.4  2004/02/12 08:12:13  dave
 *   Merged development branch, dave-dev-200401131047, into HEAD
 *
 *   Revision 1.2.4.4  2004/02/06 16:19:04  dave
 *   Replaced import java.rmi.Remote
 *   Replaced import java.rmi.RemoteException
 *
 *   Revision 1.2.4.3  2004/02/06 16:15:49  dave
 *   Removed import java.rmi.RemoteException
 *
 *   Revision 1.2.4.2  2004/02/06 16:14:17  dave
 *   Removed import java.rmi.Remote
 *
 *   Revision 1.2.4.1  2004/02/06 16:06:05  dave
 *   Commented out Remote import
 *
 *   Revision 1.2  2004/01/07 10:45:38  dave
 *   Merged development branch, dave-dev-20031224, back into HEAD
 *
 *   Revision 1.1.2.2  2004/01/05 06:47:18  dave
 *   Moved policy data classes into policy.data package
 *
 *   Revision 1.1.2.1  2003/12/24 05:54:48  dave
 *   Initial Maven friendly structure (only part of the service implemented)
 *
 *   Revision 1.3  2003/11/06 15:35:26  dave
 *   Replaced tabs with spaces
 *
 *   Revision 1.2  2003/09/12 12:59:17  dave
 *   1) Fixed RemoteException handling in the manager and service implementations.
 *
 *   Revision 1.1  2003/09/10 02:56:03  dave
 *   Added PermissionManager and tests
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.manager ;

import java.rmi.Remote ;
import java.rmi.RemoteException ;

import org.astrogrid.community.common.policy.data.PolicyPermission ;
import org.astrogrid.community.common.service.CommunityService ;

/**
 * Public interface for our PermissionManager service.
 *
 */
 public interface PermissionManager
    extends Remote, CommunityService
    {
    /**
     * Create a new PolicyPermission.
     *
     */
    public PolicyPermission addPermission(String resource, String group, String action)
        throws RemoteException ;

    /**
     * Request a PolicyPermission.
     *
     */
    public PolicyPermission getPermission(String resource, String group, String action)
        throws RemoteException ;

    /**
     * Update a PolicyPermission.
     *
     */
    public PolicyPermission setPermission(PolicyPermission permission)
        throws RemoteException ;

    /**
     * Delete a PolicyPermission.
     *
     */
    public boolean delPermission(String resource, String group, String action)
        throws RemoteException ;

    /**
     * Request a list of PolicyPermissions for a resource.
     * TODO - Need to put something like this back in.
     * TODO - Possibly requesting all permissions for a resource.
     * TODO - Possibly requesting all permissions for a group and resource.
     * 
    public Object[] getPermissionList(String resource)
        throws RemoteException ;
     */

    }
