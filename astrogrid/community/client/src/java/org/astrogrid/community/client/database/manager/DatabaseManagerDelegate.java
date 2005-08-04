/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/database/manager/DatabaseManagerDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/08/04 09:40:11 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: DatabaseManagerDelegate.java,v $
 *   Revision 1.6  2005/08/04 09:40:11  clq2
 *   kevin's second batch
 *
 *   Revision 1.5.182.1  2005/07/28 13:35:53  KevinBenson
 *   No longer uses resetDatabaseTables as a web service method it is not allowed anymore.
 *
 *   Revision 1.5  2004/06/18 13:45:19  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.4.38.1  2004/06/17 13:38:58  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.database.manager ;

import org.astrogrid.community.common.service.data.ServiceStatusData ;

import org.astrogrid.community.common.database.manager.DatabaseManager ;
import org.astrogrid.community.common.exception.CommunityServiceException ;

/**
 * Public interface for our DatabaseManager delegate.
 * This mirrors the DatabaseManager interface without the RemoteExceptions.
 * @todo Extend a common base class.
 *
 */
public interface DatabaseManagerDelegate
    extends DatabaseManager
    {
    /**
     * Get the current database name.
     * @throws CommunityServiceException If there is an server error.
     *
     */
    public String getDatabaseName()
        throws CommunityServiceException ;

    /**
     * Get our JDO configuration resource name.
     * @throws CommunityServiceException If there is an server error.
     *
     */
    public String getDatabaseConfigResource()
        throws CommunityServiceException ;

    /**
     * Get the database SQL script name.
     * @throws CommunityServiceException If there is an server error.
     *
     */
    public String getDatabaseScriptResource()
        throws CommunityServiceException ;

    /**
     * Get the database configuration URL.
     * @throws CommunityServiceException If there is an server error.
     *
     */
    public String getDatabaseConfigUrl()
        throws CommunityServiceException ;

    /**
     * Get the database engine description.
     * @throws CommunityServiceException If there is an server error.
     *
     */
    public String getDatabaseDescription()
        throws CommunityServiceException ;

    /**
     * Check the database tables.
     * @throws CommunityServiceException If there is an server error.
     *
     */
    public boolean checkDatabaseTables()
        throws CommunityServiceException ;

    /**
     * This method is only availabe through the build and test environment.  It is NOT 
     * avaliable as a web service interface method in the actual release.
     * @throws CommunityServiceException If there is an server error.
     *
     */
    public void resetDatabaseTables()
        throws CommunityServiceException ;

    /**
     * Service health check.
     * @return ServiceStatusData with details of the Service status.
     * @throws CommunityServiceException If there is an server error.
     * @todo Move this to a common base class.
     *
     */
    public ServiceStatusData getServiceStatus()
        throws CommunityServiceException ;

    }
