/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/junit/org/astrogrid/community/server/database/configuration/DatabaseConfigurationTest.java,v $</cvs:source>
 * <cvs:author>$Author: gtr $</cvs:author>
 * <cvs:date>$Date: 2008/01/23 15:24:12 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: DatabaseConfigurationTest.java,v $
 *   Revision 1.6  2008/01/23 15:24:12  gtr
 *   Branch community-gtr-2512 is merged.
 *
 *   Revision 1.5.176.1  2008/01/22 16:10:12  gtr
 *   I corrected the tests of the DB's health-check.
 *
 *   Revision 1.5  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.4.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.4  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.3.52.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.database.configuration ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import junit.framework.TestCase ;

import org.exolab.castor.jdo.JDO ;
import org.exolab.castor.jdo.Database ;
import org.exolab.castor.jdo.DatabaseNotFoundException ;

import java.io.FileNotFoundException ;

/**
 * Test cases for our DatabaseConfiguration.
 *
 */
public class DatabaseConfigurationTest
    extends TestCase
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(DatabaseConfigurationTest.class);

    /**
     * Try creating an empty DatabaseConfiguration.
     *
     */
    public void testCreateDefaultConfig()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("DatabaseConfigurationTest:testCreateDefaultConfig()") ;
        //
        // Create a new database configuration.
        DatabaseConfiguration config = new DatabaseConfiguration() ;
        assertNotNull(
            "Null configuration",
            config
            ) ;
        //
        // Check the database name.
        assertEquals(
            "Wrong database name",
            config.getDatabaseName(),
            DatabaseConfiguration.DEFAULT_DATABASE_NAME
            ) ;
        }

    /**
     * Try creating a DatabaseConfiguration with a non-existient config file.
     *
     */
    public void testCreateUnknownConfig()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("DatabaseConfigurationTest:testCreateUnknownConfig()") ;
        //
        // Try creating a database with a non-existient config file.
        try {
            DatabaseConfiguration config = new DatabaseConfiguration("unknown-database") ;
            fail("FAIL : Should have thrown a FileNotFoundException") ;
            }
        //
        // Catch the expected Exception.
        catch (FileNotFoundException ouch)
            {
            log.debug("PASS : expected FileNotFoundException thrown") ;
            }
        }

    /**
     * Try creating a DatabaseConfiguration with a valid config file.
     *
     */
    public void testCreateValid()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("DatabaseConfigurationTest:testValidConfig()") ;
        //
        // Create a new database configuration.
        DatabaseConfiguration config = new DatabaseConfiguration("test-database-001") ;
        assertNotNull(
            "Null configuration",
            config) ;
        //
        // Check the database name.
        assertEquals(
            "Wrong database name",
            config.getDatabaseName(),
            "test-database-001") ;
        }

    /**
     * Try openning a connection with an invalid database name.
     *
     */
    public void testInvalidName()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("DatabaseConfigurationTest:testInvalidName()") ;
        //
        // Create a new database configuration with an invalid name.
        DatabaseConfiguration config = new DatabaseConfiguration("INVALID-NAME", "test-database-001.xml", null) ;
        assertNotNull(
            "Null configuration",
            config) ;
        //
        // Try to create a new database connection.
        try {
            Database database = config.getDatabase() ;
            fail("FAIL : Should have thrown a DatabaseNotFoundException") ;
            }
        catch (DatabaseNotFoundException ouch)
            {
            log.debug("PASS : expected DatabaseNotFoundException thrown") ;
            }
        }

    /**
     * Try openning a valid database connection.
     *
     */
    public void testConnectValid()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("DatabaseConfigurationTest:testConnectValid()") ;
        //
        // Create a new database configuration.
        DatabaseConfiguration config = new DatabaseConfiguration("test-database-001") ;
        assertNotNull(
            "Null configuration",
            config) ;
        //
        // Create a new database connection.
        assertNotNull(
            "Null JDO database connection",
            config.getDatabase()
            ) ;
        }

    /**
     * Try accessing the JDO engine.
     *
     */
    public void testCreateEngine()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("DatabaseConfigurationTest:testCreateEngine()") ;
        //
        // Create a new database configuration.
        DatabaseConfiguration config = new DatabaseConfiguration("test-database-001") ;
        assertNotNull(
            "Null configuration",
            config) ;
        //
        // Check our database engine.
        JDO engine = config.getDatabaseEngine() ;
        assertNotNull(
            "Null JDO database engine",
            engine) ;
        }

    /**
     * Try checking the database test data.
     *
     */
    public void testDatabaseTables()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("DatabaseConfigurationTest:testDatabaseTables()") ;
        //
        // Create a new database configuration.
        DatabaseConfiguration config = new DatabaseConfiguration("test-database-001") ;
        assertNotNull("Null configuration", config) ;

        // Initially, there should be no tables.
        assertFalse(
            "Check database data returned true",
            config.checkDatabaseTables()
            );
        
        // Create the schema in the database.
        config.resetDatabaseTables();
        
        // Now we should see the tables.
        assertTrue(
            "Check database data returned false",
            config.checkDatabaseTables()
            );
        }

    /**
     * Try creating a new set of database tables.
     *
     */
    public void testCreateTables()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("DatabaseConfigurationTest:testCreateTables()") ;
        //
        // Create a new database configuration.
        DatabaseConfiguration config = new DatabaseConfiguration("test-database-002") ;
        assertNotNull(
            "Null configuration",
            config) ;
        //
        // Try checking the database data.
        assertFalse(
            "Check database tables returned true",
            config.checkDatabaseTables()
            ) ;
        //
        // Try resetting the database tables.
        config.resetDatabaseTables() ;
        //
        // Try checking the database tables.
        assertTrue(
            "Check database tables returned false",
            config.checkDatabaseTables()
            ) ;
        }

    /**
     * Try loading two database engines.
     *
     */
    public void testMultipleDatabases()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("DatabaseConfigurationTest:testMultipleDatabases()") ;
        //
        // Create a new database configuration.
        DatabaseConfiguration alpha = new DatabaseConfiguration("test-database-003") ;
        assertNotNull(
            "Null configuration",
            alpha) ;
        //
        // Create a new database connection.
        assertNotNull(
            "Null JDO database connection",
            alpha.getDatabase()
            ) ;
        //
        // Check the database tables.
        assertFalse(
            "Check database tables returned true",
            alpha.checkDatabaseTables()
            ) ;
        //
        // Reset the database tables.
        alpha.resetDatabaseTables() ;
        //
        // Check the database tables.
        assertTrue(
            "Check database tables returned false",
            alpha.checkDatabaseTables()
            ) ;

        //
        // Create a new database configuration.
        DatabaseConfiguration beta = new DatabaseConfiguration("test-database-004") ;
        //
        // Create a new database connection.
        assertNotNull(
            "Null JDO database connection",
            beta.getDatabase()
            ) ;
        //
        // Try checking the database tables.
        assertFalse(
            "Check database tables returned true",
            beta.checkDatabaseTables()
            ) ;
        //
        // Reset the database tables.
        beta.resetDatabaseTables() ;
        //
        // Try checking the database tables.
        assertTrue(
            "Check database tables returned false",
            beta.checkDatabaseTables()
            ) ;
        }
    }
