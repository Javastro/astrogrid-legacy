/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/database/manager/Attic/DatabaseManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/05 17:19:59 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: DatabaseManagerImpl.java,v $
 *   Revision 1.3  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.2.2.4  2004/03/02 23:31:00  dave
 *   Added DatabaseManager to service tests
 *
 *   Revision 1.2.2.3  2004/02/23 19:43:47  dave
 *   Refactored DatabaseManager tests to test the interface.
 *   Refactored DatabaseManager tests to use common DatabaseManagerTest.
 *
 *   Revision 1.2.2.2  2004/02/23 08:55:20  dave
 *   Refactored CastorDatabaseConfiguration into DatabaseConfiguration
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
package org.astrogrid.community.server.database.manager ;

import java.net.URL ;
import java.rmi.RemoteException ;

import org.exolab.castor.jdo.JDO ;

import org.astrogrid.community.common.config.CommunityConfig ;

import org.astrogrid.community.common.database.manager.DatabaseManager ;

import org.astrogrid.community.server.service.CommunityServiceImpl ;
import org.astrogrid.community.server.database.configuration.DatabaseConfiguration ;

public class DatabaseManagerImpl
    extends CommunityServiceImpl
    implements DatabaseManager
    {
    /**
     * Switch for our debug statements.
     *
     */
    protected static final boolean DEBUG_FLAG = true ;

    /**
     * Public constructor, using default database configuration.
     *
     */
    public DatabaseManagerImpl()
        {
        super() ;
        }

    /**
     * Public constructor, using specific database configuration.
     *
     */
    public DatabaseManagerImpl(DatabaseConfiguration config)
        {
        super(config) ;
        }

	/**
	 * Get the current database name.
	 *
	 */
	public String getDatabaseName()
		{
		String result = null ;
		if (null != this.getDatabaseConfiguration())
			{
			result = this.getDatabaseConfiguration().getDatabaseName() ;
			}
		return result ;
		}

	/**
	 * Get our JDO configuration resource name.
	 *
	 */
	public String getDatabaseConfigResource()
		{
		String result = null ;
		if (null != this.getDatabaseConfiguration())
			{
			result = this.getDatabaseConfiguration().getDatabaseConfigResource() ;
			}
		return result ;
		}

	/**
	 * Get the database SQL script name.
	 *
	 */
	public String getDatabaseScriptResource()
		{
		String result = null ;
		if (null != this.getDatabaseConfiguration())
			{
			result = this.getDatabaseConfiguration().getDatabaseScriptResource() ;
			}
		return result ;
		}

	/**
	 * Get the database configuration URL.
	 *
	 */
	public String getDatabaseConfigUrl()
		{
		String result = null ;
		if (null != this.getDatabaseConfiguration())
			{
			URL url = this.getDatabaseConfiguration().getDatabaseConfigUrl() ;
			if (null != url)
				{
				result = url.toString() ;
				}
			}
		return result ;
		}

	/**
	 * Get the database engine description.
	 *
	 */
	public String getDatabaseDescription()
		{
		String result = null ;
		if (null != this.getDatabaseConfiguration())
			{
			JDO jdo = this.getDatabaseConfiguration().getDatabaseEngine() ;
			if (null != jdo)
				{
				result = jdo.getDescription() ;
				}
			}
		return result ;
		}

	/**
	 * Check our database tables.
	 *
	 */
	public boolean checkDatabaseTables()
		{
		boolean result = false ;
		if (null != this.getDatabaseConfiguration())
			{
			try {
				result = this.getDatabaseConfiguration().checkDatabaseTables() ;
				}
			catch (Exception ouch)
				{
	            logException(ouch, "DatabaseManagerImpl.checkDatabaseTables()") ;
				}
			}
		return result ;
		}

	/**
	 * Create our database tables.
	 * Use with care ... this may delete any existing data.
	 *
	 */
	public void resetDatabaseTables()
		{
		if (null != this.getDatabaseConfiguration())
			{
			try {
				this.getDatabaseConfiguration().resetDatabaseTables() ;
				}
			catch (Exception ouch)
				{
	            logException(ouch, "DatabaseManagerImpl.createDatabaseTables()") ;
				}
			}
		}
    }



