/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/database/manager/DatabaseManagerDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/05 17:19:59 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: DatabaseManagerDelegate.java,v $
 *   Revision 1.2  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/04 17:13:30  dave
 *   Added DatabaseManager delegates
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.database.manager ;

import org.astrogrid.community.common.service.data.ServiceStatusData ;

import org.astrogrid.community.common.database.manager.DatabaseManager ;

/**
 * Public interface for our DatabaseManager delegate.
 * This extends the DatabaseManager interface, without the RemoteExceptions.
 *
 */
public interface DatabaseManagerDelegate
	extends DatabaseManager
    {
	/**
	 * Get the current database name.
	 *
	 */
	public String getDatabaseName() ;

	/**
	 * Get our JDO configuration resource name.
	 *
	 */
	public String getDatabaseConfigResource() ;

	/**
	 * Get the database SQL script name.
	 *
	 */
	public String getDatabaseScriptResource() ;

	/**
	 * Get the database configuration URL.
	 *
	 */
	public String getDatabaseConfigUrl() ;

	/**
	 * Get the database engine description.
	 *
	 */
	public String getDatabaseDescription() ;

	/**
	 * Check the database tables.
	 *
	 */
	public boolean checkDatabaseTables() ;

	/**
	 * Reset our database tables.
	 *
	 */
	public void resetDatabaseTables() ;

    /**
     * Service health check.
     * @return ServiceStatusData with details of the Service status.
     *
     */
    public ServiceStatusData getServiceStatus() ;

    }
