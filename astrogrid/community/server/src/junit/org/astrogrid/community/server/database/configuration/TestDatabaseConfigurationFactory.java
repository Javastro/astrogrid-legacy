/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/junit/org/astrogrid/community/server/database/configuration/Attic/TestDatabaseConfigurationFactory.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: TestDatabaseConfigurationFactory.java,v $
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

/**
 * Factory for deafult test database configurations.
 *
 */
public class TestDatabaseConfigurationFactory
    extends DatabaseConfigurationFactory
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Our test database name.
     *
     */
    public static final String TEST_DATABASE_NAME = "test-database-001" ;

    /**
     * Our test database XML file.
     *
     */
    public static final String TEST_DATABASE_XML = "test-database-001.xml" ;

    /**
     * Our test database SQL file.
     *
     */
    public static final String TEST_DATABASE_SQL = "test-database-001.sql" ;

    /**
     * Load our test database configuration.
     *
     */
    public DatabaseConfiguration testDatabaseConfiguration()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("TestDatabaseConfigurationFactory:testDatabaseConfiguration()") ;
        //
        // Load our database configuration.
        DatabaseConfiguration config = this.loadDatabaseConfiguration(
            TEST_DATABASE_NAME,
            TEST_DATABASE_XML,
            TEST_DATABASE_SQL) ;
        //
        // Reset the database tables.
//        config.createDatabaseTables() ;
        //
        // Return the new database configuration.
        return config ;
        }
    }
