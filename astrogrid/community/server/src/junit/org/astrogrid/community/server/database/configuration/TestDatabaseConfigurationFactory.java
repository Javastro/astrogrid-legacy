/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/junit/org/astrogrid/community/server/database/configuration/Attic/TestDatabaseConfigurationFactory.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/08 13:42:33 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: TestDatabaseConfigurationFactory.java,v $
 *   Revision 1.3  2004/03/08 13:42:33  dave
 *   Updated Maven goals.
 *   Replaced tabs with Spaces.
 *
 *   Revision 1.2.2.1  2004/03/08 12:53:18  dave
 *   Changed tabs to spaces
 *
 *   Revision 1.2  2004/03/05 17:20:00  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.1.2.2  2004/03/02 23:31:00  dave
 *   Added DatabaseManager to service tests
 *
 *   Revision 1.1.2.1  2004/02/27 14:36:34  dave
 *   Added test database configuration factory
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
