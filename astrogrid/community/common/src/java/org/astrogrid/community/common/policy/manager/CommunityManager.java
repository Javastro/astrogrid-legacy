/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/CommunityManager.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/01/07 10:45:38 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityManager.java,v $
 *   Revision 1.2  2004/01/07 10:45:38  dave
 *   Merged development branch, dave-dev-20031224, back into HEAD
 *
 *   Revision 1.1.2.2  2004/01/05 06:47:18  dave
 *   Moved policy data classes into policy.data package
 *
 *   Revision 1.1.2.1  2003/12/24 05:54:48  dave
 *   Initial Maven friendly structure (only part of the service implemented)
 *
 *   Revision 1.5  2003/11/06 15:35:26  dave
 *   Replaced tabs with spaces
 *
 *   Revision 1.4  2003/09/11 03:15:06  dave
 *   1) Implemented PolicyService internals - no tests yet.
 *   2) Added getLocalAccountGroups and getRemoteAccountGroups to PolicyManager.
 *   3) Added remote access to groups.
 *
 *   Revision 1.3  2003/09/08 20:28:50  dave
 *   Added CommunityIdent, with isLocal() and isValid()
 *
 *   Revision 1.2  2003/09/08 11:01:35  KevinBenson
 *   A check in of the Authentication authenticateToken roughdraft and some changes to the groudata and community data
 *   along with an AdministrationDelegate
 *
 *   Revision 1.1  2003/09/06 20:10:07  dave
 *   Split PolicyManager into separate components.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.manager ;

import java.rmi.Remote ;
import java.rmi.RemoteException ;
import java.util.ArrayList;

import org.astrogrid.community.common.policy.data.CommunityData ;

public interface CommunityManager
    extends java.rmi.Remote
    {
    /**
     * Create a new Community.
     *
     */
    public CommunityData addCommunity(String ident)
        throws RemoteException ;

    /**
     * Request an Community details.
     *
     */
    public CommunityData getCommunity(String ident)
        throws RemoteException ;

    /**
     * Update an Community details.
     *
     */
    public CommunityData setCommunity(CommunityData community)
        throws RemoteException ;

    /**
     * Delete an Community.
     *
     */
    public CommunityData delCommunity(String ident)
        throws RemoteException ;

    /**
     * Request a list of Communitys.
     *
     */
    public Object[] getCommunityList()
        throws RemoteException ;

    }
