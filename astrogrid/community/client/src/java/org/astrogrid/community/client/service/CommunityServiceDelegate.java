/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/service/CommunityServiceDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/19 14:43:14 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityServiceDelegate.java,v $
 *   Revision 1.2  2004/03/19 14:43:14  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/19 00:18:09  dave
 *   Refactored delegate Exception handling
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
