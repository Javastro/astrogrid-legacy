/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/junit/org/astrogrid/community/server/database/castor/Attic/CastorDatabaseConfigurationTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/02/12 08:12:13 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CastorDatabaseConfigurationTest.java,v $
 *   Revision 1.2  2004/02/12 08:12:13  dave
 *   Merged development branch, dave-dev-200401131047, into HEAD
 *
 *   Revision 1.1.2.3  2004/01/30 03:21:23  dave
 *   Added initial code for SecurityManager and SecurityService
 *
 *   Revision 1.1.2.2  2004/01/26 16:59:22  dave
 *   Experimented with in-memory databases - they don't work
 *
 *   Revision 1.1.2.1  2004/01/26 13:18:08  dave
 *   Added new DatabaseManager to enable local JUnit testing
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.database.castor ;

import org.astrogrid.community.server.database.DatabaseConfiguration ;
import org.astrogrid.community.server.database.castor.CastorDatabaseConfiguration ;

import junit.framework.TestCase ;

import org.exolab.castor.jdo.JDO ;
import org.exolab.castor.jdo.Database ;
import org.exolab.castor.jdo.DatabaseNotFoundException ;

import java.io.FileNotFoundException ;

/**
 * Test cases for our CastorDatabaseConfiguration.
 *
 */
public class CastorDatabaseConfigurationTest
    extends TestCase
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Try creating an empty CastorDatabaseConfiguration.
     *
     */
    public void testCreateDefaultConfig()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CastorDatabaseConfigurationTest:testCreateDefaultConfig()") ;
        //
        // Create a new database configuration.
        DatabaseConfiguration config = new CastorDatabaseConfiguration() ;
        assertNotNull(
        	"Null configuration",
        	config
        	) ;
        //
        // Check the database name.
        assertEquals(
        	"Wrong database name",
        	config.getName(),
        	CastorDatabaseConfiguration.DEFAULT_DATABASE_NAME
        	) ;
        }

    /**
     * Try creating a CastorDatabaseConfiguration with a non-existient config file.
     *
     */
    public void testCreateUnknownConfig()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CastorDatabaseConfigurationTest:testCreateUnknownConfig()") ;
        //
        // Try creating a CastorDatabaseConfiguration with a non-existient config file.
        try {
            DatabaseConfiguration config = new CastorDatabaseConfiguration("unknown-database", "unknown-database.xml") ;
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
     * Try creating a CastorDatabaseConfiguration with a valid config file.
     *
     */
    public void testCreateValid()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CastorDatabaseConfigurationTest:testValidConfig()") ;
        //
        // Create a new database configuration.
        DatabaseConfiguration config = new CastorDatabaseConfiguration("test-database-001") ;
        assertNotNull("Null configuration", config) ;
        //
        // Check the database name.
        assertEquals("Wrong database name", config.getName(), "test-database-001") ;
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
        if (DEBUG_FLAG) System.out.println("CastorDatabaseConfigurationTest:testInvalidName()") ;
        //
        // Create a new database configuration with an invalid name.
        DatabaseConfiguration config = new CastorDatabaseConfiguration("INVALID-NAME", "test-database-001.xml") ;
        assertNotNull("Null configuration", config) ;
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
        if (DEBUG_FLAG) System.out.println("CastorDatabaseConfigurationTest:testConnectValid()") ;
        //
        // Create a new database configuration.
        DatabaseConfiguration config = new CastorDatabaseConfiguration("test-database-001") ;
        assertNotNull("Null configuration", config) ;
        //
        // Create a new database connection.
        assertNotNull("Null JDO database connection",
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
        if (DEBUG_FLAG) System.out.println("CastorDatabaseConfigurationTest:testCreateEngine()") ;
        //
        // Create a new database configuration.
        CastorDatabaseConfiguration config = new CastorDatabaseConfiguration("test-database-001") ;
        assertNotNull("Null configuration", config) ;
        //
        // Check our database engine.
        JDO engine = config.getDatabaseEngine() ;
        assertNotNull("Null JDO database engine", engine) ;
        }

    /**
     * Try checking the database test data.
     *
     */
    public void testDatabaseData()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CastorDatabaseConfigurationTest:testDatabaseData()") ;
        //
        // Create a new database configuration.
        CastorDatabaseConfiguration config = new CastorDatabaseConfiguration("test-database-001") ;
        assertNotNull("Null configuration", config) ;
        //
        // Try checking the database data.
        assertTrue("Check database data returned false",
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
        if (DEBUG_FLAG) System.out.println("CastorDatabaseConfigurationTest:testMultipleDatabases()") ;
        //
        // Create a new database configuration.
        CastorDatabaseConfiguration alpha = new CastorDatabaseConfiguration("test-database-001") ;
        assertNotNull("Null configuration", alpha) ;
        //
        // Create a new database connection.
        assertNotNull("Null JDO database connection",
        	alpha.getDatabase()
        	) ;
        //
        // Try checking the database data.
        assertTrue("Check database data returned false",
        	alpha.checkDatabaseTables()
        	) ;
        //
        // Create a new database configuration.
        CastorDatabaseConfiguration beta = new CastorDatabaseConfiguration("test-database-002") ;
        //
        // Create a new database connection.
        assertNotNull("Null JDO database connection",
        	beta.getDatabase()
        	) ;
        //
        // Try checking the database data.
        assertTrue("Check database data returned false",
        	beta.checkDatabaseTables()
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
        if (DEBUG_FLAG) System.out.println("CastorDatabaseConfigurationTest:testCreateTables()") ;
        //
        // Create a new database configuration.
        CastorDatabaseConfiguration config = new CastorDatabaseConfiguration("test-database-003") ;
        assertNotNull("Null configuration", config) ;
        //
        // Try creating the database tables.
        config.createDatabaseTables() ;
        //
        // Try checking the database data.
        assertTrue("Check database data returned false",
        	config.checkDatabaseTables()
        	) ;
        }


    }
