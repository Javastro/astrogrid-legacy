/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/database/manager/DatabaseManagerMock.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.8 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: DatabaseManagerMock.java,v $
 *   Revision 1.8  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.7.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.7  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.6.36.3  2004/06/17 13:38:58  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.database.manager ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.community.common.service.CommunityServiceMock ;
import org.astrogrid.community.common.policy.manager.GroupManagerMock ;
import org.astrogrid.community.common.policy.manager.AccountManagerMock ;
import org.astrogrid.community.common.policy.manager.ResourceManagerMock ;

/**
 * Mock implementation of our DatabaseManager service.
 *
 */
public class DatabaseManagerMock
    extends CommunityServiceMock
    implements DatabaseManager
    {
    /**
     * Our debug logger.
     *
     */
	private static Log log = LogFactory.getLog(DatabaseManagerMock.class);

    /**
     * Public constructor.
     *
     */
    public DatabaseManagerMock()
        {
        super() ;
        }

    /**
     * Our database name.
     *
     */
    private String databaseName = "Mock database name" ;

    /**
     * Get the current database name.
     *
     */
    public String getDatabaseName()
        {
        return this.databaseName ;
        }

    /**
     * Set the current database name.
     *
     */
    public void setDatabaseName(String value)
        {
        this.databaseName = value ;
        }

    /**
     * Our configuration resource name.
     *
     */
    private String databaseConfigResource = "Mock database config resource" ;

    /**
     * Get our JDO configuration resource name.
     *
     */
    public String getDatabaseConfigResource()
        {
        return this.databaseConfigResource ;
        }

    /**
     * Set our JDO configuration resource name.
     *
     */
    public void setDatabaseConfigResource(String value)
        {
        this.databaseConfigResource = value ;
        }

    /**
     * Our SQL script name
     *
     */
    private String databaseScriptResource = "Mock database script resource" ;

    /**
     * Get our database SQL script name.
     *
     */
    public String getDatabaseScriptResource()
        {
        return this.databaseScriptResource ;
        }

    /**
     * Set our database SQL script name.
     *
     */
    public void setDatabaseScriptResource(String value)
        {
        this.databaseScriptResource = value ;
        }

    /**
     * Our database configuration URL.
     *
     */
    private String databaseConfigUrl = "Mock database config URL" ;

    /**
     * Get our database configuration URL.
     *
     */
    public String getDatabaseConfigUrl()
        {
        return this.databaseConfigUrl ;
        }

    /**
     * Set our database configuration URL.
     *
     */
    public void setDatabaseConfigUrl(String value)
        {
        this.databaseConfigUrl = value ;
        }

    /**
     * Our database engine description.
     *
     */
    private String databaseDescription = "Mock database description" ;

    /**
     * Get our database engine description.
     *
     */
    public String getDatabaseDescription()
        {
        return this.databaseDescription ;
        }

    /**
     * Set our database engine description.
     *
     */
    public void setDatabaseDescription(String value)
        {
        this.databaseDescription = value ;
        }

    /**
     * Our check database tables result.
     *
     */
    private boolean databaseTablesValid = true ;

    /**
     * Check the database tables.
     *
     */
    public boolean checkDatabaseTables()
        {
        return this.databaseTablesValid ;
        }

    /**
     * Set the check database tables result.
     *
     */
    public void setDatabaseTablesValid(boolean value)
        {
        this.databaseTablesValid = value ;
        }

    /**
     * Reset our mock database tables.
     * @todo This needs to reset the other mock databases.
     *
     */
    public void resetDatabaseTables()
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("DatabaseManagerMock.resetDatabaseTables()") ;
        //
        // Reset our mock data.
        GroupManagerMock.reset() ;
        AccountManagerMock.reset() ;
        ResourceManagerMock.reset() ;
        }
    }
