/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/CommunityManager.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/19 14:43:14 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityManager.java,v $
 *   Revision 1.6  2004/03/19 14:43:14  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.5.22.2  2004/03/19 00:18:09  dave
 *   Refactored delegate Exception handling
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.manager ;

import java.rmi.Remote ;
import java.rmi.RemoteException ;

import org.astrogrid.community.common.policy.data.CommunityData ;
import org.astrogrid.community.common.service.CommunityService ;

import org.astrogrid.community.common.exception.CommunityPolicyException     ;
import org.astrogrid.community.common.exception.CommunityServiceException    ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

public interface CommunityManager
    extends Remote, CommunityService
    {
    /**
     * Add a new Community, given the Account ident.
     * @param  ident The Community identifier.
     * @return A CommunityData for the Community.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is already in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the WebService call fails.
     *
     */
    public CommunityData addCommunity(String ident)
        throws RemoteException, CommunityServiceException, CommunityIdentifierException, CommunityPolicyException ;

    /**
     * Request a Community details, given the Community ident.
     * @param  ident The Community identifier.
     * @return A CommunityData for the Community.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the WebService call fails.
     *
     */
    public CommunityData getCommunity(String ident)
        throws RemoteException, CommunityServiceException, CommunityIdentifierException, CommunityPolicyException ;

    /**
     * Update a Community.
     * @param  community The new CommunityData to update.
     * @return A new CommunityData for the Community.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the WebService call fails.
     *
     */
    public CommunityData setCommunity(CommunityData community)
        throws RemoteException, CommunityServiceException, CommunityIdentifierException, CommunityPolicyException ;

    /**
     * Delete a Community.
     * @param  ident The Community identifier.
     * @return The CommunityData for the old Community.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the WebService call fails.
     *
     */
    public CommunityData delCommunity(String ident)
        throws RemoteException, CommunityServiceException, CommunityIdentifierException, CommunityPolicyException ;

    /**
     * Request a list of Communities.
     * @return An array of CommunityData objects.
     * @throws CommunityServiceException If there is an internal error in the service.
     * @throws RemoteException If the WebService call fails.
     *
     */
    public Object[] getCommunityList()
        throws RemoteException, CommunityServiceException ;

    }
