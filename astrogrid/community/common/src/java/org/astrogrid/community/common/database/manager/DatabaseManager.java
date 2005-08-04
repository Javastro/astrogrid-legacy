/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/database/manager/DatabaseManager.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/08/04 09:40:11 $</cvs:date>
 * <cvs:version>$Revision: 1.7 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: DatabaseManager.java,v $
 *   Revision 1.7  2005/08/04 09:40:11  clq2
 *   kevin's second batch
 *
 *   Revision 1.6.182.1  2005/07/28 13:35:53  KevinBenson
 *   No longer uses resetDatabaseTables as a web service method it is not allowed anymore.
 *
 *   Revision 1.6  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.5.38.1  2004/06/17 13:38:58  dave
 *   Tidied up old CVS log entries
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
     * This is only for the build and test environment, this method will NOT be available as
     * a web service interface method in the actual release.
     * @throws CommunityServiceException If there is an server error.
     * @throws RemoteException If the WebService call fails.
     *
     */
    public void resetDatabaseTables()
        throws RemoteException, CommunityServiceException ;

    }
