/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/service/CommunityServiceMock.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.7 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityServiceMock.java,v $
 *   Revision 1.7  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.6.8.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.6  2004/09/09 01:19:50  dave
 *   Updated MIME type handling in MySpace.
 *   Extended test coverage for MIME types in FileStore and MySpace.
 *   Added VM memory data to community ServiceStatusData.
 *
 *   Revision 1.5.74.1  2004/09/07 04:01:47  dave
 *   Added memory stats ...
 *
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

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.community.common.service.data.ServiceStatusData ;

/**
 * Mock implementation of our CommunityService interface.
 *
 */
public class CommunityServiceMock
    implements CommunityService
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(CommunityServiceMock.class);

    /**
     * Public constructor.
     *
     */
    public CommunityServiceMock()
        {
        }

    /**
     * Service health check.
     *
     */
    public ServiceStatusData getServiceStatus()
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityServiceMock.getServiceStatus()") ;
		ServiceStatusData status = new ServiceStatusData() ;
		//
		// Get the current runtime data ...
		Runtime runtime = Runtime.getRuntime() ;
		status.setFreeMemory(
			runtime.freeMemory()
			) ;
		status.setTotalMemory(
			runtime.totalMemory()
			) ;
        return status ;
        }
    }
