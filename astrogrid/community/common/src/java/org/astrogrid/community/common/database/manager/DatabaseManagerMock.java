/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/database/manager/DatabaseManagerMock.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/08 13:42:33 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: DatabaseManagerMock.java,v $
 *   Revision 1.3  2004/03/08 13:42:33  dave
 *   Updated Maven goals.
 *   Replaced tabs with Spaces.
 *
 *   Revision 1.2.2.1  2004/03/08 12:53:17  dave
 *   Changed tabs to spaces
 *
 *   Revision 1.2  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.1.2.3  2004/03/02 23:31:00  dave
 *   Added DatabaseManager to service tests
 *
 *   Revision 1.1.2.2  2004/02/26 13:10:28  dave
 *   Added AccountManagerMock
 *
 *   Revision 1.1.2.1  2004/02/23 19:43:47  dave
 *   Refactored DatabaseManager tests to test the interface.
 *   Refactored DatabaseManager tests to use common DatabaseManagerTest.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.database.manager ;

import org.astrogrid.community.common.service.CommunityServiceMock ;

/**
 * Mock implementation of our DatabaseManager service.
 *
 */
public class DatabaseManagerMock
    extends CommunityServiceMock
    implements DatabaseManager
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

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
     * Create our database tables.
     *
     */
    public void resetDatabaseTables()
        {
        }
    }
