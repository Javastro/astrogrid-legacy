/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/policy/manager/CommunityManagerDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/05 17:19:59 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityManagerDelegate.java,v $
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
    extends CommunityManager
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
