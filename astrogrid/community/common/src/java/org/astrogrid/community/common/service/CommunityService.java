/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/service/CommunityService.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityService.java,v $
 *   Revision 1.4  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.3.38.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.service ;

import java.rmi.Remote ;
import java.rmi.RemoteException ;

import org.astrogrid.community.common.service.data.ServiceStatusData ;
import org.astrogrid.community.common.exception.CommunityServiceException ;

/**
 * Generic interface for all of our services.
 *
 */
public interface CommunityService
    extends Remote
    {

    /**
     * Service health check.
     * @throws CommunityServiceException If there is an server error.
     * @throws RemoteException If the WebService call fails.
     *
     */
    public ServiceStatusData getServiceStatus()
        throws RemoteException, CommunityServiceException ;

    }
