/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/service/PolicyService.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/23 16:34:08 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyService.java,v $
 *   Revision 1.6  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.5.24.2  2004/03/22 02:25:35  dave
 *   Updated delegate interfaces to include Exception handling.
 *
 *   Revision 1.5.24.1  2004/03/21 06:41:41  dave
 *   Refactored to include Exception handling.
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
 *   Revision 1.2.4.3  2004/02/06 16:19:04  dave
 *   Replaced import java.rmi.Remote
 *   Replaced import java.rmi.RemoteException
 *
 *   Revision 1.2.4.2  2004/02/06 16:15:49  dave
 *   Removed import java.rmi.RemoteException
 *
 *   Revision 1.2.4.1  2004/02/06 16:14:17  dave
 *   Removed import java.rmi.Remote
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
 *   Revision 1.4  2003/11/06 15:35:26  dave
 *   Replaced tabs with spaces
 *
 *   Revision 1.3  2003/09/03 15:23:33  dave
 *   Split API into two services, PolicyService and PolicyManager
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.service ;

import java.rmi.Remote ;
import java.rmi.RemoteException ;

import org.astrogrid.community.common.policy.data.PolicyPermission  ;
import org.astrogrid.community.common.policy.data.PolicyCredentials ;

import org.astrogrid.community.common.service.CommunityService ;

import org.astrogrid.community.common.exception.CommunityPolicyException     ;
import org.astrogrid.community.common.exception.CommunityServiceException    ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

public interface PolicyService
    extends Remote, CommunityService
    {

    /**
     * Confirm permissions.
     * @param credentials The credentials, containing the account and group identifiers.
     * @param resource The resource identifier.
     * @param action The action you want to perform.
     * @return A PolicyPermission object confirming the permission.
     * @throws CommunityIdentifierException If one of the identifiers is invalid.
     * @throws CommunityPolicyException If there is no matching permission.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the WebService call fails.
     *
     */
    public PolicyPermission checkPermissions(PolicyCredentials credentials, String resource, String action)
        throws RemoteException, CommunityServiceException, CommunityPolicyException, CommunityIdentifierException ;

    /**
     * Confirm membership.
     * @param credentials The credentials, containing the account and group identifiers.
     * @return A PolicyCredentials object confirming the membership.
     * @throws CommunityIdentifierException If one of the identifiers is invalid.
     * @throws CommunityPolicyException If there is no matching permission.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the WebService call fails.
     *
     */
    public PolicyCredentials checkMembership(PolicyCredentials credentials)
        throws RemoteException, CommunityServiceException, CommunityPolicyException, CommunityIdentifierException ;

    }
