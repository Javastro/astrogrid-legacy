/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/service/CommunityServiceMock.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityServiceMock.java,v $
 *   Revision 1.5  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.4.36.2  2004/06/17 14:50:03  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.4.36.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.service ;

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
