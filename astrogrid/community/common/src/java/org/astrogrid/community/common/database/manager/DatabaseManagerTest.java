/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/database/manager/DatabaseManagerTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: DatabaseManagerTest.java,v $
 *   Revision 1.6  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.5.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.5  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.4.38.1  2004/06/17 13:38:58  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.database.manager ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.community.common.service.CommunityServiceTest ;

/**
 * A JUnit test case for our DatabaseManager.
 * This is designed to be extended by each set of tests, mock, client and server.
 *
 */
public class DatabaseManagerTest
    extends CommunityServiceTest
    {
    /**
     * Our debug logger.
     *
     */
	private static Log log = LogFactory.getLog(DatabaseManagerTest.class);

    /**
     * Public constructor.
     *
     */
    public DatabaseManagerTest()
        {
        }

    /**
     * Public constructor, with reference to target manager.
     *
     */
    public DatabaseManagerTest(DatabaseManager manager)
        {
        this.setDatabaseManager(manager) ;
        }

    /**
     * Our target DatabaseManager.
     *
     */
    private DatabaseManager manager ;

    /**
     * Set our target DatabaseManager.
     *
     */
    public void setDatabaseManager(DatabaseManager manager)
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("DatabaseManagerTest.setDatabaseManager()") ;
        log.debug("  Manager : " + manager.getClass()) ;
        //
        // Set our target DatabaseManager.
        this.manager = manager ;
        //
        // Set our base class DatabaseManager.
        super.setDatabaseManager(manager) ;
        //
        // Set our CommunityService reference.
        this.setCommunityService(manager) ;
        }

    /**
     * Test the current database name.
     *
     */
    public void testGetDatabaseName()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("DatabaseManagerTest.testGetDatabaseName()") ;
        log.debug("  Manager : " + manager.getClass()) ;
        assertNotNull(
            "getDatabaseName returned NULL",
            manager.getDatabaseName()
            ) ;
        }

    /**
     * Test the JDO configuration resource name.
     *
     */
    public void testDatabaseConfigResource()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("DatabaseManagerTest.testDatabaseConfigResource()") ;
        log.debug("  Manager : " + manager.getClass()) ;
        assertNotNull(
            "getDatabaseConfigResource returned NULL",
            manager.getDatabaseConfigResource()
            ) ;
        }

    /**
     * Get the database SQL script name.
     *
     */
    public void testGetDatabaseScriptResource()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("DatabaseManagerTest.testGetDatabaseScriptResource()") ;
        log.debug("  Manager : " + manager.getClass()) ;
        assertNotNull(
            "getDatabaseScriptResource returned NULL",
            manager.getDatabaseScriptResource()
            ) ;
        }

    /**
     * Get the database configuration URL.
     *
     */
    public void testGetDatabaseConfigUrl()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("DatabaseManagerTest.testGetDatabaseConfigUrl()") ;
        log.debug("  Manager : " + manager.getClass()) ;
        assertNotNull(
            "getDatabaseConfigUrl returned NULL",
            manager.getDatabaseConfigUrl()
            ) ;
        }

    /**
     * Get the database engine description.
     *
     */
    public void testGetDatabaseDescription()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("DatabaseManagerTest.testGetDatabaseDescription()") ;
        log.debug("  Manager : " + manager.getClass()) ;
        assertNotNull(
            "getDatabaseDescription returned NULL",
            manager.getDatabaseDescription()
            ) ;
        }

    /**
     * Create our database tables.
     * This calls resetDatabaseTables() to create the tables,
     * and then calls checkDatabaseTables() to check that they are healthy.
     *
     */
    public void testResetDatabaseTables()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("DatabaseManagerTest.testResetDatabaseTables()") ;
        log.debug("  Manager : " + manager.getClass()) ;
        //
        // Create the database tables.
        manager.resetDatabaseTables() ;
        //
        // Check the tables were created.
        assertTrue(
            "checkDatabaseTables returned false",
            manager.checkDatabaseTables()
            ) ;
        }

    }
