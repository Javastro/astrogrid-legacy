/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/database/manager/DatabaseManagerDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/19 14:43:14 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: DatabaseManagerDelegate.java,v $
 *   Revision 1.4  2004/03/19 14:43:14  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.3.14.1  2004/03/19 00:18:09  dave
 *   Refactored delegate Exception handling
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
     * Reset our database tables.
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
