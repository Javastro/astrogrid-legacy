/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/policy/manager/CommunityManagerDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/15 07:49:30 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityManagerDelegate.java,v $
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
package org.astrogrid.community.client.policy.manager ;

import org.astrogrid.community.common.policy.data.CommunityData ;
import org.astrogrid.community.common.policy.manager.CommunityManager ;
import org.astrogrid.community.common.service.data.ServiceStatusData ;

/**
 * Interface for our CommunityManager delegate.
 * This extends the CommunityManager interface, without the RemoteExceptions.
 *
 */
public interface CommunityManagerDelegate
//    extends CommunityManager
    {
    /**
     * Create a new Community.
     *
     */
    public CommunityData addCommunity(String ident) ;

    /**
     * Request an Community details.
     *
     */
    public CommunityData getCommunity(String ident) ;

    /**
     * Update an Community details.
     *
     */
    public CommunityData setCommunity(CommunityData community) ;

    /**
     * Delete an Community.
     *
     */
    public CommunityData delCommunity(String ident) ;

    /**
     * Request a list of Communitys.
     *
     */
    public Object[] getCommunityList() ;

    }
