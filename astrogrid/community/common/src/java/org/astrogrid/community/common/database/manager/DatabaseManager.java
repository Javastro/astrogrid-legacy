/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/database/manager/DatabaseManager.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/08 13:42:33 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: DatabaseManager.java,v $
 *   Revision 1.4  2004/03/08 13:42:33  dave
 *   Updated Maven goals.
 *   Replaced tabs with Spaces.
 *
 *   Revision 1.3.2.1  2004/03/08 12:53:17  dave
 *   Changed tabs to spaces
 *
 *   Revision 1.3  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.2.2.2  2004/03/02 23:31:00  dave
 *   Added DatabaseManager to service tests
 *
 *   Revision 1.2.2.1  2004/02/22 20:03:16  dave
 *   Removed redundant DatabaseConfiguration interfaces
 *
 *   Revision 1.2  2004/02/20 21:11:05  dave
 *   Merged development branch, dave-dev-200402120832, into HEAD
 *
 *   Revision 1.1.2.1  2004/02/19 21:09:26  dave
 *   Refactored ServiceStatusData into a common package.
 *   Refactored CommunityServiceImpl constructor to take a parent service.
 *   Refactored default database for CommunityServiceImpl
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.database.manager ;

import java.rmi.Remote ;
import java.rmi.RemoteException ;

import org.astrogrid.community.common.service.CommunityService ;

/**
 * Interface for our DatabaseManager service.
 *
 */
public interface DatabaseManager
    extends Remote, CommunityService
    {
    /**
     * Get the current database name.
     *
     */
    public String getDatabaseName()
        throws RemoteException ;

    /**
     * Get our JDO configuration resource name.
     *
     */
    public String getDatabaseConfigResource()
        throws RemoteException ;

    /**
     * Get the database SQL script name.
     *
     */
    public String getDatabaseScriptResource()
        throws RemoteException ;

    /**
     * Get the database configuration URL.
     *
     */
    public String getDatabaseConfigUrl()
        throws RemoteException ;

    /**
     * Get the database engine description.
     *
     */
    public String getDatabaseDescription()
        throws RemoteException ;

    /**
     * Check the database tables.
     *
     */
    public boolean checkDatabaseTables()
        throws RemoteException ;

    /**
     * Reset our database tables.
     *
     */
    public void resetDatabaseTables()
        throws RemoteException ;

    }
