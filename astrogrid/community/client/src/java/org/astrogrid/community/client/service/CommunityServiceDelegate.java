/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/service/CommunityServiceDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:19 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityServiceDelegate.java,v $
 *   Revision 1.3  2004/06/18 13:45:19  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.2.38.1  2004/06/17 13:38:58  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.service ;

import org.astrogrid.community.common.service.data.ServiceStatusData ;
import org.astrogrid.community.common.exception.CommunityServiceException ;

/**
 * Generic interface for all of our service delegates.
 * This mirrors the CommunityService interface without the RemoteExceptions.
 *
 */
public interface CommunityServiceDelegate
    {

    /**
     * Service health check.
     * @throws CommunityServiceException If there is an server error.
     *
     */
    public ServiceStatusData getServiceStatus()
        throws CommunityServiceException ;

    }
