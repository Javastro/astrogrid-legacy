/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/database/manager/DatabaseManager.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/19 14:43:14 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: DatabaseManager.java,v $
 *   Revision 1.5  2004/03/19 14:43:14  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.4.14.2  2004/03/19 00:18:09  dave
 *   Refactored delegate Exception handling
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.database.manager ;

import java.rmi.Remote ;
import java.rmi.RemoteException ;

import org.astrogrid.community.common.service.CommunityService ;
import org.astrogrid.community.common.exception.CommunityServiceException ;

/**
 * Interface for our DatabaseManager service.
 *
 */
public interface DatabaseManager
    extends Remote, CommunityService
    {
    /**
     * Get the current database name.
     * @throws CommunityServiceException If there is an server error.
     * @throws RemoteException If the WebService call fails.
     *
     */
    public String getDatabaseName()
        throws RemoteException, CommunityServiceException ;

    /**
     * Get our JDO configuration resource name.
     * @throws CommunityServiceException If there is an server error.
     * @throws RemoteException If the WebService call fails.
     *
     */
    public String getDatabaseConfigResource()
        throws RemoteException, CommunityServiceException ;

    /**
     * Get the database SQL script name.
     * @throws CommunityServiceException If there is an server error.
     * @throws RemoteException If the WebService call fails.
     *
     */
    public String getDatabaseScriptResource()
        throws RemoteException, CommunityServiceException ;

    /**
     * Get the database configuration URL.
     * @throws CommunityServiceException If there is an server error.
     * @throws RemoteException If the WebService call fails.
     *
     */
    public String getDatabaseConfigUrl()
        throws RemoteException, CommunityServiceException ;

    /**
     * Get the database engine description.
     * @throws CommunityServiceException If there is an server error.
     * @throws RemoteException If the WebService call fails.
     *
     */
    public String getDatabaseDescription()
        throws RemoteException, CommunityServiceException ;

    /**
     * Check the database tables.
     * @throws CommunityServiceException If there is an server error.
     * @throws RemoteException If the WebService call fails.
     *
     */
    public boolean checkDatabaseTables()
        throws RemoteException, CommunityServiceException ;

    /**
     * Reset our database tables.
     * @throws CommunityServiceException If there is an server error.
     * @throws RemoteException If the WebService call fails.
     *
     */
    public void resetDatabaseTables()
        throws RemoteException, CommunityServiceException ;

    }
