/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/service/CommunityServiceMock.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/23 16:34:08 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityServiceMock.java,v $
 *   Revision 1.4  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.3.16.1  2004/03/23 14:52:27  dave
 *   Modified the mock ivorn syntax.
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
 *   Revision 1.1.2.1  2004/02/23 19:43:47  dave
 *   Refactored DatabaseManager tests to test the interface.
 *   Refactored DatabaseManager tests to use common DatabaseManagerTest.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.service ;

import java.rmi.Remote ;
import java.rmi.RemoteException ;

import org.astrogrid.community.common.service.data.ServiceStatusData ;

/**
 * Mock implementation of our CommunityService interface.
 *
 */
public class CommunityServiceMock
    implements CommunityService
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Public constructor.
     *
     */
    public CommunityServiceMock()
        {
        }

    /**
     * Our service status.
     *
     */
    private ServiceStatusData serviceStatus = new ServiceStatusData() ;

    /**
     * Service health check.
     *
     */
    public ServiceStatusData getServiceStatus()
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityServiceMock.getServiceStatus()") ;
        return this.serviceStatus ;
        }

    /**
     * Set the service status.
     *
     */
    public void setServiceStatus(ServiceStatusData value)
        {
        this.serviceStatus = value ;
        }

    }
