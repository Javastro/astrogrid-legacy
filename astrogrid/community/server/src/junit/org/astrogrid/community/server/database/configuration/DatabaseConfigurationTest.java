/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/junit/org/astrogrid/community/server/database/configuration/DatabaseConfigurationTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: DatabaseConfigurationTest.java,v $
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
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Try creating an empty DatabaseConfiguration.
     *
     */
    public void testCreateDefaultConfig()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("DatabaseConfigurationTest:testCreateDefaultConfig()") ;
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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("DatabaseConfigurationTest:testCreateUnknownConfig()") ;
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
            if (DEBUG_FLAG) System.out.println("PASS : expected FileNotFoundException thrown") ;
            }
        }

    /**
     * Try creating a DatabaseConfiguration with a valid config file.
     *
     */
    public void testCreateValid()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("DatabaseConfigurationTest:testValidConfig()") ;
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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("DatabaseConfigurationTest:testInvalidName()") ;
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
            if (DEBUG_FLAG) System.out.println("PASS : expected DatabaseNotFoundException thrown") ;
            }
        }

    /**
     * Try openning a valid database connection.
     *
     */
    public void testConnectValid()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("DatabaseConfigurationTest:testConnectValid()") ;
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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("DatabaseConfigurationTest:testCreateEngine()") ;
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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("DatabaseConfigurationTest:testDatabaseTables()") ;
        //
        // Create a new database configuration.
        DatabaseConfiguration config = new DatabaseConfiguration("test-database-001") ;
        assertNotNull("Null configuration", config) ;
        //
        // Try checking the database tables.
        assertTrue(
            "Check database data returned false",
            config.checkDatabaseTables()
            ) ;
        }

    /**
     * Try creating a new set of database tables.
     *
     */
    public void testCreateTables()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("DatabaseConfigurationTest:testCreateTables()") ;
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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("DatabaseConfigurationTest:testMultipleDatabases()") ;
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
